package item.equipable.light;

import game.GamePanel;
import object.SuperObject;

public class Lantern extends Light {
    public Lantern(GamePanel gPanel) {
        super(gPanel, 200);
        setImage("lantern.png");
        name = "Lanterna";
        description = "[" + name + "]\nLumineaza imprejur.";
        setRadius(gPanel.screenHeight/2.3f);
    }

    @Override
    public SuperObject generateObject() {
        return new Lantern(getGamePanel());
    }

    @Override
    public void setDefaultSolidArea() {

    }
}
