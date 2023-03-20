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

public class AssetPool {

    GamePanel gPanel;

    public AssetPool(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    // setarea obiectelor in lumea jocului
    public void setObjects() {
        gPanel.objects.clear();
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
        loadCoin(23, 30);
    }

    public void loadCoin(int worldX, int worldY) {
        OBJ_Coin coin = new OBJ_Coin(gPanel);
        coin.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        gPanel.objects.add(coin);
    }

    public void setStatesObjects() {
        gPanel.statesObjectList.clear();
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
        if (object != null) {
            object.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
        gPanel.objects.add(object);
    }

    public void loadKey(String objName, int worldX, int worldY, KeyModel keyModel) {
        OBJ_Key key = null;
        switch (keyModel) {
            case KeyGold -> {
                key = new KeyGold(gPanel);
                key.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
                gPanel.objects.add(key);
            }
        }
    }

    public void loadMultipleStatesObject(String folderName, int worldX, int worldY, TypeStatesObject typeStatesObject) {
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

    private void loadShield(String itemName, int worldX, int worldY, ModelShield modelShield) {
        Shield shield = null;
        switch (modelShield) {
            case NormalShield -> shield = new NormalShield(gPanel);
            case BlueShield -> shield = new BlueShield(gPanel);
        }
        shield.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        gPanel.objects.add(shield);
    }

    private void loadSword(String itemName, int worldX, int worldY, ModelSword modelSword) {
        Sword sword = null;
        switch (modelSword) {
            case NormalSword -> sword = new NormalSword(gPanel);
        }
        if (sword != null) {
            sword.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
        gPanel.objects.add(sword);
    }

    private void loadAxe(String itemName, int worldX, int worldY, ModelAxe modelAxe) {
        Axe axe = null;
        switch (modelAxe) {
            case Baltag -> axe = new Baltag(gPanel);
        }
        if (axe != null) {
            axe.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
        gPanel.objects.add(axe);
    }

    private void loadPotion(String itemName, int worldX, int worldY, PotionModel potionModel) {
        OBJ_Potion potion = null;
        switch (potionModel) {
            case PotionRed -> potion = new PotionRed(gPanel);
        }
        if (potion != null) {
            potion.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
        }
        gPanel.objects.add(potion);
    }

    public void setNPC() {
        gPanel.npcList.clear();
        loadNPC(TypeNPC.OldMan, 21, 21);
        loadNPC(TypeNPC.OldMan, 31, 21);

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
        if (entity != null) {
            entity.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.npcList.add(entity);
        }
    }

    public void setMonster() {
        gPanel.monsterList.clear();
        loadMonster(TypeMonster.GreenSlime, 21, 38);
        loadMonster(TypeMonster.GreenSlime, 23, 42);
        loadMonster(TypeMonster.GreenSlime, 24, 37);
        loadMonster(TypeMonster.GreenSlime, 34, 42);
        loadMonster(TypeMonster.GreenSlime, 38, 42);

        // debug
//        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*11, gPanel.tileSize*10);
//        loadMonster(TypeMonster.GreenSlime, gPanel.tileSize*11, gPanel.tileSize*11);
    }

    public void loadMonster(TypeMonster typeMonster, double worldX, double worldY) {
        Entity entity = null;
        switch (typeMonster) {
            case GreenSlime -> entity = new MON_GreenSlime(gPanel);
        }
        if (entity != null) {
            entity.setPosition(gPanel.tileSize * worldX, gPanel.tileSize *  worldY);
            gPanel.monsterList.add(entity);
        }
    }

    public void setInteractiveTiles() {
        gPanel.interactiveTiles.clear();
        loadDestructibleTile(TypeDestructibleTile.DryTree, 27, 12);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 28, 12);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 29, 12);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 30, 12);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 31, 12);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 32, 12);
        loadDestructibleTile(TypeDestructibleTile.DryTree, 33, 12);

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

    public void loadDestructibleTile(TypeDestructibleTile typeDestructibleTile, double worldX, double worldY) {
        DestructibleTile destructibleTile = null;
        switch (typeDestructibleTile) {
            case DryTree -> destructibleTile = new IT_DryTree(gPanel);
        }
        if (destructibleTile != null) {
            destructibleTile.setPosition(gPanel.tileSize * worldX, gPanel.tileSize * worldY);
            gPanel.interactiveTiles.add(destructibleTile);
        }
    }
}
