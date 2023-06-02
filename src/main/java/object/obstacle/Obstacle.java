package object.obstacle;

import game.GamePanel;
import object.SuperObject;
import object.TypeMaterial;
import object.TypeObject;

public abstract class Obstacle extends SuperObject {

    public TypeObstacle typeObstacle;
    public TypeMaterial typeMaterial;

    public Obstacle(GamePanel gPanel, TypeMaterial typeMaterial) {
        super(gPanel);
        this.typeMaterial = typeMaterial;
        typeObject = TypeObject.Obstacle;
        objPath += "obstacle/";
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public abstract void interact();
}
