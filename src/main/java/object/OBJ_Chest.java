package object;

import game.GamePanel;

public class OBJ_Chest extends SuperObject{
    public OBJ_Chest(GamePanel gPanel) {
        super(gPanel);
        this.typeObject = TypeObject.Chest;
        this.isSolid = true;
        this.setSolidArea(14, 8, 74, 62);
        setImage("chest.png");
    }

    @Override
    public void setDefaultSolidArea() {
        this.setSolidArea(14, 8, 74, 62);
    }
}
