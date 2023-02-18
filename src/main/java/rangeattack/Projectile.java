package rangeattack;

import animations.TypeAnimation;
import damage.IDoDamage;
import damage.NormalDamage;
import entity.Entity;
import features.Direction;
import features.Sound;
import game.CharacterClass;
import game.GamePanel;
import item.Item;
import item.TypeItem;
import item.equipable.weapon.TypeWeapon;
import item.equipable.weapon.Weapon;

public abstract class Projectile extends Weapon {

    Entity user;

    public IDoDamage damageType;
    public String projectilePath = "res/item/projectile/";
    public TypeProjectile typeProjectile;
    public float durability;
    public float maxDurability;

    public Projectile (int damage, IDoDamage damageType, CharacterClass characterClass, GamePanel gp) {
        super(gp, TypeWeapon.Projectile, characterClass);
        typeAnimation = TypeAnimation.OBJECT;
        this.damage = damage;
        addDamageType(damageType);
    }

    public void set(double worldX, double worldY, Direction direction, boolean alive, Entity user) {

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.durability = this.maxDurability;
    }

    public void update() {

        // DETECTARE COLIZIUNE
        // proiectil aruncat de jucator
        if(user == getGamePanel().player) {
            int monsterIndex = getGamePanel().collisionDetector.checkEntity(this, getGamePanel().monsterList);
            if (monsterIndex > -1) {
                getGamePanel().player.doDamage(monsterIndex);
                alive = false;
            }
        }
        // proiectil aruncat de alte entitati
        if (user != getGamePanel().player) {

        }

        manageMovement();

//        System.out.println("DURABILITY: " + durability);
        durability--;
        if (durability < 0) {
            alive = false;
        }

        image = objAnimation.manageAnimations(this, direction);
        currentAnimation.updateFrames();
    }


}
