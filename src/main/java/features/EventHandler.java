package features;

import data.GameProgress;
import entity.Entity;
import game.Area;
import game.GamePanel;
import game.GameState;
import game.SceneState;

import java.awt.*;

public class EventHandler {

    GamePanel gPanel;
    EventRect[][][] eventRect;

    public double previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public int tempMap, tempCol, tempRow;

    public EventHandler(GamePanel gPanel) {
        this.gPanel = gPanel;

        eventRect = new EventRect[gPanel.maxMap][gPanel.maxWorldCol][gPanel.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while (map < gPanel.maxMap && col < gPanel.maxWorldCol && row < gPanel.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gPanel.maxWorldCol) {
                col = 0;
                row++;

                if (row == gPanel.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }
    }

    public void checkEvent() {

        // Verifica daca jucatorul este la mai mult de o textura indepartare
        // fata de ultimul eveniment
        double xDistance = Math.abs(gPanel.player.worldX - previousEventX);
        double yDistance = Math.abs(gPanel.player.worldY - previousEventY);
        double distane = Math.max(xDistance, yDistance);

        if (distane > gPanel.tileSize) {
            canTouchEvent = true;
        }

        if (canTouchEvent) {
//        if (hit(27, 16, Direction.RIGHT)) {
//            teleport(GameState.Dialogue);
//        }
            if (hit(0, 27, 16, Direction.RIGHT)) {
                damagePit(GameState.Dialogue);
            }
            else if (hit(0, 23, 12, Direction.UP)) {
                healingPool(GameState.Dialogue);
            }
            else if (hit(0, 10, 39, Direction.ANY)) {
                teleport(1, 12, 13, Area.Indoor); // in casa negustorului
            }
            else if (hit(1, 12, 13, Direction.ANY)) {
                teleport(0, 10, 39, Area.Outside); // afara
            }
            else if (hit(1, 12, 9, Direction.UP)) {
                speak(gPanel.npcList.get(1).get(0));
            }
            else if (hit(0, 12, 9, Direction.ANY)) {
                teleport(2,9,41, Area.Dungeon); // in temnita
            }
            else if (hit(2, 9, 41, Direction.ANY)) {
                teleport(0,12,9, Area.Outside); // afara
            }
            else if (hit(2, 8, 7, Direction.ANY)) {
                teleport(3,26,41, Area.Dungeon); // in B2
            }
            else if (hit(3, 26, 41, Direction.ANY)) {
                teleport(2,8,7, Area.Dungeon); // in B1
            }
            else if (hit(3, 25, 27, Direction.ANY)) {
                skeletonLord(); // BOSS
            }
        }
    }

    private void speak(Entity entity) {

        if (gPanel.keyH.enterPressed) {
            GamePanel.gameState = GameState.Dialogue;
            gPanel.player.attackCanceled = true;
            entity.speak();
        }
    }

    /** Coliziune player cu arie de declansare eveniment */
    public boolean hit(int map, int col, int row, Direction reqDirection) {

        boolean hit = false;

        if (map == gPanel.currentMap) {
            gPanel.player.solidArea.x = (int) (gPanel.player.worldX + gPanel.player.solidArea.x);
            gPanel.player.solidArea.y = (int) (gPanel.player.worldY + gPanel.player.solidArea.y);
            eventRect[map][col][row].x = col * gPanel.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gPanel.tileSize + eventRect[map][col][row].y;

            if (gPanel.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
                if (gPanel.player.direction.equals(reqDirection) || reqDirection.equals(Direction.ANY)) {
                    hit = true;

                    previousEventX = gPanel.player.worldX;
                    previousEventY = gPanel.player.worldY;
                }
            }

            gPanel.player.solidArea.x = gPanel.player.solidAreaDefaultX;
            gPanel.player.solidArea.y = gPanel.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;
    }

    public void teleport(GameState gameState) {

        GamePanel.gameState = gameState;
        gPanel.ui.setCurrentDialogue(new Dialogue("Teleportare!"));
        gPanel.player.worldX = gPanel.tileSize*37;
        gPanel.player.worldY = gPanel.tileSize*10;
    }

    public void damagePit(GameState gameState) {
        GamePanel.gameState = gameState;
        gPanel.player.invincible = true;
        gPanel.playSE("receivedamage.wav");
        gPanel.ui.setCurrentDialogue(new Dialogue("Ai cazut intr-o groapa!"));
        gPanel.player.life--;
//        eventRect[col][row].eventDone = true;
        canTouchEvent = false;
    }

    public void healingPool(GameState gameState) {

        if (gPanel.keyH.enterPressed) {
            if (gPanel.player.life < gPanel.player.maxLife) {
                GamePanel.gameState = gameState;
                gPanel.playSE("powerup.wav");
                gPanel.ui.setCurrentDialogue(new Dialogue("Bei din apa.\nViata ta este recuperata!\n(Progresul a fost salvat)"));
                gPanel.player.life = gPanel.player.maxLife;
                gPanel.assetPool.setMonster();
                gPanel.saveLoad.save();
            }
        }
    }

    public void teleport(int map, int col, int row, Area area) {

        GamePanel.gameState = GameState.TransitionState;
        gPanel.nextArea = area;

        tempMap = map;
        tempCol = col;
        tempRow = row;



//        gPanel.currentMap = map;
//
//        gPanel.player.worldX = gPanel.tileSize * col;
//        gPanel.player.worldY = gPanel.tileSize * row;
//
//        previousEventX = gPanel.player.worldX;
//        previousEventY = gPanel.player.worldY;

        gPanel.playSE("stairs.wav");
        canTouchEvent = false;
    }

    public void skeletonLord() {
        if (!gPanel.bossBattleOn && !GameProgress.skeletonLordDefeated) {
            GamePanel.gameState = GameState.CutSceneState;
            gPanel.cutSceneManager.sceneState = SceneState.Skeleton;
        }
    }
}
