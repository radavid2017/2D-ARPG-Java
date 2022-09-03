package features;

import game.GamePanel;
import game.GameState;

import java.awt.*;

public class EventHandler {

    GamePanel gPanel;
    EventRect[][] eventRect;

    double previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gPanel) {
        this.gPanel = gPanel;

        eventRect = new EventRect[gPanel.maxWorldCol][gPanel.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < gPanel.maxWorldCol && row < gPanel.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gPanel.maxWorldCol) {
                col = 0;
                row++;
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
            int col, row;
//        if (hit(27, 16, Direction.RIGHT)) {
//            teleport(GameState.Dialogue);
//        }
            col = 27; row = 16;
            if (hit(col, row, Direction.RIGHT)) {
                damagePit(col, row, GameState.Dialogue);
            }
            col = 23; row = 12;
            if (hit(col, row, Direction.UP)) {
                healingPool(GameState.Dialogue);
            }
        }
    }

    /** Coliziune player cu arie de declansare eveniment */
    public boolean hit(int col, int row, Direction reqDirection) {

        boolean hit = false;

        gPanel.player.solidArea.x = (int) (gPanel.player.worldX + gPanel.player.solidArea.x);
        gPanel.player.solidArea.y = (int) (gPanel.player.worldY + gPanel.player.solidArea.y);
        eventRect[col][row].x = col * gPanel.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gPanel.tileSize + eventRect[col][row].y;

        if (gPanel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gPanel.player.direction.equals(reqDirection) || reqDirection.equals(Direction.ANY)) {
                hit = true;

                previousEventX = gPanel.player.worldX;
                previousEventY = gPanel.player.worldY;
            }
        }

        gPanel.player.solidArea.x = gPanel.player.solidAreaDefaultX;
        gPanel.player.solidArea.y = gPanel.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void teleport(GameState gameState) {

        GamePanel.gameState = gameState;
        gPanel.ui.setCurrentDialogue(new Dialogue("Teleportare!"));
        gPanel.player.worldX = gPanel.tileSize*37;
        gPanel.player.worldY = gPanel.tileSize*10;
    }

    public void damagePit(int col, int row, GameState gameState) {
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
                gPanel.ui.setCurrentDialogue(new Dialogue("Bei din apa.\nViata ta este recuperata!"));
                gPanel.player.life = gPanel.player.maxLife;
                gPanel.assetPool.setMonster();
            }
        }
    }
}
