package item.equipable.weapon.axe;

import damage.NormalDamage;
import game.GamePanel;
import object.SuperObject;

public class Baltag extends Axe {


    public Baltag(GamePanel gp) {

        super(2, new NormalDamage(), gp, 20);
//        loadObject(gp, axePath + "baltag.png");
        setImage("baltag.png");
        name = "Baltag";
        modelAxe = ModelAxe.Baltag;
        description = "[" + name + "]\nBaltagul lui Ghita.";

        knockBackPower = 10;

        setAttackAreaValues();
    }

    @Override
    public SuperObject generateObject() {
        return new Baltag(getGamePanel());
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
