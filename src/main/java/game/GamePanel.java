package game;

import entity.Entity;
import entity.Player;
import features.*;
import interactive_tile.InteractiveTile;
import monster.Monster;
import object.SuperObject;
import object.SuperStatesObject;
import item.equipable.weapon.rangeattack.Projectile;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/** Setari ecran
 *  Panoul de joc mosteneste clasa JPanel
 *  si implementeaza clasa Runnable pentru a rula jocul printr-un fir de executie cu moment de start - stop
 *  */
public class GamePanel extends JPanel implements Runnable {

    // variabile
    public final int originalTileSize = 16; // 16x16 tile - dimensiunea implicita a unui obiect din joc
    public final int scale = (Toolkit.getDefaultToolkit().getScreenResolution() / originalTileSize);// de implementat pentru scale-ul rezolutiei: (125% de exemplu) * Toolkit.getDefaultToolkit().getScreenResolution(); // valoarea scalarii dimensiunii obiectului

    // original scale * scaleValue --> tileSize
    public int tileSize = originalTileSize*scale; // valoarea dimensiunii finale a obiectului de joc
    // realizarea aspectului ratio - 4:3
    public int maxScreenCol = 20;//Toolkit.getDefaultToolkit().getScreenSize().width/tileSize + 1;//20; // 16 tiles horizontal
    public int maxSCreenRow = 12;//Toolkit.getDefaultToolkit().getScreenSize().height/tileSize + 1;//12; // 12 tiles vertical
    // realizarea dimensiunii jocului
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixeli
    public final int screenHeight = tileSize * maxSCreenRow; // 576 pixeli

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    // Instiantiere limite Zoom in & out
    public final int defaultTileSize = tileSize;
    public final int defaultZoom = tileSize * maxWorldCol;
    public final int limitZoomIn = defaultZoom + 1000;
    public final int limitZoomOut = defaultZoom - 900;
    // FOR FULL SCREEN
    public boolean fullScreenOn = true;
//    int screenWidth2 = screenWidth;
//    int screenHeight2 = screenHeight;
//    BufferedImage tempScreen;
//    Graphics2D g2D;

    // Config file
    Config config = new Config(this);

    // FPS - limitarea cadrelor pe secunda
    float FPS = 60.0f;

    /** instantierea texturiilor */
    // texturile generale
    public TileManager tiles = new TileManager(this);
    /** Instantierea clasei ce manevreaza tastatura - KeyHandler */
    public KeyHandler keyH = new KeyHandler(this);
    /** Sunete joc */
    public Sound music = new Sound("res/sound/music");
    public Sound soundEffect = new Sound("res/sound/effects");
    public boolean hasPlayed = false;
    /** Coliziuni si asset-uri */
    public CollisionDetector collisionDetector = new CollisionDetector(this);
    public AssetPool assetPool = new AssetPool(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);

    /** Creand firul de executie al jocului, adaugam conceptul de timp in joc */
    // crearea firului de executie a jocului
    Thread gameThread;

    /** Entitate si obiect */
    // Setarea pozitiei implicite a jucatorului
    // player
    public Player player;
    public String characterClassPath;
    // lista NPC
    public Entity[][] npcList = new Entity[maxMap][20];
    // lista Monster
    public Entity[][] monsterList = new Entity[maxMap][10];
    // lista obiecte
    public SuperObject[][] objects = new SuperObject[maxMap][20];
    // lista obiecte cu ipostaze
    public List<SuperStatesObject> statesObjectList = new ArrayList<>();
    // lista tiles interactive
    public Entity[][] interactiveTiles = new Entity[maxMap][50];

    public ArrayList<Projectile> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    public ArrayList<Entity> entities = new ArrayList<>();

    public boolean hasZoomed = false;

    // GAME STATE - starea jocului
    public static GameState gameState = GameState.NULL;

    /** Constructorul panoului de joc */
    public GamePanel() {
        System.out.println("scale: " + scale);
        // dimensiunea preferata a ecranului de joc
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        // culoare background
        this.setBackground(Color.black);
        // desenele realizate intr-un painting buffer din afara ecranului
        // astfel, performanta de randare este imbunatatita
        this.setDoubleBuffered(true);
        // recunoasterea input-ului butoanelor
        this.addKeyListener(keyH);
        // focusarea panoului de joc pe input-ul de la tastatura
        this.setFocusable(true);
    }

    /** instantieri joc */
    public void setupGame() {
        // adaugarea obiectelor in joc
        assetPool.setObjects();
        // adaugarea npc-urilor
        assetPool.setNPC();
        // adaugarea monstriilor
        assetPool.setMonster();
        // interactive tiles
        assetPool.setInteractiveTiles();
        // setup muzica de fundal
//        playMusic("BlueBoyAdventure.wav");
//        stopMusic();
        gameState = GameState.Title;
    }

    public void retry() {
        player.setDefaultPositions();
        player.restoreLifeMana();
        assetPool.setNPC();
        assetPool.setMonster();
        this.playMusic("BlueBoyAdventure.wav");
    }

    public void restart() {
        retry();
        player.setItems();
        assetPool.setObjects();
        assetPool.setInteractiveTiles();
        this.stopMusic();
    }

    /** startGameThread - instantierea la inceperea rularii jocului */
    public void startGameThread() {

        //instantierea firului de executie
        gameThread = new Thread(this);
        // incepe rualrea thread-ului
        gameThread.start();
    }

    /** Metoda Runnable - manageriaza actiuni ce se petrec in timpul rularii jocului */
    @Override
    public void run() {

        // variabile pentru limitarea cadrelor pe secunda (fps)
        // intervalul de redesenare
        double drawInterval = 1000000000/FPS;
        // urmatorul moment pentru redesenare
        double nextDrawTime = System.nanoTime() + drawInterval;

        /** GAME LOOP */
        while(gameThread != null) {

            // UPDATE: actualizarea informatiilor
            update();

            // DRAW: re-desenarea informatiilor actualizate
            repaint();

            try {
                // timpul de sleep intre update-uri
                double remainingTime = nextDrawTime - System.nanoTime();
                // convertirea timpului din nanosecunde in milisecunde
                remainingTime = remainingTime/1000000;

                /** daca update si repaint dureaza mai mult decat intervalul de timp
                 * inseamna ca timpul alocat sleep-ului este deja folosit
                 * prin urmare, timpul ramas devine nul */
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                // opreste metoda update pentru timpul acordat sleep-ului
                Thread.sleep((long) remainingTime);

                // actualizarea momentului urmator pentru sleep
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Metoda de actualizare a informatiilor */
    public void update() {
        switch (gameState) {
            case Play -> {

                // PLAYER UPDATE
                player.update();

                // NPC UPDATE
                for (int i = 0; i < npcList[1].length; i++) {
                    if (npcList[currentMap][i] != null) {
                        npcList[currentMap][i].update();
                    }
                }

                // ACTUALIZARI MONSTRII
                for (int i = 0; i < monsterList[1].length; i++) {
                    if (monsterList[currentMap][i] != null) {
                        if (monsterList[currentMap][i].alive && !monsterList[currentMap][i].dying) {
                            monsterList[currentMap][i].update();
                        }
                        if (!monsterList[currentMap][i].alive) {
                            ((Monster) monsterList[currentMap][i]).checkDrop();
                            monsterList[currentMap][i] = null;
                        }
                    }
                }

                // ACTUALIZARI PROIECTILE
                for (int i = 0; i < projectileList.size(); i++) {
                    if (projectileList.get(i) != null) {
                        if (projectileList.get(i).alive) {
                            projectileList.get(i).update();
                        }
                        if (!projectileList.get(i).alive) {
                            projectileList.remove(i);
                        }
                    }
                    else {
                        projectileList.remove(i);
                    }
                }

                // PARTICULE
                for (int i = 0; i < particleList.size(); i++) {
                    if (particleList.get(i) != null) {
                        if (particleList.get(i).alive) {
                            particleList.get(i).update();
                        }
                        if (!particleList.get(i).alive) {
                            particleList.remove(i);
                        }
                    }
                    else {
                        particleList.remove(i);
                    }
                }

                // INTERACTIVE TILES
                for (int i = 0; i < interactiveTiles[1].length; i++) {
                    if (interactiveTiles[currentMap][i] != null) {
                        interactiveTiles[currentMap][i].update();
                    }
                }
            }
            case Pause -> {
                // nimic
            }
        }
    }

    /** Metoda de redesenare a informatiilor actualizate */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // convertim grafica la grafica 2D
        Graphics2D g2D = (Graphics2D) graphics;

        long drawStart = 0;
        // DEBUG
        if (keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if (gameState == GameState.Title) {
            ui.draw(g2D);
        }
        // ALTELE
        else {

            // harta
            if (player != null)
                tiles.draw(g2D);

            // tiles interactive
            for (int i = 0; i < interactiveTiles[1].length; i++) {
                if(interactiveTiles[currentMap][i] != null) {
                    interactiveTiles[currentMap][i].draw(g2D);
                }
            }

            addAllLists();

            entities.sort(Comparator.comparingDouble(e -> e.worldY));

            for (Entity entity : entities) {
//                System.out.println(entity.getClass().getName() + " worldY: " + entity.worldY);
                if (entity != null)
                    entity.draw(g2D);
            }

            entities.clear();


//            // obiecte
//            for (SuperObject obj : objects) {
//                if (obj != null) {
//                    obj.draw(g2D, this);
//                }
//            }
//
//            // NPC
//            for (Entity entity : npc) {
//                if (entity != null) {
//                    entity.draw(g2D);
//                }
//            }
//
//            // player
//            if (player != null)
//                player.draw(g2D);
//
////            npc.sort(Comparator.comparingDouble(e -> e.worldY));
////            objects.sort(Comparator.comparingDouble(o -> o.worldY));


            // UI
            ui.draw(g2D);
        }

        // DEBUG
        if (keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2D.setFont(ui.font.deriveFont(20f));
            g2D.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2D.drawString("WorldX: " + (int) player.worldX, x, y);
            y += lineHeight;
            g2D.drawString("WorldY: " + (int) player.worldY, x, y);
            y += lineHeight;
            g2D.drawString("Col: " + (int) (player.worldX + player.solidArea.x)/tileSize, x, y);
            y += lineHeight;
            g2D.drawString("Row: " + (int) (player.worldY + player.solidArea.y)/tileSize, x, y);
            y += lineHeight;

            g2D.drawString("Timp randare: " + passed, x, y);
            System.out.println("Timp acordat randarii: " + passed);
        }

        // eliberarea memoriei ocupate de contextul graficii
        // si resursele pe care le foloseste acesta
        g2D.dispose();
    }

    public void playMusic(String soundName) { // sunet de fundal
        music.setFile(soundName);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(String soundName) { // sunet de efect
        if (!hasPlayed) {
            soundEffect.setFile(soundName);
            soundEffect.play();
//            hasPlayed = true;
        }
    }

    public void stopSE() {
        soundEffect.stop();
    }

    public void addAllLists() {
        if (player != null)
            entities.add(player);
        for (int i = 0; i < npcList[1].length; i++) {
            if (npcList[currentMap][i] != null) {
                entities.add(npcList[currentMap][i]);
            }
        }
        for (int i = 0; i < objects[1].length; i++) {
            if (objects[currentMap][i] != null) {
                entities.add(objects[currentMap][i]);
            }
        }
        for (int i = 0; i < monsterList[1].length; i++) {
            if (monsterList[currentMap][i] != null) {
                entities.add(monsterList[currentMap][i]);
            }
        }
        for (Entity projectile : projectileList) {
            if (projectile != null)
                entities.add(projectile);
        }
        for (Entity particle : particleList) {
            if (particle != null)
                entities.add(particle);
        }
    }

    public void addAllAI() {
        for (int i = 0; i < npcList[1].length; i++) {
            if (npcList[currentMap][i] != null) {
                entities.add(npcList[currentMap][i]);
            }
        }
        for (int i = 0; i < monsterList[1].length; i++) {
            if (monsterList[currentMap][i] != null) {
                entities.add(monsterList[currentMap][i]);
            }
        }
    }
}
