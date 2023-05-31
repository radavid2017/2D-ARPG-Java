package item.consumable.key;

import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import item.Consumable;
import item.TypeItem;
import item.consumable.TypeConsumable;
import object.SuperObject;
import object.TypeObject;
import object.obstacle.Obstacle;
import object.obstacle.TypeObstacle;

import java.util.ArrayList;

public abstract class OBJ_Key extends Consumable {
    KeyModel keyModel;

    public OBJ_Key(GamePanel gPanel, int price) {
        super(gPanel, TypeConsumable.Key, price);
        this.typeObject = TypeObject.Key;
        objPath += "keys/";
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public int getDetected(Entity user, ArrayList<Entity> target, TypeObstacle targetType) {
        int index = -1;

        // Verifica obiectul inconjurator
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction) {
            case UP -> nextWorldY = (int) (user.getTopY() - getGamePanel().player.speed);
            case DOWN -> nextWorldY = (int) (user.getBottomY() + getGamePanel().player.speed);
            case LEFT -> nextWorldX = (int) (user.getLeftX() - getGamePanel().player.speed);
            case RIGHT -> nextWorldX = (int) (user.getRightX() + getGamePanel().player.speed);
        }

        int col = nextWorldX/getGamePanel().tileSize;
        int row = nextWorldY/getGamePanel().tileSize;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null && target.get(i) instanceof Obstacle obstacle) {
                if(obstacle.getCol() == col && obstacle.getRow() == row && obstacle.typeObstacle == targetType) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }
}
