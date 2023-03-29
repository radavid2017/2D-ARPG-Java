package item.equipable.weapon.rangeattack.spell;

import damage.IDoDamage;
import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import item.equipable.weapon.rangeattack.TypeProjectile;
import item.equipable.weapon.rangeattack.Projectile;

public abstract class Spell extends Projectile {

    public int useCost;

    public Spell(int damage, IDoDamage damageType, GamePanel gp, int price) {
        super(damage, damageType, CharacterClass.MAGE, gp, price);
        typeProjectile = TypeProjectile.Spell;
        objPath += "spell/";
    }

    public abstract boolean haveResource(Entity user);
    public abstract void subtractResource(Entity user);
}
