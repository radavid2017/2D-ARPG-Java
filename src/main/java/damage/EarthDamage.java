package damage;

import entity.Entity;

import java.io.Serializable;

public class EarthDamage implements IDoDamage, Serializable {
    @Override
    public int doDamage(Entity entity, Entity target) {
        int totalDamage = entity.attack - target.defense;
        if (totalDamage < 0) {
            totalDamage = 0;
        }

        if (totalDamage != 0) {
            target.transparent = true;
        }

        if (target.life > 0) {
            target.life -= totalDamage;
        }
        if (target.life < 0) {
            target.life = 0;
        }
        return totalDamage;
    }
}
