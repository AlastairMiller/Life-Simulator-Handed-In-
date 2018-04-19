
/**
 This Class handles the food which the fish eat. it has several properties such as a energy level which when consumes
 increases the fish's energy.
 */
public class PokeFood extends GUIEntity {
    PokeFood(GUIWorld inputworld) {
        Species = "PokeFood";
        Positionx = 0;
        Positiony = 0;
        energy = 5;
        ID = 0;
        currentworld= inputworld;
        imageType="OranBerry.png";
    }

    public PokeFood(int x, int y, int energy, int ID, GUIWorld itsWorld) {
        setpositionx(x);
        setpositiony(y);
        setEnergy(energy);
        setID(ID);
        setCurrentworld(itsWorld);
    }
}
