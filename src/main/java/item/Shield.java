package item;

import damage.IDoDamage;
import defense.IDoDefense;
import entity.Entity;
import game.CharacterClass;
import game.GamePanel;
import object.SuperObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Shield extends Item {
    public int defense = 0;
    public List<IDoDefense> defenseTypes = new ArrayList<>();
    public String shieldPath = "res/item/shield/";

    public Shield (int defense, IDoDefense defenseType, GamePanel gp, TypeItem typeItem, CharacterClass playerClass) {
        super(gp, typeItem, playerClass);
        this.defense = defense;
        addDefenseType(defenseType);
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
