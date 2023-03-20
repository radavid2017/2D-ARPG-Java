package particles;

import entity.Entity;

import java.awt.Color;

public interface Atomic {
    public Color getParticleColor();
    public int getParticleSize();
    public int getParticleSpeed();
    public int getParticleMaxLife();

    public void generateParticle(Entity target);
}
