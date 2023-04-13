package object;

import game.GamePanel;

public class OBJ_ManaCrystal extends SuperStatesObject {

    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp, TypeStatesObject.ManaCrystal);
        name = "manaCrystal";
        folderPath += "manaCrystal";
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
