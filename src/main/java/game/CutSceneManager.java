package game;

import entity.Entity;
import entity.PlayerDummy;
import features.Direction;
import item.consumable.LegendaryTreasure.OBJ_BlueHeart;
import monster.MON_SkeletonLord;
import monster.Monster;
import object.obstacle.door.IronDoor;

import java.awt.*;
import java.util.ArrayList;

public class CutSceneManager {

    GamePanel gp;
    Graphics2D g2D;
    public int sceneNum;
    public int scenePhase;

    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    // Scene Number
    public SceneState sceneState = SceneState.NA;

    public CutSceneManager (GamePanel gp) {
        this.gp = gp;

        endCredit = "Program\n" +
                "Hoka David-Stelian\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n" +
                "Muzica\n" +
                "RyiSnow, David Renda\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n" +
                "Arta\n" +
                "RyiSnow, Domyouji\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n" +
                "Ne revedem in continuarea povestii!\n" +
                "\n\n" +
                "Va continua...\n";
    }

    public void draw (Graphics2D g2D) {
        this.g2D = g2D;


        switch (sceneState) {
            case Skeleton -> scene_skeletonLord();
            case Ending -> scene_ending();
        }

    }

    public void scene_skeletonLord() {
        if (scenePhase == 0) {
            gp.bossBattleOn = true;

            // Shut the iron door
            IronDoor ironDoor = new IronDoor(gp);
            ironDoor.setPosition(gp.tileSize*25, gp.tileSize*28);
            ironDoor.temp = true;
            gp.objects.get(gp.currentMap).add(ironDoor);
            gp.playSE("dooropen.wav");

            PlayerDummy playerDummy = new PlayerDummy(gp);
            playerDummy.setPosition(gp.player.worldX, gp.player.worldY);
            playerDummy.direction = gp.player.direction;
            gp.objects.computeIfAbsent(gp.currentMap, k -> new ArrayList<>());
            gp.objects.get(gp.currentMap).add(playerDummy);

            gp.player.drawing = false;

            scenePhase++;
        }
        if (scenePhase == 1) {
            gp.player.worldY -= 6;

            if (gp.player.worldY < gp.tileSize*18) {
                scenePhase++;
            }
        }
        if (scenePhase == 2) {
            // Search the boss
            if (gp.monsterList.get(gp.currentMap) != null) {
                for (int i = 0; i < gp.monsterList.get(gp.currentMap).size(); i++) {
                    Monster monster = (Monster) gp.monsterList.get(gp.currentMap).get(i);
                    if (monster instanceof MON_SkeletonLord skeletonLord) {
                        skeletonLord.sleep = false;
                        gp.ui.setCurrentDialogue(skeletonLord.getDialogue());
                        scenePhase++;
                        break;
                    }
                }
            }
        }
        if (scenePhase == 3) {
            // The boss speaks
        }
        if (scenePhase == 4) {
            // Return to the player
            System.out.println("SCENEEEE");
            // Search the dummy
            if (gp.objects.get(gp.currentMap) != null) {
                for (int i = 0; i < gp.objects.get(gp.currentMap).size(); i++) {
                    Entity entity = gp.objects.get(gp.currentMap).get(i);
                    if (entity instanceof PlayerDummy) {
                        // Restore the player position
                        gp.player.worldX = entity.worldX;
                        gp.player.worldY = entity.worldY;
                        // Delete the dummy
                        gp.objects.get(gp.currentMap).set(i, null);
                        break;
                    }
                }
            }

            // Start drawing the player
            gp.player.drawing = true;

            sceneState = SceneState.NA;
            scenePhase = 0;
            GamePanel.gameState = GameState.Play;

            // Change the music
            gp.stopMusic();
            gp.playMusic("FinalBattle.wav");
        }
    }

    public void scene_ending() {

        if (scenePhase == 0) {
            gp.stopMusic();
            gp.ui.setCurrentDialogue(new OBJ_BlueHeart(gp).dialogue);
            scenePhase++;
        }
        if (scenePhase == 1) {
            // Display dialogues
            gp.ui.drawDialogueScreen();
        }
        if (scenePhase == 2) {
            // play the fanfare
            GamePanel.gameState = GameState.Ending;
            gp.playSE("fanfare.wav");
            scenePhase++;
        }
        if (scenePhase == 3) {
            // Wait until the sound effect ends
            if (counterReached(300)) { // 300 frames = 5 seconds
                scenePhase++;
            }
        }
        if (scenePhase == 4) {
            // The Screen gets darker
            alpha += 0.005f;
            if(alpha > 1f) {
                alpha = 1f;
            }
            drawBlackBackground(alpha);
            if (alpha == 1f) {
                alpha = 0;
                scenePhase++;
            }
        }
        if (scenePhase == 5) {
            drawBlackBackground(1f);

            alpha += 0.005f;
            if (alpha > 1f) {
                alpha = 1f;
            }

            String text = "Dupa luptele cu inversunare ai reusit\nsa gasesti comoara pe care o cautai.\n" +
                    "Dar calatoria ta nu se sfarseste aici.\nE timpul sa deschidem poarta!\n" +
                    "Calatoria in timp este posibila cu aceasta cheie!\nAici incepe povestea...";

            drawString(alpha, 38f, 200, text, 70);

            if (counterReached(900)) {
                gp.stopMusic();
                gp.playSE("ending.wav");
                scenePhase++;
            }
        }
        if (scenePhase == 6) {
            drawBlackBackground(1f);

            drawString(1f, 120f, gp.screenHeight/2, "Huntily Poke", 0);

            if (counterReached(200)) {
                scenePhase++;
            }
        }
        if (scenePhase == 7) {
            drawBlackBackground(1f);

            y = gp.screenHeight/2;
            drawString(1f, 38f, y, endCredit, 40);

            if (counterReached(200)) {
                scenePhase++;
            }
        }
        if (scenePhase == 8) {
            drawBlackBackground(1f);

            // Scrolling the credit
            y--;
            drawString(1f, 38, y, endCredit, 40);

            if (counterReached(2000)) {
                scenePhase = 0;
                sceneState = SceneState.NA;
                GamePanel.gameState = GameState.Title;
            }
        }
    }

    public boolean counterReached(int target) {
        boolean counterReached = false;
        counter++;
        if (counter > target) {
            counterReached = true;
            counter = 0;
        }
        return counterReached;
    }

    public void drawBlackBackground(float alpha) {
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2D.setColor(Color.black);
        g2D.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight) {
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2D.setColor(Color.white);
        g2D.setFont(g2D.getFont().deriveFont(fontSize));

        if (lineHeight == 0) {
            int x = gp.ui.getXForCenteredText(text);
            x /= 1.75f;
            g2D.drawString(text, x, y);
        }
        else {
            for (String line : text.split("\n")) {
                int x = gp.ui.getXForCenteredText(line);
                g2D.drawString(line, x, y);
                y += lineHeight;
            }
        }

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
