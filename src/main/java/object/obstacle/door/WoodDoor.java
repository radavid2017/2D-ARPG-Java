package object.obstacle.door;

import features.Dialogue;
import game.GamePanel;
import game.GameState;
import object.SuperObject;
import object.TypeMaterial;

public class WoodDoor extends OBJ_Door {
    public WoodDoor(GamePanel gPanel) {
        super(gPanel, TypeMaterial.Wood);
        name = "Usa de lemn";
        setImage("wood_door.png");
    }

    @Override
    public SuperObject generateObject() {
        return new WoodDoor(getGamePanel());
    }

    @Override
    public void interact() {
        GamePanel.gameState = GameState.Dialogue;
        getGamePanel().ui.setCurrentDialogue(new Dialogue("Ai nevoie de o cheie pentru a deschide usa"));
    }
}
