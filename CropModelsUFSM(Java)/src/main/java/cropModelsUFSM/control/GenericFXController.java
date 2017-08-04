package cropModelsUFSM.control;

import cropModelsUFSM.task.TaskController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public abstract class GenericFXController implements Initializable {

    /**
     *
     */
    @FXML private Group root;
    @FXML private StackPane transparentPane;
    @FXML private BorderPane roundBorderPane;

    /**
     *
     */
    protected Double initX, initY, maxWidth, maxHeight;
    protected final Double minWidth = 800.0 , minHeight = 600.0;
    protected Rectangle2D primaryScreenBounds;
    protected Stage stage;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initX = 0.0;
        initY = 0.0;
    }

    /**
     *
     * @param stage
     */
    public void postInitializeTasks (Stage stage){
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        windowsLimitsInitialization();
        this.stage = stage;
    }

    /**
     *
     */
    public void windowsLimitsInitialization ()
    {
        maxWidth = primaryScreenBounds.getWidth();
        maxHeight = primaryScreenBounds.getHeight();
        System.out.println(maxWidth);
        if(maxWidth > 1920) {
            roundBorderPane.setTranslateX(minWidth/4);
            roundBorderPane.setTranslateY(minWidth/4);
            roundBorderPane.setScaleX(1.5);
            roundBorderPane.setScaleY(1.5);
        }
        transparentPane.setMaxSize(maxWidth, maxHeight);
        roundBorderPane.setMaxSize(maxWidth, maxHeight);
    }


    /***************************************************************************

     Acoes da interface

     **************************************************************************/


    /**
     *
     * @param event
     */
    @FXML
    private void closeAction (Event event) {
        stage.close();
        TaskController.finalizeTaskPool();
    }

    /**
     *
     * @param event
     */
    @FXML
    protected void maximizeAction (MouseEvent event){
        String style = "fx-background-color: rgb(232, 232, 232);";
        if(stage.isMaximized()) {
            stage.setMaximized(false);
            style = style + "-fx-pref-width: " + minWidth + ";" +
                    "-fx-pref-height: " + minHeight + ";" +
                    "-fx-min-width: " + minWidth + ";" +
                    "-fx-min-height: " + minHeight + ";" +
                    "-fx-background-radius: 10;";
            transparentPane.setMinWidth(minWidth + 50.0);
            transparentPane.setMinHeight(minHeight + 50.0);
            roundBorderPane.setStyle(style);
        }
        else {
            stage.setMaximized(true);
            style = style + "-fx-pref-width: " + maxWidth + ";" +
                    "-fx-pref-height: " + maxHeight + ";" +
                    "-fx-min-width: " + maxWidth + ";" +
                    "-fx-min-height: " + maxHeight + ";" +
                    "-fx-background-radius: 0;";
            transparentPane.setMinWidth(maxWidth);
            transparentPane.setMinHeight(maxHeight);
            roundBorderPane.setMinWidth(maxWidth);
            roundBorderPane.setMinHeight(maxHeight);
            roundBorderPane.setStyle(style);
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    private void minimizeAction (MouseEvent event){
        stage.setIconified(true);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void resizeEvent (MouseEvent event) {
        if((event.getScreenX() < stage.getX()+31) &&
                (event.getScreenX() > stage.getX()+24) &&
                (event.getScreenY() < stage.getY() + minHeight+10)  )
            stage.getScene().setCursor(Cursor.H_RESIZE);
        else
            stage.getScene().setCursor(Cursor.DEFAULT);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void moveWindowInitialPosition (MouseEvent event) {
        initX = (stage.getX() - event.getScreenX());
        initY = (stage.getY() - event.getScreenY());
    }

    /**
     *
     * @param event
     */
    @FXML
    private void moveWindow (MouseEvent event) {
        stage.setX(event.getScreenX() + initX);
        stage.setY(event.getScreenY() + initY);
    }

}
