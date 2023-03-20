package interactive_tile;

import game.GamePanel;

public abstract class ResultTile extends InteractiveTile {

    TypeResultTile typeResultTile;

    public ResultTile(GamePanel gp, TypeResultTile typeResultTile) {
        super(gp, TypeInteractiveTile.ResultTile);
        this.typeResultTile = typeResultTile;
    }
}
