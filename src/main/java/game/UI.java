package game;

import entity.Player;
import features.Dialogue;
import features.Direction;
import features.UtilityTool;
import hud.window.InventoryWindow;
import item.equipable.light.DayState;
import monster.Monster;
import npc.NPC;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.SuperStatesObject;
import player.CharacterStatus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class UI {

    GamePanel gPanel;
    Graphics2D g2D;
    public Font font;
//    BufferedImage keyImage;
    int xKey = 40,yKey=40, keySize;
    int xMsg, yMsg;
    int screenWidth;

//    int messageCounter = 0;
    public boolean messageOn = false;
//    public String message = "";
    public Dialogue currentDialogue;

    ArrayList<String> msgs = new ArrayList<>();
    ArrayList<Integer> msgCounters = new ArrayList<>();

    public boolean gameOver = false;
    public static boolean GAME_OVER = false;

    float playTime;
    DecimalFormat decFormat = new DecimalFormat("#0.00");

    BufferedImage catBackground;
    public Menu menu = Menu.NEW_GAME;
    public TitleScreenState titleScreenState = TitleScreenState.MAIN_PAGE;
    public CharacterClass characterClass = CharacterClass.MAGE;

    /** OPTIONS MENU */
    int subState = 0;
    int commandNum = 0;

    /** TRANZITIE */
    int counter = 0;

    /** TRADING NPC */
    private NPC merchant;

    /** INVENTAR */
    public InventoryWindow playerInventoryWindow;
    public InventoryWindow npcInventoryWindow;

    /** UI HUD */
//    public ArrayList<SuperStatesObject> hudList = new ArrayList<>();
    SuperStatesObject heart;
    SuperStatesObject manaCrystal;
    // pozitii viata jcuator
//    public int heartX, heartY;

    /** Constrcutor UI */
    public UI(GamePanel gPanel) {
        this.gPanel = gPanel;
        // instantiere imagini
//        keyImage = SuperObject.setImage("res/objects/key.png");
        keySize = gPanel.tileSize;
        // nume font, stil font, dimensiune font
        try {
            // creare font
            font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/lilliput steps.ttf")).deriveFont(40f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // inregistrare font
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        xMsg = gPanel.tileSize*6 + 20;
        yMsg = gPanel.tileSize * 5;
        screenWidth = gPanel.tileSize*10;

        // IMAGINE BACKGROUND TITLU
        try {
            catBackground = ImageIO.read(new FileInputStream("res/background/cat.png"));
            catBackground = UtilityTool.scaledImage(catBackground, gPanel.tileSize*2, gPanel.tileSize*2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // INSTANTIERE HUD HP JUCATOR
        heart = new OBJ_Heart(gPanel);
        heart.screenX = 15;
        heart.screenY = 15;
        heart.loadObject(gPanel);
//        hudList.add(heart);
        // INSTANTIERE MANA CRYSTAL
        manaCrystal = new OBJ_ManaCrystal(gPanel);
        manaCrystal.screenX = heart.screenX - 15;
        manaCrystal.screenY = heart.screenY * 7;
        manaCrystal.loadObject(gPanel);

        // INSTANTIERE COORDONATE CADRU INVENTAR
        playerInventoryWindow = new InventoryWindow(
                gPanel,
                (int) ((gPanel.screenWidth/22) * 13.75),
                gPanel.screenHeight/25,
                gPanel.defaultTileSize * 5 + 80,
                gPanel.defaultTileSize * 6 + 90,
                true
        );

        npcInventoryWindow = new InventoryWindow(
                gPanel,
                playerInventoryWindow.x - 1000,
                gPanel.screenHeight/25,
                gPanel.defaultTileSize * 5 + 80,
                gPanel.defaultTileSize * 6 + 90,
                false
        );
    }

    /** Director general UI
     *  Aici se apeleaza toate metodele ce manevreaza diversele roluri ale ui, precum
     *  - Mesaje de notificare
     *  - Informatii utile legate de jucator si joc */
    public void draw(Graphics2D g2D) {

        this.g2D = g2D;
        g2D.setFont(font);
        g2D.setColor(Color.WHITE);

        switch (GamePanel.gameState) {
            case Title -> {
                // starea de titlu
                drawTitleScreen();

            }
            case Play -> {
                // play staff
                currentDialogue = null;
                drawLifeManaPlayer();
                drawMonsterLife();
                drawMessage();
            }
            case Pause -> {
                // pause stuff
                drawLifeManaPlayer();
                drawPauseScreen();
            }
            case Dialogue, CutSceneState -> {
                // starea de dialog
                if (this.currentDialogue != null) {
                    drawDialogueScreen();
                }
            }
            case CharacterState -> {
                // starea de status caracter
                drawCharacterScreen();
                drawPlayerInventory(true);
            }
            case OptionsState -> {
                drawOptionsScreen();
            }
            case GameOverState -> {
                // final de joc
                drawGameOverScreen();
            }
            case TransitionState -> {
                drawTransition();
            }
            case TradeState -> {
                drawTradeScreen();
            }
            case SleepState -> {
                drawSleepScreen();
            }
        }
//        if (!gameOver) {
//            // setarea culorii si a fontului default
//            setFontColorDefault(g2D);
//            // afisarea informatiilor
//            drawKey(g2D);
//            displayTime(g2D);
//            // manevrarea afisarii textelor notificatoare
//            manageTextDisplays(g2D);
//        } else {
//            // game over
//            // manevrarea si afisarea textelor pentru finalul rundei / jocului
//            manageGameOverDisplay(g2D);
//        }
    }

    private void drawSleepScreen() {
        counter++;

        if (counter < 120) {
            gPanel.environmentManager.getLighting().filterAlpha += 0.01f;
            if (gPanel.environmentManager.getLighting().filterAlpha > 1f) {
                gPanel.environmentManager.getLighting().filterAlpha = 1f;
            }
        }

        if (counter >= 120) {
            gPanel.environmentManager.getLighting().filterAlpha -= 0.01f;
            if (gPanel.environmentManager.getLighting().filterAlpha <= 0f) {
                gPanel.environmentManager.getLighting().filterAlpha = 0f;
                counter = 0;
                gPanel.environmentManager.getLighting().dayState = DayState.Day;
                gPanel.environmentManager.getLighting().resetDayCounter();
                GamePanel.gameState = GameState.Play;
            }
        }
    }

    private void drawTradeScreen() {
        switch (subState) {
            case 0 -> trade_select();
            case 1 -> trade_buy();
            case 2 -> trade_sell();
        }
        gPanel.keyH.enterPressed = false;
    }

    public void trade_select() {

        drawDialogueScreen();

        // DRAW WINDOW
        int x = (int) (gPanel.screenWidth/1.45f);
        int y = (int) (gPanel.screenHeight/4f);
        int width = gPanel.screenWidth/7;
        int height = (int) (gPanel.screenHeight/6);
        drawSubWindow(x, y, width, height);

        // DRAW TEXTS
        x += 50;
        y += 50;
        y += drawTradeItemOptionSelection("Cumpara", x, y, 0);
        y += drawTradeItemOptionSelection("Vinde", x, y, 1);
        drawTradeItemOptionSelection("Paraseste", x, y, 2);
    }

    public int drawTradeItemOptionSelection(String itemName, int textX, int textY, int cmd) {
        g2D.drawString(itemName, textX, textY);
        if (commandNum == cmd) {
            g2D.drawString(">", textX-30, textY);
            if (gPanel.keyH.enterPressed) {
                switch (cmd) {
                    case 0 -> subState = 1;
                    case 1 -> subState = 2;
                    case 2 -> {
                        commandNum = 0;
                        GamePanel.gameState = GameState.Dialogue;
                        currentDialogue = new Dialogue("Te mai astept!");
                    }
                }
            }
        }
        return 50; // text Level Distance Between Items
    }

    public void increaseTradeCmdSelection() {
        if (commandNum < 2) {
            commandNum++;
            gPanel.playSE("cursor.wav");
        }
    }

    public void decreaseTradeCmdSelection() {
        if (commandNum > 0) {
            commandNum--;
            gPanel.playSE("cursor.wav");
        }
    }

    public void trade_buy() {

        // DRAW PLAYER INVENTORY
        drawPlayerInventory(false);

        // DRAW NPC INVENTORY
        drawNPCInventory(true);

        int itemIndex = npcInventoryWindow.getItemIndexOnSlot();

        // BUY AN ITEM
        if (gPanel.keyH.enterPressed) {
            if (itemIndex < merchant.inventory.size()) {
                if (merchant.inventory.get(itemIndex).price > gPanel.player.coin) {
                    gPanel.ui.setSubState(0);
                    GamePanel.gameState = GameState.Dialogue;
                    gPanel.ui.setCurrentDialogue(new Dialogue("Monede insuficiente! Revino dupa ce aduni mai multe!"));
                    gPanel.ui.drawDialogueScreen();
                }
                else {
                    if (gPanel.player.obtainItem(merchant.inventory.get(itemIndex))) {
                        gPanel.player.coin -= merchant.inventory.get(itemIndex).price;
                    }
                    else {
                        gPanel.ui.setSubState(0);
                        GamePanel.gameState = GameState.Dialogue;
                        gPanel.ui.setCurrentDialogue(new Dialogue("Inventarul tau este plin!"));
                    }
                }
            }
        }
    }

    public void trade_sell() {
        // DRAW PLAYER INVENTORY
        drawPlayerInventory(true);

        int itemIndex = playerInventoryWindow.getItemIndexOnSlot();

        // SELL AN ITEM
        if (gPanel.keyH.enterPressed) {
            if (itemIndex < gPanel.player.inventory.size()) {
                if (gPanel.player.inventory.get(itemIndex) == gPanel.player.currentWeapon ||
                        gPanel.player.inventory.get(itemIndex) == gPanel.player.currentShield) {
                    commandNum = 0;
                    subState = 0;
                    GamePanel.gameState = GameState.Dialogue;
                    currentDialogue = new Dialogue("Nu poti vinde un obiect echipat!");
                } else {
                    gPanel.player.coin += gPanel.player.inventory.get(itemIndex).price / 2;
                    gPanel.player.removeItem(itemIndex);
                }
            }
        }
    }

    private void drawTransition() {

        counter++;
        g2D.setColor(new Color(0, 0, 0, counter*5));
        g2D.fillRect(0, 0, gPanel.screenWidth, gPanel.screenHeight);

        if(counter == 50) { // Tranzitia este terminata
            counter = 0;
            GamePanel.gameState = GameState.Play;
            gPanel.currentMap = gPanel.eHandler.tempMap;
            gPanel.player.worldX = gPanel.tileSize * gPanel.eHandler.tempCol;
            gPanel.player.worldY = gPanel.tileSize * gPanel.eHandler.tempRow;
            gPanel.eHandler.previousEventX = gPanel.player.worldX;
            gPanel.eHandler.previousEventY = gPanel.player.worldY;
            gPanel.changeArea();
        }
    }

    public void setCommandNum(int commandNum) {
        this.commandNum = commandNum;
    }

    private void drawGameOverScreen() {
        g2D.setColor(new Color(0,0,0, 150));
        g2D.fillRect(0, 0, gPanel.screenWidth, gPanel.screenHeight);

        int x;
        int y;
        String text;
        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 110f));

        text = "Sfarsit joc";
        // Umbra
        g2D.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gPanel.screenHeight/4;
        g2D.drawString(text, x, y);
        // Main
        g2D.setColor(Color.white);
        g2D.drawString(text, x-4, y-4);

        // Retry
        g2D.setFont(g2D.getFont().deriveFont(50f));
        displayText("Reincepe", getXForCenteredText("Reincepe"), y += 200);
        if (commandNum == 0) {
            g2D.drawString(">", x-40, y);
        }

        // Inapoi la meniul principal
        displayText("Paraseste", getXForCenteredText("Paraseste"), y += 100);
        if (commandNum == 1) {
            g2D.drawString(">", x-40, y);
        }
    }

    public void changeCmdGameOver() {
        if (commandNum == 0) {
            commandNum = 1;
        }
        else {
            commandNum = 0;
        }
        gPanel.playSE("cursor.wav");
    }

    public void displayText(String text, int x, int y) {
        g2D.drawString(text, x, y);
    }

    private void drawOptionsScreen() {
        g2D.setColor(Color.white);
        g2D.setFont(g2D.getFont().deriveFont(32F));

        // SUB WINDOW
        int frameX = gPanel.tileSize*6;
        int frameY = gPanel.tileSize;
        int frameWidth = gPanel.tileSize*8;
        int frameHeight = (int) (gPanel.screenHeight/1.5f);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0 -> {
                showOptions(frameX, frameY, frameWidth, frameHeight);
                actionOptions();
            }
            case 1 -> {
                options_fullscreenNotification(frameX, frameY);
            }
            case 2 -> {
                options_controls(frameX, frameY);
            }
            case 3 -> {
                options_endGameConfirmation(frameX, frameY);
            }
        }

        gPanel.keyH.enterPressed = false;
    }

    private void options_endGameConfirmation(int frameX, int frameY) {
        int textX = frameX + 50;
        int textY = screenWidth/3;

        currentDialogue = new Dialogue("Iesi din joc si te \nintorci la meniul principal?");

        for (String line : currentDialogue.peekText().split("\n")) {
            g2D.drawString(line, getXForCenteredText(line), textY);
            textY += 40;
        }

        // YES
        String text = "Da";
        textX = getXForCenteredText(text);
        textY += 50;
        g2D.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2D.drawString(">", textX-30, textY);
            if (gPanel.keyH.enterPressed) {
                subState = 0;
                GamePanel.gameState = GameState.Title;
                gPanel.resetGame(true);
                gPanel.stopMusic();
                gPanel.currentMap = 0;
            }
        }

        // NO
        text = "Nu";
        textX = getXForCenteredText(text);
        textY += 50;
        g2D.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2D.drawString(">", textX-30, textY);
            if (gPanel.keyH.enterPressed) {
                subState = 0;
                commandNum = 4;
            }
        }
    }

    public void showOptions(int frameX, int frameY, int frameWidth, int frameHeight) {
        int textX;
        int textY;

        // TITLE
        String text = "Optiuni";
        textX = getXForCenteredText(text);
        textY = frameY + 50;

        g2D.drawString(text, textX, textY);

        /** STANGA */
        textX = frameX + 50;
        textY += 100;
        // FULL SCREEN ON/OFF
        textY += drawItemOptionSelection("Ecran complet", textX, textY, 0);

        // MUSIC
        textY += drawItemOptionSelection("Muzica", textX, textY, 1);

        // SE
        textY += drawItemOptionSelection("Sunet Efecte", textX, textY, 2);

        // CONTROL
        textY += drawItemOptionSelection("Controale", textX, textY, 3);

        // END GAME
        textY += drawItemOptionSelection("Sfarseste joc", textX, textY, 4) * 1.35f;

        // BACK
        drawItemOptionSelection("Inapoi", textX, textY, 5);

        /** DREAPTA */
        // FULL SCREEN CHECKBOX
        textX = (int) (frameX + gPanel.screenWidth/4f);
        textY = frameY + 115;
        g2D.setStroke(new BasicStroke(3));
        g2D.drawRect(textX, textY, gPanel.tileSize/2, gPanel.tileSize/2);
        if (gPanel.fullScreenOn) {
            g2D.fillRect(textX+6, textY+6, gPanel.tileSize/2-11, gPanel.tileSize/2-11);
        }

        // MUSIC VOLUME
        textY += 100;
        g2D.drawRect(textX, textY, frameWidth/3, gPanel.tileSize/2);
        int volumeWidth = gPanel.tileSize/2 * gPanel.music.getVolumeScale();
        g2D.fillRect(textX, textY, volumeWidth, gPanel.tileSize/2);

        // SE VOLUME
        textY += 100;
        g2D.drawRect(textX, textY, frameWidth/3, gPanel.tileSize/2);
        volumeWidth = gPanel.tileSize/2 * gPanel.soundEffect.getVolumeScale();
        g2D.fillRect(textX, textY, volumeWidth, gPanel.tileSize/2);

        gPanel.config.saveConfig();
    }

    public int drawItemOptionSelection(String itemName, int textX, int textY, int cmd) {
        g2D.drawString(itemName, textX, textY);
        if (commandNum == cmd) {
            g2D.drawString(">", textX-30, textY);
        }
        return 100; // text Level Distance Between Items
    }

    public void actionOptions() {
        switch (commandNum) {
            case 0 -> {
                if (gPanel.keyH.enterPressed) {
                    gPanel.fullScreenOn = !gPanel.fullScreenOn;
                    subState = 1;
                }
            }
            case 3 -> {
                if (gPanel.keyH.enterPressed) {
                    subState = 2;
                    commandNum = 0;
                }
            }
            case 4 -> {
                if (gPanel.keyH.enterPressed) {
                    subState = 3;
                    commandNum = 0;
                }
            }
            case 5 -> {
                if (gPanel.keyH.enterPressed) {
                    GamePanel.gameState = GameState.Play;
                    commandNum = 0;
                }
            }
        }
    }

    public void changeYesNoCursor() {
        commandNum = commandNum == 1 ? 0 : 1;
        gPanel.playSE("cursor.wav");
    }

    public void increaseCommandOptionsLine() {
        if (commandNum < 5 && subState == 0) {
            commandNum++;
            gPanel.playSE("cursor.wav");
        }
    }

    public void decreaseCommandOptionsLine() {
        if (commandNum > 0 && subState == 0) {
            commandNum--;
            gPanel.playSE("cursor.wav");
        }
    }

    public void options_fullscreenNotification(int frameX, int frameY) {
        int textX = (int) (frameX + screenWidth/7.5f);
        int textY = frameY + gPanel.screenHeight/4;

        currentDialogue = new Dialogue("Schimbarea va avea \nefect dupa reinceperea \njocului.");
        for (String line: currentDialogue.peekText().split("\n")) {
            g2D.drawString(line, textX, textY);
            textY += 40;
        }

        // INAPOI
        textY += 150;
        g2D.drawString("Inapoi", textX, textY);
            g2D.drawString(">", textX-30,textY);
            if (gPanel.keyH.enterPressed) {
                subState = 0;
            }
    }

    public void options_controls(int frameX, int frameY) {
        int textX;
        int textY;

        // TITLE
        String text = "Controale";
        textX = getXForCenteredText(text);
        textY = frameY + 50;
        g2D.drawString(text, textX, textY);

        textX = frameX + 50;
        textY += 75;
        textY += writeControl("Miscari jucator", textX, textY);
        textY += writeControl("Ataca", textX, textY);
        textY += writeControl("Trage", textX, textY);
        textY += writeControl("Ecran caracter", textX, textY);
        textY += writeControl("Pauza", textX, textY);
        writeControl("Optiuni", textX, textY);

        textX = (int) ((frameX + 50) * 1.75f);
        textY = frameY + 50 + 75;
        textY += writeControl("W,A,S,D", textX, textY);
        textY += writeControl("ENTER", textX, textY);
        textY += writeControl("K", textX, textY);
        textY += writeControl("C", textX, textY);
        textY += writeControl("P", textX, textY);
        textY += writeControl("ESC", textX, textY);

        // BACK
        textX = frameX + 50;
        g2D.drawString("Inapoi", textX, textY);
        g2D.drawString(">", textX-30, textY);
        g2D.drawString(">", textX-30, textY);
        if (gPanel.keyH.enterPressed) {
            subState = 0;
            commandNum = 3;
        }
    }

    private int writeControl(String text, int textX, int textY) {
        g2D.drawString(text, textX, textY);
        return 100;
    }

    public int getSubState() {
        return subState;
    }

    public void setSubState(int subState) {
        this.subState = subState;
    }

    public int getCommandNum() {
        return commandNum;
    }

//    private OBJ_Heart getHeart() {
//        for (int i = 0; i < hudList.size(); i++) {
//            if (hudList.get(i).typeStatesObject == TypeStatesObject.HEART)
//                return (OBJ_Heart) hudList.get(i);
//        }
//        return null;
//    }

    public void drawMonsterLife() {

        if (gPanel.monsterList.get(gPanel.currentMap) != null) {

            for (int i = 0; i < gPanel.monsterList.get(gPanel.currentMap).size(); i++) {

                Monster monster = (Monster) gPanel.monsterList.get(gPanel.currentMap).get(i);

                if (monster != null && monster.camera.isOnCamera()) {
                    if (monster.hpBarOn && !monster.boss) {
                        monster.showHPBar(g2D);
                    }
                    else if (monster.boss){
                        monster.showHPBossBar(g2D);
                    }
                }
            }
        }
    }

    public void drawLifeManaPlayer() {

        assert heart != null : "Obiectul heart din drawPlayerLife() din clasa ui este null!";

        int x = (int) heart.screenX;
        int posY = (int) heart.screenY;
        int iconSize = 54;

        // AFISEAZA HP CURENT PLAYER
        int i;
        // cat timp am 2 vieti, adaug o inima intreaga
        for (i = 0; i < gPanel.player.life-1; i+=2) {
            g2D.drawImage(heart.imgStates.get(0), x, posY, iconSize, iconSize, null);
            x += 54;
            if (x >= screenWidth/1.75) {
                x = (int) heart.screenX;
                posY += iconSize;
            }
        }

        // daca mai am o viata de adaugat, adaug jumatate de inima
        if (gPanel.player.life - i == 1) {
            g2D.drawImage(heart.imgStates.get(1), x, posY, iconSize, iconSize, null);
            x += 54;
        }

        // AFISEAZA HP MAXIM POSIBIL
        int missingLives = gPanel.player.life > 0 ? gPanel.player.maxLife - gPanel.player.life : gPanel.player.maxLife;
        for (int k = 0; k < missingLives/2; k++) {
            g2D.drawImage(heart.imgStates.get(2), x, posY, iconSize, iconSize, null);
            x += 54;
        }

        int xMana = (int) manaCrystal.screenX;
        // CURRENT MANA
        for(i=0; i<gPanel.player.mana; i++) {
            g2D.drawImage(manaCrystal.imgStates.get(1), xMana, posY + 48, iconSize, iconSize, null);
            xMana += 32;
            if (xMana >= screenWidth/1.75) {
                xMana = (int) manaCrystal.screenX;
                posY += iconSize;
            }
        }

        // MAX MANA
        for (i=gPanel.player.mana; i<gPanel.player.maxMana; i++) {
            g2D.drawImage(manaCrystal.imgStates.get(0), xMana, posY + 48, iconSize, iconSize, null);
            xMana += 32;
        }
    }

    public void drawMessage() {
        int msgX = gPanel.defaultTileSize;
        int msgY = gPanel.defaultTileSize * 4;
        g2D.setFont(g2D.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < msgs.size(); i++) {
            String msg = msgs.get(i);
            if (msg != null) {

                g2D.setColor(Color.black);
                g2D.drawString(msg, msgX+2, msgY+2);

                g2D.setColor(Color.white);
                g2D.drawString(msg, msgX, msgY);

                int counter = msgCounters.get(i) + 1;
                msgCounters.set(i, counter);
                msgY += 50;

                if (msgCounters.get(i) > 180) { // 3 secunde
                    msgs.remove(i);
                    msgCounters.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {

        switch (titleScreenState) {
            case MAIN_PAGE -> {
                // NUME TITLU
                g2D.setFont(font.deriveFont(Font.BOLD, 96f));
                String text = "Huntily Poke";
                int x = getXForCenteredText(text);
                int y = gPanel.tileSize * 2;

                // UMBRIRE
                g2D.setColor(Color.DARK_GRAY);
                g2D.drawString(text, x+5, y+5);

                // CULOARE TITLU
                Random rand = new Random();
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                g2D.setColor(new Color(r, g, b).brighter());

                // AFISARE TITLU
                g2D.drawString(text, x, y);

                // IMAGINE CARACTER
                x = gPanel.screenWidth / 2 - (gPanel.tileSize*2)/2;
                y += gPanel.tileSize*2;

                g2D.drawImage(catBackground, x, y, null);

                // MENIU
                g2D.setColor(Color.WHITE);
                g2D.setFont(font.deriveFont(Font.BOLD, 48f));

                drawItemMenu("JOC NOU", y += gPanel.tileSize*2, Menu.NEW_GAME);
                drawItemMenu("INCARCA JOC", y += gPanel.tileSize, Menu.LOAD_GAME);
                drawItemMenu("EXIT", y += gPanel.tileSize, Menu.EXIT);
            }
            case CLASS_SELECTION -> {

                // FEREASTERA DE SELECTARE CLASA
                g2D.setColor(Color.WHITE);
                g2D.setFont(font.deriveFont(42f));

                String text = "Selecteaza-ti clasa!";
                int x = getXForCenteredText(text);
                int y = gPanel.tileSize;

                Random rand = new Random();
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();
                g2D.setColor(new Color(r, g, b).brighter());

                g2D.drawString(text, x, y);

                g2D.setColor(Color.WHITE);
                drawItemClassSelection("Vrajitor", y += gPanel.tileSize, CharacterClass.MAGE);
                drawItemClassSelection("Arcas", y += gPanel.tileSize, CharacterClass.ARCHER);
                drawItemClassSelection("Razboinic", y += gPanel.tileSize, CharacterClass.WARRIOR);
//                drawItemClassSelection("Pisicuta", y += gPanel.tileSize, CharacterClass.CAT);
                drawItemClassSelection("Inapoi", y += gPanel.tileSize*2, CharacterClass.BACK);
            }
        }
    }

    private void drawItemMenu(String text, int y, Menu menu) {
        int x = getXForCenteredText(text);
        y += gPanel.tileSize;
        g2D.drawString(text, x, y);

        if (this.menu == menu) {
            g2D.drawString(">", x-gPanel.tileSize, y);
        }
    }

    private void drawItemClassSelection(String text, int y, CharacterClass characterClass) {
        int x = getXForCenteredText(text);
        y += gPanel.tileSize;
        g2D.drawString(text, x, y);

        if (this.characterClass == characterClass) {
            g2D.drawString(">", x-gPanel.tileSize, y);
        }
    }

    public void nextItem() {
        switch (titleScreenState) {
            case MAIN_PAGE -> {
                switch (menu) {
                    case NEW_GAME -> menu = Menu.LOAD_GAME;
                    case LOAD_GAME -> menu = Menu.EXIT;
                    case EXIT -> menu = Menu.NEW_GAME;
                }
            }
            case CLASS_SELECTION -> {
                switch (characterClass) {
                    case MAGE -> characterClass = CharacterClass.ARCHER;
                    case ARCHER -> characterClass = CharacterClass.WARRIOR;
                    case WARRIOR -> characterClass = CharacterClass.BACK;
//                    case CAT -> characterClass = CharacterClass.BACK;
                    case BACK -> characterClass = CharacterClass.MAGE;
                }
            }
        }
    }

    public void previousItem() {
        switch (titleScreenState){
            case MAIN_PAGE -> {
                switch (menu) {
                    case EXIT -> menu = Menu.LOAD_GAME;
                    case LOAD_GAME -> menu = Menu.NEW_GAME;
                    case NEW_GAME -> menu = Menu.EXIT;
                }
            }
            case CLASS_SELECTION -> {
                switch (characterClass) {
//                    case CAT -> characterClass = CharacterClass.WARRIOR;
                    case WARRIOR -> characterClass = CharacterClass.ARCHER;
                    case ARCHER -> characterClass = CharacterClass.MAGE;
                    case MAGE -> characterClass = CharacterClass.BACK;
                    case BACK -> characterClass = CharacterClass.WARRIOR;
                }
            }
        }
    }

    public void chooseItem() {
        switch (menu) {
            case NEW_GAME -> {
                titleScreenState = TitleScreenState.CLASS_SELECTION;
//                GamePanel.gameState = GameState.Play;
//                gPanel.playMusic("BlueBoyAdventure.wav");
            }
            case LOAD_GAME -> {
                gPanel.saveLoad.load();
//                GamePanel.gameState = GameState.Play;
//                gPanel.playMusic("BlueBoyAdventure.wav");
            }
            case EXIT -> {
                System.exit(0);
            }
        }
    }

    public void chooseClass(GamePanel gp) {
        switch (characterClass) {
            case MAGE -> gPanel.characterClassPath = "mage";
            case ARCHER -> gPanel.characterClassPath = "archer";
            case WARRIOR -> gPanel.characterClassPath = "warrior";
//            case CAT -> gPanel.characterClassPath = "cat";
            case BACK -> {
                titleScreenState = TitleScreenState.MAIN_PAGE;
                return;
            }
        }
        gPanel.startGame();
    }

    public int getXForCenteredText(String text) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        return gPanel.screenWidth / 2 - length / 2;
    }

    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        int x = tailX - length;
        return x;
    }

    public void drawPauseScreen() {
        displayCenterNotification(g2D, "PAUZA", 220f);
    }

    // setarea fontului si a culorii sale
    private void setFontColorDefault(Graphics2D g2D) {
        // nume font, stil font si dimensiune font
        g2D.setFont(font);
        g2D.setColor(Color.white);
    }

    // afisarea numarului de chei si a imaginii
    private void drawKey(Graphics2D g2D) {
        g2D.setFont(g2D.getFont().deriveFont(80.0f));
//        g2D.drawImage(keyImage, xKey, yKey, keySize, keySize, null);
//        g2D.drawString("" + gPanel.player.numKeys, xKey + 95, yKey + 95);
    }

    // functie de preluare text si declansare de afisare a textului
//    public void showMessage(String text) {
//        message = text;
//        messageOn = true;
//    }

    public void drawDialogueScreen() {
        // fereastra de dialog
        int x = 156;
        int y = 24;
        int width = gPanel.screenWidth - (screenWidth-x)/2;
        int height = gPanel.screenHeight / 4;

        drawSubWindow(x, y, width, height);

        // textul de afisat in fereastra de dialog
        g2D.setFont(font.deriveFont(Font.PLAIN, gPanel.screenHeight/40f));
        x += gPanel.tileSize;
        y += gPanel.tileSize;

        String text = currentDialogue.peekText();
        for (String line : text.split("\n")) {
            g2D.drawString(line, x, y);
            y += 40;
        }

//        if (GamePanel.gameState == GameState.CutSceneState && gPanel.keyH.enterPressed) {
//            gPanel.cutSceneManager.scenePhase++;
//        }
    }

    private void drawCharacterScreen() {
        // date text
        final int lineHeight = font.getSize() + 3;
        CharacterStatus characterStatus = new CharacterStatus(gPanel);

        // creare cadru
        final int frameX = gPanel.screenWidth/22;
        final int frameY = gPanel.screenHeight/6;
        final int frameWidth = (int) (gPanel.screenWidth/3.25f);
        final int frameHeight = characterStatus.parameters.size() * (lineHeight + 3) + characterStatus.numImages();
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2D.setColor(Color.white);
        g2D.setFont(font.deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gPanel.defaultTileSize/2;

        // NUME PARAMETRII
        for (String param : characterStatus.parameters.keySet()) {
//            int currentX = textX + getXForCenteredFrame(param, frameX);
            if (characterStatus.parameters.get(param).getClass() == BufferedImage.class) {
                textY += 50;
            }
            g2D.drawString(param + ":", textX, textY);
            textY += lineHeight;
        }

        // VALORI
        int tailX = (frameX + frameWidth) - 180;
        // resetare textY
        textY = frameY + gPanel.defaultTileSize/2;
        for (Object value : characterStatus.parameters.values()) {
            if (value.getClass() == String.class) {
                int currentX = tailX + getXForCenteredFrame((String) value, frameX);
                g2D.drawString((String) value, currentX, textY);
                textY += lineHeight;
            }
            else {
                g2D.drawImage((BufferedImage) value, tailX, textY-14, null);
                textY += gPanel.defaultTileSize;
            }

        }
    }

    private void drawPlayerInventory(boolean isUsing) {
        playerInventoryWindow.draw(gPanel, gPanel.player, gPanel.player.inventory, g2D, isUsing);
    }

    private void drawNPCInventory(boolean isUsing) {
        npcInventoryWindow.draw(gPanel, merchant, merchant.inventory, g2D, isUsing);
    }

    public int getXForCenteredFrame(String text, int frameX) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        return frameX / 2 - length / 2;
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2D.setColor(c);
        g2D.fillRoundRect(x,y,width,height,35, 35);

        c = new Color(255, 255, 255);
        g2D.setColor(c);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void addMessage(String text) {
        msgs.add(text);
        msgCounters.add(0);
    }

    // manevrarea afisarii textelor din Game Over si a opririi thread-ului de rulare a jocului dupa un anumit timp
//    private void manageGameOverDisplay(Graphics2D g2D) {
//        displayGameOverLayout(g2D);
//        if (messageCounter > 320) { // textul isi va schimba culorile timp de 5.33 secunde
//            gPanel.gameThread = null;
//        }
//
//        messageCounter++;
//    }

    // manevrarea afisarii notificarilor
//    private void manageTextDisplays(Graphics2D g2D) {
//        if (messageOn) {
//            displayCenterNotification(g2D, message, null);
//            if (messageCounter > 120) { // textul dispare dupa 2 secunde
//                messageCounter = 0;
//                messageOn = false;
//            }
//
//            messageCounter++;
//        }
//    }

    // afisarea unui mesaj notificator
    private void displayCenterNotification(Graphics2D g2D, String text, Float sizeFont) {
        FontRenderContext frc = g2D.getFontRenderContext();
        TextLayout layout = new TextLayout(text, font.deriveFont(sizeFont == null ? 60.0f : sizeFont), frc);
        Rectangle2D bounds = layout.getBounds();

        int width = (int) Math.round(bounds.getWidth());
        int height = (int) Math.round(bounds.getHeight());
        int x = (gPanel.screenWidth - width) / 2;
        int y = height + (gPanel.screenHeight - height) / 2;
        layout.draw(g2D, (float) x, (float) y / 1.5f);
    }

    // afisarea felicitarilor
    private void displayCongrats(Graphics2D g2D) {
        final String congratsMsg = "Felicitari";
        FontRenderContext frc = g2D.getFontRenderContext();
        TextLayout layout = new TextLayout(congratsMsg, font.deriveFont(220.0f), frc); // 220 sau 120
        Rectangle2D bounds = layout.getBounds();

        int width = (int) Math.round(bounds.getWidth());
        int height = (int) Math.round(bounds.getHeight());
        int x = (gPanel.screenWidth - width) / 2;
        int y = height + (gPanel.screenHeight - height) / 2;

        // setari culori congrats
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        g2D.setColor(new Color(r,g,b));

        layout.draw(g2D, (float) x, (float) y / 1.75f);
    }

    // afisarea gasirii comorii
    private void displayFoundTreasure(Graphics2D g2D) {
        final String foundTreasure = "Ai gasit comoara!";
        FontRenderContext frc = g2D.getFontRenderContext();
        TextLayout layout = new TextLayout(foundTreasure, font.deriveFont(90.0f), frc); // 220 sau 120
        Rectangle2D bounds = layout.getBounds();

        int width = (int) Math.round(bounds.getWidth());
        int height = (int) Math.round(bounds.getHeight());
        int x = (gPanel.screenWidth - width) / 2;
        int y = height + (gPanel.screenHeight - height) / 2;

        // setari culori foundTreasure
        Random rand = new Random();
        float r = rand.nextFloat() / 2f + 0.5f;
        float g = rand.nextFloat() / 2f + 0.5f;
        float b = rand.nextFloat() / 2f + 0.5f;
        g2D.setColor(new Color(r,g,b));

        layout.draw(g2D, (float) x, (float) y * 1.25f);
    }

    // afisarea timpului scurs pana la gasirea comorii
    private void displayTimePlayed(Graphics2D g2D) {
        final String timePlayed = "Timpul tau: " + decFormat.format(playTime) + "!";
        FontRenderContext frc = g2D.getFontRenderContext();
        TextLayout layout = new TextLayout(timePlayed, font.deriveFont(40.0f), frc); // 220 sau 120
        Rectangle2D bounds = layout.getBounds();

        int width = (int) Math.round(bounds.getWidth());
        int height = (int) Math.round(bounds.getHeight());
        int x = (gPanel.screenWidth - width) / 2;
        int y = height + (gPanel.screenHeight - height) / 2;

        layout.draw(g2D, (float) x, (float) y * 1.4f);
    }

    // afisarea mesajelor pentru Game Over
    private void displayGameOverLayout(Graphics2D g2D) {
        displayCongrats(g2D);
        displayFoundTreasure(g2D);
        displayTimePlayed(g2D);
    }

    // afisarea trecerii timpului
    private void displayTime(Graphics2D g2D) {
        if (playTime < 99.99f)
            playTime += 1/60f;
        else playTime = 99.99f;
        final String timePlayed = "Timp:" + decFormat.format(playTime);
        FontRenderContext frc = g2D.getFontRenderContext();
        TextLayout layout = new TextLayout(timePlayed, font.deriveFont(40.0f), frc); // 220 sau 120

        layout.draw(g2D, (float) xKey*42 - 55f, (float) yKey + 35f);
    }

    public Dialogue getCurrentDialogue() {
        return currentDialogue;
    }

    public void setCurrentDialogue(Dialogue currentDialogue) {
        this.currentDialogue = currentDialogue;
    }

    public NPC getMerchant() {
        return merchant;
    }

    public void setMerchant(NPC merchant) {
        this.merchant = merchant;
    }
}
