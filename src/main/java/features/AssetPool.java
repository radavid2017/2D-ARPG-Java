package features;

import axe.Axe;
import axe.Baltag;
import axe.ModelAxe;
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

public class AssetPool {

    GamePanel gPanel;

    public AssetPool(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    // setarea obiectelor in lumea jocului
    public void setObjects() {
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
        loadKey("key.png", 25, 23, KeyModel.KeyGold);
        loadKey("key.png", 21, 19, KeyModel.KeyGold);
        loadKey("key.png", 26, 21, KeyModel.KeyGold);
        loadAxe("axe.png", 33, 21, ModelAxe.Baltag);
        loadShield("shield_blue.png", 35, 21, ModelShield.BlueShield);
        loadPotion("potion_red.png", 22, 27, PotionModel.PotionRed);

    }

    public void setStatesObjects() {

    }

    // chei
    public void loadSingleStateObject(String objectImgName, int worldX, int worldY, TypeObject typeObject) {
        SuperObject object = null;
        switch (typeObject) {
            case Door -> object = new OBJ_Door(gPanel);
            case Chest -> object = new OBJ_Chest(gPanel);
            case Boots -> object = new OBJ_Boots(gPanel);
        }
//        object.loadObject(gPanel, "res/objects/" + objectImgName);
        object.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(object);
    }

    public void loadKey(String objName, int worldX, int worldY, KeyModel keyModel) {
        OBJ_Key key = null;
        switch (keyModel) {
            case KeyGold -> {
                key = new KeyGold(gPanel);
                key.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
                gPanel.objects.add(key);
            }
        }
    }

    public void loadMultipleStatesObject(String folderName, int worldX, int worldY, TypeStatesObject typeStatesObject) {
        SuperStatesObject statesObject = null;
        switch (typeStatesObject) {
            case HEART -> statesObject = new OBJ_Heart();
        }
        statesObject.loadObject(gPanel, "res/objectsWithStates/" + folderName);
        statesObject.setPosition(worldX*gPanel.tileSize, worldY*gPanel.tileSize);
        gPanel.statesObjectList.add(statesObject);
    }

//    public void loadItem(String itemName, int worldX, int worldY, TypeItem typeItem) {
//        Item item = null;
//        switch (typeItem) {
//            case Shield -> loadShield(itemName, worldX, worldY, typeItem);
//        }
//    }

    private void loadShield(String itemName, int worldX, int worldY, ModelShield modelShield) {
        Shield shield = null;
        switch (modelShield) {
            case NormalShield -> shield = new NormalShield(gPanel);
            case BlueShield -> shield = new BlueShield(gPanel);
        }
        shield.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(shield);
    }

    private void loadSword(String itemName, int worldX, int worldY, ModelSword modelSword) {
        Sword sword = null;
        switch (modelSword) {
            case NormalSword -> sword = new NormalSword(gPanel);
        }
        sword.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(sword);
    }

    private void loadAxe(String itemName, int worldX, int worldY, ModelAxe modelAxe) {
        Axe axe = null;
        switch (modelAxe) {
            case Baltag -> axe = new Baltag(gPanel);
        }
        axe.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(axe);
    }

    private void loadPotion(String itemName, int worldX, int worldY, PotionModel potionModel) {
        OBJ_Potion potion = null;
        switch (potionModel) {
            case PotionRed -> potion = new PotionRed(gPanel);
        }
        potion.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(potion);
    }

    public void setNPC() {
        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 21, gPanel.tileSize * 21);
        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 31, gPanel.tileSize * 21);

//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 11, gPanel.tileSize * 21);
//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 21, gPanel.tileSize * 11);
//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 21, gPanel.tileSize * 31);

        // debug
//        loadNPC(TypeNPC.OldMan, gPanel.tileSize * 9, gPanel.tileSize * 10);
    }

    public void loadNPC( TypeNPC typeNPC, double worldX, double worldY) {
        Entity entity = null;
        switch (typeNPC) {
            case OldMan -> entity = new NPC_OldMan(gPanel);
        }
        entity.setPosition(worldX, worldY);
        gPanel.npcList.add(entity);
    }

    public void setMonster() {
        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*21, gPanel.tileSize*38);
        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*23, gPanel.tileSize*42);
        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*24, gPanel.tileSize*37);
        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*34, gPanel.tileSize*42);
        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*38, gPanel.tileSize*42);

        // debug
//        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*11, gPanel.tileSize*10);
//        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*11, gPanel.tileSize*11);
    }

    public void loadMonster(TypeMonster typeMonster, double worldX, double worldY) {
        Entity entity = null;
        switch (typeMonster) {
            case GreenSlime -> entity = new MON_GreenSlime(gPanel);
        }
        entity.setPosition(worldX, worldY);
        gPanel.monsterList.add(entity);
    }
}
