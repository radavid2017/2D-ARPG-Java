package shield;

import defense.NormalDefense;
import game.CharacterClass;
import game.GamePanel;
import item.equipable.Shield;
import item.TypeItem;

public class NormalShield extends Shield {


    public NormalShield(GamePanel gp) {
        super(4, new NormalDefense(), gp, CharacterClass.ANY);
        loadObject(gp, shieldPath + "shield_wood.png");
        modelShield = ModelShield.NormalShield;
        name = "Scut Normal";
        description = "[" + name + "]\nFacut din lemn.";
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
