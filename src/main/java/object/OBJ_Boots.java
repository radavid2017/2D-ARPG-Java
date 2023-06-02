package object;

import game.GamePanel;

public class OBJ_Boots extends SuperObject {
    public OBJ_Boots(GamePanel gPanel) {
        super(gPanel);
        this.typeObject = TypeObject.Boots;
        setImage("boots.png");
    }

    @Override
    public SuperObject generateObject() {
        return new OBJ_Boots(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
