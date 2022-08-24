package game;

import entity.Entity;
import entity.Player;
import features.*;
import monster.Monster;
import object.SuperObject;
import object.SuperStatesObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
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
    public int maxScreenCol = Toolkit.getDefaultToolkit().getScreenSize().width/tileSize + 1;//20; // 16 tiles horizontal
    public int maxSCreenRow = Toolkit.getDefaultToolkit().getScreenSize().height/tileSize + 1;//12; // 12 tiles vertical
    // realizarea dimensiunii jocului
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixeli
    public final int screenHeight = tileSize * maxSCreenRow; // 576 pixeli

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    // Instiantiere limite Zoom in & out
    public final int defaultZoom = tileSize * maxWorldCol;
    public final int limitZoomIn = defaultZoom + 1000;
    public final int limitZoomOut = defaultZoom - 900;

    // FPS - limitarea cadrelor pe secunda
    float FPS = 60.0f;

    /** instantierea texturiilor */
    // texturile generale
    public TileManager tiles = new TileManager(this);
    /** Instantierea clasei ce manevreaza tastatura - KeyHandler */
    public KeyHandler keyH = new KeyHandler(this);
    /** Sunete joc */
    Sound music = new Sound("res/sound/music");
    Sound soundEfect = new Sound("res/sound/efects");
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
    // lista NPC
    public List<Entity> npcList = new ArrayList<Entity>();
    // lista Monster
    public List<Entity> monsterList = new ArrayList<>();
    // lista obiecte
    public List<SuperObject> objects = new ArrayList<>();
    // lista obiecte cu ipostaze
    public List<SuperStatesObject> statesObjectList = new ArrayList<>();

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
        player = new Player(this, keyH, tileSize * 23, tileSize * 21, Direction.DOWN);

        // adaugarea obiectelor in joc
        assetPool.setObjects();
        // adaugarea npc-urilor
        assetPool.setNPC();
        // adaugarea monstriilor
        assetPool.setMonster();
        // setup muzica de fundal
        playMusic("BlueBoyAdventure.wav");
        stopMusic();
        gameState = GameState.Title;
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
                for (Entity npc : npcList) {
                    if (npc != null) {
                        npc.update();
                    }
                }
                for (Entity monster : monsterList) {
                    if (monster != null) {
                        monster.update();
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
        if (keyH.checkDrawTime) {
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

            addAllLists();

            entities.sort(Comparator.comparingDouble(e -> e.worldY));

            for (Entity entity : entities) {
//                System.out.println(entity.getClass().getName() + " worldY: " + entity.worldY);
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
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2D.setColor(Color.white);
            g2D.drawString("Timp randare: " + passed, 10, 400);
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
            soundEfect.setFile(soundName);
            soundEfect.play();
        }
    }

    public void stopSE() {
        soundEfect.stop();
    }

    public void addAllLists() {
        if (player != null)
            entities.add(player);
        entities.addAll(npcList);
        for (Entity obj : objects)
            if (obj != null)
                entities.add(obj);
        for (Entity monster : monsterList)
            if (monster != null)
                entities.add(monster);
    }

    public void addAllAI() {
        entities.addAll(npcList);
        entities.addAll(monsterList);
    }
}
