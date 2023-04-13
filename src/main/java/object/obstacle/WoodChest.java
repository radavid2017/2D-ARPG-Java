package object.obstacle;

import entity.Entity;
import game.GamePanel;
import item.Item;

import java.awt.*;

public class WoodChest extends OBJ_Chest{

    public WoodChest(GamePanel gPanel, Item loot) {
        super(gPanel, loot);
        name = "Cufarul Padurarului";
        objPath += "wood_chest";
        loadObject(gPanel);
    }

}
