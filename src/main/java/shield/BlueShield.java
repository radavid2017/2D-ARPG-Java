package shield;

import defense.IDoDefense;
import defense.NormalDefense;
import game.CharacterClass;
import game.GamePanel;
import item.equipable.Shield;

public class BlueShield extends Shield {

    public BlueShield(GamePanel gp) {
        super(5, new NormalDefense(), gp, CharacterClass.ANY);
        loadObject(gp, shieldPath + "shield_blue.png");
        modelShield = ModelShield.BlueShield;
        name = "Scut Albastru";
        description = "[" + name + "]\nUn scut albastru stralucitor.";
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
