package cropModelsUFSM.graphic;

import javafx.application.Application;
import javafx.fxml.LoadException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cropModelsUFSM.control.GuiController;
import cropModelsUFSM.support.Util;

import java.util.logging.Level;

import static cropModelsUFSM.support.Util.GuiFxmlLoader;
import static cropModelsUFSM.support.Util.loader;
import static cropModelsUFSM.support.Util.logger;

/**
 *  Starting point for the application, it initializes the interface and load what is
 *  necessary for runing the program
 *
 * @author romulo
 * @see cropModelsUFSM
 */
public class Gui extends Application {

    /** The scene is the background for all user interface elements, stored on a stage.*/
    private Scene scene;
    
    /** Most external UI element in the FXML structured user interface.*/
    private Group group;
    
    /** The interface controller, responsible for treating events triggered by user, fill
     * UI elements, delegate tasks.*/
    private GuiController controller;

    /**
     * Build the common hierarchy in a JavaFX interface, a <b>stage</b>(window) containing
     * a <b>scene</b>(background) that store all user interface elements, in this case
     * with a <b>group</b> on as the root element of all others.
     *
     * It load all the UI hierarchy from the <b>Gui.fxml</b>  resource, and bind its
     * <b>group</b> element to a <b>transparent background scene</b>, set the <b>stage</b>
     * window launcher icon and title bar name, binding the <b>scene</b> to it and stating
     * the stage as an <>transparent</> one, with no decoration, finally it load the
     * controller, do its post initialization tasks and show the stage.
     *
     * @param stage the stage given by {@link Application} after initializing the program
     * @throws Exception
     */
    @Override
    public void start (Stage stage) throws Exception
    {
        try {
            group = GuiFxmlLoader.load(Util.fxml);
            scene = new Scene(group);
            stage.getIcons().add(Util.icon);
            stage.setTitle("cropModelsUFSM");
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            controller = GuiFxmlLoader.getController();
            controller.postInitializeTasks(stage);
            stage.show();
        } catch (LoadException e){
            logger.log(Level.SEVERE, "CropModelsUFSM -> controller class not defined in " +
                    "Gui.fxml, refere to cropModelsUFSM.control.GuiController" +
                    e.toString(), e);
        }
    }

    /**
     * load all programs resources from jar file, preventing the program from shutting
     * down without unloading the fortran model executable, subsequently launch the javaFX
     * interface.
     *
     */
    public static void loadCropModelsUfsm ()
    {
        Util.loadRessources();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Util.unloadExecutables();
            Util.unloadLogger();
        }, "Shutdown-thread"));
        launch();
    }

    public static void main (String[] args){
        loadCropModelsUfsm();
    }
}
