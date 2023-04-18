package entity;

import animations.StateMachine;
import animations.TypeAnimation;
import features.*;
import game.CharacterClass;
import game.GamePanel;
import game.GameState;
import interactive_tile.DestructibleTile;
import item.Consumable;
import item.Equipable;
import item.Item;
import item.consumable.coin.OBJ_Coin;
import item.consumable.key.KeyGold;
import item.consumable.potion.PotionRed;
import item.equipable.light.Light;
import item.equipable.shield.Shield;
import item.equipable.weapon.Weapon;
import item.equipable.weapon.rangeattack.Projectile;
import item.equipable.weapon.rangeattack.spell.Fireball;
import item.equipable.shield.NormalShield;
import item.equipable.weapon.sword.NormalSword;
import monster.Monster;
import object.obstacle.Obstacle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// clasa north ce detine informatii despre jucator
// precum pozitia sa si viteza de deplasare
public class Player extends Creature {

    Graphics2D g2D = null;

    public KeyHandler keyH;

    public boolean lightUpdated = false;

    public final int screenX;
    public final int screenY;

    public String characterClassPath = "";
    public CharacterClass characterClass;

    public int level;
    public int strength;
    public int dexterity;
    public int nextLevelExp;
    public int coin;
    public Weapon currentWeapon;
    public Shield currentShield;
    public Light currentLight;

    /** Lista animatii arme */
    public StateMachine attackWeapon = null;
    public StateMachine attackAxe = null;
//    public Projectile currentProjectile;

    // ATRIBUTE ITEME
    public int attackValue;
    public int defenseValue;

    // DISABLE ATTACK
    public boolean attackCanceled = false;

    // INVENTAR JUCATOR
    public ArrayList<Item> inventory = new ArrayList<>();
    public final int maxInventorySize = 30;

    // numarul de chei pe care jucatorul le detine in timp real
//    public int numKeys = 0;

//    public boolean invincible = false;
//    public int invincibleCounter = 0;

    /** Constructor player */
    public Player(GamePanel gPanel, KeyHandler keyH, int x, int y, Direction direction, String characterClassPath) {

        super(gPanel);

        isPlayer = true;
        this.characterClassPath = characterClassPath;

        this.keyH = keyH;
        this.worldX = x;
        this.worldY = y;
        defaultSpeed = this.gPanel.worldWidth/600.0d;
        this.speed = defaultSpeed;
        System.out.println("VITEZA PLAYER: " + this.speed);
        this.direction = direction;

        screenX = gPanel.screenWidth/2 - (gPanel.tileSize/2);
        screenY = gPanel.screenHeight/2 - (gPanel.tileSize/2);

        // INSTANTIERE COLIZIUNE
        solidArea = new Rectangle();
        solidArea.x = gPanel.tileSize/4;
        solidArea.y = gPanel.tileSize/2;
        solidAreaDefaultX = gPanel.tileSize/4;
        solidAreaDefaultY = gPanel.tileSize/2;
        solidArea.width = gPanel.tileSize/2;
        solidArea.height = (int) (gPanel.tileSize/2.25);

        // INSTANTIERE STATUS JUCATOR
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        level = 1;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 500;
        currentWeapon = new NormalSword(gPanel);
        currentShield = new NormalShield(gPanel);



        // debug
//        setPosition(gPanel.tileSize * 10, gPanel.tileSize * 13);

//        solidArea.x = 32;
//        solidArea.y = 32;
//        solidAreaDefaultX = 32;
//        solidAreaDefaultY = 32;
//        solidArea.width = 38; // 64;
//        solidArea.height = 62; //48; //62;

//        setDefaultAttackArea();

        this.getPlayerSprites();


        setUpClassChooser();

        updateAttack();
        updateDefense();
 
        setItems();
        setDefaultPositions();
    }

    public void setDefaultPositions() {
        worldX = gPanel.tileSize * 23;
        worldY = gPanel.tileSize * 21;

        // map 2
//        worldX = gPanel.tileSize * 12;
//        worldY = gPanel.tileSize * 10;

        // langa hut
//        worldX = gPanel.tileSize * 10;
//        worldY = gPanel.tileSize * 41;
        direction = Direction.DOWN;
    }

    public void restoreLifeMana() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    public void setUpClassChooser() {
        switch (characterClassPath) {
            case "warrior" -> characterClass = CharacterClass.WARRIOR;
            case "mage" -> {
                characterClass = CharacterClass.MAGE;
                currentWeapon = new Fireball(gPanel);
            }
            case "archer" -> characterClass = CharacterClass.ARCHER;
            case "cat" -> characterClass = CharacterClass.CAT;
        }
    }

    public void setupAttackAnimationPlayer(String creaturePath) {
        attackAxe = new StateMachine();
        attackAxe.loadCompleteAnimation(gPanel, creaturePath + "\\attack\\axe", TypeAnimation.ATTACK);
        attackWeapon = new StateMachine();
        attackWeapon.loadCompleteAnimation(gPanel, creaturePath + "\\attack\\sword", TypeAnimation.ATTACK);
        attackState = attackWeapon;
    }

    /** incarcarea animatiilor pentru player */
    public void getPlayerSprites() {
//        String playerPath = "res/player/"+characterClassPath;
        setupMovement("res/player/" + characterClassPath);
        setupIdle("res/player/" + characterClassPath);
        setupAttackAnimationPlayer("res/player/" + characterClassPath);
//        this.loadMovementAnimations("res/player/" + characterClassPath);
    }

    public void update() {
        // Animatii jucator
        this.managePlayerMovement();

        /** actualizare imagine/avansare animatie cadru urmator dupa un interval de cadre rulate din cele 60 per secunda */
        currentAnimation.updateFrames();

        manageInvincible();
        if (life <= 0) {
            GamePanel.gameState = GameState.GameOverState;
            gPanel.ui.setCommandNum(0);
            gPanel.stopMusic();
            gPanel.playSE("gameover.wav");
        }
    }

    public int searchItemInInventory(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    private boolean addIfPossible(Item item) {
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
            return true;
        }
        return false;
    }

    public boolean obtainItem(Item item) {
        // Verific stackable
        if (item.stackable) {
            int index = searchItemInInventory(item.name);

            if (index > -1) {
                inventory.get(index).amount++;
                return true;
            }
            else {
                // Item nou
                // Verificam slot disponibil
                return addIfPossible(item);
            }
        }
        else {
            // Nu e stackable
            // Verificam slot disponibil
            return addIfPossible(item);
        }
    }

    @Override
    public void draw(Graphics2D g2D) {

        if (gPanel == null)
            return;

        if (this.g2D == null)
            this.g2D = g2D;

        /** Management animatii */
        BufferedImage sprite = null;

        inMotion = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
        /** Animatiile de atac */
        if (isAttacking()) {
            if(inMotion) inMotion = false;
            switch (characterClass) {
                case WARRIOR -> {
                    sprite = attackState.manageAnimations(this, direction);
                }
                case MAGE -> {
                    sprite = idle.manageAnimations(this, direction);
                    // SETAREA COORDONATELOR, DIRECTIEI SI A UTILIZATORULUI DE PROIECTIL
                    if (!currentWeapon.alive) {
                        Projectile playerProjectile = (Projectile) currentWeapon;
                        playerProjectile.set(worldX, worldY, direction, true, this);

                        // ADAUGAREA PROIECTILULUI IN LISTA
                        gPanel.projectileList.add(playerProjectile);
                    }
                }
            }
        }
        else if (inMotion) {
            sprite = movement.manageAnimations(this, direction);
        }
        else {
            sprite = idle.manageAnimations(this, direction);
        }

        int x = screenX;
        int y = screenY;

        if (screenX > worldX) {
            x = (int) worldX;
        }
        if (screenY > worldY) {
            y = (int) worldY;
        }
        int rightOffset = gPanel.screenWidth - screenX;
        if (rightOffset > gPanel.worldWidth - worldX) {
            x = (int) (gPanel.screenWidth - (gPanel.worldWidth - worldX));
        }
        int bottomOffset = gPanel.screenHeight - screenY;
        if (bottomOffset > gPanel.worldHeight - worldY) {
            y = (int) (gPanel.screenHeight - (gPanel.worldHeight - worldY));
        }

        // setarea opacitatii
        if (invincible) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.UP)
            g2D.drawImage(sprite, x, y-gPanel.tileSize, null);
        else if (currentAnimation.typeAnimation == TypeAnimation.ATTACK && direction == Direction.LEFT)
            g2D.drawImage(sprite, x-gPanel.tileSize, y, null);
        else
            g2D.drawImage(sprite, x, y, null);

        g2D.setColor(Color.red);
        g2D.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

        // resetarea opacitatii
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    @Override
    public void setDefaultSolidArea() {
        gPanel.player.solidAreaDefaultX = gPanel.tileSize / 4;
        gPanel.player.solidAreaDefaultY = gPanel.tileSize / 2;
        gPanel.player.solidArea.width = gPanel.tileSize / 2;
        gPanel.player.solidArea.height = (int) (gPanel.tileSize / 2.25);
    }

    public void setItems() {
        inventory.clear();
        obtainItem(currentWeapon);
        obtainItem(currentShield);
        for (int i = 0; i < 2; i++) {
            obtainItem(new KeyGold(gPanel));
            obtainItem(new PotionRed(gPanel));
        }
    }

    public boolean hasSpace() {
        return inventory.size() < maxInventorySize;
    }

    public boolean addToInventory(Item item) {
        if (hasSpace()) {
            inventory.add(item);
            return true;
        }
        return false;
    }

    void updateAttack() {
        attackArea = currentWeapon.attackArea;
        attack = strength * currentWeapon.damage;

        switch (currentWeapon.typeWeapon) {
            case Sword -> attackState = attackWeapon;
            case Axe -> attackState = attackAxe;
        }
    }

    void updateDefense() {
        defense = dexterity * currentShield.defense;
    }

//    public void setDefaultAttackArea() {
//        attackArea.width = 36;
//        attackArea.height = 36;
//    }

    public void attacking() {
        // salveaza datele curente ale ariei solide
        int currentWorldX = (int) worldX;
        int currentWorldY = (int) worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        // ajusteaza coordonatele jucatorului pentru aria de atac
        switch (direction) {
            case UP -> worldY -= attackArea.height;
            case DOWN -> worldY += attackArea.height;
            case LEFT -> worldX -= attackArea.width;
            case RIGHT -> worldX += attackArea.width;
        }

        // schimbarea dimensiunii ariei solide pentru aria de atac
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        // verifica coliziunea ariei de atac cu monstrii
        int monsterIndex = gPanel.collisionDetector.
                checkEntity(this, gPanel.monsterList.get(gPanel.currentMap));
        doDamageToMonster(monsterIndex, currentWeapon.knockBackPower);

        // coliziunea cu tiles interactive
        int iTileIndex = gPanel.collisionDetector.checkEntity(this, gPanel.interactiveTiles.get(gPanel.currentMap));
        doDamageToITile(iTileIndex);

        // coliziunea cu proiectile
        int projectileIndex = gPanel.collisionDetector.checkEntity(this, gPanel.projectileList);
        doDamageToProjectile(projectileIndex);

        // dupa verificarea coliziunii, restabileste datele originale
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;

    }

    private void doDamageToProjectile(int projectileIndex) {
        if (projectileIndex > -1 &&
                gPanel.projectileList.get(projectileIndex)
                        instanceof Projectile projectile) {

            projectile.alive = false;

            generateParticle(projectile, projectile.getParticleColor(),
                    projectile.getParticleSize(), projectile.getParticleSpeed(),
                    projectile.getParticleMaxLife());
        }
    }

    public void doDamageToMonster(int monsterIndex, int knockBackPower) {
        if (monsterIndex > -1) {
            if (!gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).invincible) { // FIXED
                // ofera daune
                gPanel.playSE("hitmonster.wav");

                if (knockBackPower > 0) {
                    knockBack(gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex), knockBackPower);
                }

                int damageValue = currentWeapon.tryDoAttack(this, gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex));
                gPanel.ui.addMessage(damageValue + " dauna!");

//                gPanel.monsterList.get(monsterIndex).life -= 1;
                gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).invincible = true; // FIXED
                gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).damageReaction(); // FIXED

                if (gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).life <= 0) { // FIXED
                    gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).dying = true; // FIXED
                    gPanel.ui.addMessage("Ai ucis un " + gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).name + "!"); // FIXED
                    gPanel.ui.addMessage("Exp: " + gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).exp); // FIXED
                    exp += gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).exp; // FIXED
                    checkLevelUp();
                }
            }
        }
    }

    public void knockBack(Entity entity, int knockBackPower) {
        entity.direction = direction;
        entity.speed += knockBackPower;
        entity.knockBack = true;
    }

    public void doDamageToITile(int iTileIndex) {
        if (iTileIndex > -1 && gPanel.interactiveTiles.get(gPanel.currentMap).get(iTileIndex) instanceof DestructibleTile destructibleTile // FIXED
                && destructibleTile.isCorrectItem(this) && !destructibleTile.invincible) {
            destructibleTile.playSE();
            destructibleTile.life--;
            destructibleTile.invincible = true;
            destructibleTile.generateParticle(destructibleTile);
            if (destructibleTile.life == 0) {
                gPanel.interactiveTiles.get(gPanel.currentMap).set(iTileIndex, destructibleTile.getDestroyedForm()); // FIXED
//                gPanel.interactiveTiles.remove(iTileIndex);
//                gPanel.interactiveTiles.add(destructibleTile.getDestroyedForm());
            }
        }
    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp) {

            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            updateAttack();
            updateDefense();

            gPanel.playSE("levelup.wav");
            GamePanel.gameState = GameState.Dialogue;
            gPanel.ui.setCurrentDialogue(new Dialogue("Esti la nivelul " + level + " acum!"));
        }
    }

    public void selectItem() {

        int itemIndex = gPanel.ui.playerInventoryWindow.getItemIndexOnSlot();

        if (itemIndex < inventory.size()) {
            Item selectedItem = inventory.get(itemIndex);

            switch (selectedItem.typeItem) {
                case Equipable -> {
                    Equipable selectedEquipable = (Equipable) selectedItem;
                    if (selectedEquipable.playerClass == characterClass || selectedEquipable.playerClass == CharacterClass.ANY) {
                        switch (selectedEquipable.typeEquipable) {
                            case Weapon -> {
                                currentWeapon = (Weapon) selectedEquipable;
                                updateAttack();
                            }
                            case Shield -> {
                                currentShield = (Shield) selectedEquipable;
                                updateDefense();
                            }
                            case Light -> {
                                updateCurrentLight((Light) selectedEquipable);
                            }
                        }
                    }
                }
                case Consumable -> {
                    Consumable selectedConsumable = (Consumable) selectedItem;
                    boolean hasUsed = selectedConsumable.use(this);
                    if (hasUsed) {
                        removeItem(selectedConsumable);
                    }
                }
            }
        }
    }

    private void updateCurrentLight(Light light) {
        if (currentLight == light) {
            currentLight = null;
        }
        else {
            currentLight = light;
        }
        lightUpdated = true;
    }

    public void removeItem(Item item) {
        if (item.amount > 1) {
            item.amount--;
        }
        else {
            inventory.remove(item);
        }
    }

    public void removeItem(int itemIndex) {
        if (inventory.get(itemIndex).amount > 1) {
            inventory.get(itemIndex).amount--;
        }
        else {
            inventory.remove(itemIndex);
        }
    }

    /** inregistrarea animatiilor pentru player */
//    @Override
//    public void loadMovementAnimations() {
//        /** ANIMATII MOVEMENT */
//        String movementFolderName = JOptionPane.showInputDialog("Numele folderului de animatie:");
////        String movementFolderName = "archer";
//        // UP
////        RenameFolderFiles.rename("E:\\AplicatiiCV\\2DAdventure\\res\\player\\movement\\north");
//        walkUp = new AnimationState(this.gPanel, "walkUp", Direction.UP, "res\\player\\" + movementFolderName + "\\north");
//        System.out.println("Animatia " + walkUp.title + " incarcata cu succes.");
//        // RIGHT
//        walkRight = new AnimationState(this.gPanel, "walkRight", Direction.RIGHT,"res\\player\\" + movementFolderName + "\\east");
//        System.out.println("Animatia " + walkRight.title + " incarcata cu succes.");
//        // DOWN
//        walkDown = new AnimationState(this.gPanel, "walkDown", Direction.DOWN, "res\\player\\" + movementFolderName + "\\south");
//        System.out.println("Animatia " + walkDown.title + " incarcata cu succes.");
//        // LEFT
//        walkLeft = new AnimationState(this.gPanel, "walkLeft", Direction.LEFT, "res\\player\\" + movementFolderName + "\\west");
//        System.out.println("Animatia " + walkLeft.title + " incarcata cu succes.");
//
//        movement = new StateMachine();
//        for (AnimationState animationState : Arrays.asList(this.walkUp, this.walkDown, this.walkRight, this.walkLeft)) {
//            this.movement.add(animationState);
//        }
//    }

    public void pickUpObj(int objIndex) {

        String textMsg;

        if (objIndex > -1) {
            // PICKUP COIN
            if (gPanel.objects.get(gPanel.currentMap).get(objIndex) instanceof Item item) {
                if (item instanceof OBJ_Coin coin) {//[gPanel.currentMap][objIndex] instanceof OBJ_Coin) {  // FIXED
                    // FIXED
                    coin.use(this);
                    gPanel.objects.get(gPanel.currentMap).remove(objIndex);  // FIXED
                }
                else {
                    // PICKUP ITEME INVENTAR
                    if (obtainItem(item)) {
                        gPanel.playSE("coin.wav");
//                switch (gPanel.objects.get(objIndex).typeObject) {
//                    case Key -> gPanel.playSE("coin.wav");
//                }
                        textMsg = "Ai primit " + item.name + "!"; // FIXED
                    }
                    else {
                        textMsg = "Nu mai ai spatiu in inventar!";
                    }
                    gPanel.ui.addMessage(textMsg);
                    gPanel.objects.get(gPanel.currentMap).remove(objIndex); // FIXED
                }
            }
            else if (gPanel.objects.get(gPanel.currentMap).get(objIndex) instanceof Obstacle obstacle) {
                if (keyH.enterPressed) {
                    System.out.println("AICI");
                    obstacle.interact();
                }
            }
        }
    }

    public boolean isMoving() {
        if (keyH.upPressed) { // nord
            this.direction = Direction.UP;
            return true;
        }
        if (keyH.downPressed) { // deplasare sud
            this.direction = Direction.DOWN;
            return true;
        }
        if (keyH.leftPressed) { // deplasare vest
            this.direction = Direction.LEFT;
            return true;
        }
        if (keyH.rightPressed) { // deplasare est
            this.direction = Direction.RIGHT;
            return true;
        }
        return false;
    }

    public boolean isAttacking() {
        return keyH.spacePressed;
    }


    public void managePlayerMovement() {

        // verifica evenimente
        gPanel.eHandler.checkEvent();

        if (isMoving()) {
//            System.out.println("up: " + keyH.upPressed + " down: " + keyH.downPressed + " left: " + keyH.leftPressed + " right: " + keyH.rightPressed);
            // verifica coliziunea cu texturile hartii
            collisionOn = false;
            gPanel.collisionDetector.manageTileCollision(this);

            // verifica coliziunea cu NPC
            gPanel.collisionDetector.checkEntity(this, gPanel.npcList.get(gPanel.currentMap));

            // verifica coliziunea cu obiecte
            int objIndex = gPanel.collisionDetector.manageObjCollision(this);
            pickUpObj(objIndex);

            // verifica coliziuni cu monstrii
            int monsterIndex = gPanel.collisionDetector.checkEntity(this, gPanel.monsterList.get(gPanel.currentMap));
            contactMonster(monsterIndex);

            // verifica coliziuni cu iTiles
            gPanel.collisionDetector.checkEntity(this, gPanel.interactiveTiles.get(gPanel.currentMap));

            if (!collisionOn && inMotion)
                manageMovement();
        }
        // interactiune cu npc
        if (keyH.enterPressed) {
            int npcIndex = gPanel.collisionDetector.checkEntity(this, gPanel.npcList.get(gPanel.currentMap));
            interactNPC(npcIndex);
            gPanel.keyH.enterPressed = false;
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex > -1) {
            if (gPanel.keyH.enterPressed) {
                // interactionari cu npc-uri
                GamePanel.gameState = GameState.Dialogue;
                gPanel.npcList.get(gPanel.currentMap).get(npcIndex).speak(); // FIXED
            }
        }
    }

    private void contactMonster(int monsterIndex) {
        if (monsterIndex > -1) {
            if (!invincible && !gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex).dying) { // FIXED
                gPanel.playSE("receivedamage.wav");

//                int damageValue = gPanel.monsterList.get(monsterIndex).touchingDamage(this);
                Monster monster = (Monster) gPanel.monsterList.get(gPanel.currentMap).get(monsterIndex); // FIXED
                monster.doDamage();


                invincible = true;
            }
        }
    }

    public void contactProjectile() {
        invincible = true;
        gPanel.playSE("receivedamage.wav");
    }

}
