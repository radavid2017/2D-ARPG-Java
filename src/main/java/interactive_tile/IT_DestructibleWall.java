package interactive_tile;

import entity.Entity;
import entity.Player;
import game.GamePanel;
import item.Item;
import item.consumable.coin.Coin;
import item.consumable.potion.PotionRed;
import item.equipable.shield.BlueShield;
import item.equipable.weapon.TypeWeapon;
import object.SuperObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class IT_DestructibleWall extends DestructibleTile {

    public IT_DestructibleWall(GamePanel gp) {
        super(gp, TypeDestructibleTile.DestructibleWall);
        setImage("wall/destructiblewall.png");
        setDefaultSolidArea();
        life = 2;
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
        return player.currentWeapon.typeWeapon == TypeWeapon.Pickaxe;
    }

    @Override
    public void playSE() {
        getGamePanel().playSE("chipwall.wav");
    }

    @Override
    public ResultTile getDestroyedForm() {
        return null;
    }

    @Override
    public SuperObject generateObject() {
        return new IT_DestructibleWall(getGamePanel());
    }

    @Override
    public Color getParticleColor() {
        return new Color(65, 65, 65);
    }

    @Override
    public int getParticleSize() {
        return 6;
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 20;
    }

    private void replaceWithItem(Item item, ArrayList<Entity> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) {
                objects.set(i, item);
                return;
            }
        }
    }

    public void dropItem(Item item) {

        item.setPosition(worldX, worldY); // atribuim pozitia obiectului distrus (monstrul mort)

        replaceWithItem(item, getGamePanel().objects.get(getGamePanel().currentMap));

//        getGamePanel().objects.add(item);
    }

    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        // 50% sanse de a arunca un banut
        if (i < 50 && i > 25) {
            dropItem(new Coin(getGamePanel()));
        }
        // 25% sanse de a arunca potiune rosie
        if (i >= 50 && i < 75) {
            dropItem(new PotionRed(getGamePanel()));
        }
        // 25% sanse de a arunca un scut albstru
        if (i >= 75 && i < 100) {
            dropItem(new BlueShield(getGamePanel()));
        }
    }
}
