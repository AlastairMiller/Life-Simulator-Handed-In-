import javafx.scene.image.Image;

/**
 * The Magikarp class which inherits from GUIEntity. I handle the Magikarp entity which navigates the food
 * looking for food
 */
public class AMagikarp extends GUIEntity {
    AMagikarp(GUIWorld inputworld) {
        Species = "Magikarp";
        Positionx = 0;
        Positiony = 0;
        energy = 5;
        ID = 0;
        currentworld= inputworld;
       imageType="Magikarp.png";
        whoihunt= "PokeFood";
    }

    public AMagikarp(int x, int y, int energy, int ID, GUIWorld itsWorld) {
        setpositionx(x);
        setpositiony(y);
        setEnergy(energy);
        setID(ID);
        setCurrentworld(itsWorld);
    }



}
