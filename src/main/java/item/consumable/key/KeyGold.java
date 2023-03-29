package item.consumable.key;

import entity.Entity;
import game.GamePanel;
import item.TypeItem;

public class KeyGold extends OBJ_Key {
    public KeyGold(GamePanel gPanel) {
        super(gPanel, 100);
        name = "Cheie de aur";
        description = "[" + name + "]\nDeschide o usa.";
        this.setImage("key.png");
        keyModel = KeyModel.KeyGold;
    }

    @Override
    public void use() {

    }
}
