package item.consumable.potion;

import entity.Entity;
import features.Dialogue;
import game.GamePanel;
import game.GameState;
import item.Consumable;
import item.TypeItem;
import item.consumable.TypeConsumable;
import item.consumable.key.KeyModel;

public abstract class OBJ_Potion extends Consumable {
    PotionModel potionModel;
    int healingValue;

    public OBJ_Potion(GamePanel gPanel, int price) {
        super(gPanel, TypeConsumable.Potion, price);
        setHealingValue();
        objPath += "potions/";
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public abstract void setHealingValue();

    @Override
    public boolean use(Entity user) {

        if (user.life != user.maxLife) {
            GamePanel.gameState = GameState.Dialogue;
            getGamePanel().ui.setCurrentDialogue(new Dialogue("Ai consumat " + name + "!\n" +
                    "Incepi sa te vindeci..."));

            getGamePanel().player.life += healingValue;

            if (getGamePanel().player.life > getGamePanel().player.maxLife) {
                getGamePanel().player.life = getGamePanel().player.maxLife;
            }
            getGamePanel().playSE("powerup.wav");
            return true;
        }
        return false;
    }
}
