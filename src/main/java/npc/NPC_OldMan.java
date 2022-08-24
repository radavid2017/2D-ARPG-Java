package npc;

import features.Direction;
import game.GamePanel;

import java.util.Random;
import java.util.random.RandomGenerator;

public class NPC_OldMan extends NPC {

    public NPC_OldMan(GamePanel gPanel) {

        super(gPanel);

        setDefaultSolidArea();

        direction = Direction.DOWN;
        speed = 1;

        timeToChangeInMotion = RandomGenerator.getDefault().nextInt(120)+1;
        timeToChangeDirection = RandomGenerator.getDefault().nextInt(220)+timeToChangeInMotion;

        // incarcare animatii movement
        setupMovement("res\\npc\\oldMan");
        setupIdle("res\\npc\\oldMan");
        //loadMovementAnimations("res\\npc\\oldMan");
        // setarea dialogului
        setDialogue();
    }

    private void setDialogue() {
        dialogue.addText("Buna, aventurierule!");
        dialogue.addText("Stiu ce cauti aici...comoara!!");
        dialogue.addText("Candva faceam parte din prima linie a maretilor vrajitori!\nDar acum...sunt putin cam batran pentru a mai cauta comoara secreta.");
        dialogue.addText("Ei bine, succes tie!");
    }

    /** AI Old Man */
    @Override
    public void AI() {
        super.AI();
    }

    public void update() {

        super.update();
    }

    public void speak() {
        super.speak();
    }

    @Override
    public void setDefaultSolidArea() {
        solidAreaDefaultX = getGamePanel().tileSize/8;
        solidAreaDefaultY = getGamePanel().tileSize/2;
        solidArea.width = (int) (getGamePanel().tileSize/1.5);
        solidArea.height = (int) (getGamePanel().tileSize/2.25);
        getGamePanel().hasZoomed = false;
    }
}
