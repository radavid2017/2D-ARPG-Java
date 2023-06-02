package object.obstacle;

import entity.Entity;
import game.GamePanel;
import item.Item;
import object.SuperObject;
import object.TypeMaterial;

import java.awt.*;

public class WoodChest extends OBJ_Chest{

    public WoodChest(GamePanel gPanel) {
        super(gPanel, TypeMaterial.Wood);
        name = "Cufarul Padurarului";
        objPath += "wood_chest";
        loadObject(gPanel);
    }

    @Override
    public SuperObject generateObject() {
        return new WoodChest(getGamePanel());
    }

}
