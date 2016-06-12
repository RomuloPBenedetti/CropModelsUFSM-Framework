package cropModelsUFSM.Control;

import cropModelsUFSM.control.GuiController;
import cropModelsUFSM.data.SerializableSimulation;
import cropModelsUFSM.data.VisualizableSimulation;
import cropModelsUFSM.task.abstractTasks.MeteorologicFileTask;
import cropModelsUFSM.task.abstractTasks.SimulationTask;
import cropModelsUFSM.task.abstractTasks.VisualizationTask;

import java.io.File;
import java.util.List;

/**
 * Created by romulo on 11/06/16.
 */
public class ConcreteGuiController extends GuiController {
    @Override
    protected void InteractionElementsInitialization() {

    }

    @Override
    protected MeteorologicFileTask newMeteorologicFileTask(File file, GuiController guiController) {
        return null;
    }

    @Override
    protected SimulationTask newSimulationTask(List<String> input, GuiController guiController) {
        return null;
    }

    @Override
    protected VisualizationTask newVisualizationTask(SerializableSimulation input, GuiController guiController) {
        return null;
    }

    @Override
    protected void Listenners() {

    }

    @Override
    protected void setSimulationOnGui(VisualizableSimulation currentTaskSimulation) {

    }

    @Override
    protected List<String> getSimulationInput() {
        return null;
    }
}
