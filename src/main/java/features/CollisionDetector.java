package features;

import entity.Entity;
import game.GamePanel;
import tile.Tile;

import java.util.List;

public class CollisionDetector {

    GamePanel gPanel;

    public CollisionDetector (GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    public void manageTileCollision(Entity entity) {
        double entityLeftWorldX = entity.worldX + entity.solidArea.x;
        double entityRightWorldX = entityLeftWorldX + entity.solidArea.width;
        double entityTopWorldY = entity.worldY + entity.solidArea.y;
        double entityBottomWorldY = entityTopWorldY + entity.solidArea.height;

        int entityLeftCol = (int) (entityLeftWorldX / gPanel.tileSize);
        int entityRightCol = (int) (entityRightWorldX / gPanel.tileSize);
        int entityTopRow = (int) (entityTopWorldY / gPanel.tileSize);
        int entityBottomRow = (int) (entityBottomWorldY / gPanel.tileSize);

        int leftTile, rightTile;

        switch (entity.direction) {
            case UP -> {
                entityTopRow = (int) ((entityTopWorldY - entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityLeftCol][entityTopRow];
                rightTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityRightCol][entityTopRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
            case DOWN -> {
                entityBottomRow = (int) ((entityBottomWorldY + entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityLeftCol][entityBottomRow];
                rightTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityRightCol][entityBottomRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
            case LEFT -> {
                entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityLeftCol][entityTopRow];
                rightTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityLeftCol][entityBottomRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
            case RIGHT -> {
                entityRightCol = (int) ((entityRightWorldX + entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityRightCol][entityTopRow];
                rightTile = gPanel.tiles.mapTileNum[gPanel.currentMap][entityRightCol][entityBottomRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
        }
    }

    private void setMotionOff(Entity entity) {
        if (!entity.isPlayer)
            entity.inMotion = false;
    }

    private boolean detectTileCollision(Entity entity, int leftTile, int rightTile) {
        for (Tile tile : gPanel.tiles.generalTiles) {
            if ((tile.idTile == leftTile && tile.isColliding) || (tile.idTile == rightTile && tile.isColliding)) {
                setMotionOff(entity);
                return true;
            }
        }
        return false;
    }

    // returnez index-ul obiectului cu care are loc colizunea
    public int manageObjCollision(Entity entity) {

        int index=-1;
        for (int i = 0; i < gPanel.objects[1].length; i++) { // FIXED
            if (gPanel.objects[gPanel.currentMap][i] != null) { // FIXED
                // preia pozitia ariei de coliziune a entitatii
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                // preia pozitia ariei de coliziune a obiectului
                gPanel.objects[gPanel.currentMap][i].solidArea.x += gPanel.objects[gPanel.currentMap][i].worldX; // FIXED
                gPanel.objects[gPanel.currentMap][i].solidArea.y += gPanel.objects[gPanel.currentMap][i].worldY; // FIXED

                switch (entity.direction) {
                    case UP -> entity.solidArea.y -= entity.speed;
                    case DOWN -> entity.solidArea.y += entity.speed;
                    case LEFT -> entity.solidArea.x -= entity.speed;
                    case RIGHT -> entity.solidArea.x += entity.speed;
                }
                if (entity.solidArea.intersects(gPanel.objects[gPanel.currentMap][i].solidArea)) { // FIXED
                    if (gPanel.objects[gPanel.currentMap][i].isSolid) { // FIXED
                        entity.collisionOn = true;
                        setMotionOff(entity);
                    }
                    if (entity.isPlayer) {
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gPanel.objects[gPanel.currentMap][i].solidArea.x = gPanel.objects[gPanel.currentMap][i].solidAreaDefaultX; // FIXED
                gPanel.objects[gPanel.currentMap][i].solidArea.y = gPanel.objects[gPanel.currentMap][i].solidAreaDefaultY; // FIXED
            }
        }
        return index;
    }

    /** NPC SAU MONSTRII */
    public int checkEntity(Entity entity, Entity[][] target) { // FIXED
//        int index=-1;
        for (int i = 0; i < target[1].length; i++) { // FIXED
            if (target[gPanel.currentMap][i] != null) { // FIXED
                if (collisionOnTarget(entity, target[gPanel.currentMap][i])) { // FIXED
                    entity.collisionOn = true;
                    return i;
                }
            }
        }
        return -1;
    }

    /** PLAYER */
    public boolean checkPlayer(Entity entity) {
        if (collisionOnTarget(entity, gPanel.player)) {
            entity.collisionOn = true;
            return true;
        }
        return false;
    }

    /** fucntia returneaza true daca exista coliziune intre doua entitati */
    private boolean collisionOnTarget(Entity entity, Entity target) {
        boolean isColliding = false;
        if (target != null && target.isSolid && entity.isSolid) {
            // preia pozitia ariei de coliziune a entitatii
            entity.solidArea.x += entity.worldX;
            entity.solidArea.y += entity.worldY;

            // preia pozitia ariei de coliziune a obiectului
            target.solidArea.x += target.worldX;
            target.solidArea.y += target.worldY;

            switch (entity.direction) {
                case UP -> entity.solidArea.y -= entity.speed;
                case DOWN -> entity.solidArea.y += entity.speed;
                case LEFT -> entity.solidArea.x -= entity.speed;
                case RIGHT -> entity.solidArea.x += entity.speed;
            }
            if (entity.solidArea.intersects(target.solidArea)) {
                if (target != entity) {
                    isColliding = true;
                }
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            target.solidArea.x = target.solidAreaDefaultX;
            target.solidArea.y = target.solidAreaDefaultY;
        }
        return isColliding;
    }
}
