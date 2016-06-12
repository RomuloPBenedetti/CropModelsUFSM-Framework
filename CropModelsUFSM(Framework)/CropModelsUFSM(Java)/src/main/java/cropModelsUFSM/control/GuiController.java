package cropModelsUFSM.control;

import cropModelsUFSM.task.abstractTasks.MeteorologicFileTask;
import cropModelsUFSM.task.abstractTasks.SimulationTask;
import cropModelsUFSM.task.abstractTasks.VisualizationTask;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import cropModelsUFSM.data.SerializableSimulation;
import cropModelsUFSM.data.VisualizableSimulation;
import cropModelsUFSM.graphic.AnimationEvents;
import cropModelsUFSM.graphic.ImageAnimation;
import cropModelsUFSM.support.Util;
import cropModelsUFSM.task.ConcreteTask.ExtractSimulationTask;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.TaskInterfaces.TaskObserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static cropModelsUFSM.support.Util.*;

public abstract class GuiController
        extends GenericFXController implements TaskObserver {

    /**elements */
    @FXML private ImageView upImv, downImv;
    @FXML private MediaView video;

    @FXML private ImageView okImv, playImv;

    @FXML private AnchorPane simulation, tables, charts;
    @FXML private AnchorPane meteorologic, development;
    @FXML private VBox meteorologicLegend, developmentLegend;

    @FXML public Label dataLabel;
    @FXML private Shape dataShape;

    @FXML private ComboBox<String> simulationMenuA, simulationMenuB;
    @FXML private ComboBox<String> simulationYearA, simulationYearB;

    @FXML public AnchorPane warningPane;
    @FXML private Circle warningButton, simulationButtonCircle;

    private static Tooltip tip = new Tooltip("");
    public static List<SerializableSimulation> currentSimulation;
    private Rectangle2D galeryViewport;
    private Stage helpStage, aboutStage, legendStage, configStage;
    private static Stage warningStage;

/*******************************************************************************

                            Class Initialization

*******************************************************************************/



    @Override
    public void postInitializeTasks (Stage stage)
    {
        super.postInitializeTasks(stage);
        menusBindInitialization();
        InteractionElementsInitialization();
        decideAnimation();
        valueListeners();
        coldStart();
    }

    public void menusBindInitialization()
    {

        simulationMenuA.selectionModelProperty().bind(simulationMenuB.selectionModelProperty());
        simulationMenuA.valueProperty().bind(simulationMenuB.valueProperty());
        simulationMenuA.itemsProperty().bind(simulationMenuB.itemsProperty());

        simulationYearA.selectionModelProperty().bind(simulationYearB.selectionModelProperty());
        simulationYearA.valueProperty().bind(simulationYearB.valueProperty());
        simulationYearA.itemsProperty().bind(simulationYearB.itemsProperty());

    }

    protected abstract void InteractionElementsInitialization();

    public void decideAnimation ()
    {
        try {
            String source = loader.getResource("vd.mp4").toURI().toURL().toString();
            Media media = new Media(source);
            if(media.getError() != null) {
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                video.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
            }
        } catch(URISyntaxException | MalformedURLException ex) {
            ex.printStackTrace();
        }
        finally {
            if(video.getMediaPlayer() == null) {
                ImageAnimation imageAnimation = new ImageAnimation(upImv,downImv);
                imageAnimation.play();
            }
        }
    }


    /** This method detect on program start up, if there is any old results and/or meteorologic
     *  data stored on the program root directory, presenting data range for any meteorologic
     *  file found and showing the results list in SimulationMenus if any old result is found.
     */
    public void coldStart() {
        File resultFolder = new File(resultFolderpath);
        File meteorologicF = new File(meteorologicFile);

        if(meteorologicF.exists()) {
            MeteorologicFileTask meteorologicTask;
            meteorologicTask = newMeteorologicFileTask(meteorologicF, this);
            meteorologicTask.execute();
        }

        if( resultFolder.isDirectory() && resultFolder.list().length != 0) {
            for (File folder : new File(resultFolderpath).listFiles())
                if (folder.isDirectory())
                    simulationMenuA.getItems().add(folder.getName());

            simulationMenuA.getSelectionModel().select(0);
        }
    }

    protected abstract MeteorologicFileTask
        newMeteorologicFileTask(File file, GuiController guiController);

    protected abstract SimulationTask
    newSimulationTask(List<String> input, GuiController guiController);

    protected abstract VisualizationTask
    newVisualizationTask(SerializableSimulation input, GuiController guiController);

    /*******************************************************************************

                            Acoes do modelo/dados

    *******************************************************************************/

    @FXML
    private void insertDataAction (MouseEvent event)
    {
        File selectedFile = getMeteoroloficFile();
        if (selectedFile != null) {
            MeteorologicFileTask meteorologicTask;
            meteorologicTask = newMeteorologicFileTask(selectedFile, this);
            meteorologicTask.execute();
        }
    }

    @FXML
    private void runAction (MouseEvent event)
    {
        SimulationTask simulationTask = newSimulationTask(getSimulationInput(), this);
        simulationTask.execute();
    }

    private void valueListeners ()
    {
        simulationMenuA.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, newValue) -> {
            String simulationFolderName = simulationMenuA.getSelectionModel().getSelectedItem();
            if(simulationFolderName != null) {
                String SimulationPath = resultFolderpath + s + simulationFolderName;
                ExtractSimulationTask extractSimulationTask;
                extractSimulationTask = new ExtractSimulationTask(SimulationPath, this);
                extractSimulationTask.execute();
            }
        });

        simulationYearA.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, newValue) -> {
            Integer index = simulationYearA.getSelectionModel().getSelectedIndex();
            if(index<0 || index > currentSimulation.size()-1) index = 0;
            SerializableSimulation selectedYear = currentSimulation.get(index);
            VisualizationTask visualizationTask = newVisualizationTask(selectedYear,this);
            visualizationTask.execute();
        });

        Listenners();
    }

    protected abstract void Listenners();

    public void warningNature (String warning, Color from, Color to)
    {
        tip.setText(warning);
        AnimationEvents.setWarning(warningButton, from, to);
        alertTip(null);
    }

/*******************************************************************************

                            Acoes da interface

*******************************************************************************/


    @FXML
    private void settingsAction (Event event)
    {
        if(Util.rb.equals(Util.en)) {
            configStage = newWindow(configStage, Util.fxmlLoaderC, Util.configFxml,
                    "cropModelsUFSM Configurations", null);
        } else {
            configStage = newWindow(configStage, Util.fxmlLoaderC, Util.configFxml,
                    "cropModelsUFSM Configurations", null);
        }
    }

    @FXML
    private void legendAction (Event event)
    {
        if(Util.rb.equals(Util.en)) {
            legendStage =newWindow(legendStage, Util.fxmlLoaderL, Util.legendFxml,
                    "cropModelsUFSM Legend", Util.legendHtml_EN);
        } else {
            legendStage = newWindow(legendStage, Util.fxmlLoaderL, Util.legendFxml,
                    "cropModelsUFSM Legend", Util.legendHtml_PT);
        }
    }

    @FXML
    private void aboutAction (Event event)
    {
        if(Util.rb.equals(Util.en)) {
            aboutStage = newWindow(aboutStage, Util.aboutFxmlLoader, Util.aboutFxml,
                    "cropModelsUFSM About", Util.aboutHtml_EN);
        } else {
            aboutStage = newWindow(aboutStage, Util.aboutFxmlLoader, Util.aboutFxml,
                    "cropModelsUFSM About", Util.aboutHtml_PT);
        }
    }

    @FXML
    private void helpAction (Event event)
    {
        if(Util.rb.equals(Util.en)) {
            helpStage = newWindow(helpStage, Util.HelpFxmlLoader, Util.helpFxml,
                    "cropModelsUFSM Manual", Util.helpHtml_EN);
        } else {
            helpStage = newWindow(helpStage, Util.HelpFxmlLoader, Util.helpFxml,
                    "cropModelsUFSM Manual", Util.helpHtml_PT);
        }
    }



    @FXML
    private void changePaneAction (MouseEvent event)
    {
        String label = ((Button)event.getSource() ).getText();
        if (label.contains("SIMULA"))
        {
            simulation.setVisible(true);
            tables.setVisible(false);
            charts.setVisible(false);
        }
        if(label.contains("TAB")){
            simulation.setVisible(false);
            tables.setVisible(true);
            charts.setVisible(false);
        }
        if(label.contains("CHART") || label.contains("GR\u0193F")){
            simulation.setVisible(false);
            tables.setVisible(false);
            charts.setVisible(true);
        }
        if(label.contains("METEORO")){
            meteorologic.setVisible(true);
            meteorologicLegend.setVisible(true);
            development.setVisible(false);
            developmentLegend.setVisible(false);
        }
        if(label.contains("EVOLU") || label.contains("PROGRESS")){
            meteorologic.setVisible(false);
            meteorologicLegend.setVisible(false);
            development.setVisible(true);
            developmentLegend.setVisible(true);
        }
    }

    @FXML
    protected void maximizeAction (MouseEvent event)
    {
        if(stage.isMaximized()) {
            galeryViewport = new Rectangle2D((1920 - minWidth)/2, 0, minWidth, 150.0);
            upImv.setViewport(galeryViewport);
            upImv.setFitWidth(minWidth);
            downImv.setViewport(galeryViewport);
            downImv.setFitWidth(minWidth);
            video.setViewport(galeryViewport);
            video.setFitWidth(minWidth);
        }
        else {
            galeryViewport = new Rectangle2D(0, 0, maxWidth, 150.0);
            upImv.setViewport(galeryViewport);
            upImv.setFitWidth(maxWidth);
            downImv.setViewport(galeryViewport);
            downImv.setFitWidth(maxWidth);
            video.setViewport(galeryViewport);
            video.setFitWidth(maxWidth);
        }
        super.maximizeAction(event);
    }

    @FXML
    private void alertTip (MouseEvent event)
    {
        tip.setFont(Font.font("Roboto", 15));
        if(!tip.isShowing()) tip.show(stage);
        else                 tip.hide();
    }

    /***************************************************************************

                                subacoes da classe

     **************************************************************************/




    private File getMeteoroloficFile ()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("key7"));
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showOpenDialog(stage);
    }

    private static void showWarning (String warning)
    {
        warningStage = warn(warningStage, Util.WarningFxmlLoader, Util.warningFxml,
                    "cropModelsUFSM Manual", warning);
    }

    private Stage newWindow (Stage stage, FXMLLoader loader, InputStream fxml,
                            String winTitle, String url)
    {
        try {
            WebPanesFXController controller = null;
            if (stage == null) {
                Group group = (Group) loader.load(fxml);
                Scene scene = new Scene(group);
                scene.setFill(Color.TRANSPARENT);
                stage = new Stage();
                stage.getIcons().add(Util.icon);
                stage.setTitle(winTitle);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                controller = loader.getController();
                controller.postInitializeTasks(stage);
                controller.loadWebContent(url);
                stage.show();
            } else {
                if (stage.isShowing())
                    stage.hide();
                else

                    controller = loader.getController();
                    controller.postInitializeTasks(stage);
                    controller.loadWebContent(url);
                    stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    private static Stage warn (Stage stage, FXMLLoader loader, InputStream fxml,
                             String winTitle, String warn)
    {
        try {
            WarningFXController controller = null;
            if (stage == null) {
                Group group = (Group) loader.load(fxml);
                Scene scene = new Scene(group);
                scene.setFill(Color.TRANSPARENT);
                stage = new Stage();
                stage.getIcons().add(Util.icon);
                stage.setTitle(winTitle);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                controller = loader.getController();
                controller.postInitializeTasks(stage);
                controller.loadWarning(warn);
                stage.show();
            } else {
                if (stage.isShowing())
                    stage.hide();
                else
                    controller = loader.getController();
                controller.postInitializeTasks(stage);
                controller.loadWarning(warn);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public void changeChartDataVisibility (Boolean newValue, LineChart chart,
                                            String series, int index)
    {
        List<Node> nodes = new ArrayList<Node>(chart.lookupAll(series));
        List<Node> label = new ArrayList<Node>
                (chart.lookupAll(".chart-legend-item"));
        List<Node> symbol = new ArrayList<Node>
                (chart.lookupAll(".chart-legend-item-symbol"));
        if(!newValue){
            nodes.forEach(n -> n.setVisible(false));
            label.get(index).setVisible(false);
            symbol.get(index).setVisible(false);
        } else {
            nodes.forEach(n -> n.setVisible(true));
            label.get(index).setVisible(true);
            symbol.get(index).setVisible(true);
        }
    }

    protected abstract void setSimulationOnGui(VisualizableSimulation currentTaskSimulation);

    protected abstract List<String> getSimulationInput();

    private void updateSimulationMenus(String simulationName)
    {
        simulationMenuA.getItems().clear();
        if(new File(resultFolderpath).exists()) {
            for (File folder : new File(resultFolderpath).listFiles())
                if (folder.isDirectory())
                    simulationMenuA.getItems().add(folder.getName());

            System.out.println(simulationName);
            simulationMenuA.getSelectionModel().select(simulationName);
            updateYearsMenu();
        }
    }

    private void updateYearsMenu()
    {
        simulationYearA.getItems().clear();
        currentSimulation.forEach(s -> {
            simulationYearA.getItems().add(s.getParameter().get(3));
        });
        simulationYearA.getSelectionModel().select(0);
    }

    @Override
    public void acceptTask(Task validTask)
    {
        Platform.runLater(() -> {
            SerializableSimulation firstYear;
            if(validTask instanceof SimulationTask){
                currentSimulation = ((SimulationTask) validTask).getOutput();
                firstYear = currentSimulation.get(0);
                VisualizationTask visualisationTask;
                visualisationTask = newVisualizationTask(firstYear,this);
                visualisationTask.execute();
                try {
                    updateSimulationMenus(Util.generateSimulationName(firstYear.getParameter()));
                } catch (Exception e) {
                    logger.log(Level.SEVERE , e.getMessage(), e);
                }
                AnimationEvents.simulationSucess(simulationButtonCircle, okImv, playImv,
                        Color.web("#a54bff"), Color.web("#43E186"));
            }
            if(validTask instanceof VisualizationTask){
                setSimulationOnGui(((VisualizationTask) validTask).getOutput());
            }
            if(validTask instanceof MeteorologicFileTask) {
                AnimationEvents.setDataRange(Duration.seconds(0.5), 1, dataLabel,
                        dataShape, Color.web("#D6462F"), Color.web("#43E186"),
                        ((MeteorologicFileTask) validTask).getOutput());
            }
            if(validTask instanceof ExtractSimulationTask) {
                currentSimulation = ((ExtractSimulationTask) validTask).getOutput();
                updateYearsMenu();
            }
        });
    }


    @Override
    public void regectTask(Task invalidTask, String warning)
    {
        Platform.runLater(() -> showWarning(warning));
    }
}

