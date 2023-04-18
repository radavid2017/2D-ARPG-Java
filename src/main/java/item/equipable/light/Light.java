package item.equipable.light;

import game.CharacterClass;
import game.GamePanel;
import item.Equipable;
import item.equipable.TypeEquipable;

public abstract class Light extends Equipable {

    float lightRadius;

    public Light(GamePanel gPanel, int price) {
        super(gPanel, TypeEquipable.Light, CharacterClass.ANY, price);
        objPath += "light/";
    }

    public float getRadius() {
        return lightRadius;
    }

    public void setRadius(float lightRadius) {
        this.lightRadius = lightRadius;
    }
}
