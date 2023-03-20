package item.equipable.weapon.axe;

import damage.NormalDamage;
import game.GamePanel;

public class Baltag extends Axe {


    public Baltag(GamePanel gp) {

        super(2, new NormalDamage(), gp);
//        loadObject(gp, axePath + "baltag.png");
        setImage("baltag.png");
        name = "Baltag";
        modelAxe = ModelAxe.Baltag;
        description = "[" + name + "]\nBaltagul lui Ghita.";

        setAttackAreaValues();
    }

    @Override
    public void setDefaultSolidArea() {

    }

    @Override
    public void setAttackAreaValues() {
        attackArea.width = 30;
        attackArea.height = 30;
    }
}
