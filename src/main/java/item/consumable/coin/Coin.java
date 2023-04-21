package item.consumable.coin;

import entity.Entity;
import game.GamePanel;
import item.Consumable;
import item.consumable.TypeConsumable;

public class Coin extends Consumable {

    int value = 1;

    public Coin(GamePanel gPanel) {
        super(gPanel, TypeConsumable.Coin, 0);
        name = "Bani";
        setImage("coin/coin_bronze.png");
        description = "O moneda";
    }

    @Override
    public boolean use(Entity user) {
        getGamePanel().playSE("coin.wav");
        getGamePanel().ui.addMessage(name + " +" + value);
        getGamePanel().player.coin += value;
        return true;
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
