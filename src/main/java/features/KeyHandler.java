package features;

import game.GamePanel;
import game.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/** KeyHandler - implementarea controlului de la tastatura asupra jocului */
public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean upWasPressed,downWasPressed,leftWasPressed,rightWasPressed;
    public boolean enterPressed;
    public boolean showDebugText = false;
    public boolean spacePressed;
    public boolean shiftPressed;
    GamePanel gPanel;

    public boolean hasPlayed = false;

    public KeyHandler(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    /** Pentru tastele de mers spre nord/sud am observat ca daca apas alta directie, player-ul nu schimba directia
     *  Asadar, aceste 2 functii fac posibila comutarea intre deplasari */
    // adauga pe stiva prima tasta apasata
    private void stackFirstPressed() {

    }
    // primeste ca comanda prima tasta apasata
    private void retrieveFirstPressed() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        /** getKeyCode - returneaza valoarea din codul ascii asociat butonului */
        int code = e.getKeyCode();

        switch (GamePanel.gameState) {
            case Title -> titleState(code);
            case Play -> playState(code);
            case Dialogue -> dialogueState(code);
            case CharacterState -> characterState(code);
            case OptionsState -> optionsState(code);
            case GameOverState -> gameOverState(code);
            case TradeState -> tradeState(code);
            case MapState -> mapState(code);
        }


        switch (code) {
            // EXIT GAME
//            case KeyEvent.VK_ESCAPE -> System.exit(0);
            // DEBUG
            case KeyEvent.VK_T -> showDebugText = !showDebugText;
            // REIMPROSPATARE HARTA
            case KeyEvent.VK_R -> {
                switch (gPanel.currentMap) {
                    case 0 -> gPanel.tiles.loadMap(gPanel.tiles.mapPath.get(0), gPanel.currentMap);
                    case 1 -> gPanel.tiles.loadMap(gPanel.tiles.mapPath.get(1), gPanel.currentMap);
                }
            }
            // PAUSE - UNPAUSE
            case KeyEvent.VK_P -> GamePanel.gameState =
                    GamePanel.gameState == GameState.Play ? GameState.Pause : GameState.Play;
            // CHARACTER STATE
            case KeyEvent.VK_C -> GamePanel.gameState =
                    GamePanel.gameState == GameState.Play ? GameState.CharacterState : GameState.Play;
            // MAP STATE
            case KeyEvent.VK_M -> GamePanel.gameState =
                    GamePanel.gameState == GameState.Play ? GameState.MapState : GameState.Play;
        }

    }

    private void mapState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            GamePanel.gameState = GameState.Play;
        }
    }

    private void tradeState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        switch (gPanel.ui.getSubState()) {
            case 0 -> {
                if (code == KeyEvent.VK_UP) {
                    gPanel.ui.decreaseTradeCmdSelection();
                }
                if (code == KeyEvent.VK_DOWN) {
                    gPanel.ui.increaseTradeCmdSelection();
                }
            }
        }
        switch (gPanel.ui.getSubState()) {
            case 1 -> {
                npcInventory(code);
                if (code == KeyEvent.VK_ESCAPE) {
                    gPanel.ui.setSubState(0);
                }
            }
            case 2 -> {
                playerInventory(code);
                if (code == KeyEvent.VK_ESCAPE) {
                    gPanel.ui.setSubState(0);
                }
            }
        }
    }

    private void gameOverState(int code) {
        switch (code) {
            case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> {
                gPanel.ui.changeCmdGameOver();
            }
            case KeyEvent.VK_ENTER -> {
                if (gPanel.ui.getCommandNum() == 0) {
                    GamePanel.gameState = GameState.Play;
                    gPanel.resetGame(false);
                }
                else {
                    if (gPanel.ui.getCommandNum() == 1) {
                        GamePanel.gameState = GameState.Title;
                        gPanel.resetGame(true);
                    }
                }
            }
        }
    }

    public void optionsState(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                GamePanel.gameState = GameState.Play;
            }

            case KeyEvent.VK_ENTER -> {
                enterPressed = true;
            }

            case KeyEvent.VK_UP -> {
                switch (gPanel.ui.getSubState()) {
                    case 0 -> gPanel.ui.decreaseCommandOptionsLine();
                    case 3 -> gPanel.ui.changeYesNoCursor();
                }
            }

            case KeyEvent.VK_DOWN -> {
                switch (gPanel.ui.getSubState()) {
                    case 0 -> gPanel.ui.increaseCommandOptionsLine();
                    case 3 -> gPanel.ui.changeYesNoCursor();
                }
            }

            case KeyEvent.VK_LEFT -> {
                if (gPanel.ui.getSubState() == 0) {
                    if (gPanel.ui.getCommandNum() == 1) {
                        decreaseMusicVolume();
                    }
                    if (gPanel.ui.getCommandNum() == 2) {
                        decreaseSoundEffectVolume();
                    }
                }
            }

            case KeyEvent.VK_RIGHT -> {
                if (gPanel.ui.getSubState() == 0) {
                    if (gPanel.ui.getCommandNum() == 1) {
                        increaseMusicVolume();
                    }
                    if (gPanel.ui.getCommandNum() == 2) {
                        increaseSoundEffectVolume();
                    }
                }
            }
        }
    }

    private  void decreaseSoundEffectVolume() {
        if (gPanel.soundEffect.volumeScale > 0) {
            gPanel.soundEffect.volumeScale--;
            gPanel.playSE("cursor.wav");
        }
    }

    private void increaseSoundEffectVolume() {
        if (gPanel.soundEffect.volumeScale < 5) {
            gPanel.soundEffect.volumeScale++;
            gPanel.playSE("cursor.wav");
        }
    }

    private void decreaseMusicVolume() {
        if (gPanel.music.volumeScale > 0) {
            gPanel.music.volumeScale--;
            gPanel.music.checkVolume();
            gPanel.playSE("cursor.wav");
        }
    }

    private void increaseMusicVolume() {
        if (gPanel.music.volumeScale < 5) {
            gPanel.music.volumeScale++;
            gPanel.music.checkVolume();
            gPanel.playSE("cursor.wav");
        }
    }

    public void titleState(int code) {
        switch (gPanel.ui.titleScreenState){
            case MAIN_PAGE -> {
                switch (code) {
                    case KeyEvent.VK_DOWN -> gPanel.ui.nextItem();
                    case KeyEvent.VK_UP -> gPanel.ui.previousItem();
                    case KeyEvent.VK_ENTER -> gPanel.ui.chooseItem();
                }
            }
            case CLASS_SELECTION -> {
                switch (code) {
                    case KeyEvent.VK_DOWN -> gPanel.ui.nextItem();
                    case KeyEvent.VK_UP -> gPanel.ui.previousItem();
                    case KeyEvent.VK_ENTER -> gPanel.ui.chooseClass(gPanel);
                }
            }
        }
    }

    public void playState(int code) {
        switch (code) {
            // MOVEMENT
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            // ATTACKING
            case KeyEvent.VK_SPACE -> {
                if (!hasPlayed) {
                    switch (gPanel.player.characterClass) {
                        case WARRIOR -> gPanel.player.currentWeapon.playSound(); //gPanel.playSE("swingweapon.wav");
                        case MAGE -> {
                            if (!gPanel.player.currentWeapon.alive) {
                                gPanel.player.currentWeapon.playSound();
                            }
                        }
                    }
                        hasPlayed = true;
                        spacePressed = true;
                }
            }
            // GUARD
            case KeyEvent.VK_SHIFT -> shiftPressed = true;
            // ZOOM IN
            case KeyEvent.VK_UP -> {
//                Camera.zoomInOut(1);
//                Camera.rescaleAll();
//                    Camera.validatePositions(gPanel.npc, gPanel.player);
//                    Camera.fixPlayerStuckInTile();
            }
            // ZOOM OUT
            case KeyEvent.VK_DOWN -> {
//                Camera.zoomInOut(-1);
//                Camera.rescaleAll();
//                    Camera.validatePositions(gPanel.npc, gPanel.player);
//                    Camera.fixPlayerStuckInTile();
            }
            // ENTER DIALOGUE
            case KeyEvent.VK_ENTER -> {
                enterPressed = true;
            }
            // CHARACTER STATE
            case KeyEvent.VK_C -> {
                GamePanel.gameState = GameState.Play;
            }
            // OPTIONS MENU
            case KeyEvent.VK_ESCAPE -> {
                GamePanel.gameState = GameState.OptionsState;
            }
            // MINI MAP
            case KeyEvent.VK_N -> gPanel.map.miniMapOn = !gPanel.map.miniMapOn;
        }
    }

    public void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            GamePanel.gameState = GameState.Play;
        }
    }

    public void characterState(int code) {
        playerInventory(code);
        if (code == KeyEvent.VK_ENTER) {// ALEGEREA UNUI ITEM
            gPanel.player.selectItem();
        }
    }

    public void playerInventory(int code) {
        switch (code) {
            case KeyEvent.VK_W -> {
                if (gPanel.ui.playerInventoryWindow.slotRow > 0) {
                    gPanel.ui.playerInventoryWindow.slotRow--;
//                    gPanel.ui.slotRow--;
                    gPanel.playSE("cursor.wav");
                }
            }
            case KeyEvent.VK_A -> {
                if (gPanel.ui.playerInventoryWindow.slotCol > 0) {
                    gPanel.ui.playerInventoryWindow.slotCol--;
//                    gPanel.ui.slotCol--;
                    gPanel.playSE("cursor.wav");
                }
            }
            case KeyEvent.VK_S -> {
                if (gPanel.ui.playerInventoryWindow.slotRow < gPanel.ui.playerInventoryWindow.maxSlotRow) {
                    gPanel.ui.playerInventoryWindow.slotRow++;
//                    gPanel.ui.slotRow++;
                    gPanel.playSE("cursor.wav");
                }
            }
            case KeyEvent.VK_D -> {
                if (gPanel.ui.playerInventoryWindow.slotCol < gPanel.ui.playerInventoryWindow.maxSlotCol) {
                    gPanel.ui.playerInventoryWindow.slotCol++;
//                    gPanel.ui.slotCol++;
                    gPanel.playSE("cursor.wav");
                }
            }
        }
    }

    public void npcInventory(int code) {
        switch (code) {
            case KeyEvent.VK_W -> {
                if (gPanel.ui.npcInventoryWindow.slotRow > 0) {
                    gPanel.ui.npcInventoryWindow.slotRow--;
//                    gPanel.ui.slotRow--;
                    gPanel.playSE("cursor.wav");
                }
            }
            case KeyEvent.VK_A -> {
                if (gPanel.ui.npcInventoryWindow.slotCol > 0) {
                    gPanel.ui.npcInventoryWindow.slotCol--;
//                    gPanel.ui.slotCol--;
                    gPanel.playSE("cursor.wav");
                }
            }
            case KeyEvent.VK_S -> {
                if (gPanel.ui.npcInventoryWindow.slotRow < gPanel.ui.npcInventoryWindow.maxSlotRow) {
                    gPanel.ui.npcInventoryWindow.slotRow++;
//                    gPanel.ui.slotRow++;
                    gPanel.playSE("cursor.wav");
                }
            }
            case KeyEvent.VK_D -> {
                if (gPanel.ui.npcInventoryWindow.slotCol < gPanel.ui.npcInventoryWindow.maxSlotCol) {
                    gPanel.ui.npcInventoryWindow.slotCol++;
//                    gPanel.ui.slotCol++;
                    gPanel.playSE("cursor.wav");
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            // MOVEMENT
            case KeyEvent.VK_W ->  upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            // ATTACKING
            case KeyEvent.VK_SPACE -> {
                hasPlayed = false;
                spacePressed = false;
                gPanel.player.currentAnimation.intervalChangingFrames = 0;
            }
            case KeyEvent.VK_SHIFT -> shiftPressed = false;
        }
    }
}
