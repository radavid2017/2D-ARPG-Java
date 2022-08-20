package entity;

import game.GamePanel;

public abstract class NPC extends Entity {

    public NPC(GamePanel gp) {
        super(gp);
    }

    public abstract void AI();

    public void update() {
        AI();
        super.update();
    }
}
