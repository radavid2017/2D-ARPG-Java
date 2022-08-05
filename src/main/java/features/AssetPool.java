package features;

import game.GamePanel;
import object.*;

public class AssetPool {

    GamePanel gPanel;

    public AssetPool(GamePanel gPanel) {
        this.gPanel = gPanel;
    }

    // setarea obiectelor in lumea jocului
    public void setObjects() {
        loadAsset("res/objects/key.png",23 , 7, TypeObject.Key);
        loadAsset("res/objects/key.png", 23, 40, TypeObject.Key);
        loadAsset("res/objects/key.png", 38, 8, TypeObject.Key);
        loadAsset("res/objects/door.png", 10, 12, TypeObject.Door);
        loadAsset("res/objects/door.png", 8, 28, TypeObject.Door);
        loadAsset("res/objects/door.png", 12, 23, TypeObject.Door);
        loadAsset("res/objects/chest.png", 10, 8, TypeObject.Chest);
//        loadAsset("res/objects/key.png", 23, 22, TypeObject.Key);
//        loadAsset("res/objects/key.png", 23, 23, TypeObject.Key);
//        loadAsset("res/objects/key.png", 23, 24, TypeObject.Key);
//        loadAsset("res/objects/chest.png", 23, 23, TypeObject.Chest);
//        loadAsset("res/objects/door.png", 23, 23, TypeObject.Door);
        loadAsset("res/objects/boots.png", 37, 42, TypeObject.Boots);
    }

    // chei
    public void loadAsset(String filePath, int worldX, int worldY, TypeObject typeObject) {
        SuperObject object = null;
        switch (typeObject) {
            case Key -> object = new OBJ_Key();
            case Door -> object = new OBJ_Door();
            case Chest -> object = new OBJ_Chest();
            case Boots -> object = new OBJ_Boots();
        }
        object.loadObject(gPanel, filePath);
        object.setPosition(worldX * gPanel.tileSize, worldY * gPanel.tileSize);
        gPanel.objects.add(object);
    }
}
