/**
 This entity is a obstacle which no entity can pass through. the entity must go round to proceed.
 */

  
public class Obstacle extends GUIEntity {
    Obstacle(GUIWorld inputworld) {
        Species = "Obstacle";
        Positionx = 0;
        Positiony = 0;
        energy = 0;
        ID = 0;
        currentworld= inputworld;
        imageType="Rock.png";
    }

    public Obstacle(int x, int y, int energy, int ID, GUIWorld itsWorld) {
        setpositionx(x);
        setpositiony(y);
        setEnergy(energy);
        setID(ID);
        setCurrentworld(itsWorld);
    }
}

