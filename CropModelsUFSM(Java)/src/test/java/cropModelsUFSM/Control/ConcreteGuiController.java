package cropModelsUFSM.Control;

import cropModelsUFSM.control.GuiController;
import cropModelsUFSM.data.Tuple;
import cropModelsUFSM.data.task.SerializableSimulation;
import cropModelsUFSM.data.task.SimulationInput;
import cropModelsUFSM.data.task.VisualizableSimulation;
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
    protected SimulationTask newSimulationTask(SimulationInput input, GuiController guiController) {
        return null;
    }

    @Override
    protected VisualizationTask newVisualizationTask(Tuple<SerializableSimulation, List<SerializableSimulation>> input, GuiController guiController) {
        return null;
    }

    @Override
    protected void Listenners() {

    }

    @Override
    protected void setSimulationOnGui(VisualizableSimulation currentTaskSimulation) {

    }

    @Override
    protected SimulationInput getSimulationInput() {
        return null;
    }
}
