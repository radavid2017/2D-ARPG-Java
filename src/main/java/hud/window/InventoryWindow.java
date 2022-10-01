package hud.window;

import game.GamePanel;

import java.awt.*;

public class InventoryWindow extends WindowHUD {
    /** Variabile INVENTAR */
    public int slotCol = 0;
    public int slotRow = 0;
    public int maxSlotCol = 4;
    public int maxSlotRow = 5;

    public InventoryWindow(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void draw(GamePanel gp, Graphics2D g2D) {

//        // CADRU FEREASTRA
//        x = (int) ((gp.screenWidth/22) * 13.75);
//        y = gp.screenHeight/6;
//        width = gp.defaultTileSize * 5 + 40;
//        height = gp.defaultTileSize * 6 + 40;
        drawWindow(g2D);

        // SLOT
        final int slotXstart = x + 20;
        final int slotYstart = y + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.defaultTileSize+10;

        // DESENAREA ITEMELOR JUCATORULUI
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            // CURSOR ECHIPARE
            if (gp.player.inventory.get(i) == gp.player.currentWeapon
                    || gp.player.inventory.get(i) == gp.player.currentShield) {
                g2D.setColor(new Color(240, 190, 90));
                g2D.fillRoundRect(slotX, slotY, gp.defaultTileSize, gp.defaultTileSize, 10, 10);
            }

            g2D.drawImage(gp.player.inventory.get(i).image, slotX, slotY, null);
//            System.out.println("i: " + i);
            slotX += slotSize;
            if (i % 10 == 4 || i % 10 == 9) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.defaultTileSize;
        int cursorHeight = gp.defaultTileSize;

        // DESENARE CURSOR
        g2D.setColor(Color.white);
        g2D.setStroke(new BasicStroke(3));
        g2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // CADRU DESCRIERE ITEM/OBIECT
        ObjDescriptionWindow objDescriptionWindow =
                new ObjDescriptionWindow(x,y+height,width,gp.defaultTileSize * 2);

        // TEXT DESCRIERE
        int textX = objDescriptionWindow.x + 20;
        int textY = objDescriptionWindow.y + gp.defaultTileSize/2;
        g2D.setFont(gp.ui.font.deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()) {

            objDescriptionWindow.drawWindow(g2D);

            for (String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2D.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot() {
        return slotCol + (slotRow * maxSlotRow);
    }
}
