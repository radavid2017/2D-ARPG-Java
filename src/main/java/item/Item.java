package item;

import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

public abstract class Item  extends SuperObject {

    public TypeItem typeItem;

    public Item(GamePanel gPanel, TypeItem typeItem) {
        super(gPanel);
        this.typeItem = typeItem;
        objPath = "res/item/";
    }

}
