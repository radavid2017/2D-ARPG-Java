package damage;

import entity.Entity;
import game.GamePanel;

public class TouchableDamage implements IDoDamage {

    @Override
    public int doDamage(Entity entity, Entity target) {
        int totalDamage = entity.attack - target.defense;
        if (totalDamage < 0) {
            totalDamage = 0;
        }
        target.life -= totalDamage;
        return totalDamage;
    }
}
