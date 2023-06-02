package item.equipable.weapon.sword;

import damage.NormalDamage;
import game.GamePanel;
import object.SuperObject;

import java.io.Serializable;

public class NormalSword extends Sword {

    public NormalSword(GamePanel gp) {

        super(1, new NormalDamage(), gp, 6);
//        loadObject(gp, swordPath + "sword_normal.png");
        setImage("sword_normal.png");
        name = "Sabie Normala";
        description = "[" + name + "]\nO sabie veche.";
        modelSword = ModelSword.NormalSword;

        knockBackPower = 2;

        setAttackAreaValues();

    }

    @Override
    public SuperObject generateObject() {
        return new NormalSword(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {

    }

    @Override
    public void setAttackAreaValues() {
        attackArea.width = 36;
        attackArea.height = 36;
    }
}
