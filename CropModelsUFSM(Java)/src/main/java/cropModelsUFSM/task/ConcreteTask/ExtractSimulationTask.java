package cropModelsUFSM.task.ConcreteTask;

import cropModelsUFSM.data.SerializableSimulation;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.TaskInterfaces.TaskObserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Level;

import static cropModelsUFSM.support.Util.*;

/**
 * <pre>
 * </pre>
 *
 * @author romulo Pulcinelli Benedetti
 * @see phenoglad
 */

public class ExtractSimulationTask extends Task<String, List<SerializableSimulation>> {

    /**
     * @param input
     * @param observer
     */
    public ExtractSimulationTask(String input, TaskObserver observer) {
        super(input, observer);
    }

    /**
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            String serializationPath = getInput() + s + "SimulationObjects.srz";
            FileInputStream fileIn = new FileInputStream(serializationPath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            setOutput((List<SerializableSimulation>) in.readObject());
            if(!Thread.currentThread().isInterrupted()) succeeded();
        } catch (IOException | ClassNotFoundException e) {
            failed("program Failed!");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

}