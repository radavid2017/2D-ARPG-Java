package item;

import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import item.consumable.TypeConsumable;

public abstract class Consumable extends Item{

    TypeConsumable typeConsumable;

    public Consumable(GamePanel gPanel, TypeConsumable typeConsumable) {
        super(gPanel, TypeItem.Consumable);
        this.typeConsumable = typeConsumable;
        objPath += "consumable/";
    }

    public abstract void use();
}
