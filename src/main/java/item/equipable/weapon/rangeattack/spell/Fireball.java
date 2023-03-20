package item.equipable.weapon.rangeattack.spell;

import damage.FireDamage;
import entity.Entity;
import game.GamePanel;

import java.awt.*;

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
        setupAnimation(objPath + "fireball");
    }

    @Override
    public boolean haveResource(Entity user) {
        return user.mana >= useCost;
    }

    public void subtractResource(Entity user) {
        user.mana -= useCost;
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

    @Override
    public Color getParticleColor() {
        return new Color(240, 50, 0);
    }

    @Override
    public int getParticleSize() {
        return 10; // pixeli
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 20;
    }
}
