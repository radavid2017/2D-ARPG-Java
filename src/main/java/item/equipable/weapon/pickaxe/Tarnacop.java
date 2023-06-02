package item.equipable.weapon.pickaxe;

import damage.IDoDamage;
import damage.NormalDamage;
import game.GamePanel;
import object.SuperObject;

public class Tarnacop extends Pickaxe {

    public Tarnacop(GamePanel gp) {
        super(2, new NormalDamage(), gp, 400);
        setImage("tarnacop.png");
        name = "Tarnacop";
        modelPickaxe = ModelPickaxe.Tarnacop;
        description = "[" + name +"]\nSe poate mineri cu el.";

        knockBackPower = 6;
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

    @Override
    public SuperObject generateObject() {
        return new Tarnacop(getGamePanel());
    }
}
