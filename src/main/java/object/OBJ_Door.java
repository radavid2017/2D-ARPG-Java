package object;

import game.GamePanel;

public class OBJ_Door extends SuperObject {

    public OBJ_Door(GamePanel gPanel) {
        super(gPanel);
        this.typeObject = TypeObject.Door;
        this.collision = true;

        solidArea.x = 0;
        solidArea.y = gPanel.tileSize/3;
        solidArea.width = gPanel.tileSize;
        solidArea.height = (int) (gPanel.tileSize/1.5);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
