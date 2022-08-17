package features;

import game.GamePanel;
import game.GameState;

import java.awt.*;

public class EventHandler {

    GamePanel gPanel;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    public EventHandler(GamePanel gPanel) {
        this.gPanel = gPanel;

        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {

//        if (hit(27, 16, Direction.RIGHT)) {
//            damagePit(GameState.Dialogue);
//        }
        if (hit(27, 16, Direction.RIGHT)) {
            teleport(GameState.Dialogue);
        }
        if (hit(23, 12, Direction.UP)) {
            healingPool(GameState.Dialogue);
        }
    }

    /** Coliziune player cu arie de declansare eveniment */
    public boolean hit(int eventCol, int eventRow, Direction reqDirection) {

        boolean hit = false;

        gPanel.player.solidArea.x = (int) (gPanel.player.worldX + gPanel.player.solidArea.x);
        gPanel.player.solidArea.y = (int) (gPanel.player.worldY + gPanel.player.solidArea.y);
        eventRect.x = eventCol * gPanel.tileSize + eventRect.x;
        eventRect.y = eventRow * gPanel.tileSize + eventRect.y;

        if (gPanel.player.solidArea.intersects(eventRect)) {
            if (gPanel.player.direction.equals(reqDirection) || reqDirection.equals(Direction.ANY)) {
                hit = true;
            }
        }

        gPanel.player.solidArea.x = gPanel.player.solidAreaDefaultX;
        gPanel.player.solidArea.y = gPanel.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

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
        gPanel.ui.setCurrentDialogue(new Dialogue("Ai cazut intr-o groapa!"));
        gPanel.player.life--;
    }

    public void healingPool(GameState gameState) {

        if (gPanel.keyH.enterPressed) {
            GamePanel.gameState = gameState;
            gPanel.ui.setCurrentDialogue(new Dialogue("Bei din apa.\nViata ta este recuperata!"));
            gPanel.player.life = gPanel.player.maxLife;
        }
    }
}
