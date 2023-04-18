package item;

import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;
import object.TypeObject;

public abstract class Item  extends SuperObject {

    public TypeItem typeItem;
    public int price;
    public boolean stackable = false;
    public int amount = 1;

    public Item(GamePanel gPanel, TypeItem typeItem, int price) {
        super(gPanel);
        this.typeItem = typeItem;
        typeObject = TypeObject.Item;
        objPath = "res/item/";

        this.price = price;
    }

}
