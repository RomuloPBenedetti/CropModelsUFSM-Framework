package cropModelsUFSM.task;

import java.util.concurrent.*;

/**
 *
 */
public final class TaskController {

    /**
     *
     */
    private static final Integer cores =
        Runtime.getRuntime().availableProcessors();
    /**
     *
     */
    private static final ExecutorService taskExecutor =
        new ThreadPoolExecutor (cores*2, cores*4, 60,
        TimeUnit.MINUTES, new LinkedBlockingQueue());

    /**
     * @param task
     */
    protected static void execute (Task task)
    {
            taskExecutor.execute(task);
    }

    /**
     *
     */
    public static void finalizeTaskPool()
    {
        taskExecutor.shutdown();
    }
}
