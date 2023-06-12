package animations;

import animations.AnimationState;
import entity.Entity;
import features.Direction;
import game.GamePanel;
import game.GameState;
import game.UI;
import npc.NPC_Merchant;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateMachine {
    public AnimationState up, down, left, right;
    public List<AnimationState> states = new ArrayList<>();

    public void add(AnimationState animation) {
        states.add(animation);
    }

    public AnimationState get(String title) {
        for (AnimationState state : states) {
            if (state.title.equals(title)) {
                return state;
            }
        }
        return null;
    }

    /** Management animatii */
    public BufferedImage manageAnimations(Entity entity, Direction direction) {
        if (!UI.GAME_OVER) {
                for (AnimationState state : states) {
                    if (state.direction == direction) {
                        entity.currentAnimation = state;
                        return state.nextFrame();
                    }
                }
            return null;
        }
        return null;
//        return idleGameOver();
    }

//    private BufferedImage idleGameOver() {
//        for (AnimationState state : states) {
//            if (state.direction == Direction.DOWN)
//                return state.idle();
//        }
//        return null;
//    }

    public void loadCompleteAnimation(GamePanel gp, String animationFolderName, TypeAnimation typeAnimation) {
        // instantiere
        up = new AnimationState(gp, Direction.UP, typeAnimation);
        down = new AnimationState(gp, Direction.DOWN, typeAnimation);
        right = new AnimationState(gp, Direction.RIGHT, typeAnimation);
        left = new AnimationState(gp, Direction.LEFT, typeAnimation);

        // incarcare resurse
        up.loadDirectionAnimation(animationFolderName);
        down.loadDirectionAnimation(animationFolderName);
        right.loadDirectionAnimation(animationFolderName);
        left.loadDirectionAnimation(animationFolderName);

        states.addAll(Arrays.asList(up, down, left, right));
    }

    public void loadCompleteAnimation(GamePanel gp, int scale, String animationFolderName, TypeAnimation typeAnimation) {
        // instantiere
        up = new AnimationState(gp, Direction.UP, typeAnimation);
        down = new AnimationState(gp, Direction.DOWN, typeAnimation);
        right = new AnimationState(gp, Direction.RIGHT, typeAnimation);
        left = new AnimationState(gp, Direction.LEFT, typeAnimation);

        // incarcare resurse
        up.loadDirectionAnimation(scale, animationFolderName);
        down.loadDirectionAnimation(scale, animationFolderName);
        right.loadDirectionAnimation(scale, animationFolderName);
        left.loadDirectionAnimation(scale, animationFolderName);

        states.addAll(Arrays.asList(up, down, left, right));
    }

    public void loadSingleAnimation(GamePanel gp, String animationFolderName, TypeAnimation typeAnimation, Direction direction) {
        switch (direction) {
            case UP -> {
                up = new AnimationState(gp, Direction.UP, typeAnimation);
                up.loadDirectionAnimation(animationFolderName);
                states.add(up);
            }
            case DOWN -> {
                down = new AnimationState(gp, Direction.DOWN, typeAnimation);
                down.loadDirectionAnimation(animationFolderName);
                states.add(down);
            }
            case LEFT -> {
                left = new AnimationState(gp, Direction.LEFT, typeAnimation);
                left.loadDirectionAnimation(animationFolderName);
                states.add(left);
            }
            case RIGHT -> {
                right = new AnimationState(gp, Direction.RIGHT, typeAnimation);
                right.loadDirectionAnimation(animationFolderName);
                states.add(right);
            }
        }
    }
}