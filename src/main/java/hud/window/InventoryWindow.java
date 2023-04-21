package hud.window;

import entity.Entity;
import entity.Player;
import game.GamePanel;
import item.Item;
import item.consumable.coin.Coin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class InventoryWindow extends WindowHUD {
    /** Variabile INVENTAR */
    GamePanel gp;
    public int slotCol = 0;
    public int slotRow = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    public int maxSlotCol = 4;
    public int maxSlotRow = 5;

    public ObjDescriptionWindow objDescriptionWindow;
    public ObjDescriptionWindow moneyInfoWindow;
    public ObjDescriptionWindow priceWindow;
    BufferedImage coinImg;

    public InventoryWindow(GamePanel gp, int x, int y, int width, int height, boolean isPlayer) {
        super(x, y, width, height);
        this.gp = gp;
        // CADRU DESCRIERE ITEM/OBIECT
        objDescriptionWindow =
                new ObjDescriptionWindow(x,y+height,width,gp.defaultTileSize * 2);
        // CADRU MONEY / INFO
        moneyInfoWindow =
                new ObjDescriptionWindow(x, objDescriptionWindow.y + objDescriptionWindow.height, width, gp.defaultTileSize);
        // CADRU PRET ITEM
        priceWindow =
                new ObjDescriptionWindow((int) (gp.screenWidth/3.53f), (int) (gp.screenHeight/1.7f),
                        gp.screenWidth/9, gp.screenHeight/15);

        if (isPlayer) {
            priceWindow.x += 1000;
        }

        Coin coin = new Coin(gp);
        coinImg = coin.image;
    }


    public void draw(GamePanel gp, Entity entity, ArrayList<Item> inventory, Graphics2D g2D, Boolean isUsing) {

//        // CADRU FEREASTRA
//        x = (int) ((gp.screenWidth/22) * 13.75);
//        y = gp.screenHeight/6;
//        width = gp.defaultTileSize * 5 + 40;
//        height = gp.defaultTileSize * 6 + 40;

//        if (entity instanceof NPC merchant) {
//            x -= 500;
//            slotCol = npcSlotCol;
//            slotRow = npcSlotRow;
//        }
//        else {
//            // player slot
//            slotCol = playerSlotCol;
//            slotRow = playerSlotRow;
//        }

        drawWindow(g2D);

        // SLOT
        final int slotXstart = x + 20;
        final int slotYstart = y + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.defaultTileSize+10;

        // DESENAREA ITEMELOR JUCATORULUI
        for (int i = 0; i < inventory.size(); i++) {

            // CURSOR ECHIPARE PLAYER
            if (entity instanceof Player player) {
                if (player.inventory.get(i) == player.currentWeapon
                        || player.inventory.get(i) == player.currentShield
                        || player.inventory.get(i) == player.currentLight) {
                    g2D.setColor(new Color(240, 190, 90));
                    g2D.fillRoundRect(slotX, slotY, gp.defaultTileSize, gp.defaultTileSize, 10, 10);
                }
            }
            g2D.drawImage(inventory.get(i).image, slotX, slotY, null);

            // AFISARE CANTITATE OBIECTE STACKABLE
            if (inventory.get(i).amount > 1) {
                g2D.setFont(g2D.getFont().deriveFont(48f));
                int amountX;
                int amountY;

                String s = String.valueOf(inventory.get(i).amount);
                amountX = gp.ui.getXForAlignToRightText(s, slotX + 95);
                amountY = slotY + 95;

                // UMBRA
                g2D.setColor(new Color(60, 60, 60));
                g2D.drawString(s, amountX, amountY);

                // NUMARUL
                g2D.setColor(Color.white);
                g2D.drawString(s, amountX - 3, amountY - 3);
            }

//            System.out.println("i: " + i);
            slotX += slotSize;
            if (i % 10 == 4 || i % 10 == 9) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // TEXT DESCRIERE
        int textX = objDescriptionWindow.x + 20;
        int textY = objDescriptionWindow.y + gp.defaultTileSize/2;
        g2D.setFont(gp.ui.font.deriveFont(28F));

        // CURSOR
        if (isUsing) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.defaultTileSize;
            int cursorHeight = gp.defaultTileSize;

            // DESENARE CURSOR
            g2D.setColor(Color.white);
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            int itemIndex = getItemIndexOnSlot();

            if (itemIndex < inventory.size()) {

                objDescriptionWindow.drawWindow(g2D);

                for (String line : inventory.get(itemIndex).description.split("\n")) {
                    g2D.drawString(line, textX, textY);
                    textY += 32;
                }

                // DRAW PRICE WINDOW
                if (itemIndex < inventory.size()) {
                    priceWindow.drawWindow(g2D);
                    g2D.drawImage(coinImg, priceWindow.x + 10, priceWindow.y + 5, (int) (gp.tileSize / 1.5f), (int) (gp.tileSize / 1.5f), null);
                    String text;
                    if (entity instanceof Player) {
                        text = "" + inventory.get(itemIndex).price / 2;
                    }
                    else {
                        text = "" + inventory.get(itemIndex).price;
                    }
                    g2D.setFont(gp.ui.font.deriveFont(45F));
                    int priceX;
                    if (entity instanceof Player) {
                        priceX = gp.ui.getXForCenteredFrame(text, priceWindow.x + 1800);
                    }
                    else {
                        priceX = gp.ui.getXForCenteredFrame(text, priceWindow.x + 800);
                    }
                    g2D.drawString(text, priceX, priceWindow.y + 51);
                }
            }
        }

        g2D.setFont(gp.ui.font.deriveFont(28F));
        moneyInfoWindow.drawWindow(g2D);
        if (entity instanceof Player player) {
            g2D.drawString("Monedele tale: " + player.coin, textX, moneyInfoWindow.y + gp.defaultTileSize/2);
        }
        else {
            g2D.drawString("[ESC] Inapoi", textX, moneyInfoWindow.y + gp.defaultTileSize/2);
        }
    }

    public int getItemIndexOnSlot() {
        return slotCol + (slotRow * maxSlotRow);
    }
}
