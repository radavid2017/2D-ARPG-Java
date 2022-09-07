package shield;

import defense.NormalDefense;
import game.CharacterClass;
import game.GamePanel;
import item.Shield;
import item.TypeItem;

public class NormalShield extends Shield {


    public NormalShield(GamePanel gp) {
        super(4, new NormalDefense(), gp, TypeItem.Shield, CharacterClass.ANY);
        loadObject(gp, shieldPath + "shield_wood.png");
        name = "Scut Normal";
        description = "[" + name + "]\nFacut din lemn.";
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
