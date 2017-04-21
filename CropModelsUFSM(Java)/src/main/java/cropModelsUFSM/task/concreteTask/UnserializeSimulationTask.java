package cropModelsUFSM.task.concreteTask;

import cropModelsUFSM.data.task.SerializableSimulation;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.taskInterfaces.TaskObserver;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import static cropModelsUFSM.support.Util.s;

/**
 * {@link UnserializeSimulationTask}
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */

public class UnserializeSimulationTask extends Task<String, List<SerializableSimulation>> {

    /**
     * @param input
     * @param observer
     */
    public UnserializeSimulationTask(String input, TaskObserver observer) {
        super(input, observer);
    }

    /**
     *
     */
    @Override
    protected void onExecution() throws Exception {
        String serializationPath = getInput() + s + "SimulationObjects.srz";
        FileInputStream fileIn = new FileInputStream(serializationPath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        setOutput((List<SerializableSimulation>) in.readObject());
    }

}