package object.obstacle;

import game.GamePanel;
import object.SuperObject;
import object.TypeObject;

public abstract class Obstacle extends SuperObject {

    public TypeObstacle typeObstacle;

    public Obstacle(GamePanel gPanel) {
        super(gPanel);
        typeObject = TypeObject.Obstacle;
        objPath += "obstacle/";
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public abstract void interact();
}
