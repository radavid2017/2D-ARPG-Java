package object.obstacle;

import entity.Entity;
import features.Direction;
import game.GamePanel;
import object.SuperObject;
import object.TypeMaterial;

public abstract class MovableObstacle extends Obstacle{

    public Entity linkedEntity;

    public MovableObstacle(GamePanel gPanel, TypeMaterial typeMaterial) {
        super(gPanel, typeMaterial);
    }

    public abstract void move (Direction direction);
}
