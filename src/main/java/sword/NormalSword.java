package sword;

import damage.IDoDamage;
import damage.NormalDamage;
import game.GamePanel;

public class NormalSword extends Sword {

    public NormalSword(GamePanel gp) {

        super(1, new NormalDamage(), gp);
        loadObject(gp, swordPath + "sword_normal.png");
        name = "Sabie Normala";
        description = "[" + name + "]\nO sabie veche.";
        modelSword = ModelSword.NormalSword;
        setAttackAreaValues();

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
