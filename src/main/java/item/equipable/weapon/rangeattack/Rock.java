package item.equipable.weapon.rangeattack;

import damage.EarthDamage;
import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

import java.awt.*;

public class Rock extends Projectile {

    public Rock(GamePanel gp) {
        super(2, new EarthDamage(), CharacterClass.ANY, gp, 0);

        name = "Piatra";
        speed = 6.5f;
        durability = maxDurability = 80;
//        damageType = new FireDamage();
        // damage = 2;
//        useCost = 1;
        alive = false;
        setupAnimation(objPath + "rock");
        setDefaultSolidArea();
    }

    @Override
    public SuperObject generateObject() {
        return new Rock(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {
        solidAreaDefaultX = getGamePanel().tileSize/4;
        solidAreaDefaultY = getGamePanel().tileSize/4;
        solidArea.width = getGamePanel().tileSize/2;
        solidArea.height = getGamePanel().tileSize/2;
    }

    public void playSound() {
        getGamePanel().playSE("burning.wav");
    }

    @Override
    public void setAttackAreaValues() {

    }

    @Override
    public Color getParticleColor() {
        return new Color(40, 50, 0);
    }

    @Override
    public int getParticleSize() {
        return 10;
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
