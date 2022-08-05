package features;

import game.UI;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class StateMachine {
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
    public BufferedImage manageAnimations(Direction direction, boolean isIdle) {
        if (!UI.GAME_OVER) {
            if (isIdle) {
                for (int i = 0; i < states.size(); i++) {
                    if (states.get(i).direction == direction) {
                        return states.get(i).nextFrame();
                    }
                }
            } else {
                for (AnimationState state : states) {
                    if (state.direction == direction) {
                        return state.idle();
                    }
                }
            }
            return null;
        }
        return idleGameOver();
    }

    private BufferedImage idleGameOver() {
        for (AnimationState state : states) {
            if (state.direction == Direction.DOWN)
                return state.idle();
        }
        return null;
    }
}