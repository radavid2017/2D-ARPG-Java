package object.obstacle;

import features.Dialogue;
import game.GamePanel;
import game.GameState;
import object.obstacle.Obstacle;

public abstract class OBJ_Door extends Obstacle {

    public OBJ_Door(GamePanel gPanel) {
        super(gPanel);
        this.isSolid = true;
        objPath += "door/";

        typeObstacle = TypeObstacle.Door;

        setDefaultSolidArea();
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.x = 0;
        solidArea.y = getGamePanel().tileSize/3;
        solidArea.width = getGamePanel().tileSize;
        solidArea.height = (int) (getGamePanel().tileSize/1.5);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void interact() {
        GamePanel.gameState = GameState.Dialogue;
        getGamePanel().ui.setCurrentDialogue(new Dialogue("Ai nevoie de o cheie pentru a deschide usa"));
    }
}
