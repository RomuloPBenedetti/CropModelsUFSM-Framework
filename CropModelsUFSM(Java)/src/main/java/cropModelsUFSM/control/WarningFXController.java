package cropModelsUFSM.control;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WarningFXController extends GenericFXController {

    @FXML private BorderPane roundBorderPane;
    @FXML private Label warning;

    public void postInitializeTasks (Stage stage){
        super.postInitializeTasks(stage);
    }

    public void loadWarning(String warn) {
        warning.setText(warn);
    }

    @FXML
    private void closeAction (MouseEvent event) {
        stage.hide();
    }
}
