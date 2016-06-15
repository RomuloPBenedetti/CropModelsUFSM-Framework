package cropModelsUFSM.task.abstractTasks;

import cropModelsUFSM.data.SerializableSimulation;
import cropModelsUFSM.support.Util;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.TaskInterfaces.TaskObserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import static cropModelsUFSM.support.Util.*;

/**
 * <pre>
 * SimulationTask Is a {@link Task} that will execute a simulation. It receives a
 * {@link java.awt.List<String>} as {@link #input} and produce an
 * {@link java.awt.List<SerializableSimulation>} as {@link #output}.
 *
 * It will parallelize the simulation if {@link #modelInputList()} return a list bigger than
 * one. Finally, the result will be categorized, serialized and stored on <b>results\%FOLDER%</b>
 * at the program's root directory, the <b>%FOLDER%</b> is generated with the simulation input,
 * by {@link Util#generateSimulationName(List)}.

 * You should do the adequate input validation
 * </pre>
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */

// TODO SimulationTask :
// 1. validate input exptensively, each input limit, ensure even cast type validation. Remember
//    exceptions are for #NON EXPECTED FAILURES#, not expected failures...
// 2. translate model output file header, acordingly with the locale
// 3. do some more extensive exception treatment
// 4. Do more extensive logging

public abstract class SimulationTask extends Task<List<String>, List<SerializableSimulation>> {

    private volatile List<String> exceptionalR2 = Collections.synchronizedList(new ArrayList<>());

    /**
     * Create a simulation task, adding its input and binding an observer to it.
     * @param input an {@link java.awt.List<String>}, the simulation task input.
     * @param observer Object observing the {@link #failed(String)} or {@link #succeeded()}
     *                 events of this task.
     */
    public SimulationTask(List<String> input, TaskObserver observer)
    {
        super(input, observer);
        setOutput(Collections.synchronizedList(new ArrayList<>()));
    }

    /**
     * Method executed by the task thread when {@link #execute()} is called. If any  exception
     * is detected the task will be terminated and will not {@link #succeeded()}.
     */
    @Override
    public void run()
    {
        try {
            validateInput();
            modelInputList().parallelStream().forEach(simulationYearInput -> {
                try {
                    executeModel(simulationYearInput);
                } catch (IOException | InterruptedException e) {
                    failed("program failure!");
                    logger.log(Level.SEVERE, e.toString(), e);
                }
            });
            orderOutput();
            storeSimulation();
            afterTask();
            if(!Thread.currentThread().isInterrupted()) succeeded();
        } catch (Exception e) {
            failed("program failure!");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    protected abstract void afterTask();

    /**
     * Write the fortran model commandline input on <b>parameters.txt</b> in
     * {@link Util#ioFolder} executing the model with it's input redirected to it, reading and
     * storing the result in a {@link SerializableSimulation}.
     *
     * @param simulationYearInput A list of input lists compatible with the current used
     *                            <b>mathematic model</b> generated with base on the <b>number
     *                            of years to simulate</b>.
     * @throws IOException If can't create or access needed file resources.
     * @throws InterruptedException if the current thread is interrupted by another thread
     *                              while it is waiting, then the wait is ended and an
     *                              InterruptedException is thrown.
     */
    private void executeModel (List<String> simulationYearInput)
            throws IOException, InterruptedException
    {
        String year = getExecutionYear(simulationYearInput);
        String inputPathName = ioFolder + s + year + "parameters.txt";
        String outputPathName = ioFolder + s + year + "result.txt";
        Util.writeAfile(new File(inputPathName),simulationYearInput);

        String[] command = Util.fortranModelCommand(inputPathName);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        List<String> result = Util.readAfile(new File(outputPathName));
        result.set(0, Util.getText(104));

        getOutput().add(new SerializableSimulation(result, executionNewInputList(getInput())));
    }

    private void orderOutput()
    {
        Comparator<SerializableSimulation> simulationComparator =
                (first, second) ->
                        first.getParameter().get(ParameterComparatorIndex()).
                        compareTo(second.getParameter().get(ParameterComparatorIndex()));
        Collections.sort(getOutput(), simulationComparator);
    }


    /**
     * Create directory structure to organize and store simulation, moving result, parameters
     * an serialized simulation.
     * @throws Exception if it cant access resources to serialize the simulation data.
     */
    private void storeSimulation() throws Exception {
        String simulationName = Util.generateSimulationName(getInput());
        String simulationPathString = resultFolderpath + s + simulationName;
        File simulationPath = new File(simulationPathString);

        Util.createResultsFolder();
        Util.DeleteDirectory(simulationPath);
        simulationPath.mkdir();

        for (File f : new File(ioFolder).listFiles()) {
            if(f.getName().contains("result"))
                f.renameTo(new File(simulationPath.getPath() + s + f.getName()));
            if(f.getName().contains("parameters"))
                f.renameTo(new File(simulationPath.getPath() + s + f.getName()));
        }

        serializeSimulation(simulationPathString);
    }

    /** Serialize the task {@link #output}, a {@link java.awt.List<SerializableSimulation>}
     * and store it on the given simulation path.
     * @param path Path where the serialized simulation should be stored.
     * @throws Exception If it cant find the path, create the serialized object file, or if the
     * resource is blocked.
     */
    private void serializeSimulation(String path) throws Exception {
        FileOutputStream fileOut;
        fileOut = new FileOutputStream(path + s +"SimulationObjects.srz");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(getOutput());
        out.close();
    }

    /**
     * Validate conditions to run tasks, if there exist a meteorologic file to execute a
     * simulation and if the user selected necessary elements and filled field correctly
     * inside data range limitations.
     */
    protected abstract void validateInput();

    /**
     * Generate a list of inputs compatible with the current used <b>mathematic model interface
     * </b> based on the <b>number of simulated years</b>.
     *
     * @return A list of input lists compatible with the current used <b>mathematic model</b>
     *         generated with base on the <b>number of years to simulate</b>.
     */
    protected abstract List<List<String>> modelInputList();

    protected abstract String getExecutionYear(List<String> simulationYearInput);
    protected abstract List<String> executionNewInputList(List<String> simulationYearInput);
    protected abstract Integer ParameterComparatorIndex();

}
