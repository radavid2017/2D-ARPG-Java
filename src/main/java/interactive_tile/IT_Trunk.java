package interactive_tile;

import entity.Player;
import game.GamePanel;

public class IT_Trunk extends ResultTile {

    public IT_Trunk(GamePanel gp) {
        super(gp, TypeResultTile.Trunk);
        setImage("tree/trunk.png");
        setDefaultSolidArea();
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.width = 0;
        solidArea.height = 0;
    }
}
