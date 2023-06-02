package interactive_tile;

import game.GamePanel;
import object.SuperObject;

public class IT_MetalPlate extends ResultTile {

    public IT_MetalPlate(GamePanel gp) {
        super(gp, TypeResultTile.MetalPlate);
        setImage("wall/metalplate.png");
        setDefaultSolidArea();
        name = "Placa metalica";
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.width = 0;
        solidArea.height = 0;
    }

    @Override
    public SuperObject generateObject() {
        return new IT_MetalPlate(getGamePanel());
    }
}
