package object;

import animations.StateMachine;
import animations.TypeAnimation;
import entity.Entity;
import features.UtilityTool;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/** Clasa parinte a obiectelor de interactionare & iteme din joc */
public abstract class SuperObject extends Entity {

    public StateMachine animation;
    public TypeObject typeObject;
    public BufferedImage image;
    public String name;
    public String objPath = "res/objects/";

    public BufferedImage originalObjImage;

    public String description = "";

    public SuperObject(GamePanel gPanel) {
        super(gPanel);
    }

    public void loadObject(GamePanel gp, String imageFilePath) {
//        name = imageFilePath.substring(imageFilePath.lastIndexOf("/")+1);
//        name = name.substring(0, name.indexOf("."));
//        System.out.println("numele obiectului: " + this.getClass().getName());
        try {
            image = ImageIO.read(new FileInputStream(imageFilePath));
            originalObjImage = image;
            image = UtilityTool.scaledImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupAnimation(String objFolderPath) {
        animation = new StateMachine();
        animation.loadCompleteAnimation(getGamePanel(), objFolderPath, TypeAnimation.OBJECT);
    }

    public void update() {
        super.update();
    }

    public void draw(Graphics2D g2D) {
        if (getGamePanel().player!=null) {
            super.draw(g2D);
            camera.drawEntity(g2D, image);
            drawSolidArea(g2D);
        }
    }

    public void setWidth(int width) {
        solidArea.width = width;
    }

    public void setHeight(int height) {
        solidArea.height = height;
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

    public void setImage(String imgName) {
        try {
            image = ImageIO.read(new FileInputStream(objPath + imgName));
            originalObjImage = image;
            image = UtilityTool.scaledImage(image, getGamePanel().tileSize, getGamePanel().tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
