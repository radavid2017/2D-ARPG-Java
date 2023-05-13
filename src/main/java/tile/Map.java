package tile;

import game.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map extends TileManager {

    GamePanel gp;
    ArrayList<BufferedImage> worldMap = new ArrayList<>();
    public boolean miniMapOn = false;

    public Map(GamePanel gp) {
        super(gp);
        this.gp = gp;
        createWorldMap();
    }

    public void createWorldMap() {
        int worldMapWidth = gp.tileSize * gp.maxWorldCol;
        int worldMapHeight = gp.tileSize * gp.maxWorldRow;

        for (int i = 0; i < gp.maxMap; i++) {
            BufferedImage currentMap = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            worldMap.add(currentMap);
            /** Atasam g2D hartii, deci tot ce g2D deseneaza va fi inregistrat in worldMap BufferedImage */
            Graphics2D g2D = (Graphics2D) worldMap.get(i).createGraphics();

            /** Desenarea mini-hartii */
            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                int tileNum = mapTileNum[i][col][row];
                int x = gp.tileSize * col;
                int y = gp.tileSize * row;
                g2D.drawImage(generalTiles.get(tileNum).image, x, y, null);

                col++;
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            g2D.dispose();
        }
    }

    public void drawFullMapScreen(Graphics2D g2D) {
        // Culoare background
        g2D.setColor(Color.black);
        g2D.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Deseneaza Harta
        int width = (int) (gp.screenWidth / 1.25f);
        int height = (int) (gp.screenHeight / 1.25f);
        int x = gp.screenWidth / 2 - width / 2;
        int y = (int) ((gp.screenHeight / 2 - height / 2) / 3f);
        g2D.drawImage(worldMap.get(gp.currentMap), x, y, width, height, null);

        // Deseneaja jucator
        double scale = (double) (gp.tileSize * gp.maxWorldCol) / width;
        int playerX = (int) (x + gp.player.worldX / scale);
        int playerY = (int) (y + gp.player.worldY / (scale * 1.7f));
        int playerSize = (int) (gp.tileSize / scale);
        g2D.drawImage(gp.player.idle.down.animationFrames.get(0), playerX, playerY, playerSize, playerSize, null);

        // Mesaj de sugestie
        g2D.setFont(gp.ui.font.deriveFont(32f));
        g2D.setColor(Color.white);
        g2D.drawString("Apasa M sau ESC pentru a inchide harta", gp.screenWidth / 2, gp.screenHeight - 165);

    }

    public void drawMiniMap(Graphics2D g2D) {
        if (miniMapOn) {
            // Display Mini Map
            int width = 300;//(int) (gp.screenWidth / 3.25f);
            int height = 300;//(int) (gp.screenHeight / 3.25f);
            int x = gp.screenWidth - width - 50;
            int y = 100;

            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2D.drawImage(worldMap.get(gp.currentMap), x, y, width, height, null);

            // Deseneaja jucator
            double scale = (double) (gp.tileSize * gp.maxWorldCol) / width;
            int playerX = (int) (x + gp.player.worldX / scale);
            int playerY = (int) (y + gp.player.worldY / scale);
            int playerSize = (int) (gp.tileSize / (scale/2f));
            g2D.drawImage(gp.player.idle.down.animationFrames.get(0), playerX-4, playerY-5, playerSize, playerSize, null);

            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
}
