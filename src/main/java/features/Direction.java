package features;

import entity.Entity;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ANY;

    public void switchDirection(Entity entity) {
        switch (entity.direction) {
            case UP -> entity.direction = Direction.DOWN;
            case DOWN -> entity.direction = Direction.UP;
            case LEFT -> entity.direction = Direction.RIGHT;
            case RIGHT -> entity.direction = Direction.LEFT;
        }
    }

}
