package interactive_tile;

import entity.Entity;
import entity.Player;
import game.GamePanel;
import particles.Atomic;

import java.awt.*;

public abstract class DestructibleTile extends InteractiveTile implements Atomic {

    TypeDestructibleTile typeDestructibleTile;

    public DestructibleTile(GamePanel gp, TypeDestructibleTile typeDestructibleTile) {
        super(gp, TypeInteractiveTile.DestructibleTile);
        this.typeDestructibleTile = typeDestructibleTile;
        invincibleTime = 20;
    }

    public abstract boolean isCorrectItem(Player player);
    public abstract void playSE();
    public abstract ResultTile getDestroyedForm();

    public void update() {
        manageInvincible();
    }

    public void generateParticle(Entity target) {
        super.generateParticle(target, getParticleColor(), getParticleSize(), getParticleSpeed(), getParticleMaxLife());
    }
}
