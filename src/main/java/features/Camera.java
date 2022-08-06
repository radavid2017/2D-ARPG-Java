package features;

import game.GamePanel;
import object.SuperObject;
import tile.Tile;
import tile.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public void manageTiles(Graphics2D g2D, TileManager tiles, int idTileMap) {
        // opreste camera la marginile hartii

        if (gPanel.player.screenX > gPanel.player.worldX) {
            screenX = worldX;
        }
        if (gPanel.player.screenY > gPanel.player.worldY) {
            screenY = worldY;
        }
        int rightOffset = gPanel.screenWidth - gPanel.player.screenX;
        if (rightOffset > gPanel.worldWidth - gPanel.player.worldX) {
            screenX = gPanel.screenWidth - (gPanel.worldWidth - worldX);
        }
        int bottomOffset = gPanel.screenHeight - gPanel.player.screenY;
        if (bottomOffset > gPanel.worldHeight - gPanel.player.worldY) {
            screenY = gPanel.screenHeight - (gPanel.worldHeight - worldY);
        }

        if (worldX + gPanel.tileSize > gPanel.player.worldX - gPanel.player.screenX &&
                worldX - gPanel.tileSize < gPanel.player.worldX + gPanel.player.screenX &&
                worldY + gPanel.tileSize > gPanel.player.worldY - gPanel.player.screenY &&
                worldY - gPanel.tileSize < gPanel.player.worldY + gPanel.player.screenY) {

            drawTiles(g2D, tiles, idTileMap);
//            g2D.drawImage(tiles.generalTiles.get(tileNum).image, (int) screenX, (int) screenY, null);
        }
        else if (gPanel.player.screenX > gPanel.player.worldX ||
                gPanel.player.screenY > gPanel.player.worldY ||
                rightOffset > gPanel.worldWidth - gPanel.player.worldX ||
                bottomOffset > gPanel.worldHeight - gPanel.player.worldY) {
            drawTiles(g2D, tiles, idTileMap);
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

    public void manageObjects(Graphics2D g2D, BufferedImage image) {
        if (worldX + gPanel.tileSize > gPanel.player.worldX - gPanel.player.screenX &&
                worldX - gPanel.tileSize < gPanel.player.worldX + gPanel.player.screenX &&
                worldY + gPanel.tileSize > gPanel.player.worldY - gPanel.player.screenY &&
                worldY - gPanel.tileSize < gPanel.player.worldY + gPanel.player.screenY) {

            g2D.drawImage(image, (int) screenX, (int) screenY, null);
        }
    }

    public static void rescaleTiles() {
        for (int i = 0; i < gPanel.tiles.generalTiles.size(); i++) {
            gPanel.tiles.generalTiles.get(i).image = UtilityTool.scaledImage(TileManager.originalTilesImage.get(i), gPanel.tileSize, gPanel.tileSize);
        }
    }

    public static void rescalePlayer() {
        for (AnimationState animationState : gPanel.player.movement.states) {
            for (int j = 0; j < animationState.animationFrames.size(); j++) {
                BufferedImage scaledImage = UtilityTool.scaledImage(animationState.entityOriginalImages.get(j), gPanel.tileSize, gPanel.tileSize);
                animationState.animationFrames.set(j, scaledImage);
            }
        }
    }

    public static void rescaleObjects() {
        for (SuperObject object : gPanel.objects) {
            object.image = UtilityTool.scaledImage(object.originalObjImage, gPanel.tileSize, gPanel.tileSize);
        }
    }

    public static void rescaleAll() {
        rescaleTiles();
        rescalePlayer();
        rescaleObjects();
    }

    public static void zoomInOut(int i) {
//        System.out.println("default zoom: " + gPanel.defaultZoom);

        boolean canScale = true;
//        System.out.println("player inainte de zoom: " + gPanel.player.worldX + " " + gPanel.player.worldY);

        int oldWorldWidth = gPanel.tileSize * gPanel.maxWorldCol; // 2400
        if (gPanel.tileSize * gPanel.maxWorldCol > gPanel.limitZoomOut && gPanel.tileSize * gPanel.maxWorldCol < gPanel.limitZoomIn) gPanel.tileSize += i; // actualizare marime texturi
        else if (gPanel.tileSize * gPanel.maxWorldCol == gPanel.limitZoomOut) {
            gPanel.tileSize++;
            canScale = false;
        }
        else if (gPanel.tileSize * gPanel.maxWorldCol == gPanel.limitZoomIn) {
            gPanel.tileSize--;
            canScale = false;
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

        double newPlayerWorldX = gPanel.player.worldX * mul;
        double newPlayerWorldY = gPanel.player.worldY * mul;

        // actualizare marime zoom pentru jucator
        gPanel.player.worldX = newPlayerWorldX;
        gPanel.player.worldY = newPlayerWorldY;

        // actualizare marime zoom pentru aria de coliziune a jucatorului
        int nr = gPanel.tileSize*gPanel.maxWorldCol;
        if (canScale && nr > gPanel.limitZoomOut+150 && nr < gPanel.limitZoomIn-100) {
            gPanel.player.solidArea.width += i * mul;
            gPanel.player.solidArea.height += i * mul;
        }
//        gPanel.player.solidArea.x += mul;
//        gPanel.player.solidArea.y += mul;

//        System.out.println("player: " + gPanel.player.worldX + " " + gPanel.player.worldY);
//        System.out.println("solidArea: " + gPanel.player.solidArea.x + " " + gPanel.player.solidArea.y);

        // actualizare marime zoom pentru obiecte
        for (SuperObject object : gPanel.objects) {
            double newObjWorldX = object.worldX * mul;
            double newObjWorldY = object.worldY * mul;
            object.worldX = newObjWorldX;
            object.worldY = newObjWorldY;
        }
    }
}
