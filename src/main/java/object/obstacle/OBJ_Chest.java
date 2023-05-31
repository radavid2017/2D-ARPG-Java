package object.obstacle;

import features.Dialogue;
import game.GamePanel;
import game.GameState;
import item.Item;
import item.consumable.Tent;
import item.consumable.key.KeyGold;
import item.consumable.potion.PotionRed;
import item.equipable.light.Lantern;
import item.equipable.shield.BlueShield;
import item.equipable.shield.NormalShield;
import item.equipable.weapon.axe.Baltag;
import item.equipable.weapon.sword.NormalSword;

import java.util.Random;

public abstract class OBJ_Chest extends Obstacle {

    Item loot;
    boolean opened = false;

    public OBJ_Chest(GamePanel gPanel) {
        super(gPanel);
        typeObstacle = TypeObstacle.Chest;
        this.isSolid = true;
        this.setSolidArea(14, 8, 74, 62);
        objPath += "chest/";
        name = "cufar";
//        this.loot = loot;
        setRandomLoot();
        setDefaultSolidArea();
    }

    public void setLoot(Item loot) {
        this.loot = loot;
    }

    public void setRandomLoot() {
        Random random = new Random();
        switch (random.nextInt(100)+1) {
            case 1 -> this.loot = new PotionRed(getGamePanel());
            case 2 -> this.loot = new BlueShield(getGamePanel());
            case 3 -> this.loot = new NormalShield(getGamePanel());
            case 4 -> this.loot = new Baltag(getGamePanel());
            case 5 -> this.loot = new NormalSword(getGamePanel());
            case 6 -> this.loot = new Lantern(getGamePanel());
            case 7 -> this.loot = new Tent(getGamePanel());
            default -> this.loot = new KeyGold(getGamePanel());
        }
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

    public Item getLoot() {
        return this.loot;
    }

    public boolean getIsOpened() {
        return this.opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
