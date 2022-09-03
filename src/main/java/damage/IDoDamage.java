package damage;

import entity.Entity;
import game.GamePanel;

public interface IDoDamage {

    int doDamage(Entity entity, Entity target);
}
