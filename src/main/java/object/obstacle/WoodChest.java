package object.obstacle;

import entity.Entity;
import game.GamePanel;
import item.Item;

import java.awt.*;

public class WoodChest extends OBJ_Chest{

    public WoodChest(GamePanel gPanel) {
        super(gPanel);
        name = "Cufarul Padurarului";
        objPath += "wood_chest";
        loadObject(gPanel);
    }

}
