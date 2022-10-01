package sword;

import damage.IDoDamage;
import game.CharacterClass;
import item.TypeItem;
import item.equipable.Weapon;
import game.GamePanel;
import item.equipable.weapon.TypeWeapon;

public abstract class Sword extends Weapon {

    public String swordPath = "res/item/equipable/weapon/sword/";
    public ModelSword modelSword;

    public Sword (int damage, IDoDamage damageType, GamePanel gp) {
        super(gp, TypeWeapon.Sword, CharacterClass.WARRIOR);
        this.damage = damage;
        addDamageType(damageType);
    }
}
