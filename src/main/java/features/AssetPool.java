package features;

import interactive_tile.*;
import item.consumable.coin.OBJ_Coin;
import item.equipable.weapon.axe.Axe;
import item.equipable.weapon.axe.Baltag;
import item.equipable.weapon.axe.ModelAxe;
import entity.Entity;
import item.consumable.key.KeyGold;
import item.consumable.key.KeyModel;
import item.consumable.key.OBJ_Key;
import item.consumable.potion.OBJ_Potion;
import item.consumable.potion.PotionModel;
import item.consumable.potion.PotionRed;
import item.equipable.shield.Shield;
import monster.MON_GreenSlime;
import monster.TypeMonster;
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

import java.util.Arrays;

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
        Arrays.fill(gPanel.objects[mapNum], null);
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
        loadCoin(23, 30, mapNum);
    }

    public void loadCoin(int worldX, int worldY, int mapNum) {
        gPanel.objects[mapNum][iteratorObj] = new OBJ_Coin(gPanel);
        gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
//        gPanel.objects[mapNum][i].add(coin);
    }

    public void setStatesObjects() {
        gPanel.statesObjectList.clear();
    }

    // chei
    public void loadSingleStateObject(String objectImgName, int worldX, int worldY, TypeObject typeObject, int mapNum) {
        switch (typeObject) {
            case Door -> gPanel.objects[mapNum][iteratorObj] = new OBJ_Door(gPanel);
            case Chest -> gPanel.objects[mapNum][iteratorObj] = new OBJ_Chest(gPanel);
            case Boots -> gPanel.objects[mapNum][iteratorObj] = new OBJ_Boots(gPanel);
        }
//        object.loadObject(gPanel, "res/objects/" + objectImgName);
        if (gPanel.objects[mapNum][iteratorObj] != null) {
            gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
//        gPanel.objects.add(object);
    }

    public void loadKey(String objName, int worldX, int worldY, KeyModel keyModel, int mapNum) {
        switch (keyModel) {
            case KeyGold -> {
                gPanel.objects[mapNum][iteratorObj] = new KeyGold(gPanel);
                gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
//                gPanel.objects.add(key);
            }
        }
    }

    public void loadMultipleStatesObject(String folderName, int worldX, int worldY, TypeStatesObject typeStatesObject) {
        int mapNum = 0;
        SuperStatesObject statesObject = null;
        switch (typeStatesObject) {
            case HEART -> statesObject = new OBJ_Heart();
        }
        if (statesObject != null) {
            statesObject.loadObject(gPanel);
            statesObject.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }

        gPanel.statesObjectList.add(statesObject);
    }

//    public void loadItem(String itemName, int worldX, int worldY, TypeItem typeItem) {
//        Item item = null;
//        switch (typeItem) {
//            case Shield -> loadShield(itemName, worldX, worldY, typeItem);
//        }
//    }

    private void loadShield(String itemName, int worldX, int worldY, ModelShield modelShield, int mapNum) {
        switch (modelShield) {
            case NormalShield -> gPanel.objects[mapNum][iteratorObj] = new NormalShield(gPanel);
            case BlueShield -> gPanel.objects[mapNum][iteratorObj] = new BlueShield(gPanel);
        }
        gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
//        gPanel.objects.add(shield);
    }

    private void loadSword(String itemName, int worldX, int worldY, ModelSword modelSword, int mapNum) {
        switch (modelSword) {
            case NormalSword -> gPanel.objects[mapNum][iteratorObj] = new NormalSword(gPanel);
        }
        if (gPanel.objects[mapNum][iteratorObj] != null) {
            gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
//        gPanel.objects.add(sword);
    }

    private void loadAxe(String itemName, int worldX, int worldY, ModelAxe modelAxe, int mapNum) {
        switch (modelAxe) {
            case Baltag -> gPanel.objects[mapNum][iteratorObj] = new Baltag(gPanel);
        }
        if (gPanel.objects[mapNum][iteratorObj] != null) {
            gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
//        gPanel.objects.add(axe);
    }

    private void loadPotion(String itemName, int worldX, int worldY, PotionModel potionModel, int mapNum) {
        switch (potionModel) {
            case PotionRed -> gPanel.objects[mapNum][iteratorObj] = new PotionRed(gPanel);
        }
        if (gPanel.objects[mapNum][iteratorObj] != null) {
            gPanel.objects[mapNum][iteratorObj++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
//        gPanel.objects.add(potion);
    }

    public void setNPC() {
        int mapNum = 0;
        Arrays.fill(gPanel.npcList[mapNum], null);
        loadNPC(TypeNPC.OldMan, 21, 21, mapNum);
        loadNPC(TypeNPC.OldMan, 31, 21, mapNum);

//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 11, gPanel.tileSize * 21);
//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 21, gPanel.tileSize * 11);
//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 21, gPanel.tileSize * 31);

        // debug
//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 9, gPanel.tileSize * 10);
    }

    public void loadNPC( TypeNPC typeNPC, double worldX, double worldY, int mapNum) {
        switch (typeNPC) {
            case OldMan -> gPanel.npcList[mapNum][iteratorNPC] = new NPC_OldMan(gPanel);
        }
        if (gPanel.npcList[mapNum][iteratorNPC] != null) {
            gPanel.npcList[mapNum][iteratorNPC++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
//            gPanel.npcList.add(entity);
        }
    }

    public void setMonster() {
        int mapNum = 0;
        Arrays.fill(gPanel.monsterList[mapNum], null);
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
        switch (typeMonster) {
            case GreenSlime -> gPanel.monsterList[mapNum][iteratorMonsters] = new MON_GreenSlime(gPanel);
        }
        if (gPanel.monsterList[mapNum][iteratorMonsters] != null) {
            gPanel.monsterList[mapNum][iteratorMonsters++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize *  worldY);
//            gPanel.monsterList.add(entity);
        }
    }

    public void setInteractiveTiles() {
        int mapNum = 0;
        Arrays.fill(gPanel.interactiveTiles[mapNum], null);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 27, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 28, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 29, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 30, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 31, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 32, 12, mapNum);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 33, 12, mapNum);

//        loadDestructibleTile(TypeDestructibleTile.DryTree, 30, 20);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 30, 21);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 30, 22);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 20, 20);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 20, 21);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 20, 22);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 22, 24);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 23, 24);
//        loadDestructibleTile(TypeDestructibleTile.DryTree, 24, 24);
    }

    public void loadDestructibleTile(TypeDestructibleTile typeDestructibleTile, double worldX, double worldY, int mapNum) {
        switch (typeDestructibleTile) {
            case DryTree -> gPanel.interactiveTiles[mapNum][iteratorInteractiveTiles] = new IT_DryTree(gPanel);
        }
        if (gPanel.interactiveTiles[mapNum][iteratorInteractiveTiles] != null) {
            gPanel.interactiveTiles[mapNum][iteratorInteractiveTiles++].setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
//            gPanel.interactiveTiles.add(destructibleTile);
        }
    }
}
