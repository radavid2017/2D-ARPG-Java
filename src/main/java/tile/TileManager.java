package tile;

import features.Camera;
import features.UtilityTool;
import game.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TileManager {

    public GamePanel gp;
    public List<Tile> generalTiles;
    public static List<BufferedImage> originalTilesImage = new ArrayList<>();
    public int[][][] mapTileNum;
    private int idTiles = 9;
    boolean drawPath = true;

    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    public List<String> mapPath;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        // Citeste din fisierul tiledata.txt
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("res/maps/tiledata.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Preia nume tile si collision info din fisier
        String line;

        try {
            while ((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }



//        InputStream is = getClass().getResourceAsStream("res/maps/tiledata.txt");
//        BufferedReader br = null;
//        if (is != null) {
//            br = new BufferedReader(new InputStreamReader(is));
//        }
//
//        // Preia nume tile si collision info din fisier
//        String line;
//
//        try {
//            while ((line = br.readLine()) != null) {
//                fileNames.add(line);
//                collisionStatus.add(br.readLine());
//            }
//            br.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        generalTiles = new ArrayList<>();
        loadTiles();

        // Preia maxWorldCol & Row pentru harta
//        is = getClass().getResourceAsStream("res/maps/worldV3.txt");
        try {
            br = new BufferedReader(new FileReader("res/maps/worldmap.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            String line2 = br.readLine();
            String maxTile[] = line2.split(" ");

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;

            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

            br.close();

            System.out.println("Texturi generale: " + this.generalTiles.size());
            mapPath = new ArrayList<>();
            mapPath.add("res/maps/worldmap.txt");
            loadMap(mapPath.get(0), 0);
            mapPath.add("res/maps/indoor01.txt");
            loadMap(mapPath.get(1), 1);
            mapPath.add("res/maps/dungeon01.txt");
            loadMap(mapPath.get(2), 2);
            mapPath.add("res/maps/dungeon02.txt");
            loadMap(mapPath.get(3), 3);

        } catch (IOException e) {
            System.out.println("Exception!");
        }

    }

//    public TileManager(GamePanel gPanel) {
//        this.gPanel = gPanel;
//        mapTileNum = new int[gPanel.maxMap][gPanel.maxWorldCol][gPanel.maxWorldRow];
//        generalTiles = loadTiles("res/tiles/grass", false);
//        generalTiles.addAll(loadTiles("res/tiles/water", true));
//        generalTiles.addAll(loadTiles("res/tiles/road", false));
//        generalTiles.addAll(loadTiles("res/tiles/earth", false));
//        generalTiles.addAll(loadTiles("res/tiles/wall", true));
//        generalTiles.addAll(loadTiles("res/tiles/tree", true));
//
//        generalTiles.addAll(loadTiles("res/tiles/interior/hut", false));
//        generalTiles.addAll(loadTiles("res/tiles/interior/floor", false));
//        generalTiles.addAll(loadTiles("res/tiles/interior/table", true));
//
//        System.out.println("Texturi generale: " + this.generalTiles.size());
//        mapPath = new ArrayList<>();
//        mapPath.add("res/maps/worldV3.txt");
//        loadMap(mapPath.get(0), 0);
//        mapPath.add("res/maps/interior01.txt");
//        loadMap(mapPath.get(1), 1);
//    }

    public void loadTiles() {
        for (int i = 0; i < fileNames.size(); i++) {

            String fileName;
            boolean collision;

            // Preia numele fisierului
            fileName = fileNames.get(i);

            // Preia statusul de coliziune
            if (collisionStatus.get(i).equals("true")) {
                collision = true;
            }
            else {
                collision = false;
            }

            setup(i, fileName, collision);
        }
    }

    public void setup(int index, String imagePath, boolean collision) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream("res/tiles/" + imagePath));
            image = UtilityTool.scaledImage(image, gp.tileSize, gp.tileSize);
            Tile tile = new Tile(index, image, collision);
            generalTiles.add(tile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public List<Tile> loadTiles(String filePath, boolean isSolid) {
//        try {
//            DecimalFormat decFormat = new DecimalFormat("#00");
//            File directory = new File(filePath);
//            List<Tile> tileList = new ArrayList<>();
//            for (int i = 0; i < directory.list().length; i++) {
//                // citirea imaginii
//                String spriteName = filePath + "/" + filePath.substring(filePath.lastIndexOf("/")+1) + decFormat.format(i) + ".png";
//                System.out.println("TEXTURA " + spriteName + " incarcata cu succes.");
//                Tile tile = new Tile();
//                tile.image = ImageIO.read(new FileInputStream(spriteName));
//                // scalarea imaginii
//                tile.image = UtilityTool.scaledImage(tile.image, gPanel.tileSize, gPanel.tileSize);
//                // atribuire id bloc textura
//                tile.idTile = ++idTiles;
//                tile.isColliding = isSolid;
//                // se adauga textura in lista
//                originalTilesImage.add(tile.image);
//                tileList.add(tile);
//            }
//            return tileList;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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
             while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                 String line = br.readLine();
                 while (col < gp.maxWorldCol) {
                     String[] numbers = line.split(" ");
                     int num = Integer.parseInt(numbers[col]);
                     mapTileNum[map][col][row] = num;
                     col++;
                 }
                 if (col == gp.maxWorldCol) {
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

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int idTileMap = mapTileNum[gp.currentMap][worldCol][worldRow];

            /** IMPLEMENTARE CAMERA */
            // instantierea coordonatelor
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            double screenX = worldX - gp.player.worldX + gp.player.screenX;
            double screenY = worldY - gp.player.worldY + gp.player.screenY;
            // Instantiere camera
            Camera camera = new Camera(worldX, worldY, screenX, screenY, gp);
            // Management Camera
            camera.manageTiles(g2D, this, idTileMap);

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        if (drawPath) {
            g2D.setColor(new Color(255, 0, 0, 70));
            for (int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
                int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY);

                g2D.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }
    }
}
