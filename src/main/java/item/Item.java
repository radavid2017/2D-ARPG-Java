package item;

import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

public abstract class Item  extends SuperObject {

    TypeItem typeItem;
    CharacterClass playerClass;

    public Item(GamePanel gPanel, TypeItem typeItem, CharacterClass playerClass) {
        super(gPanel);
        this.typeItem = typeItem;
        this.playerClass = playerClass;
    }
}
