package features;

import entity.Entity;
import monster.MON_GreenSlime;
import monster.TypeMonster;
import npc.NPC;
import npc.NPC_OldMan;
import npc.TypeNPC;
import game.GamePanel;
import object.*;

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
    }

    public void setStatesObjects() {

    }

    // chei
    public void loadSingleStateObject(String objectImgName, int worldX, int worldY, TypeObject typeObject) {
        SuperObject object = null;
        switch (typeObject) {
            case Key -> object = new OBJ_Key(gPanel);
            case Door -> object = new OBJ_Door(gPanel);
            case Chest -> object = new OBJ_Chest(gPanel);
            case Boots -> object = new OBJ_Boots(gPanel);
        }
//        object.loadObject(gPanel, "res/objects/" + objectImgName);
        object.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(object);
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
