package cropModelsUFSM.task;

import cropModelsUFSM.task.TaskInterfaces.TaskObserver;

/**
 * <pre>
 * </pre>
 *
 * @param <IN>
 * @param <OUT>
 *
 * @author romulo Pulcinelli Benedetti
 * @see phenoglad
 */

public abstract class Task<IN,OUT> implements Runnable {

    private TaskObserver observer;
    private final IN input;
    private volatile OUT output;

    /**
     * @param input
     * @param observer
     */
    public Task(IN input, TaskObserver observer){
        this.input = input;
        this.observer = observer;
    }

    /**
     * @return
     */
    public OUT getOutput() { return output; }

    /**
     *
     */
    public void execute() {
        TaskController.execute(this);
    }

    /**
     * @param output
     */
    protected void setOutput(OUT output) { this.output = output; }

    /**
     * @return
     */
    protected IN getInput() { return input; }

    /**
     * @param warning
     */
    protected void failed(String warning)
    {
        if(!Thread.currentThread().isInterrupted()) {
             observer.regectTask(this, warning);
             Thread.currentThread().interrupt();
        }
    }

    /**
     *
     */
    protected void succeeded() { observer.acceptTask(this); }
}
