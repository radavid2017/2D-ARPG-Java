package item.equipable.weapon.pickaxe;

import animations.AnimationState;
import damage.IDoDamage;
import game.CharacterClass;
import game.GamePanel;
import item.equipable.weapon.TypeWeapon;
import item.equipable.weapon.Weapon;

public abstract class Pickaxe extends Weapon {

    ModelPickaxe modelPickaxe;

    public Pickaxe(int damage, IDoDamage damageType, GamePanel gp, int price) {
        super(gp, TypeWeapon.Pickaxe, CharacterClass.ANY, price);
        this.damage = damage;
        addDamageType(damageType);
        objPath += "pickaxe/";
        for (AnimationState state : animation.states) {
            state.motion1_duration = 10;
            state.motion2_duration = 20;
        }
    }

    @Override
    public void playSound() {
        getGamePanel().playSE("swingweapon.wav");
    }
}
