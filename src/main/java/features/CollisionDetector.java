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
                leftTile = gPanel.tiles.mapTileNum[entityLeftCol][entityTopRow];
                rightTile = gPanel.tiles.mapTileNum[entityRightCol][entityTopRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
            case DOWN -> {
                entityBottomRow = (int) ((entityBottomWorldY + entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[entityLeftCol][entityBottomRow];
                rightTile = gPanel.tiles.mapTileNum[entityRightCol][entityBottomRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
            case LEFT -> {
                entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[entityLeftCol][entityTopRow];
                rightTile = gPanel.tiles.mapTileNum[entityLeftCol][entityBottomRow];
//                System.out.println("left tile: " + leftTile + " right tile: " + rightTile);
                entity.collisionOn = detectTileCollision(entity, leftTile, rightTile);
            }
            case RIGHT -> {
                entityRightCol = (int) ((entityRightWorldX + entity.speed) / gPanel.tileSize);
                leftTile = gPanel.tiles.mapTileNum[entityRightCol][entityTopRow];
                rightTile = gPanel.tiles.mapTileNum[entityRightCol][entityBottomRow];
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
        for (int i = 0; i < gPanel.objects.size(); i++) {
            if (gPanel.objects.get(i) != null) {
                // preia pozitia ariei de coliziune a entitatii
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                // preia pozitia ariei de coliziune a obiectului
                gPanel.objects.get(i).solidArea.x += gPanel.objects.get(i).worldX;
                gPanel.objects.get(i).solidArea.y += gPanel.objects.get(i).worldY;

                switch (entity.direction) {
                    case UP -> entity.solidArea.y -= entity.speed;
                    case DOWN -> entity.solidArea.y += entity.speed;
                    case LEFT -> entity.solidArea.x -= entity.speed;
                    case RIGHT -> entity.solidArea.x += entity.speed;
                }
                if (entity.solidArea.intersects(gPanel.objects.get(i).solidArea)) {
                    if (gPanel.objects.get(i).isSolid) {
                        entity.collisionOn = true;
                        setMotionOff(entity);
                    }
                    if (entity.isPlayer) {
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gPanel.objects.get(i).solidArea.x = gPanel.objects.get(i).solidAreaDefaultX;
                gPanel.objects.get(i).solidArea.y = gPanel.objects.get(i).solidAreaDefaultY;
            }
        }
        return index;
    }

    /** NPC SAU MONSTRII */
    public int checkEntity(Entity entity, List<Entity> target) {
        int index=-1;
        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                if (collisionOnTarget(entity, target.get(i))) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
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
