package item;

import damage.IDoDamage;
import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Weapon extends Item {

    public int damage = 0;
    public List<IDoDamage> damageTypes = new ArrayList<>();
    public String weaponPath = "res/item/weapon/";

    public Weapon(GamePanel gPanel, TypeItem typeItem, CharacterClass playerClass) {
        super(gPanel, typeItem, playerClass);
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

}
