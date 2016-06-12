package cropModelsUFSM.task.abstractTasks;

import cropModelsUFSM.data.SerializableSimulation;
import cropModelsUFSM.data.VisualizableSimulation;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.TaskInterfaces.TaskObserver;

import java.util.logging.Level;

import static cropModelsUFSM.support.Util.logger;

/**
 * <pre>
 * VisualizationTask is a {@link Task} that receives a {@link SerializableSimulation} and
 * process it so it can fit in a {@link VisualizationTask} that contain the needed JavaFX
 * elements.
 * </pre>
 *
 * @author romulo Pulcinelli Benedetti
 * @see phenoglad
 */

// TODO VisualizationTask :

public abstract class VisualizationTask
        extends Task<SerializableSimulation,VisualizableSimulation> {

    public VisualizationTask(SerializableSimulation input, TaskObserver listener)
    {
        super(input, listener);
        setOutput(new VisualizableSimulation());
    }

    @Override
    public void run() {
        try {
            taskWork();
            if(!Thread.currentThread().isInterrupted()) succeeded();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    protected abstract void taskWork();
}