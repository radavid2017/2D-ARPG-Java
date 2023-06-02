package object.obstacle.door;

import features.Dialogue;
import game.GamePanel;
import game.GameState;
import object.SuperObject;
import object.TypeMaterial;

public class IronDoor extends OBJ_Door {

    public IronDoor(GamePanel gPanel) {
        super(gPanel, TypeMaterial.Iron);
        name = "Usa din fier";
        setImage("iron_door.png");
    }

    @Override
    public void interact() {
        GamePanel.gameState = GameState.Dialogue;
        getGamePanel().ui.setCurrentDialogue(new Dialogue("Nu se va clinti"));
    }

    @Override
    public SuperObject generateObject() {
        return new IronDoor(getGamePanel());
    }
}
