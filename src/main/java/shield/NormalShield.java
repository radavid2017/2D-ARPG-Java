package shield;

import defense.NormalDefense;
import game.CharacterClass;
import game.GamePanel;
import item.Shield;
import item.TypeItem;

public class NormalShield extends Shield {


    public NormalShield(GamePanel gp) {
        super(10, new NormalDefense(), gp, TypeItem.Shield, CharacterClass.ANY);
        loadObject(gp, shieldPath + "shield_wood.png");
    }

    @Override
    public void setDefaultSolidArea() {

    }
}