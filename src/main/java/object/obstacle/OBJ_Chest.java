package object.obstacle;

import entity.Entity;
import features.Dialogue;
import game.GamePanel;
import game.GameState;
import item.Item;
import object.TypeObject;
import object.obstacle.Obstacle;

public abstract class OBJ_Chest extends Obstacle {

    Item loot;
    boolean opened = false;

    public OBJ_Chest(GamePanel gPanel, Item loot) {
        super(gPanel);
        typeObstacle = TypeObstacle.Chest;
        this.isSolid = true;
        this.setSolidArea(14, 8, 74, 62);
        objPath += "chest/";
        name = "cufar";
        this.loot = loot;
        setDefaultSolidArea();
    }

    @Override
    public void setDefaultSolidArea() {
        this.setSolidArea(14, 8, 74, 62);
    }

    @Override
    public void interact() {
        if (!opened) {
            GamePanel.gameState = GameState.Dialogue;
            getGamePanel().playSE("unlock.wav");

            StringBuilder sb = new StringBuilder();
            sb.append("Ai gasit " + loot.name + "!");

            if (!getGamePanel().player.obtainItem(loot)) {
                sb.append("\n...Dar nu ai spatiu inventar!");
            }
            else {
                sb.append("\nAi obtinut " + loot.name + "!");
                image = nextState();
                opened = true;
            }
            getGamePanel().ui.setCurrentDialogue(new Dialogue(sb.toString()));
        }
    }
}
