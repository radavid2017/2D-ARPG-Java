package item.equipable.shield;

import defense.NormalDefense;
import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

public class BlueShield extends Shield {

    public BlueShield(GamePanel gp) {
        super(5, new NormalDefense(), gp, CharacterClass.ANY, 15);
        loadObject(gp, shieldPath + "shield_blue.png");
        modelShield = ModelShield.BlueShield;
        name = "Scut Albastru";
        description = "[" + name + "]\nScut albastru stralucitor.";
    }

    @Override
    public SuperObject generateObject() {
        return new BlueShield(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
