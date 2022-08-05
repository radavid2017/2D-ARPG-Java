package object;

public class OBJ_Chest extends SuperObject{
    public OBJ_Chest() {
        this.typeObject = TypeObject.Chest;
        this.collision = true;
        this.setSolidArea(14, 8, 74, 62);
    }
}
