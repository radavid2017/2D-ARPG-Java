package item.consumable;

import entity.Entity;
import game.GamePanel;
import game.GameState;
import item.Consumable;
import item.equipable.light.DayState;
import object.SuperObject;

import java.awt.image.BufferedImage;

public class Tent extends Consumable {

    public static BufferedImage Image;

    public Tent(GamePanel gPanel) {
        super(gPanel, TypeConsumable.Tent, 300);
        name = "Cort";
        setImage("tent/tent.png");
        description = "[" + name + "]\nPentru dormit noaptea.";
        stackable = true;
        Image = image;
    }

    @Override
    public SuperObject generateObject() {
        return new Tent(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {

    }

    @Override
    public boolean use(Entity user) {
        if (getGamePanel().getEnvironmentManager().getLighting().dayState == DayState.Dusk ||
                getGamePanel().getEnvironmentManager().getLighting().dayState == DayState.Night) {

            GamePanel.gameState = GameState.SleepState;
            getGamePanel().playSE("sleep.wav");

            if (getGamePanel().player.life < getGamePanel().player.maxLife) {
                getGamePanel().player.life = getGamePanel().player.maxLife;
            }

            if (getGamePanel().player.mana < getGamePanel().player.maxMana) {
                getGamePanel().player.mana = getGamePanel().player.maxMana;
            }

            return true;
        }

        return false;
    }
}
