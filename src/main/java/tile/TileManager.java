package tile;

import features.Camera;
import features.UtilityTool;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TileManager {

    public GamePanel gPanel;
    public List<Tile> generalTiles;
    public List<Tile> specialTiles;
    public List<Tile> interiorTiles;
    public static List<BufferedImage> originalTilesImage = new ArrayList<>();
    public int[][][] mapTileNum;
    private int idTiles = 9;
    boolean drawPath = true;

    public List<String> mapPath;

    public TileManager(GamePanel gPanel) {
        this.gPanel = gPanel;
        mapTileNum = new int[gPanel.maxMap][gPanel.maxWorldCol][gPanel.maxWorldRow];
        generalTiles = loadTiles("res/tiles/grass", false);
        generalTiles.addAll(loadTiles("res/tiles/water", true));
        generalTiles.addAll(loadTiles("res/tiles/road", false));
        generalTiles.addAll(loadTiles("res/tiles/earth", false));
        generalTiles.addAll(loadTiles("res/tiles/wall", true));
        generalTiles.addAll(loadTiles("res/tiles/tree", true));

        generalTiles.addAll(loadTiles("res/tiles/interior/hut", false));
        generalTiles.addAll(loadTiles("res/tiles/interior/floor", false));
        generalTiles.addAll(loadTiles("res/tiles/interior/table", true));

        System.out.println("Texturi generale: " + this.generalTiles.size());
        mapPath = new ArrayList<>();
        mapPath.add("res/maps/worldV3.txt");
        loadMap(mapPath.get(0), 0);
        mapPath.add("res/maps/interior01.txt");
        loadMap(mapPath.get(1), 1);
    }

    public List<Tile> loadTiles(String filePath, boolean isSolid) {
        try {
            DecimalFormat decFormat = new DecimalFormat("#00");
            File directory = new File(filePath);
            List<Tile> tileList = new ArrayList<>();
            for (int i = 0; i < directory.list().length; i++) {
                // citirea imaginii
                String spriteName = filePath + "/" + filePath.substring(filePath.lastIndexOf("/")+1) + decFormat.format(i) + ".png";
                System.out.println("TEXTURA " + spriteName + " incarcata cu succes.");
                Tile tile = new Tile();
                tile.image = ImageIO.read(new FileInputStream(spriteName));
                // scalarea imaginii
                tile.image = UtilityTool.scaledImage(tile.image, gPanel.tileSize, gPanel.tileSize);
                // atribuire id bloc textura
                tile.idTile = ++idTiles;
                tile.isColliding = isSolid;
                // se adauga textura in lista
                originalTilesImage.add(tile.image);
                tileList.add(tile);
            }
            return tileList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public List<Tile> loadTiles(String filePath) {
//        try {
//            File directory = new File(filePath);
//            List<Tile> tileList = new ArrayList<>();
//            for (int i = 0; i < directory.list().length; i++) {
//                // citirea imaginii
//                String spriteName = filePath + "/" + i + ".png";
//                System.out.println("TEXTURA " + spriteName + "incarcata cu succes.");
//                Tile tile = new Tile();
//                tile.image = ImageIO.read(new FileInputStream(spriteName));
//                // scalarea imaginii
//                tile.image = UtilityTool.scaledImage(tile.image, gPanel.tileSize, gPanel.tileSize);
//                // se adauga textura in lista
//                tileList.add(tile);
//            }
//            return tileList;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void loadMap(String filePath, int map) {
        try {
            FileInputStream is = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
             while (col < gPanel.maxWorldCol && row < gPanel.maxWorldRow) {
                 String line = br.readLine();
                 while (col < gPanel.maxWorldCol) {
                     String[] numbers = line.split(" ");
                     int num = Integer.parseInt(numbers[col]);
                     mapTileNum[map][col][row] = num;
                     col++;
                 }
                 if (col == gPanel.maxWorldCol) {
                     col = 0;
                     row++;
                 }
             }
             br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2D) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gPanel.maxWorldCol && worldRow < gPanel.maxWorldRow) {
            int idTileMap = mapTileNum[gPanel.currentMap][worldCol][worldRow];

            /** IMPLEMENTARE CAMERA */
            // instantierea coordonatelor
            int worldX = worldCol * gPanel.tileSize;
            int worldY = worldRow * gPanel.tileSize;
            double screenX = worldX - gPanel.player.worldX + gPanel.player.screenX;
            double screenY = worldY - gPanel.player.worldY + gPanel.player.screenY;
            // Instantiere camera
            Camera camera = new Camera(worldX, worldY, screenX, screenY, gPanel);
            // Management Camera
            camera.manageTiles(g2D, this, idTileMap);

            worldCol++;

            if (worldCol == gPanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        if (drawPath) {
            g2D.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < gPanel.pFinder.pathList.size(); i++) {
                int worldX = gPanel.pFinder.pathList.get(i).col * gPanel.tileSize;
                int worldY = gPanel.pFinder.pathList.get(i).row * gPanel.tileSize;
                int screenX = (int) (worldX - gPanel.player.worldX + gPanel.player.screenX);
                int screenY = (int) (worldY - gPanel.player.worldY + gPanel.player.screenY);

                g2D.fillRect(screenX, screenY, gPanel.tileSize, gPanel.tileSize);
            }
        }
    }
}
