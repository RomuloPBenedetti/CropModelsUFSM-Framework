package cropModelsUFSM.task.abstractTasks;

import cropModelsUFSM.data.task.SerializableSimulation;
import cropModelsUFSM.data.task.SimulationInput;
import cropModelsUFSM.data.task.VisualizableSimulation;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.taskInterfaces.TaskObserver;

/**
 *
 * VisualizationTask é uma {@link Task} que recebe uma {@link SerializableSimulation} e processa de forma a obter uma
 * {@link VisualizableSimulation} que contem os componentes visíveis tais como tabelas e gráficos, componentes JavaFX.
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public abstract class VisualizationTask extends Task<SerializableSimulation,VisualizableSimulation> {

    /**
     * Cria a tarefa de simulação, são recebidos o input e o observer da tarefa. Também é alocada uma
     * {@link java.util.Collections.SynchronizedList} segura para paralelização dos anos de simulação.
     *
     * @param input um {@link SimulationInput}, a entrada da tarefa de simulação.
     * @param observer Observador esperando {@link #failed(String)} ou {@link #succeeded()}, eventos que essa tarefa
     *                 pode gerar.
     */
    public VisualizationTask(SerializableSimulation input, TaskObserver observer)
    {
        super(input, observer);
        setOutput(new VisualizableSimulation());
    }
}