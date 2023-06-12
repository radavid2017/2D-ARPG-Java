package monster;

import entity.AttackStyle;
import features.Direction;
import game.GamePanel;
import item.consumable.coin.Coin;
import item.consumable.potion.PotionRed;
import item.equipable.shield.BlueShield;
import item.equipable.weapon.rangeattack.Rock;

import java.util.Random;
import java.util.random.RandomGenerator;

public class MON_Bat extends NonStaticMonster {

    public MON_Bat(GamePanel gp) {
        super(gp);

        name = "Liliac";

        exp = 5;

        direction = Direction.DOWN;

        timeToChangeDirection = 10;


        typeMonster = TypeMonster.Bat;
        defaultSpeed = 7;
        speed = defaultSpeed;
        maxLife = 4;
        life = maxLife;

        attackStyle = AttackStyle.Touching;
        attack = 5;
        defense = 0;

        setDefaultSolidArea();

        // setare animatii
        setupMovement("res\\monster\\bat");
//        setupIdle("res\\monster\\bat");
    }

    public void AI() {
        defaultBehavior();
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.x = getGamePanel().tileSize / 16;
        solidArea.y = getGamePanel().tileSize / 3;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = (int) (getGamePanel().tileSize / 1.15);
        solidArea.height = (int) (getGamePanel().tileSize / 2.5);
    }

    @Override
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1;

        // 50% sanse de a arunca un banut
        if (i < 50) {
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

    @Override
    public void offensiveBehaviour() {

    }
}
