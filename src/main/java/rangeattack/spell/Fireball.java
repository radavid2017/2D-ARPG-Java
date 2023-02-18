package rangeattack.spell;

import damage.FireDamage;
import features.Sound;
import game.GamePanel;
import rangeattack.Projectile;

public class Fireball extends Spell {

    public Fireball(GamePanel gp) {
        super(2, new FireDamage(), gp);

        name = "Minge de foc";
        speed = 6.5f;
        durability = maxDurability = 80;
//        damageType = new FireDamage();
        // damage = 2;
        useCost = 1;
        alive = false;
        setupObjectAnimation(spellPath + "fireball");
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public void playSound() {
        getGamePanel().playSE("burning.wav");
    }

    @Override
    public void setAttackAreaValues() {

    }
}
