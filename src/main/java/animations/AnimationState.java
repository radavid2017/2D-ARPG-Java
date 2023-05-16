package animations;

import entity.Creature;
import entity.Entity;
import features.Direction;
import features.RenameFolderFiles;
import features.UtilityTool;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    GamePanel gp;
    public String title; // titlul animatiei
    public Direction direction;
    public List<BufferedImage> animationFrames = new ArrayList<>();
    public List<BufferedImage> entityOriginalImages = new ArrayList<>();

    public TypeAnimation typeAnimation;

    /** variabile pentru rularea animatiilor */
    // interval de cadre pentru schimbare imagine
    public int intervalChangingFrames = 0;
    // indexul imaginii curente din animatie
    public int currentFrame = 0;
    public int numFrames = 0;
    public int timeToChangeFrame = 10;
    public int motion1_duration = 5;
    public int motion2_duration = 25;

    public AnimationState(GamePanel gp, String title, Direction direction, String filePath, TypeAnimation typeAnimation) {
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
            this.typeAnimation = typeAnimation;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnimationState(GamePanel gp, Direction direction) {
        this.gp = gp;
        this.direction = direction;
    }

    public AnimationState(GamePanel gp, Direction direction, TypeAnimation typeAnimation) {
        this.gp = gp;
        this.direction = direction;
        this.typeAnimation = typeAnimation;
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
                if (typeAnimation == TypeAnimation.ATTACK)
                    switch (direction) {
                        case UP, DOWN -> image = UtilityTool.scaledImage(image,gp.tileSize, gp.tileSize*2);
                        case LEFT, RIGHT -> image = UtilityTool.scaledImage(image,gp.tileSize*2, gp.tileSize);
                    }
                else
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

    public void updateFrames(Entity entity) {
        /** actualizare imagine/avansare animatie cadru urmator dupa un interval de cadre rulate din cele 60 per secunda */
        switch (typeAnimation) {
            case IN_MOTION, IDLE -> {
                intervalChangingFrames++;
                if (intervalChangingFrames > timeToChangeFrame) {
                    if (currentFrame < numFrames) {
                        currentFrame++;
                    } else
                        currentFrame = 0;
                    intervalChangingFrames = 0;
                }
            }
            case ATTACK -> {
                intervalChangingFrames++;
                if (intervalChangingFrames <= motion1_duration) {
                    currentFrame = 0;
                }
                if (intervalChangingFrames > motion1_duration && intervalChangingFrames <= motion2_duration) {
                    currentFrame = 1;

                    ((Creature) entity).attacking();
                }
                if (intervalChangingFrames > motion2_duration) {
                    currentFrame = 0;
                    intervalChangingFrames = 0;
                    gp.player.keyH.spacePressed = false;
                    ((Creature) entity).attacking = false;
                }
            }
            case OBJECT -> {
                intervalChangingFrames++;
                if (intervalChangingFrames > 12) {
                    if (currentFrame < numFrames)
                        currentFrame++;
                    else currentFrame = 0;
                    intervalChangingFrames = 0;
                }
            }
        }
    }

    public BufferedImage nextFrame() {
        return this.animationFrames.get(currentFrame);
    }

//    public BufferedImage idle() {
//        return this.animationFrames.get(0);
//    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}