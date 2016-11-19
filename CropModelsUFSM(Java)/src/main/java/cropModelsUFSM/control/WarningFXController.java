package cropModelsUFSM.control;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public class WarningFXController extends GenericFXController {

    /**
     *
     */
    @FXML private BorderPane roundBorderPane;
    @FXML private Label warning;

    /**
     *
     * @param stage
     */
    public void postInitializeTasks (Stage stage){
        super.postInitializeTasks(stage);
    }

    /**
     *
     * @param warn
     */
    public void loadWarning(String warn) {
        warning.setText(warn);
    }

    /**
     *
     * @param event
     */
    @FXML
    private void closeAction (MouseEvent event) {
        stage.hide();
    }
}
