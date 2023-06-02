package object.obstacle.door;

import features.Dialogue;
import game.GamePanel;
import game.GameState;
import object.TypeMaterial;
import object.obstacle.Obstacle;
import object.obstacle.TypeObstacle;

public abstract class OBJ_Door extends Obstacle {

    public OBJ_Door(GamePanel gPanel, TypeMaterial typeMaterial) {
        super(gPanel, typeMaterial);
        this.isSolid = true;
        objPath += "door/";

        typeObstacle = TypeObstacle.Door;

        setDefaultSolidArea();
    }

    @Override
    public void setDefaultSolidArea() {
        solidArea.x = 0;
        solidArea.y = getGamePanel().tileSize/3;
        solidArea.width = getGamePanel().tileSize;
        solidArea.height = (int) (getGamePanel().tileSize/1.5);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
