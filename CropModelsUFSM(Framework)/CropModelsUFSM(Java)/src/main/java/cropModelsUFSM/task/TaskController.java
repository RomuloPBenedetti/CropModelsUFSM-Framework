package cropModelsUFSM.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TaskController é uma classe simples que objetiva controlar uma fila de execução para tarefas, por meio de
 * componentes já existentes na biblioteca de concorrência jáva
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public final class TaskController {

    /**
     * {@link #cores} Número de núcleos disponíveis para o pool.
     * {@link #taskExecutor} Fila de execução
     */
    private static final Integer cores =  Runtime.getRuntime().availableProcessors();
    private static final ExecutorService taskExecutor = new ThreadPoolExecutor (cores*2, cores*4, 2, TimeUnit.MINUTES,
                                                                                new LinkedBlockingQueue<>());

    /**
     * Adiciona uma nova tarefa a fila de execução para ser executada o quanto antes.
     * @param task Tarefa a ser executada.
     */
    protected static void execute (Task task)
    {
            taskExecutor.execute(task);
    }

    /**
     * Desativa a fila de execução adequadamente, terminando tarefas já em execução e rejeitando novas tarefas.
     */
    public static void finalizeTaskPool()
    {
        taskExecutor.shutdown();
    }
}
