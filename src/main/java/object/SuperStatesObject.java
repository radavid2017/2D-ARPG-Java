package object;

import entity.Entity;
import features.Camera;
import features.RenameFolderFiles;
import features.UtilityTool;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SuperStatesObject extends Entity {
    public TypeStatesObject typeStatesObject;
    public ArrayList<BufferedImage> imgStates = new ArrayList<>();
    public String name;
    public boolean collision = false;
    public double worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 74, 62);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    String folderPath;

    public ArrayList<BufferedImage> originalObjStatesImages = new ArrayList<>();

    public SuperStatesObject() {

    }

    public SuperStatesObject(String folderName, TypeStatesObject typeStatesObject) {
        this.folderPath = "res/objectsWithStates/" + folderName;
        this.typeStatesObject = typeStatesObject;
    }

    public void loadObject(GamePanel gp) {
        RenameFolderFiles.rename(folderPath);
        File directory = new File(folderPath);
        for (int i = 0; i < directory.list().length; i++) {
            String stateObjName = folderPath + "/" + i + ".png";
            System.out.println("Ipostaza " + name + " a obiectului " + typeStatesObject.name() + " incarcata cu succes.");
            try {
                BufferedImage image = ImageIO.read(new FileInputStream(stateObjName));
                originalObjStatesImages.add(image);
                image = UtilityTool.scaledImage(image, gp.tileSize, gp.tileSize);
                imgStates.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setPosition(int x, int y) {
        worldX = x;
        worldY = y;
    }

    public void setCollision() {
        this.collision = true;
    }

//
//    public void draw(Graphics2D g2D, GamePanel gPanel) {
//        double screenX = worldX - gPanel.player.worldX + gPanel.player.screenX;
//        double screenY = worldY - gPanel.player.worldY + gPanel.player.screenY;
//        // Instantiere camera
//        Camera camera = new Camera(worldX, worldY, screenX, screenY, gPanel);
//        // Management Camera
//        camera.manageObjects(g2D, image);
//    }

    public void setWidth(int width) {
        this.solidArea.width = width;
    }

    public void setHeight(int height) {
        this.solidArea.height = height;
    }

    public void setSolidAreaDefaultX(int solidAreaDefaultX) {
        this.solidAreaDefaultX = solidAreaDefaultX;
    }

    public void setSolidAreaDefaultY(int solidAreaDefaultY) {
        this.solidAreaDefaultY = solidAreaDefaultY;
    }

    public void setSolidArea(int solidAreaDefaultX, int solidAreaDefaultY, int width, int height) {
        this.setSolidAreaDefaultX(solidAreaDefaultX);
        this.setSolidAreaDefaultY(solidAreaDefaultY);
        this.setWidth(width);
        this.setHeight(height);
    }

    public static BufferedImage setImage(String imagePath) {
        try {
            return ImageIO.read(new FileInputStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
