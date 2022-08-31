package sword;

import damage.IDoDamage;
import damage.NormalDamage;
import game.GamePanel;

public class NormalSword extends Sword {

    public NormalSword(GamePanel gp) {

        super(10, new NormalDamage(), gp);
        loadObject(gp, swordPath + "sword_normal.png");
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
