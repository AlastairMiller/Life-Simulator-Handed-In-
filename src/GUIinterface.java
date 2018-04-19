import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Currency;

/**
 * This class manages/controls the GUIWorld so the user can see the world as well as
 * control it as they require. This class also handles the opening and saving of configuration files.
 */

public class GUIinterface extends Application {
    int width = 1000, height = 1000;
    GUIWorld currentworld;
    String currentname = "Default.txt";

    int foodcount = 0;
    int obcount = 0;
    int magicount = 0;
    int sharpcount = 0;
    int tot = 0;
    boolean show = false;

    boolean isPaused = true;

    public static void safePrintln(Object s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    /**
     * This function is for displaying the actual control window. The parameter for this class is @param primaryStage.
     * As this function is run when the stage is created, the runtime thread for visualising the entities is also in this function.
     * The menu code is in this function as it needs to be shown on start up.
     */
    public void start(final Stage primaryStage) {
        currentworld = new GUIWorld();
        primaryStage.setTitle("Life Simulator");
        primaryStage.setX(0);
        primaryStage.setY(0);
        FileReader fr = null;
        show = false;
        try {
            fr = new FileReader(currentname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fr != null;
        BufferedReader textreader = new BufferedReader(fr);
        try {
            String input = textreader.readLine();
            fromText(input);
            entityCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Canvas canvas = new Canvas();
        final GraphicsContext[] gc = {canvas.getGraphicsContext2D()};
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                canvas.getGraphicsContext2D().setFill(Color.SKYBLUE);
                canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                if (show) {
                    renderEntities(gc[0]);
                }
            }
        };
        timer.start();

        Thread Runonce = new Thread(

                new Runnable() {
                    int i = 0;

                    public void run() {
                        while (true) {
                            if (!isPaused)
                                currentworld.RunOnce();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Runonce.start();


        final MenuBar menuBar = new MenuBar();


        //pane.getChildren().addAll(canvas, entitys);

        primaryStage.setTitle("Life Simulator");
        BorderPane pane = new BorderPane();
        // StackPane stackPane= new StackPane();

        final Menu fileMenu = new Menu("_New");
        fileMenu.setMnemonicParsing(true);
        MenuItem newConfig = new MenuItem("1. New configuration");
        MenuItem openConfig = new MenuItem("2. Open configuration file");
        MenuItem save = new MenuItem("3. Save");
        MenuItem saveAs = new MenuItem("4. Save As");
        MenuItem exit = new MenuItem("5. Exit");
        newConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String filename = JOptionPane.showInputDialog("Enter file name");
                String xsize = JOptionPane.showInputDialog("Set X size");
                String ysize = JOptionPane.showInputDialog("Set Y size");

                String food = JOptionPane.showInputDialog("Insert food percentage");
                String obst = JOptionPane.showInputDialog("Insert obstacle percentage");
                String magi = JOptionPane.showInputDialog("Insert Magikarp percentage");
                String sharp = JOptionPane.showInputDialog("Insert Sharpedo percentage");


                try {
                    PrintWriter writer = new PrintWriter(filename + ".txt", "UTF-8");
                    writer.write(xsize + " " + ysize + " " + food + " " + obst + " ant " + magi + " " + sharp);
                    writer.close();
                } catch (IOException e) {
                    // do something
                }
                JOptionPane.showMessageDialog(null, "Sucessfully saved");
            }
        });
        openConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fromText(importFile(primaryStage));

            }

        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(currentname, "UTF-8");
                    String temp = ToText();
                    writer.flush();
                    writer.println(temp);
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
        saveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String filename = JOptionPane.showInputDialog("Please enter new filename");
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(filename + ".txt", "UTF-8");
                    String temp = ToText();
                    writer.println(temp);
                    writer.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        exit.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newConfig, openConfig, save, saveAs,
                new SeparatorMenuItem(), exit);

        final Menu viewMenu = new Menu("_View");
        fileMenu.setMnemonicParsing(true);
        MenuItem displayConfig = new MenuItem("1. Display configuration");
        MenuItem editConfig = new MenuItem("2. Edit configuration");
        MenuItem displayInfo = new MenuItem("3. Display info about life forms");
        MenuItem displayInfo2 = new MenuItem("4. Display info about the map");

        displayConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                entityCount();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "X size: " + currentworld.getxSize() + " , Y size: " + currentworld.getySize() + " , Number of Pokefood: " + foodcount + " , Number of obstacles: " + obcount + " , Number of Magikarp: " + magicount + " , Number of Sharpedo: " + sharpcount);
                alert.showAndWait();
            }
        });

        editConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object[] possiblities = {"X Size", "Y Size", "Number of Food", "Number of Obstacles", "Number of Magikarp", "Number of Sharpedo"};
                String s = (String) JOptionPane.showInputDialog(new Frame(), "Select an attribute to edit\n", "Edit", JOptionPane.QUESTION_MESSAGE, null, possiblities, "XSize");
                if ((s != null) && (s.length() > 0)) {

                    int newval = Integer.parseInt(JOptionPane.showInputDialog(new Frame(), "Please enter the new value for " + s + " as selected earlier"));
                    switch (s) {
                        case "X Size":
                            currentworld.setxSize(newval);
                        case "Y Size":
                            currentworld.setySize(newval);
                        case "Number of Food":
                            entityCount();
                            if (newval > foodcount) {
                                for (int i = foodcount; i != newval; i++) {
                                    currentworld.NewPokeFood();
                                }
                            } else if (newval < foodcount) {
                                for (int i = newval; i != foodcount; i++) {
                                    for (int p = 0; p < currentworld.getCurrentEntities().size(); p++) {
                                        if (currentworld.getCurrentEntities().get(p).getSpecies() == "PokeFood") {
                                            currentworld.getCurrentEntities().remove(p);
                                            break;
                                        }
                                    }
                                }
                            }
                        case "Number of Obstacles":
                            entityCount();
                            if (newval > obcount) {
                                for (int i = obcount; i != newval; i++) {
                                    currentworld.NewObstacle();
                                }
                            } else if (newval < obcount) {
                                for (int i = newval; i != obcount; i++) {
                                    for (int p = 0; p < currentworld.getCurrentEntities().size(); p++) {
                                        if (currentworld.getCurrentEntities().get(p).getSpecies() == "Obstacle") {
                                            currentworld.getCurrentEntities().remove(p);
                                            break;
                                        }
                                    }
                                }
                            }
                        case "Number of Magikarp":
                            entityCount();
                            if (newval > magicount) {
                                for (int i = magicount; i != newval; i++) {
                                    currentworld.NewMagikarp();
                                }
                            } else if (newval < magicount) {
                                for (int i = newval; i != magicount; i++) {
                                    for (int p = 0; p < currentworld.getCurrentEntities().size(); p++) {
                                        if (currentworld.getCurrentEntities().get(p).getSpecies() == "Magikarp") {
                                            currentworld.getCurrentEntities().remove(p);
                                            break;
                                        }
                                    }
                                }
                            }
                        case "Number of Sharpedo":
                            entityCount();
                            if (newval > sharpcount) {
                                for (int i = sharpcount; i != newval; i++) {
                                    currentworld.NewSharpedo();
                                }
                            } else if (newval < sharpcount) {
                                for (int i = newval; i != sharpcount; i++) {
                                    for (int p = 0; p < currentworld.getCurrentEntities().size(); p++) {
                                        if (currentworld.getCurrentEntities().get(p).getSpecies() == "Sharpedo") {
                                            currentworld.getCurrentEntities().remove(p);
                                            break;

                                        }
                                    }
                                }
                            }
                    }
                    return;
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "Please enter a valid option");
                }
            }

        });

        displayInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder b = new StringBuilder();
                for (int i = 0; i < currentworld.getCurrentEntities().size(); i++) {
                    if (currentworld.getCurrentEntities().get(i).getSpecies() != "Obstacle" && currentworld.getCurrentEntities().get(i).getSpecies() != "PokeFood") {
                        b.append("ID: " + i + " Species: " + currentworld.getCurrentEntities().get(i).getSpecies() + " Postion:(" + currentworld.getCurrentEntities().get(i).getpositionx() + " , "
                                + currentworld.getCurrentEntities().get(i).getpositiony() + ") Energy: " + currentworld.getCurrentEntities().get(i).getEnergy() + "\n");
                    }
                }
                b.toString();
                JOptionPane.showMessageDialog(new Frame(), b);
            }
        });
        displayInfo2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder b = new StringBuilder();
                b.append("X size: " + currentworld.getxSize() + " Y size: " + currentworld.getySize() + "\n");
                for (int i = 0; i < currentworld.getCurrentEntities().size(); i++) {
                    if (currentworld.getCurrentEntities().get(i).getSpecies() != "Magikarp" && currentworld.getCurrentEntities().get(i).getSpecies() != "Sharpedo") {
                        b.append("ID: " + i + " Type: " + currentworld.getCurrentEntities().get(i).getSpecies() + " Postion:(" + currentworld.getCurrentEntities().get(i).getpositionx() + " , "
                                + currentworld.getCurrentEntities().get(i).getpositiony() + ") Energy: " + currentworld.getCurrentEntities().get(i).getEnergy() + "\n");
                    }
                }
                b.toString();
                JOptionPane.showMessageDialog(new Frame(), b);
            }
        });

        viewMenu.getItems().addAll(displayConfig, editConfig,
                displayInfo, displayInfo2);

        Menu editMenu = new Menu("_Edit");
        fileMenu.setMnemonicParsing(true);
        MenuItem mod = new MenuItem("1. Modify current life form parameters");
        MenuItem remove = new MenuItem("2. Remove current life form");
        MenuItem addNew = new MenuItem("3. Add a new life form");

        mod.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer inp = Integer.valueOf(JOptionPane.showInputDialog("Please enter the entity number you require:"));
                if (inp < currentworld.getCurrentEntities().size()) {
                    Integer temp = Integer.valueOf(JOptionPane.showInputDialog("Please enter a new X value"));
                    currentworld.getCurrentEntities().get(inp).setpositionx(temp);
                    temp = 0;
                    temp = Integer.valueOf(JOptionPane.showInputDialog("Please enter a new Y value"));
                    currentworld.getCurrentEntities().get(inp).setpositiony(temp);
                    Object[] possibilities = {"PokeFood", "Obstacle", "Magikarp", "Sharpedo"};
                    String s = (String) JOptionPane.showInputDialog(new Frame(), "Select an Species to change to \n", "Species change", JOptionPane.QUESTION_MESSAGE, null, possibilities, "PokeFood");
                    switch (s) {
                        case "PokeFood":
                            currentworld.getCurrentEntities().get(inp).setSpecies("PokeFood");
                            break;
                        case "Obstacle":
                            currentworld.getCurrentEntities().get(inp).setSpecies("Obstacle");
                            break;
                        case "Magikarp":
                            currentworld.getCurrentEntities().get(inp).setSpecies("Magikarp");
                            break;
                        case "Sharpedo":
                            currentworld.getCurrentEntities().get(inp).setSpecies("Sharpedo");
                            break;
                    }

                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "There is no entity with number: " + inp);
                }
            }
        });
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int i = Integer.parseInt(JOptionPane.showInputDialog("Please enter the entity ID to remove: "));
                currentworld.getCurrentEntities().remove(i);
            }
        });
        addNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final String[] possiblities = {"Food", "Obstacle", "Magikarp", "Sharpedo"};
                String s = (String) JOptionPane.showInputDialog(new JFrame(), "Select an entity to add\n", "Add new", JOptionPane.QUESTION_MESSAGE, null, possiblities, possiblities[0]);
                switch (s) {
                    case "Food":
                        currentworld.NewPokeFood();
                        break;
                    case "Obstacle":
                        currentworld.NewObstacle();
                        break;
                    case "Magikarp":
                        currentworld.NewMagikarp();
                        break;
                    case "Sharpedo":
                        currentworld.NewSharpedo();
                        break;
                }
            }
        });
        editMenu.getItems().addAll(mod, remove, addNew);

        Menu simMenu = new Menu("_Simulation");
        fileMenu.setMnemonicParsing(true);
        MenuItem run = new MenuItem("1. Run");
        MenuItem pause = new MenuItem("2. Pause/Restart");
        MenuItem reset = new MenuItem("3. Reset");
        MenuItem display = new MenuItem("4. Display map at each iteration");
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvas.setHeight(currentworld.getySize() * 50);
                canvas.setWidth(currentworld.getxSize() * 50);
                primaryStage.setHeight(canvas.getHeight() + 75);
                primaryStage.setWidth(canvas.getWidth() + 13);
                canvas.getGraphicsContext2D().setFill(Color.SKYBLUE);
                canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                show = true;
                isPaused = false;

            }

        });
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                isPaused = !isPaused;

            }
        });
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentworld = new GUIWorld(currentworld.getxSize(), currentworld.getySize(), currentworld.getCurrentEntities().size());
                currentworld.getCurrentEntities().clear();
                for (int i = 0; i < tot; i++) {
                    if (foodcount > 0) {
                        currentworld.NewPokeFood();
                        i++;
                    }
                    if (obcount > 0) {
                        currentworld.NewObstacle();
                        i++;
                    }
                    if (magicount > 0) {
                        currentworld.NewMagikarp();
                        i++;
                    }
                    if (sharpcount > 0) {
                        currentworld.NewSharpedo();
                    }

                }

            }
        });
        display.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                show = !show;
            }
        });
        simMenu.getItems().addAll(run, pause, reset, display);

        Menu helpmenu = new Menu("_Help");
        fileMenu.setMnemonicParsing(true);
        MenuItem displayinfo = new MenuItem("1. Display info about application");
        MenuItem displayAuthor = new MenuItem("2. Display info about author");

        displayinfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JOptionPane.showMessageDialog(new JFrame(), "V1 was published 15/01/2017");
            }
        });

        displayAuthor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JOptionPane.showMessageDialog(new JFrame(), "Created by Alastair Miller");
            }
        });

        helpmenu.getItems().addAll(displayinfo, displayAuthor);
        menuBar.getMenus().addAll(fileMenu, viewMenu, editMenu, simMenu, helpmenu);
        pane.setTop(menuBar);
        pane.setLeft(canvas);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    /**
     * This function sets up the entities in preparation for displaying
     */
    public void setupEntities(Pane canvas) {
        for (int i = 0; i < currentworld.getCurrentEntities().size(); i++)
            if (!canvas.getChildren().contains(currentworld.getCurrentEntities().get(i).getRect()))
                canvas.getChildren().add(currentworld.getCurrentEntities().get(i).getRect());
    }

    /**
     * This function draws all the entities on the passed Graphics Context
     */

    public void renderEntities(GraphicsContext gc) {
        try {
            for (int i = 0; i < currentworld.getCurrentEntities().size(); i++) {
                int imageX = currentworld.getCurrentEntities().get(i).getpositionx() * 50;
                int imageY = currentworld.getCurrentEntities().get(i).getpositiony() * 50;
                gc.drawImage(new Image(getClass().getResourceAsStream(currentworld.getCurrentEntities().get(i).getImageType())), imageX, imageY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function sets up world from a text input
     */

    public void fromText(String input) {
        String[] splitter = input.split("\\s+");// splits the text input up into an array of strings
        int count = 0;
        currentworld.setxSize(Integer.parseInt(splitter[count]));// the first entry will be the x size
        count++;
        currentworld.setySize(Integer.parseInt(splitter[count]));// the second entry will the y size
        count++;
        currentworld.getCurrentEntities().clear();
        int Worldsize = currentworld.getxSize() * currentworld.getySize();// the third entry defines world size
        for (int i = 0; i < ((Integer.parseInt(splitter[count]) * Worldsize) / 100); i++) {// loops until all entity has been added
            currentworld.NewPokeFood();
        }
        count++;
        for (int i = 0; i < ((Integer.parseInt(splitter[count]) * Worldsize) / 100); i++) {// same as above
            currentworld.NewObstacle();
        }
        count += 2;
        for (int i = 0; i < (Integer.parseInt(splitter[count]) * Worldsize / 100); i++) {//same as above
            currentworld.NewMagikarp();
        }
        count++;
        for (int i = 0; i < (Integer.parseInt(splitter[count]) * Worldsize / 100); i++) {//same as above
            currentworld.NewSharpedo();
        }
    }

    /**
     * This function allows the program to save to a text file
     */

    public String ToText() {
        StringWriter a = new StringWriter();
        String output = "";
        int worldsize = currentworld.getxSize() * currentworld.getySize();
        entityCount();
        foodcount = Math.round((float) foodcount / 625 * 100);
        obcount = Math.round((float) obcount / 625 * 100);
        magicount = Math.round((float) magicount / 625 * 100);
        sharpcount = Math.round((float) sharpcount / 625 * 100);
        a.write(currentworld.getxSize() + " " + currentworld.getySize() + " " + foodcount + " " + obcount + " ant " + magicount + " " + sharpcount);
        output = a.toString();
        return output;
    }

    /**
     * This function is called when a file needs importing. It opens a filechooser so the user can look for a file visually.
     */

    public String importFile(Stage stage) {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Open World file");
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text files", "*.txt*"));
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        File input = filechooser.showOpenDialog(stage);

        String configRead = "";
        try {
            FileReader reader = new FileReader(input);
            BufferedReader outputConfig = new BufferedReader(reader);
            configRead = outputConfig.readLine();
            safePrintln(configRead);
            currentname = input.getName();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configRead;
    }


    public static void main(String[] args) {
        launch();
    }

    /**
     * A function which counts the entities to make the statistic options accurate
     */
    public void entityCount() {
        foodcount = 0;
        obcount = 0;
        magicount = 0;
        sharpcount = 0;

        for (int i = 0; i < currentworld.getCurrentEntities().size(); i++) {
            if (currentworld.getCurrentEntities().get(i).getSpecies() == "PokeFood") {
                foodcount++;
            } else if (currentworld.getCurrentEntities().get(i).getSpecies() == "Obstacle") {
                obcount++;
            } else if (currentworld.getCurrentEntities().get(i).getSpecies() == "Magikarp") {
                magicount++;
            } else if (currentworld.getCurrentEntities().get(i).getSpecies() == "Sharpedo") {
                sharpcount++;
            }
        }
        tot = foodcount + magicount + sharpcount;
    }

}
