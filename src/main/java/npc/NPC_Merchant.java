package npc;

import animations.AnimationState;
import animations.StateMachine;
import animations.TypeAnimation;
import features.Direction;
import game.GamePanel;
import game.GameState;
import item.consumable.key.KeyGold;
import item.consumable.potion.PotionRed;
import item.equipable.shield.BlueShield;
import item.equipable.shield.NormalShield;
import item.equipable.weapon.axe.Baltag;
import item.equipable.weapon.sword.NormalSword;

import java.awt.*;

public class NPC_Merchant extends NPC{

    public NPC_Merchant(GamePanel gp) {
        super(gp, TypeNPC.Merchant);
        setupStaticIdle("res\\npc\\merchant");
        setDialogue();
        setItems();
    }

    @Override
    public void pathFinding() {

    }

    @Override
    public void update() {
        currentAnimation.updateFrames(this);
    }

    @Override
    public void setDefaultSolidArea() {

    }

    @Override
    public void setDialogue() {
        dialogue.addText("Haha! Bine ai venit, aventurierule! Eu sunt cel mai bun negustor\ndin zona!Haide sa-mi vezi marfa!");
    }

    @Override
    public void setItems() {

        inventory.add(new PotionRed(getGamePanel()));
        inventory.add(new KeyGold(getGamePanel()));
        inventory.add(new NormalSword(getGamePanel()));
        inventory.add(new Baltag(getGamePanel()));
        inventory.add(new NormalShield(getGamePanel()));
        inventory.add(new BlueShield(getGamePanel()));
    }

    @Override
    public void speak() {
        super.speak();
        GamePanel.gameState = GameState.TradeState;
        getGamePanel().ui.setMerchant(this);
    }
}
