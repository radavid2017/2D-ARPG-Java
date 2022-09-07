package object;

import game.GamePanel;

public class OBJ_Key extends SuperObject {
    public OBJ_Key(GamePanel gPanel) {
        super(gPanel);
        this.typeObject = TypeObject.Key;
        this.setImage("key.png");
        name = "Cheie";
        description = "[" + name + "]\nDeschide o usa.";
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
