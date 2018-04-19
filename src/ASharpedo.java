

/**
 * This class is for the predator. This predator hunts the smaller Magikarp then consumes them to increase their strength.
 */
public class ASharpedo extends GUIEntity  {
    ASharpedo(GUIWorld inputworld) {
        Species = "Sharpedo";
        Positionx = 0;
        Positiony = 0;
        energy = 5;
        ID = 0;
        currentworld= inputworld;
        imageType="Sharpedo.png";
        whoihunt="Magikarp";
    }

    public ASharpedo(int x, int y, int energy, int ID, GUIWorld itsWorld) {
        setpositionx(x);
        setpositiony(y);
        setEnergy(energy);
        setID(ID);
        setCurrentworld(itsWorld);
    }
}
