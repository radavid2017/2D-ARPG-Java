package item.consumable.potion;

import game.GamePanel;
import item.Consumable;
import item.consumable.TypeConsumable;

public class PotionRed extends OBJ_Potion {


    public PotionRed(GamePanel gPanel) {
        super(gPanel);
        name = "Licoare rosie";
        description = "[" + name + "]\nPotiune vindecatoare:\n+" + healingValue + " viata.";
        potionModel = PotionModel.PotionRed;
        setImage("potion_red.png");
    }

    @Override
    public void setHealingValue() {
        healingValue = 1;
    }


}
