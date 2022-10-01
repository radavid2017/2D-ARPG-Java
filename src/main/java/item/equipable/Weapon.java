package item.equipable;

import damage.IDoDamage;
import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import item.Equipable;
import item.Item;
import item.TypeItem;
import item.equipable.weapon.TypeWeapon;
import object.SuperObject;
import object.TypeObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Weapon extends Equipable {

    public int damage = 0;
    public List<IDoDamage> damageTypes = new ArrayList<>();
    public String weaponPath = "res/item/equipable/weapon/";
    public TypeWeapon typeWeapon;

    public Weapon(GamePanel gPanel, TypeWeapon typeWeapon,CharacterClass playerClass) {
        super(gPanel, TypeEquipable.Weapon, playerClass);
        this.typeWeapon = typeWeapon;
        typeObject = TypeObject.Item;
    }

    public Integer tryDoAttack(Entity entity, Entity target) {
        for (IDoDamage damage : damageTypes) {
            if (damage != null)
                return damage.doDamage(entity, target);
        }
        return null;
    }

    public void addDamageType(IDoDamage damageType) {
        if (damageType != null)
            damageTypes.add(damageType);
    }

    public void setDamageType(int index, IDoDamage damageType) {
        for (int i = 0; i < damageTypes.size(); i++) {
            if (i == index)
                if (damageTypes.get(i) != null)
                    damageTypes.set(index, damageType);
        }
    }

    public abstract void setAttackAreaValues();

}
