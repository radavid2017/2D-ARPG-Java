package item.equipable.weapon.axe;

import damage.IDoDamage;
import game.CharacterClass;
import game.GamePanel;
import item.equipable.weapon.Weapon;
import item.equipable.weapon.TypeWeapon;

public abstract class Axe extends Weapon {

    public String axePath = "res/item/equipable/weapon/axe/";
    ModelAxe modelAxe;

    public Axe(int damage, IDoDamage damageType, GamePanel gp, int price) {
        super(gp, TypeWeapon.Axe, CharacterClass.ANY, price);
        this.damage = damage;
        addDamageType(damageType);
        objPath += "axe/";
    }

    @Override
    public void playSound() {
        getGamePanel().playSE("swingweapon.wav");
    }
}
