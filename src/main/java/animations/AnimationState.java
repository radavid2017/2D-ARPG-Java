package animations;

import entity.Entity;
import features.Direction;
import features.RenameFolderFiles;
import features.UtilityTool;
import game.GamePanel;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static features.Camera.gPanel;

public class AnimationState {
    GamePanel gp;
    public String title; // titlul animatiei
    public Direction direction;
    public List<BufferedImage> animationFrames = new ArrayList<>();
    public List<BufferedImage> entityOriginalImages = new ArrayList<>();

    /** variabile pentru rularea animatiilor */
    // interval de cadre pentru schimbare imagine
    public int intervalChangingFrames = 0;
    // indexul imaginii curente din animatie
    public int currentFrame = 0;
    public int numFrames = 0;
    public int timeToChangeFrame = 10;

    public AnimationState(GamePanel gp, String title, Direction direction, String filePath) {
        try {
            RenameFolderFiles.rename(filePath);
            List<BufferedImage> imgList = new ArrayList<>();
            File directory = new File(filePath);
            for (int i = 0; i < directory.list().length; i++) {
                String spriteName = filePath + "/" + i + ".png";
                System.out.println("CADRUL " + spriteName + " incarcat cu succes.");
                BufferedImage image = ImageIO.read(new FileInputStream(spriteName));
                image = UtilityTool.scaledImage(image,gp.tileSize, gp.tileSize);
                entityOriginalImages.add(image);
                imgList.add(image);
            }
            this.direction = direction;
            this.title = title;
            this.animationFrames = imgList;
            numFrames = directory.list().length-1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnimationState(GamePanel gp, Direction direction) {
        this.gp = gp;
        this.direction = direction;
    }

    public void loadAnimation(GamePanel gp, String folderPath) {
        try {
            RenameFolderFiles.rename(folderPath);
            List<BufferedImage> imgList = new ArrayList<>();
            File directory = new File(folderPath);
            for (int i = 0; i < directory.list().length; i++) {
                String spriteName = folderPath + "/" + i + ".png";
                System.out.println("CADRUL " + spriteName + " incarcat cu succes.");
                BufferedImage image = ImageIO.read(new FileInputStream(spriteName));
                image = UtilityTool.scaledImage(image,gp.tileSize, gp.tileSize);
                entityOriginalImages.add(image);
                imgList.add(image);
            }
            this.animationFrames = imgList;
            numFrames = directory.list().length-1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDirectionAnimation(String path) {
        switch (direction) {
            case UP -> loadAnimation(gp, path + "\\north");
            case DOWN -> loadAnimation(gp, path + "\\south");
            case LEFT -> loadAnimation(gp, path + "\\west");
            case RIGHT -> loadAnimation(gp, path + "\\east");
        }
    }

    public void updateFrames() {
        /** actualizare imagine/avansare animatie cadru urmator dupa un interval de cadre rulate din cele 60 per secunda */
        intervalChangingFrames++;
        if (intervalChangingFrames > timeToChangeFrame) {
            if (currentFrame < numFrames) {
                currentFrame ++;
            } else
                currentFrame = 1;
            intervalChangingFrames = 0;
        }
    }

    public BufferedImage nextFrame() {
        return this.animationFrames.get(currentFrame);
    }

    public BufferedImage idle() {
        return this.animationFrames.get(0);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}