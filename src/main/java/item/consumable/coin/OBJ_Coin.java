package item.consumable.coin;

import entity.Entity;
import game.GamePanel;
import item.Consumable;
import item.consumable.TypeConsumable;

public class OBJ_Coin extends Consumable {

    int value = 1;

    public OBJ_Coin(GamePanel gPanel) {
        super(gPanel, TypeConsumable.Coin);
        name = "Bani";
        setImage("coin/coin_bronze.png");
        description = "O moneda";
    }

    @Override
    public void use() {
        getGamePanel().playSE("coin.wav");
        getGamePanel().ui.addMessage(name + " +" + value);
        getGamePanel().player.coin += value;
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
