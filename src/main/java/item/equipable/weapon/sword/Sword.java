package item.equipable.weapon.sword;

import damage.IDoDamage;
import game.CharacterClass;
import item.equipable.weapon.Weapon;
import game.GamePanel;
import item.equipable.weapon.TypeWeapon;

public abstract class Sword extends Weapon {
    public ModelSword modelSword;

    public Sword (int damage, IDoDamage damageType, GamePanel gp, int price) {
        super(gp, TypeWeapon.Sword, CharacterClass.WARRIOR, price);
        this.damage = damage;
        addDamageType(damageType);
        objPath += "sword/";
    }

    @Override
    public void playSound() {
        getGamePanel().playSE("swingweapon.wav");
    }
}
