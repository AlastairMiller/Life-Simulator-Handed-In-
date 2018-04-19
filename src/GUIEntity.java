import javafx.scene.shape.Rectangle;

/**
 * This class is for a generic entity. All entities must have these attributes and inherit from this class.
 */
public abstract class GUIEntity {
    String Species;
    int Positionx, Positiony;
    int energy, ID;
    GUIWorld currentworld;
    private Compass.compassDir compassPos;
    String imageType = "Default.png";
    private Rectangle rect;
    String whoihunt;

    GUIEntity() {
        Species = "Undefined";
        Positionx = 0;
        Positiony = 0;
        energy = 100;
        ID = 0;
        currentworld = new GUIWorld();
        setCompassPos();
        rect = new Rectangle();
    }

    /**
     * function to get and set each of the entitys properties
     */

    int getpositionx() {
        return Positionx;
    }

    void setpositionx(int x) {
        Positionx = x;
    }

    int getpositiony() {
        return Positiony;
    }

    void setpositiony(int y) {
        Positiony = y;
    }

    String getSpecies() {
        return Species;
    }

    public void setSpecies(String species) {
        Species = species;
    }

    int getEnergy() {
        return energy;
    }

    void setEnergy(int energy) {
        this.energy = energy;
    }

    int getID() {
        return ID;
    }

    void setID(int ID) {
        this.ID = ID;
    }

    private GUIWorld getCurrentworld() {
        return currentworld;
    }

    void setCurrentworld(GUIWorld currentworld) {
        this.currentworld = currentworld;
    }

    public Compass.compassDir getCompassPos() {
        return compassPos;
    }

    public void setCompassPos() {
        this.compassPos = Compass.getCompass();
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public String getImageType() {
        return imageType;
    }

    public String toString(int x, int y) {
        String complete = "The Bug at position" + x + y + "is of species" + getSpecies();
        return complete;
    }


    /**
     * This function checks and tries to see if there is any food to eat within a passed range and direction. If there is food is will return TRUE
     * else it will return FALSE.
     * @param direction
     * @param range
     * @return
     */
    public boolean smellFood(char direction, int range) {
        int n;
        switch (direction) {
            case 'E':
                for (n = 0; n < getCurrentworld().getCurrentEntities().size(); ++n) {
                    if ((getCurrentworld().getCurrentEntities().get(n)).getpositionx() == getpositionx() + range && ((GUIEntity) getCurrentworld().getCurrentEntities().get(n)).getpositiony() == getpositiony() && (getCurrentworld().getCurrentEntities().get(n)).getSpecies() == whoihunt) {
                        return true;
                    }
                }

                return false;
            case 'N':
                for (n = 0; n < this.getCurrentworld().getCurrentEntities().size(); ++n) {
                    if (((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getpositiony() == this.getpositiony() + range && ((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getpositionx() == this.getpositionx() && ((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getSpecies() == whoihunt) {
                        return true;
                    }
                }

                return false;
            case 'S':
                for (n = 0; n < this.getCurrentworld().getCurrentEntities().size(); ++n) {
                    if (((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getpositiony() == this.getpositiony() - range && ((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getpositionx() == this.getpositionx() && ((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getSpecies() == whoihunt) {
                        return true;
                    }
                }

                return false;
            case 'W':
                for (n = 0; n < this.getCurrentworld().getCurrentEntities().size(); ++n) {
                    if (((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getpositionx() == this.getpositionx() - range && ((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getpositiony() == this.getpositiony() && ((GUIEntity) this.getCurrentworld().getCurrentEntities().get(n)).getSpecies() == whoihunt) {
                        return true;
                    }
                }

                return false;
            default:

                return false;
        }
    }

    /**
     * the function which is called to move a entity. It is passed the direction and adds the
     * entity energy to it if food was eaten
     * @param dirtomove
     */
    public void move(Compass.compassDir dirtomove) {
        int x;
        int y;
        int foodGained = 0;
        switch (dirtomove) {
            case N:
                x = this.getpositionx();
                y = this.getpositiony() + 1;
                if (this.getCurrentworld().entityCanMove(x, y, this, whoihunt)) {
                    this.setpositionx(x);
                    this.setpositiony(y);
                    this.setEnergy(this.getEnergy() + foodGained);
                    break;
                }
            case E:
                x = this.getpositionx() + 1;
                y = this.getpositiony();
                if (this.getCurrentworld().entityCanMove(x, y, this, whoihunt)) {
                    this.setpositionx(x);
                    this.setpositiony(y);
                    this.setEnergy(this.getEnergy() + foodGained);

                    break;
                }
            case S:
                x = this.getpositionx();
                y = this.getpositiony() - 1;
                if (this.getCurrentworld().entityCanMove(x, y, this, whoihunt)) {
                    this.setpositionx(x);
                    this.setpositiony(y);
                    this.setEnergy(this.getEnergy() + foodGained);
                }
                break;
            case W:
                x = this.getpositionx() - 1;
                y = this.getpositiony();
                if (this.getCurrentworld().entityCanMove(x, y, this, whoihunt)) {
                    this.setpositionx(x);
                    this.setpositiony(y);
                    this.setEnergy(this.getEnergy() + foodGained);
                }
                break;
        }


    }


    public static void P(Object s) {
        GUIinterface.safePrintln(s);
    }
}
