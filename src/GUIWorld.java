
import java.util.*;
/**
 This class handles the world in which the entities operate. It has several properties which are listed below. its also
 handle all the entities in an ArrayList.
 */
public class GUIWorld {// GUIWorld properties
    private int xSize, ySize;
    private ArrayList<GUIEntity> CurrentEntities = new ArrayList<GUIEntity>();
    private Random randomnums;
    private int currententity;

    GUIWorld() {// Constructor
        xSize = 5;
        ySize = 5;
        currententity = 0;
        CurrentEntities = new ArrayList<GUIEntity>();
        randomnums = new Random();

    }

    public GUIWorld(int x, int y, int Maxsize) {// Controlled Constructor
        setxSize(x);
        setySize(y);
        setCurrententity(CurrentEntities.size());
        CurrentEntities = new ArrayList<GUIEntity>();
        currententity = 0;
    }

    /*
    methods to get and set the properties*/

    int getxSize() {
        return xSize;
    }

    void setxSize(int xSize) {
        this.xSize = xSize;
    }

    int getySize() {
        return ySize;
    }

    void setySize(int ySize) {
        this.ySize = ySize;
    }


    public int getCurrententity() {
        return currententity;
    }

    private void setCurrententity(int currententity) {
        this.currententity = currententity;
    }

    ArrayList<GUIEntity> getCurrentEntities() {
        return CurrentEntities;
    }


    /**
    function to add a new Magikarp
     */
    void NewMagikarp() {
        randomnums = new Random();
        randomnums = new Random();
        int ranx = randomnums.nextInt(getxSize());
        int rany = randomnums.nextInt(getySize());
        while (Checker(ranx, rany) == false) {
            ranx = randomnums.nextInt(getxSize());
            rany = randomnums.nextInt(getySize());
        }
        CurrentEntities.add(new AMagikarp(this));
        CurrentEntities.get(CurrentEntities.size() - 1).setpositionx(ranx);
        CurrentEntities.get(CurrentEntities.size() - 1).setpositiony(rany);
        currententity += 1;
    }

    /**
    function to add new food
     */

    void NewPokeFood() {
        randomnums = new Random();
        int ranx = randomnums.nextInt(getxSize());
        int rany = randomnums.nextInt(getySize());
        while (Checker(ranx, rany) == false) {
            ranx = randomnums.nextInt(getxSize());
            rany = randomnums.nextInt(getySize());
        }
        CurrentEntities.add(new PokeFood(this));
        CurrentEntities.get(CurrentEntities.size() - 1).setpositionx(ranx);
        CurrentEntities.get(CurrentEntities.size() - 1).setpositiony(rany);
    }

    /**
    function to add new Obstacle
     */
    void NewObstacle() {
        randomnums = new Random();
        int ranx = randomnums.nextInt(getxSize());
        int rany = randomnums.nextInt(getySize());
        while (Checker(ranx, rany) == false) {
            ranx = randomnums.nextInt(getxSize());
            rany = randomnums.nextInt(getySize());
        }
        CurrentEntities.add(new Obstacle(this));
        CurrentEntities.get(CurrentEntities.size() - 1).setpositionx(ranx);
        CurrentEntities.get(CurrentEntities.size() - 1).setpositiony(rany);
    }

    void NewSharpedo() { //function to add new Sharpedo
        randomnums = new Random();
        int ranx = randomnums.nextInt(getxSize());
        int rany = randomnums.nextInt(getySize());
        while (Checker(ranx, rany) == false) {
            ranx = randomnums.nextInt(getxSize());
            rany = randomnums.nextInt(getySize());
        }
        CurrentEntities.add(new ASharpedo(this));
        CurrentEntities.get(CurrentEntities.size() - 1).setpositionx(ranx);
        CurrentEntities.get(CurrentEntities.size() - 1).setpositiony(rany);
        currententity += 1;
    }

    /**
    function to show all entities
     */
    private void Allentities() {
        for (int i = 0; i <= currententity; i++) {
            P(CurrentEntities.get(i).getID());
        }
    }

    /**
    A function to check to see if a entity occupies a certain xy coordinate
     */

    private boolean Checker(int ranx, int rany) {
        for (GUIEntity CurrentEntity : CurrentEntities) {
            if (ranx == CurrentEntity.getpositionx()) {// checks the x
                if (rany == CurrentEntity.getpositiony()) {// checks the y
                    return false;
                }
            }
        }
        return true;
    }

    /**
     This function reports food in a specified xy
     */
    private GUIEntity Reportfood(int x, int y) {
        for (int p = 0; p < getCurrentEntities().size(); p++) {// loops through array looking for food
            if (getCurrentEntities().get(p).getSpecies() == "PokeFood" && getCurrentEntities().get(p).getpositionx() == x
                    && getCurrentEntities().get(p).getpositiony() == y) {
                return getCurrentEntities().get(p);

            }
        }
        P("Do food found at that position!!");
        return null;
    }

    /**
  This function is pretty self explanatory, it is used to check whether any particular entity can move. It does this by checking if its in the bounds of the arena
  as well as preforming the eating mechanic if required. For example if the fish is next to food it will consume it.
   */
    boolean entityCanMove(int x, int y, GUIEntity passedEnitity, String hunted) {

        for (int p = 0; p < getCurrentEntities().size(); p++) {


            if (x >= this.getxSize() || x < 0 || y >= this.getySize() || y < 0) {
                return false;
            }
            if (getCurrentEntities().get(p).getpositionx() == x && getCurrentEntities().get(p).getpositiony() == y) {
                if (getCurrentEntities().get(p).getSpecies() == hunted) {// If entity is of type food
                    int foodgained = getCurrentEntities().get(p).energy;
                    passedEnitity.setEnergy(foodgained + passedEnitity.getEnergy());
                    getCurrentEntities().remove(p);//Removes food from the array list
                    return true;// Then Food has been eaten
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    The RunOnce function does a cycle of the simulation. It will loop through all the different types of entities that move. Then
    it proceeds to move these entities (the Fish and sharks) to there new positions which direction depends on whether they can small their desired food.
     */
    void RunOnce() {
        int maxRange = 5;
        char[] charArray = new char[4];
        charArray[0] = 'N';// sets up different compass for assigning
        charArray[1] = 'E';
        charArray[2] = 'S';
        charArray[3] = 'W';
        for (int p = 0; p < CurrentEntities.size(); p++) {// loops through all entitys
            if (CurrentEntities.get(p).getSpecies().equals("PokeFood") || CurrentEntities.get(p).getSpecies().equals("Obstacle"))
                continue; // Food cannot move
            boolean foodFound = false;
            for (int j = 1; j <= maxRange; j++) {//loop for up to max range
                for (int d = 0; d < 4; d++) {//loop for every direction
                    if (CurrentEntities.get(p).smellFood(charArray[d], j)) {// calls the smell food function
                        CurrentEntities.get(p).move(Compass.compassDir.valueOf(String.valueOf(charArray[d])));// if there is a entity detected move one in that direction

                        foodFound = true;

                        break;
                    }
                }
                if (foodFound) break;

            }

            if (!foodFound) {// if food is not found move randomly
                int tempx = CurrentEntities.get(p).getpositionx();
                int tempy = CurrentEntities.get(p).getpositiony();
                int maxTries = 100;
                int numTries = 0;
                while (tempx == CurrentEntities.get(p).getpositionx() && tempy == CurrentEntities.get(p).getpositiony()) {
                    CurrentEntities.get(p).move(Compass.getCompass());
                    numTries++;
                    if (numTries >= maxTries)
                        break;
                }


            }


        }


    }

    public void main(String[] args) {
        /* Sets up scanner and sets up a new world */
        Scanner in = new Scanner(System.in);
        GUIWorld Newworld = new GUIWorld();
    }


    /**
    Function which prints out how much food and energy is left in the world
     */
    void Worldstats() {// Function which checks the remaining food and energy of the world.
        int foodremaining = 0;
        int energyleft = 0;
        for (int i = 0; i < CurrentEntities.size(); i++) {
            if (CurrentEntities.get(i).getSpecies() == "PokeFood") {// If current entity is of type food
                foodremaining += 1;
            }
            energyleft = CurrentEntities.get(i).energy + energyleft;
        }
        P("Remaining food in the world: " + foodremaining + "All of the energy left: " + energyleft);
    }

    /**
    Function to shorten the system.out.println line of code
    */
    private static void P(Object s) {
        GUIinterface.safePrintln(s);
    }
}
