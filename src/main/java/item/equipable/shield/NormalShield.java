package item.equipable.shield;

import defense.NormalDefense;
import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

public class NormalShield extends Shield {


    public NormalShield(GamePanel gp) {
        super(4, new NormalDefense(), gp, CharacterClass.ANY, 7);
        loadObject(gp, shieldPath + "shield_wood.png");
        modelShield = ModelShield.NormalShield;
        name = "Scut Normal";
        description = "[" + name + "]\nFacut din lemn.";
    }

    @Override
    public SuperObject generateObject() {
        return new NormalShield(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
