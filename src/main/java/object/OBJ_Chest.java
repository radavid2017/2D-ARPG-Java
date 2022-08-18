package object;

import game.GamePanel;

public class OBJ_Chest extends SuperObject{
    public OBJ_Chest(GamePanel gPanel) {
        super(gPanel);
        this.typeObject = TypeObject.Chest;
        this.collision = true;
        this.setSolidArea(14, 8, 74, 62);
    }
}
