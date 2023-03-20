package interactive_tile;

import entity.Player;
import game.GamePanel;
import object.SuperObject;

public abstract class InteractiveTile extends SuperObject {

    TypeInteractiveTile typeInteractiveTile;

    public InteractiveTile(GamePanel gp, TypeInteractiveTile typeInteractiveTile) {
        super(gp);
        this.typeInteractiveTile = typeInteractiveTile;
        objPath = "res/interactive_tiles/";
    }

    public void update() {

    }
}
