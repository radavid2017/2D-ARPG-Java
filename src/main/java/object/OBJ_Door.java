package object;

public class OBJ_Door extends SuperObject {

    public OBJ_Door() {
        this.typeObject = TypeObject.Door;
        this.collision = true;
        this.setSolidArea(0,-1,96,69);
    }

}
