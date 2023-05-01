package tile;

import java.awt.image.BufferedImage;

/** Clasa ce reprezinta o textura (oricare din ele) */
public class Tile {

    public int idTile;
    public BufferedImage image; // imaginea texturii
    public boolean isColliding = false; // proprietate de coliziune

    public Tile(int idTile, BufferedImage image, boolean isColliding) {
        this.idTile = idTile;
        this.image = image;
        this.isColliding = isColliding;
    }

    public void setSolid() {
        isColliding = true;
    }
}
