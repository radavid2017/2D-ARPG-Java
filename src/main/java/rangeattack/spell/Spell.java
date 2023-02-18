package rangeattack.spell;

import damage.IDoDamage;
import game.CharacterClass;
import game.GamePanel;
import rangeattack.Projectile;
import rangeattack.TypeProjectile;

public abstract class Spell extends Projectile {

    public int useCost;
    public String spellPath = projectilePath + "spell/";

    public Spell(int damage, IDoDamage damageType, GamePanel gp) {
        super(damage, damageType, CharacterClass.MAGE, gp);
        typeProjectile = TypeProjectile.Spell;
    }
}
