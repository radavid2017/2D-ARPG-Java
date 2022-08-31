package sword;

import damage.IDoDamage;
import game.CharacterClass;
import item.TypeItem;
import item.Weapon;
import game.GamePanel;

public abstract class Sword extends Weapon {

    public String swordPath = "res/item/weapon/sword/";

    public Sword (int damage, IDoDamage damageType, GamePanel gp) {
        super(gp, TypeItem.Weapon, CharacterClass.WARRIOR);
        this.damage = damage;
        addDamageType(damageType);
    }
}
