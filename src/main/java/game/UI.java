package game;

import features.Dialogue;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;

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
            case Play -> {
                // play staff
            }
            case Pause -> {
                // pause stuff
                drawPauseScreen();
            }
            case Dialogue -> {
                // starea de dialog
                drawDialogueScreen();
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
        int x = gPanel.tileSize*2;
        int y = gPanel.tileSize/2;
        int width = gPanel.screenWidth - (gPanel.tileSize*5);
        int height = gPanel.tileSize*4;

        drawSubWindow(x, y, width, height);

        // textul de afisat in fereastra de dialog
        g2D.setFont(font.deriveFont(Font.PLAIN, 28f));
        x += gPanel.tileSize;
        y += gPanel.tileSize;

        String text = currentDialogue.peekText();
        for (String line : text.split("\n")) {
            g2D.drawString(line, x, y);
            y += 40;
        }
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
