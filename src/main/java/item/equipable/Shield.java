package item.equipable;

import damage.IDoDamage;
import defense.IDoDefense;
import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import item.Equipable;
import item.Item;
import item.TypeItem;
import object.SuperObject;
import object.TypeObject;
import shield.ModelShield;

import java.util.ArrayList;
import java.util.List;

public abstract class Shield extends Equipable {
    public int defense = 0;
    public List<IDoDefense> defenseTypes = new ArrayList<>();
    public String shieldPath = "res/item/equipable/shield/";
    public ModelShield modelShield;

    public Shield (int defense, IDoDefense defenseType, GamePanel gp, CharacterClass playerClass) {
        super(gp, TypeEquipable.Shield, playerClass);
        this.defense = defense;
        addDefenseType(defenseType);
        typeObject = TypeObject.Item;
    }

    public void addDefenseType(IDoDefense defenseType) {
        if (defenseType != null)
            defenseTypes.add(defenseType);
    }

    public void setDefenseType(int index, IDoDefense defenseType) {
        for (int i = 0; i < defenseTypes.size(); i++) {
            if (i == index)
                if (defenseTypes.get(i) != null)
                    defenseTypes.set(index, defenseType);
        }
    }
}
