package item.consumable.key;

import entity.Entity;
import features.Dialogue;
import game.GamePanel;
import game.GameState;
import item.TypeItem;
import object.obstacle.TypeObstacle;

public class KeyGold extends OBJ_Key {
    public KeyGold(GamePanel gPanel) {
        super(gPanel, 100);
        name = "Cheie de aur";
        description = "[" + name + "]\nDeschide o usa.";
        this.setImage("key.png");
        keyModel = KeyModel.KeyGold;
    }

    @Override
    public boolean use(Entity user) {
        int objIndex = getDetected(user, getGamePanel().objects.get(getGamePanel().currentMap), TypeObstacle.Door);

        if (objIndex > -1) {
            GamePanel.gameState = GameState.Dialogue;
            getGamePanel().ui.setCurrentDialogue(new Dialogue("Ai deschis usa"));
            getGamePanel().playSE("unlock.wav");
            getGamePanel().objects.get(getGamePanel().currentMap).remove(objIndex);
            return true;
        }

        return false;
    }
}
