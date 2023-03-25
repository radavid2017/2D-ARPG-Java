package features;

import animations.AnimationState;
import animations.StateMachine;
import animations.TypeAnimation;
import entity.Creature;
import entity.Entity;
import game.GamePanel;
import object.SuperObject;
import object.SuperStatesObject;
import tile.Tile;
import tile.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Camera {
    public double worldX;
    public double worldY;
    public double screenX;
    public double screenY;
    public static GamePanel gPanel;

    public Camera(int worldX, int worldY, double screenX, double screenY, GamePanel gPanel) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.screenX = screenX;
        this.screenY = screenY;
        Camera.gPanel = gPanel;
    }

    public Camera(double worldX, double worldY, double screenX, double screenY, GamePanel gPanel) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.screenX = screenX;
        this.screenY = screenY;
        Camera.gPanel = gPanel;
    }

    public boolean playerIsTouchingEdgesOfCamera() {
        // afisarea cand jucatorul se afla la marginile hartii
        boolean playerAtEdgeOfCamera = false;

        // opreste camera la marginile hartii
        if (gPanel.player.screenX > gPanel.player.worldX) {
            playerAtEdgeOfCamera = true;
            screenX = worldX;
        }
        if (gPanel.player.screenY > gPanel.player.worldY) {
            playerAtEdgeOfCamera = true;
            screenY = worldY;
        }
        int rightOffset = gPanel.screenWidth - gPanel.player.screenX;
        if (rightOffset > gPanel.worldWidth - gPanel.player.worldX) {
            playerAtEdgeOfCamera = true;
            screenX = gPanel.screenWidth - (gPanel.worldWidth - worldX);
        }
        int bottomOffset = gPanel.screenHeight - gPanel.player.screenY;
        if (bottomOffset > gPanel.worldHeight - gPanel.player.worldY) {
            playerAtEdgeOfCamera = true;
            screenY = gPanel.screenHeight - (gPanel.worldHeight - worldY);
        }
        return playerAtEdgeOfCamera;
    }

    public void manageTiles(Graphics2D g2D, TileManager tiles, int idTileMap) {
        if (playerIsTouchingEdgesOfCamera()) {
            drawTiles(g2D, tiles, idTileMap);
        }
        else if (isOnCamera()) {
            drawTiles(g2D, tiles, idTileMap);
//            g2D.drawImage(tiles.generalTiles.get(tileNum).image, (int) screenX, (int) screenY, null);
        }
    }

    private void drawTiles(Graphics2D g2D, TileManager tiles, int idTileMap) {
        boolean gasit = false;
        for (Tile tile : tiles.generalTiles) {
            if (tile.idTile == idTileMap) {
                g2D.drawImage(tile.image, (int) screenX, (int) screenY, null);
                gasit = true;
                break;
            }
        }
        if (!gasit)
            System.out.println("TILE NEGASIT");
    }

    public void drawEntity(Graphics2D g2D, BufferedImage image) {
        if (isOnCamera()) {
            g2D.drawImage(image, (int) screenX, (int) screenY, null);
        }
    }

    boolean isOnCamera() {
        return worldX + gPanel.tileSize > gPanel.player.worldX - gPanel.player.screenX &&
                worldX - gPanel.tileSize < gPanel.player.worldX + gPanel.player.screenX &&
                worldY + gPanel.tileSize > gPanel.player.worldY - gPanel.player.screenY &&
                worldY - gPanel.tileSize < gPanel.player.worldY + gPanel.player.screenY;
    }

//    public void manageObjects(Graphics2D g2D, BufferedImage image) {
//        if (worldX + gPanel.tileSize > gPanel.player.worldX - gPanel.player.screenX &&
//                worldX - gPanel.tileSize < gPanel.player.worldX + gPanel.player.screenX &&
//                worldY + gPanel.tileSize > gPanel.player.worldY - gPanel.player.screenY &&
//                worldY - gPanel.tileSize < gPanel.player.worldY + gPanel.player.screenY) {
//
//            g2D.drawImage(image, (int) screenX, (int) screenY, null);
//        }
//    }

    public static void rescaleEntities() {
        gPanel.addAllLists();
        for (Entity entity : gPanel.entities) {
            if (entity instanceof Creature creature) {
                List<StateMachine> allAnimations = Arrays.asList(creature.movement, creature.idle, creature.attackState);
                rescaleAllAnimations(allAnimations);
            }
        }
        gPanel.entities.clear();
    }

    public static void rescaleTiles() {
        for (int i = 0; i < gPanel.tiles.generalTiles.size(); i++) {
            gPanel.tiles.generalTiles.get(i).image = UtilityTool.scaledImage(TileManager.originalTilesImage.get(i), gPanel.tileSize, gPanel.tileSize);
        }
    }

    public static void rescalePlayer() {
        List<StateMachine> allAnimations = Arrays.asList(gPanel.player.movement, gPanel.player.idle, gPanel.player.attackWeapon);
        rescaleAllAnimations(allAnimations);
    }

    private static void rescaleAllAnimations(List<StateMachine> allAnimations) {
        for (StateMachine stateMachine : allAnimations) {
            for (AnimationState animationState : stateMachine.states) {
                int width = gPanel.tileSize, height = gPanel.tileSize;
                if (animationState.typeAnimation == TypeAnimation.ATTACK) {
                    switch (animationState.direction) {
                        case UP, DOWN -> height *= 2;
                        case LEFT, RIGHT -> width *= 2;
                    }
                }
                for (int i = 0; i < animationState.animationFrames.size(); i++) {
                    BufferedImage scaledImage = UtilityTool.scaledImage(animationState.entityOriginalImages.get(i), width, height);
                    animationState.animationFrames.set(i, scaledImage);
                }
            }
        }
    }

    public static void rescaleObjects() {
        for (SuperObject object : gPanel.objects[gPanel.currentMap]) {
            object.image = UtilityTool.scaledImage(object.originalObjImage, gPanel.tileSize, gPanel.tileSize);
        }
    }

    public static void rescaleObjectStates() {
        for (SuperStatesObject superStatesObject : gPanel.statesObjectList) {
            for (int i = 0; i < superStatesObject.imgStates.size(); i++) {
                superStatesObject.imgStates.set(i, UtilityTool.scaledImage(superStatesObject.originalObjStatesImages.get(i), gPanel.tileSize, gPanel.tileSize));
            }
        }
    }

    public static void rescaleAll() {
        rescaleTiles();
        rescalePlayer();
        rescaleObjects();
        rescaleEntities();
        rescaleObjectStates();
    }

    public static void fixNPCStuckInTile() {
        for (Entity npc : gPanel.npcList[gPanel.currentMap]) {
            int tileHoldingEntity = gPanel.tiles.mapTileNum[gPanel.currentMap][(int) npc.worldX][(int) npc.worldY];
            for (Tile tile : gPanel.tiles.generalTiles){
                if (tile.idTile == tileHoldingEntity && tile.isColliding) {
                    npc.collisionOn = false;
                    break;
                }
            }
        }
    }

    public static void fixPlayerStuckInTile() {
        int tileHoldingEntity = gPanel.tiles.mapTileNum[gPanel.currentMap][(int) gPanel.player.worldX/gPanel.tileSize][(int) gPanel.player.worldY/gPanel.tileSize];
        for (Tile tile : gPanel.tiles.generalTiles){
            if (tile.idTile == tileHoldingEntity && tile.isColliding) {
                gPanel.player.collisionOn = false;
                break;
            }
        }
    }

    public static void validatePositions(Entity entity, Entity target) {
        double entityLeftWorldX = entity.worldX + entity.solidArea.x;
        double entityRightWorldX = entityLeftWorldX + entity.solidArea.width;
        double entityTopWorldY = entity.worldY + entity.solidArea.y;
        double entityBottomWorldY = entityTopWorldY + entity.solidArea.height;

        double targetLeftWorldX = target.worldX + target.solidArea.x;
        double targetRightWorldX = targetLeftWorldX + target.solidArea.width;
        double targetTopWorldY = target.worldY + target.solidArea.y;
        double targetBottomWorldY = entityTopWorldY + target.solidArea.height;

        if (entityLeftWorldX < targetRightWorldX && entityRightWorldX > targetRightWorldX) {
            entity.collisionOn = false;
            target.collisionOn = false;
        }
        else if (entityBottomWorldY > targetTopWorldY && entityTopWorldY < targetTopWorldY) {
            entity.collisionOn = false;
            target.collisionOn = false;
        }
        else if (entityRightWorldX > targetLeftWorldX && entityLeftWorldX < targetLeftWorldX) {
            entity.collisionOn = false;
            target.collisionOn = false;
        }
        else if (entityTopWorldY < targetBottomWorldY && entityBottomWorldY > targetBottomWorldY) {
            entity.collisionOn = false;
            target.collisionOn = false;
        }

    }

    public static void zoomInOut(int i) {
//        System.out.println("default zoom: " + gPanel.defaultZoom);

        boolean canScale = true;
//        System.out.println("player inainte de zoom: " + gPanel.player.worldX + " " + gPanel.player.worldY);

        int oldWorldWidth = gPanel.tileSize * gPanel.maxWorldCol; // 2400
        if (gPanel.tileSize * gPanel.maxWorldCol > gPanel.limitZoomOut && gPanel.tileSize * gPanel.maxWorldCol < gPanel.limitZoomIn) gPanel.tileSize += i; // actualizare marime texturi
        else if (gPanel.tileSize * gPanel.maxWorldCol == gPanel.limitZoomOut) {
            gPanel.tileSize++;
        }
        else if (gPanel.tileSize * gPanel.maxWorldCol == gPanel.limitZoomIn) {
            gPanel.tileSize--;
        }
        else {
            System.out.println("zoomInOut a intrat pe return!");return;
        }
        int newWorldWidth = gPanel.tileSize * gPanel.maxWorldCol; // 2350
        gPanel.player.speed = (double) newWorldWidth / 600;
        double mul = (double) newWorldWidth / oldWorldWidth; // 0.9791(6)

        // actualizare latime si lungime world
        gPanel.worldWidth *= mul;
        gPanel.worldHeight *= mul;

        // actualizare marime zoom pentru jucator
        gPanel.player.worldX *= mul;
        gPanel.player.worldY *= mul;

        // solidArea entitati
        int nr = gPanel.tileSize*gPanel.maxWorldCol;
//        if (canScale && nr > gPanel.limitZoomOut && nr < gPanel.limitZoomIn) {
            gPanel.addAllLists();
            for (Entity entity : gPanel.entities) {
                entity.setDefaultSolidArea();
            }
            gPanel.entities.clear();
//        }

        // NPC & Monstrii
        gPanel.addAllAI();
        for (Entity entity : gPanel.entities) {
            // update speed
            entity.speed = newWorldWidth / (600f*gPanel.player.speed);
            // update pozitii
            entity.worldX *= mul;
            entity.worldY *= mul;
        }
        gPanel.entities.clear();

        // actualizare marime zoom pentru obiecte
//        for (SuperObject object : gPanel.objects) {
//            object.worldX *= mul;
//            object.worldY *= mul;
//
//            object.screenX *= mul;
//            object.screenY *= mul;
//
////            if (canScale && nr > gPanel.limitZoomOut && nr < gPanel.limitZoomIn) {
////                object.solidAreaDefaultX = 0;
////                object.solidAreaDefaultY = gPanel.tileSize/3;
////                object.solidArea.width = gPanel.tileSize;
////                object.solidArea.height = (int) (gPanel.tileSize/1.5);
////            }
//        }

        // actualizare pozitii HUD
    }
}
