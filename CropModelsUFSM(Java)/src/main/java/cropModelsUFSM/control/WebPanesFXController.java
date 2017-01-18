package cropModelsUFSM.control;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import cropModelsUFSM.support.Util;

public class WebPanesFXController extends GenericFXController {

    @FXML private BorderPane roundBorderPane;
    @FXML private WebView browser;

    public void postInitializeTasks (Stage stage){
        super.postInitializeTasks(stage);
    }

    public void loadWebContent (String url) {
        WebEngine webEngine = browser.getEngine();
        webEngine.load(Util.loader.
                getResource(url).toExternalForm());
        browser.setZoom(1.0);
    }

    @FXML
    private void closeAction (MouseEvent event) {
        stage.hide();
    }
}
