package interactive_tile;

import entity.Entity;
import entity.Player;
import particles.Particle;
import game.GamePanel;
import item.equipable.weapon.TypeWeapon;

import java.awt.*;

public class IT_DryTree extends DestructibleTile {

    public IT_DryTree(GamePanel gp) {
        super(gp, TypeDestructibleTile.DryTree);
        setImage("tree/drytree.png");
        setDefaultSolidArea();
        life = 3;
    }

    @Override
    public void setDefaultSolidArea() {
        solidAreaDefaultY = getGamePanel().tileSize/6;
        solidAreaDefaultX = getGamePanel().tileSize/100;
        solidArea.height = (int) (getGamePanel().tileSize/1.5f);
        solidArea.width = getGamePanel().tileSize;
    }

    @Override
    public boolean isCorrectItem(Player player) {
        return player.currentWeapon.typeWeapon == TypeWeapon.Axe;
    }

    @Override
    public void playSE() {
        getGamePanel().playSE("cuttree.wav");
    }

    @Override
    public ResultTile getDestroyedForm() {
        ResultTile resultTile = new IT_Trunk(getGamePanel());
        resultTile.setPosition(worldX, worldY);
        return resultTile;
    }

    public Color getParticleColor() {
        return new Color(65, 50, 30);
    }

    public int getParticleSize() {
        return 6; // 6 pixeli
    }

    public int getParticleSpeed() {
        return 1; // cat de repede poate zbura particula
    }

    public int getParticleMaxLife() {
        return 20; // durabilitatea particulei
    }
}
