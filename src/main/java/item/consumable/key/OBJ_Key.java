package item.consumable.key;

import game.CharacterClass;
import game.GamePanel;
import item.Consumable;
import item.TypeItem;
import item.consumable.TypeConsumable;
import object.SuperObject;
import object.TypeObject;

public abstract class OBJ_Key extends Consumable {

    String keyPath = "res/item/consumable/keys/";
    KeyModel keyModel;

    public OBJ_Key(GamePanel gPanel) {
        super(gPanel, TypeConsumable.Key);
        this.typeObject = TypeObject.Key;
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
