package game;

import features.Dialogue;
import features.UtilityTool;
import object.OBJ_Heart;
import object.SuperStatesObject;
import object.TypeStatesObject;
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
import java.util.List;

public class UI {

    GamePanel gPanel;
    Graphics2D g2D;
    Font font;
//    BufferedImage keyImage;
    int xKey = 40,yKey=40, keySize;
    int xMsg, yMsg;
    int screenWidth;
    int messageCounter = 0;

    public boolean messageOn = false;
    public String message = "";
    private Dialogue currentDialogue;
    public boolean gameOver = false;
    public static boolean GAME_OVER = false;

    float playTime;
    DecimalFormat decFormat = new DecimalFormat("#0.00");

    BufferedImage catBackground;
    public Menu menu = Menu.NEW_GAME;
    public TitleScreenState titleScreenState = TitleScreenState.MAIN_PAGE;
    public CharacterClass characterClass = CharacterClass.MAGE;

    /** UI HUD */
    public ArrayList<SuperStatesObject> hudList = new ArrayList<>();
    // pozitii viata jcuator
    public int heartX, heartY;

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
        yMsg = gPanel.tileSize*5;
        screenWidth = gPanel.tileSize*10;

        // IMAGINE BACKGROUND TITLU
        try {
            catBackground = ImageIO.read(new FileInputStream("res/background/cat.png"));
            catBackground = UtilityTool.scaledImage(catBackground, gPanel.tileSize*2, gPanel.tileSize*2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // CREARE HUD HP JUCATOR
        SuperStatesObject heart = new OBJ_Heart();
        heart.loadObject(gPanel, "res/objectsWithStates/heart");
        heartX = 15;
        heartY = 15;
        hudList.add(heart);
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
                drawPlayerLife(heartX, heartY);
            }
            case Pause -> {
                // pause stuff
                drawPlayerLife(heartX, heartY);
                drawPauseScreen();
            }
            case Dialogue -> {
                // starea de dialog
                drawPlayerLife(heartX, heartY);
                drawDialogueScreen();
            }
            case CharacterState -> {
                // starea de status caracter
                drawCharacterScreen();
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

    private OBJ_Heart getHeart() {
        for (int i = 0; i < hudList.size(); i++) {
            if (hudList.get(i).typeStatesObject == TypeStatesObject.HEART)
                return (OBJ_Heart) hudList.get(i);
        }
        return null;
    }

    public void drawPlayerLife(int posX, int posY) {

        OBJ_Heart heart = getHeart();

        assert heart != null : "Obiectul heart din drawPlayerLife() din clasa ui este null!";

        int x = posX;

        // AFISEAZA HP CURENT PLAYER
        int i;
        // cat timp am 2 vieti, adaug o inima intreaga
        for (i = 0; i < gPanel.player.life-1; i+=2) {
            g2D.drawImage(heart.imgStates.get(0), x, posY, null);
            x += 100;
        }
        // daca mai am o viata de adaugat, adaug jumatate de inima
        if (gPanel.player.life - i == 1) {
            g2D.drawImage(heart.imgStates.get(1), x, posY, null);
            x += 100;
        }

        // AFISEAZA HP MAXIM POSIBIL
        int missingLives = gPanel.player.life > 0 ? gPanel.player.maxLife - gPanel.player.life : gPanel.player.maxLife;
        for (int k = 0; k < missingLives/2; k++) {
            g2D.drawImage(heart.imgStates.get(2), x, posY, null);
            x += 100;
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
                drawItemClassSelection("Pisicuta", y += gPanel.tileSize, CharacterClass.CAT);
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
                    case WARRIOR -> characterClass = CharacterClass.CAT;
                    case CAT -> characterClass = CharacterClass.BACK;
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
                    case CAT -> characterClass = CharacterClass.WARRIOR;
                    case WARRIOR -> characterClass = CharacterClass.ARCHER;
                    case ARCHER -> characterClass = CharacterClass.MAGE;
                    case MAGE -> characterClass = CharacterClass.BACK;
                    case BACK -> characterClass = CharacterClass.CAT;
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
                // adauga mai tarziu
            }
            case EXIT -> {
                System.exit(0);
            }
        }
    }

    public void chooseClass() {
        switch (characterClass) {
            case MAGE -> gPanel.player.characterClassPath = "mage";
            case ARCHER -> gPanel.player.characterClassPath = "archer";
            case WARRIOR -> gPanel.player.characterClassPath = "warrior";
            case CAT -> gPanel.player.characterClassPath = "cat";
            case BACK -> {
                titleScreenState = TitleScreenState.MAIN_PAGE;
                return;
            }
        }
        GamePanel.gameState = GameState.Play;
        gPanel.player.getPlayerSprites();
//        gPanel.playMusic("BlueBoyAdventure.wav");
    }

    public int getXForCenteredText(String text) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        return gPanel.screenWidth / 2 - length / 2;
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
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    private void drawDialogueScreen() {

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

    public int getXForCenteredFrame(String text, int frameX) {
        int length = (int) g2D.getFontMetrics().getStringBounds(text, g2D).getWidth();
        return frameX / 2 - length / 2;
    }

    private void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2D.setColor(c);
        g2D.fillRoundRect(x,y,width,height,35, 35);

        c = new Color(255, 255, 255);
        g2D.setColor(c);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    // manevrarea afisarii textelor din Game Over si a opririi thread-ului de rulare a jocului dupa un anumit timp
    private void manageGameOverDisplay(Graphics2D g2D) {
        displayGameOverLayout(g2D);
        if (messageCounter > 320) { // textul isi va schimba culorile timp de 5.33 secunde
            gPanel.gameThread = null;
        }

        messageCounter++;
    }

    // manevrarea afisarii notificarilor
    private void manageTextDisplays(Graphics2D g2D) {
        if (messageOn) {
            displayCenterNotification(g2D, message, null);
            if (messageCounter > 120) { // textul dispare dupa 2 secunde
                messageCounter = 0;
                messageOn = false;
            }

            messageCounter++;
        }
    }

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
}
