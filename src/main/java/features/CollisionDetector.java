package features;

import entity.Entity;
import game.GamePanel;
import object.SuperObject;
import tile.Tile;

import java.util.ArrayList;
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

        // Foloseste o directie temporala cand este knockbacked
        Direction direction = entity.direction;
        if (entity.knockBack) {
            direction = entity.knockBackDirection;
        }

        switch (direction) {
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

    private int manageObjListCollision(ArrayList<Entity> objects, Entity entity) {
        int index=-1;

        // Use a temporal direction when it's being knocbacked
        Direction direction = entity.direction;
        if (entity.knockBack) {
            direction = entity.knockBackDirection;
        }

        if (objects != null) {
            for (int i = 0; i < objects.size(); i++) { // FIXED
                if (objects.get(i) != null) { // FIXED
                    // preia pozitia ariei de coliziune a entitatii
                    entity.solidArea.x += entity.worldX;
                    entity.solidArea.y += entity.worldY;

                    // preia pozitia ariei de coliziune a obiectului
                    objects.get(i).solidArea.x += objects.get(i).worldX; // FIXED
                    objects.get(i).solidArea.y += objects.get(i).worldY; // FIXED

                    switch (direction) {
                        case UP -> entity.solidArea.y -= entity.speed;
                        case DOWN -> entity.solidArea.y += entity.speed;
                        case LEFT -> entity.solidArea.x -= entity.speed;
                        case RIGHT -> entity.solidArea.x += entity.speed;
                    }
                    if (entity.solidArea.intersects(objects.get(i).solidArea)) { // FIXED
                        if (objects.get(i).isSolid) { // FIXED
                            entity.collisionOn = true;
                            setMotionOff(entity);
                        }
                        if (entity.isPlayer) {
                            index = i;
                        }
                    }
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    objects.get(i).solidArea.x = objects.get(i).solidAreaDefaultX; // FIXED
                    objects.get(i).solidArea.y = objects.get(i).solidAreaDefaultY; // FIXED
                }
            }
        }
        return index;
    }

    // returnez index-ul obiectului cu care are loc colizunea
    public int manageObjCollision(Entity entity) {
        return manageObjListCollision(gPanel.objects.get(gPanel.currentMap), entity);
    }

    /** NPC SAU MONSTRII */
    public int checkEntity(Entity entity, ArrayList<Entity> target) { // FIXED

        if (target != null) {
            for (int i = 0; i < target.size(); i++) { // FIXED
                if (target.get(i) != null) { // FIXED
                    if (collisionOnTarget(entity, target.get(i))) { // FIXED
                        entity.collisionOn = true;
                        return i;
                    }
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

        // Foloseste o directie temporala cand este knockbacked
        Direction direction = entity.direction;
        if (entity.knockBack) {
            direction = entity.knockBackDirection;
        }

        boolean isColliding = false;
        if (target != null && target.isSolid && entity.isSolid) {
            // preia pozitia ariei de coliziune a entitatii
            entity.solidArea.x += entity.worldX;
            entity.solidArea.y += entity.worldY;

            // preia pozitia ariei de coliziune a obiectului
            target.solidArea.x += target.worldX;
            target.solidArea.y += target.worldY;

            switch (direction) {
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
