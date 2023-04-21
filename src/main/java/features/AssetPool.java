package features;

import interactive_tile.*;
import item.consumable.Tent;
import item.consumable.coin.Coin;
import item.equipable.light.Lantern;
import item.equipable.light.Light;
import item.equipable.light.ModelLight;
import item.equipable.weapon.axe.Axe;
import item.equipable.weapon.axe.Baltag;
import item.equipable.weapon.axe.ModelAxe;
import item.consumable.key.KeyGold;
import item.consumable.key.KeyModel;
import item.consumable.potion.OBJ_Potion;
import item.consumable.potion.PotionModel;
import item.consumable.potion.PotionRed;
import item.equipable.shield.Shield;
import monster.MON_GreenSlime;
import monster.Monster;
import monster.TypeMonster;
import npc.NPC;
import npc.NPC_Merchant;
import npc.NPC_OldMan;
import npc.TypeNPC;
import game.GamePanel;
import object.*;
import item.equipable.shield.BlueShield;
import item.equipable.shield.ModelShield;
import item.equipable.shield.NormalShield;
import item.equipable.weapon.sword.ModelSword;
import item.equipable.weapon.sword.NormalSword;
import item.equipable.weapon.sword.Sword;
import object.obstacle.*;

import java.util.ArrayList;

public class AssetPool {

    GamePanel gPanel;
    int iteratorObj = 0;
    int iteratorNPC = 0;
    int iteratorMonsters = 0;
    int iteratorInteractiveTiles = 0;

    public AssetPool(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    // setarea obiectelor in lumea jocului
    public void setObjects() {
        int mapNum = 0;
//        Arrays.fill(gPanel.objects[mapNum], null);
        gPanel.objects.put(mapNum, new ArrayList<>());
        gPanel.objects.get(mapNum).clear();

//        loadAsset("res/objects/key.png",23 , 7, TypeObject.Key);
//        loadAsset("res/objects/key.png", 23, 40, TypeObject.Key);
//        loadAsset("res/objects/key.png", 38, 8, TypeObject.Key);
//        loadAsset("res/objects/door.png", 10, 12, TypeObject.Door);
//        loadAsset("res/objects/door.png", 8, 28, TypeObject.Door);
//        loadAsset("res/objects/door.png", 12, 23, TypeObject.Door);
//        loadAsset("res/objects/chest.png", 10, 8, TypeObject.Chest);
////        loadAsset("res/objects/key.png", 23, 22, TypeObject.Key);
////        loadAsset("res/objects/key.png", 23, 23, TypeObject.Key);
////        loadAsset("res/objects/key.png", 23, 24, TypeObject.Key);
////        loadAsset("res/objects/chest.png", 23, 23, TypeObject.Chest);
////        loadAsset("res/objects/door.png", 23, 23, TypeObject.Door);
//        loadAsset("res/objects/boots.png", 37, 42, TypeObject.Boots);
//        loadSingleStateObject("door.png", 21, 22, TypeObject.Door);
//        loadSingleStateObject("door.png", 23, 25, TypeObject.Door);
//        loadSingleStateObject("door.png", 38, 22, TypeObject.Door);
        loadKey("key.png", 25, 23, KeyModel.KeyGold, mapNum);
        loadKey("key.png", 21, 19, KeyModel.KeyGold, mapNum);
        loadKey("key.png", 26, 21, KeyModel.KeyGold, mapNum);
        loadAxe("axe.png", 33, 21, ModelAxe.Baltag, mapNum);
        loadShield("shield_blue.png", 35, 21, ModelShield.BlueShield, mapNum);
        loadPotion("potion_red.png", 22, 27, PotionModel.PotionRed, mapNum);
        loadPotion("potion_red.png", 22, 28, PotionModel.PotionRed, mapNum);
        loadCoin(23, 30, mapNum);
        loadObstacle(14, 28, TypeObstacle.Door, mapNum);
        loadObstacle(12, 12, TypeObstacle.Door, mapNum);
        loadObstacle(23, 16, TypeObstacle.Chest, mapNum);
        loadLight(18, 20, ModelLight.Lantern, mapNum);
        loadTent(19, 20, mapNum);
    }

    public void loadCoin(int worldX, int worldY, int mapNum) {
        Coin coin = new Coin(gPanel);
        coin.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        gPanel.objects.get(mapNum).add(coin);
    }

    public void loadTent(int worldX, int worldY, int mapNum) {
        Tent tent = new Tent(gPanel);
        tent.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        gPanel.objects.get(mapNum).add(tent);
    }

    public void setStatesObjects() {
        gPanel.statesObjectList.clear();
    }

    public void loadObstacle(int worldX, int worldY, TypeObstacle typeObstacle, int mapNum) {
        Obstacle obstacle = null;
        switch (typeObstacle) {
            case Door -> {
                obstacle = new WoodDoor(gPanel);
            }
            case Chest -> {
                obstacle = new WoodChest(gPanel, new BlueShield(gPanel));
            }
        }
        if (obstacle != null) {
            obstacle.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(obstacle);
        }
    }


    // chei
    public void loadSingleStateObject(String objectImgName, int worldX, int worldY, TypeObject typeObject, int mapNum) {
        SuperObject object = null;
        switch (typeObject) {
            case Door -> object = new WoodDoor(gPanel);
            case Boots -> object = new OBJ_Boots(gPanel);
        }

        if (object != null) {
            object.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(object);
        }
//        gPanel.objects.add(object);
    }

    public void loadKey(String objName, int worldX, int worldY, KeyModel keyModel, int mapNum) {
        SuperObject key = null;
        switch (keyModel) {
            case KeyGold -> {
                key = new KeyGold(gPanel);
                key.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
                gPanel.objects.get(mapNum).add(key);
            }
        }
    }

    public void loadMultipleStatesObject(String folderName, int worldX, int worldY, TypeStatesObject typeStatesObject) {
        int mapNum = 0;
        SuperStatesObject statesObject = null;
        switch (typeStatesObject) {
            case HEART -> statesObject = new OBJ_Heart(gPanel);
        }
        if (statesObject != null) {
            statesObject.loadObject(gPanel);
            statesObject.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }

        gPanel.statesObjectList.add(statesObject);
    }

    private void loadLight(int worldX, int worldY, ModelLight modelLight, int mapNum) {
        Light light = null;
        switch (modelLight) {
            case Lantern -> light = new Lantern(gPanel);
        }
        if (light != null) {
            light.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(light);
        }
    }

    private void loadShield(String itemName, int worldX, int worldY, ModelShield modelShield, int mapNum) {
        Shield shield = null;
        switch (modelShield) {
            case NormalShield -> shield = new NormalShield(gPanel);//gPanel.objects[mapNum][iteratorObj] = new NormalShield(gPanel);
            case BlueShield -> shield = new BlueShield(gPanel);//gPanel.objects[mapNum][iteratorObj] = new BlueShield(gPanel);
        }
        if (shield != null) {
            shield.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);//gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(shield);
        }
//        gPanel.objects.add(shield);
    }

    private void loadSword(String itemName, int worldX, int worldY, ModelSword modelSword, int mapNum) {
        Sword sword = null;
        switch (modelSword) {
            case NormalSword -> sword = new NormalSword(gPanel);//gPanel.objects[mapNum][iteratorObj] = new NormalSword(gPanel);
        }

        if (sword != null) {
            sword.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(sword);
        }
//        gPanel.objects.add(sword);
    }

    private void loadAxe(String itemName, int worldX, int worldY, ModelAxe modelAxe, int mapNum) {
        Axe axe = null;
        switch (modelAxe) {
            case Baltag -> axe = new Baltag(gPanel);//gPanel.objects[mapNum][iteratorObj] = new Baltag(gPanel);
        }

        if (axe != null) {
            axe.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(axe);
        }
//        gPanel.objects.add(axe);
    }

    private void loadPotion(String itemName, int worldX, int worldY, PotionModel potionModel, int mapNum) {
        OBJ_Potion potion = null;
        switch (potionModel) {
            case PotionRed -> potion = new PotionRed(gPanel);//gPanel.objects[mapNum][iteratorObj] = new PotionRed(gPanel);
        }

        if (potion != null) {
            potion.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.objects.get(mapNum).add(potion);
        }
//        gPanel.objects.add(potion);
    }

    public void setNPC() {
        int mapNum = 0;
//        Arrays.fill(gPanel.npcList[mapNum], null);
        gPanel.npcList.put(mapNum, new ArrayList<>());
        gPanel.npcList.get(mapNum).clear();

        loadNPC(TypeNPC.OldMan, 21, 21, mapNum);
//        loadNPC(TypeNPC.OldMan, 31, 21, mapNum);

        mapNum = 1;
//        Arrays.fill(gPanel.npcList[mapNum], null);
        gPanel.npcList.put(mapNum, new ArrayList<>());
        gPanel.npcList.get(mapNum).clear();
        iteratorNPC = 0;
        loadNPC(TypeNPC.Merchant, 12, 7, mapNum);

    }

    public void loadNPC( TypeNPC typeNPC, double worldX, double worldY, int mapNum) {
        NPC npc = null;
        switch (typeNPC) {
            case OldMan -> npc = new NPC_OldMan(gPanel);//gPanel.npcList[mapNum][iteratorNPC] = new NPC_OldMan(gPanel);
            case Merchant -> npc = new NPC_Merchant(gPanel);//gPanel.npcList[mapNum][iteratorNPC] = new NPC_Merchant(gPanel);
        }

        if (npc != null) {
            npc.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.npcList.get(mapNum).add(npc);
        }
    }

    public void setMonster() {
        int mapNum = 0;
//        Arrays.fill(gPanel.monsterList[mapNum], null);
        gPanel.monsterList.put(mapNum, new ArrayList<>());
        gPanel.monsterList.get(mapNum).clear();

        loadMonster(TypeMonster.GreenSlime, 21, 38, mapNum);
        loadMonster(TypeMonster.GreenSlime, 23, 42, mapNum);
        loadMonster(TypeMonster.GreenSlime, 24, 37, mapNum);
        loadMonster(TypeMonster.GreenSlime, 34, 42, mapNum);
        loadMonster(TypeMonster.GreenSlime, 38, 42, mapNum);

        // debug
//        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*11, gPanel.tileSize*10);
//        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*11, gPanel.tileSize*11);
    }

    public void loadMonster(TypeMonster typeMonster, double worldX, double worldY, int mapNum) {
        Monster monster = null;
        switch (typeMonster) {
            case GreenSlime -> monster = new MON_GreenSlime(gPanel);//gPanel.monsterList[mapNum][iteratorMonsters] = new MON_GreenSlime(gPanel);
        }

        if (monster != null) {
            monster.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.monsterList.get(mapNum).add(monster);
        }
    }

    public void setInteractiveTiles() {
        int mapNum = 0;
//        Arrays.fill(gPanel.interactiveTiles[mapNum], null);
        gPanel.interactiveTiles.put(mapNum, new ArrayList<>());
        gPanel.interactiveTiles.get(mapNum).clear();

        loadDestructibleTile(TypeDestructibleTile.DryTree, 27, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 28, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 29, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 30, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 31, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 32, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 33, 12, mapNum);

    }

    public void loadDestructibleTile(TypeDestructibleTile typeDestructibleTile, double worldX, double worldY, int mapNum) {
        DestructibleTile destructibleTile = null;
        switch (typeDestructibleTile) {
            case DryTree -> destructibleTile = new IT_DryTree(gPanel);//gPanel.interactiveTiles[mapNum][iteratorInteractiveTiles] = new IT_DryTree(gPanel);
        }

        if (destructibleTile != null) {
            destructibleTile.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.interactiveTiles.get(mapNum).add(destructibleTile);
        }
    }
}
