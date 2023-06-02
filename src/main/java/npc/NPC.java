package npc;

import entity.ArtificialIntelligence;
import entity.Entity;
import entity.TypeAI;
import features.UtilityTool;
import game.GamePanel;
import item.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.random.RandomGenerator;

public abstract class NPC extends ArtificialIntelligence {

    public BufferedImage sprite;
    public ArrayList<Item> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    TypeNPC typeNPC;

    public NPC(GamePanel gp, TypeNPC typeNPC) {
        super(gp);
        typeAI = TypeAI.NPC;
        typeNPC = this.typeNPC;
    }

    public void update() {
        AI();
        super.update();
    }

    public void draw(Graphics2D g2D) {

        super.draw(g2D);

        if (inMotion)
            sprite = movement.manageAnimations(this, direction);
        else{
            sprite = idle.manageAnimations(this, direction);
        }


        // Management Camera
        camera.drawEntity(g2D, sprite);

        drawSolidArea(g2D);
    }

    public abstract void setDialogue();

    public abstract void setItems();
}
