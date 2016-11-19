package cropModelsUFSM.control;

import cropModelsUFSM.support.Util;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public class WebPanesFXController extends GenericFXController {

    /**
     *
     */
    @FXML private BorderPane roundBorderPane;
    @FXML private WebView browser;

    /**
     *
     * @param stage
     */
    public void postInitializeTasks (Stage stage){
        super.postInitializeTasks(stage);
    }

    /**
     *
     * @param url
     */
    public void loadWebContent (String url) {
        WebEngine webEngine = browser.getEngine();
        webEngine.load(Util.loader.
                getResource(url).toExternalForm());
        browser.setZoom(1.0);
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
