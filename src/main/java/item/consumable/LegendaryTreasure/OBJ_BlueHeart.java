package item.consumable.LegendaryTreasure;

import entity.Entity;
import game.GamePanel;
import game.GameState;
import game.SceneState;
import item.Consumable;
import item.consumable.TypeConsumable;
import object.SuperObject;

public class OBJ_BlueHeart extends Consumable {

    public OBJ_BlueHeart(GamePanel gPanel) {
        super(gPanel, TypeConsumable.BlueHeart, 99999);
        name = "Inima Albastra";
        setImage("blueHeart/blueheart.png");
        description = "[" + name + "]\nCheia unui portal\nspatio-temporal.";

        setDialogues();
    }

    @Override
    public void setDefaultSolidArea() {

    }

    public void setDialogues() {
        dialogue.addText("Ai gasit o comoara legendara!\nInima Albastra poate deschide un portal dimensional!");
    }

    @Override
    public boolean use(Entity user) {

        GamePanel.gameState = GameState.CutSceneState;
        getGamePanel().cutSceneManager.sceneState = SceneState.Ending;

        return true;
    }

    @Override
    public SuperObject generateObject() {
        return new OBJ_BlueHeart(getGamePanel());
    }
}
