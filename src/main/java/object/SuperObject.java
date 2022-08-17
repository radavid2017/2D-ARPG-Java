package object;

import features.Camera;
import features.UtilityTool;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static features.Camera.gPanel;

/** Clasa parinte a obiectelor de interactionare & iteme din joc */
public abstract class SuperObject {

    public TypeObject typeObject;
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public double worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 74, 62);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public BufferedImage originalObjImage;

    public SuperObject() {

    }

    public void loadObject(GamePanel gp, String imageFilePath) {
        name = imageFilePath.substring(imageFilePath.lastIndexOf("/")+1);
//        name = name.substring(0, name.indexOf("."));
        System.out.println("numele obiectului: " + name);
        try {
            image = ImageIO.read(new FileInputStream(imageFilePath));
            originalObjImage = image;
            image = UtilityTool.scaledImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPosition(int x, int y) {
        worldX = x;
        worldY = y;
    }

    public void draw(Graphics2D g2D, GamePanel gPanel) {
        double screenX = worldX - gPanel.player.worldX + gPanel.player.screenX;
        double screenY = worldY - gPanel.player.worldY + gPanel.player.screenY;
        // Instantiere camera
        Camera camera = new Camera(worldX, worldY, screenX, screenY, gPanel);
        // Management Camera
        camera.manageObjects(g2D, image);
    }

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
