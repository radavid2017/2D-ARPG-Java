package object.obstacle;

import entity.Entity;
import features.Direction;
import game.GamePanel;
import interactive_tile.IT_MetalPlate;
import interactive_tile.InteractiveTile;
import object.SuperObject;
import object.TypeMaterial;
import object.obstacle.door.IronDoor;

import java.awt.*;
import java.util.ArrayList;

public class OBJ_BigRock extends MovableObstacle {

    public OBJ_BigRock(GamePanel gp) {
        super(gp, TypeMaterial.Rock);
        setDefaultSolidArea();
        name = "Piatra Uriasa";
        direction = Direction.DOWN;
        speed = 4;
        setImage("bigrock/bigrock.png");
    }

    @Override
    public SuperObject generateObject() {
        return new OBJ_BigRock(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = (int) (getGamePanel().tileSize/1.25);
        solidArea.height = (int) (getGamePanel().tileSize/1.25);
    }

    @Override
    public void interact() {
        move(getGamePanel().player.direction);
    }

    @Override
    public void update() {

    }

    @Override
    public void move(Direction direction) {
        this.direction = direction;

        checkCollisions();

        if (!collisionOn) {
            switch (direction) {
                case UP -> worldY -= speed;
                case DOWN -> worldY += speed;
                case LEFT -> worldX -= speed;
                case RIGHT -> worldX += speed;
            }
        }

        detectPlate();
    }

    public void detectPlate() {
        ArrayList<InteractiveTile> plateList = new ArrayList<>();
        ArrayList<MovableObstacle> rockList = new ArrayList<>();

        // Create a plate list
        if (getGamePanel().interactiveTiles.get(getGamePanel().currentMap) != null) {
            for (int i = 0; i < getGamePanel().interactiveTiles.get(getGamePanel().currentMap).size(); i++) {
                if (getGamePanel().interactiveTiles.get(getGamePanel().currentMap).get(i) != null &&
                        getGamePanel().interactiveTiles.get(getGamePanel().currentMap).get(i) instanceof IT_MetalPlate metalPlate) {
                    plateList.add(metalPlate);
                }
            }
        }

        // Create a rock list
        if (getGamePanel().objects.get(getGamePanel().currentMap) != null) {
            for (int i = 0; i < getGamePanel().objects.get(getGamePanel().currentMap).size(); i++) {
                if (getGamePanel().objects.get(getGamePanel().currentMap).get(i) != null &&
                        getGamePanel().objects.get(getGamePanel().currentMap).get(i) instanceof OBJ_BigRock bigRock) {
                    rockList.add(bigRock);
                }
            }
        }

        int count = 0;

        // Scan the plate list
        for (int i = 0; i < plateList.size(); i++) {
            int xDistance = (int) Math.abs(worldX - plateList.get(i).worldX);
            int yDistance = (int) Math.abs(worldY - plateList.get(i).worldY);
            int distance = Math.max(xDistance, yDistance);

            if (distance < 28) {
                if (linkedEntity == null) {
                    linkedEntity = plateList.get(i);
                    getGamePanel().playSE("unlock.wav");
                }
            }
            else {
                if (linkedEntity == plateList.get(i)) {
                    linkedEntity = null;
                }
            }
        }

        // Scan the rock list
        for (int i = 0; i < rockList.size(); i++) {
            // Count the rock on the plate
            if (rockList.get(i).linkedEntity != null) {
                count++;
            }
        }

        // If all the rocks are on the plates, the iron door opens
        if (count == rockList.size()) {
            if (getGamePanel().objects.get(getGamePanel().currentMap) != null) {
                for (int i = 0; i < getGamePanel().objects.get(getGamePanel().currentMap).size(); i++) {
                    if (getGamePanel().objects.get(getGamePanel().currentMap).get(i) != null &&
                            getGamePanel().objects.get(getGamePanel().currentMap).get(i) instanceof IronDoor) {
                        getGamePanel().objects.get(getGamePanel().currentMap).set(i, null);
                        getGamePanel().playSE("dooropen.wav");
                    }
                }
            }
        }
    }
}
