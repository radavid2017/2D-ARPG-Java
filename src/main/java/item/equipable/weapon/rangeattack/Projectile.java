package item.equipable.weapon.rangeattack;

import animations.TypeAnimation;
import damage.IDoDamage;
import entity.Entity;
import features.Direction;
import game.CharacterClass;
import game.GamePanel;
import item.equipable.weapon.TypeWeapon;
import item.equipable.weapon.Weapon;
import particles.Atomic;
import particles.Particle;

public abstract class Projectile extends Weapon implements Atomic {

    Entity user;

    public TypeProjectile typeProjectile;
    public float durability;
    public float maxDurability;

    public Projectile (int damage, IDoDamage damageType, CharacterClass characterClass, GamePanel gp, int price) {
        super(gp, TypeWeapon.Projectile, characterClass, price);
        typeAnimation = TypeAnimation.OBJECT;
        this.damage = damage;
        addDamageType(damageType);
        objPath += "projectile/";
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
                getGamePanel().player.doDamageToMonster(monsterIndex);
                this.generateParticle(getGamePanel().monsterList[getGamePanel().currentMap][monsterIndex]);
                alive = false;
            }
        }
        // proiectil aruncat de alte entitati
        if (user != getGamePanel().player) {
            boolean contactPlayer = getGamePanel().collisionDetector.checkPlayer(this);
            if (!getGamePanel().player.invincible && contactPlayer) {
//                doDamage(getGamePanel().player);
                tryDoAttack(user, getGamePanel().player);
                this.generateParticle(getGamePanel().player);
                getGamePanel().player.contactProjectile();
                alive = false;
            }
        }

        manageMovement();

//        System.out.println("DURABILITY: " + durability);
        durability--;
        if (durability < 0) {
            alive = false;
        }

        image = animation.manageAnimations(this, direction);
        currentAnimation.updateFrames();
    }

    public void generateParticle(Entity target) {
        super.generateParticle(target, getParticleColor(), getParticleSize(), getParticleSpeed(), getParticleMaxLife());
    }

//    public int doDamage(Entity target) {
//        int totalDamage = damage - target.defense;
//        if (totalDamage < 0) {
//            totalDamage = 1;
//        }
//        target.life -= totalDamage;
//        return totalDamage;
//    }

}
