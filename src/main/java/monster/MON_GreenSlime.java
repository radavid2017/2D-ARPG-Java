package monster;

import entity.AttackStyle;
import features.Direction;
import game.GamePanel;
import item.consumable.coin.OBJ_Coin;
import item.consumable.potion.PotionRed;
import item.equipable.shield.BlueShield;
import item.equipable.weapon.rangeattack.Projectile;
import item.equipable.weapon.rangeattack.Rock;

import java.util.Random;
import java.util.random.RandomGenerator;

public class MON_GreenSlime extends Monster{

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        name = "Green Slime";

        exp = 2;

        direction = Direction.DOWN;

        timeToChangeInMotion = RandomGenerator.getDefault().nextInt(120)+1;
        timeToChangeDirection = RandomGenerator.getDefault().nextInt(220)+timeToChangeInMotion;

        typeMonster = TypeMonster.GreenSlime;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 4;
        life = maxLife;

        attackStyle = AttackStyle.Touching;
        attack = 5;
        defense = 0;
        this.weapon = new Rock(gp);

        setDefaultSolidArea();

        // setare animatii
        setupMovement("res\\monster\\greenSlime");
        setupIdle("res\\monster\\greenSlime");
    }

    @Override
    public void offensiveBehaviour() {
        int i = new Random().nextInt(100)+1;
        if (i > 99 && !weapon.alive && shotAvailableCounter == shotAvailableTrigger) {
            weapon.set(worldX, worldY, direction, true, this);
            getGamePanel().projectileList.add(weapon);
            shotAvailableCounter = 0;
        }
    }

    public void AI() {

        super.AI();

    }

    public void update() {
        super.update();
        if (shotAvailableCounter < shotAvailableTrigger) {
            shotAvailableCounter++;
        }

        int xDistance = (int) Math.abs(worldX - getGamePanel().player.worldX);
        int yDistance = (int) Math.abs(worldY - getGamePanel().player.worldY);
        int tileDistance = (xDistance + yDistance)/getGamePanel().tileSize;

        if (!onPath && tileDistance < 5) {
            int i = new Random().nextInt(100)+1;
            if (i > 50) {
                onPath = true; // 50% sanse sa devina aggro daca player-ul e aproape de entitate
            }
        }

        if (onPath && tileDistance > 8) {
            onPath = false;
        }
    }

    @Override
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        // 50% sanse de a arunca un banut
        if (i < 50) {
            dropItem(new OBJ_Coin(getGamePanel()));
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

    @Override
    public void setDefaultSolidArea() {
        solidArea.x = getGamePanel().tileSize / 16;
        solidArea.y = (int) (getGamePanel().tileSize / 2.66);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = (int) (getGamePanel().tileSize / 1.15);
        solidArea.height = (int) (getGamePanel().tileSize / 1.6);
    }
}
