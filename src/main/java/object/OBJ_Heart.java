package object;

import game.GamePanel;

import java.awt.image.BufferedImage;

public class OBJ_Heart extends SuperStatesObject {
    public OBJ_Heart(GamePanel gp) {
        super(gp, TypeStatesObject.HEART);
        name = "heart";
        folderPath += "heart/";
    }

    @Override
    public void update() {

    }

    @Override
    public void setDefaultSolidArea() {

    }
}
