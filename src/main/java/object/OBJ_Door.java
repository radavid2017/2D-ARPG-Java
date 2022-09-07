package object;

import game.GamePanel;

public class OBJ_Door extends SuperObject {

    public OBJ_Door(GamePanel gPanel) {
        super(gPanel);
        this.typeObject = TypeObject.Door;
        this.isSolid = true;

        setDefaultSolidArea();
        setImage("door.png");
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.x = 0;
        solidArea.y = getGamePanel().tileSize/3;
        solidArea.width = getGamePanel().tileSize;
        solidArea.height = (int) (getGamePanel().tileSize/1.5);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
