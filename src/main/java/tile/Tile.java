package tile;

import java.awt.image.BufferedImage;

/** Clasa ce reprezinta o textura (oricare din ele) */
public class Tile {

    public int idTile;
    public BufferedImage image; // imaginea texturii
    public boolean isColliding = false; // proprietate de coliziune

    public void setSolid() {
        isColliding = true;
    }
}
