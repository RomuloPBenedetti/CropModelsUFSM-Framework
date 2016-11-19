package cropModelsUFSM.task;

import cropModelsUFSM.task.taskInterfaces.TaskObserver;

import java.util.logging.Level;

import static cropModelsUFSM.support.Util.logger;

/**
 * A classe abstrata task representa uma tarefa com <b>entrada</b> e <b>saída</b> genérica. Por implementar
 * {@link Runnable}, cada instância de task executa a tarefa em uma thread. Cada tarefa tem também um observador,
 * esperando pelo sucesso ou falha da tarefa. O observador no caso é qualquer objeto que implemente a interface
 * {@link cropModelsUFSM.task.taskInterfaces.TaskObserver}
 *
 * @param <IN> Entrada da tarefa. Após definida na instanciação da tarefa, só pode ser lida, não podendo ser
 *             reinstanciada. Nem Imutabilidade mem thread-safety são garantidas em caso de a tarefa ser dividida em
 *             mais sub-threads.
 * @param <OUT> Saída, produto esperado pelo observador caso a tarefa tenha sucesso na execução. Nem imutabilidade nem
 *              thread-safety são garantida em caso de a tarefa ser dividida em mais sub-threads.
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */

public abstract class Task<IN,OUT> implements Runnable {

    /**
     * {@link #observer} Observador, objeto interessado no sucesso ou fracasso dessa tarefa, e mais especificamente na
     *                   na saída, produto da tarefa.
     *
     * {@link #input} Entrada da tarefa. Após definida na instanciação da tarefa, só pode ser lida, não podendo ser
     *                reinstanciada, mesmo assim, nem Imutabilidade nem thread-safety são garantidas.
     *
     * {@link #output} Saída, produto esperado pelo observador caso a tarefa tenha sucesso na execução. Imutabilidade
     *                 não é garantida nem thread-safety.
     */
    private TaskObserver observer;
    private final IN input;
    private volatile OUT output;

    /**
     * Construtor da classe task
     * @param input Entrada da tarefa. Após definida na instanciação da tarefa, só pode ser lida, não podendo ser
     *              reinstanciada, mesmo assim, nem Imutabilidade nem thread-safety são garantidas.
     * @param observer Observador, objeto interessado no sucesso ou fracasso dessa tarefa, e mais especificamente na
     *                 na saída, produto da tarefa.
     */
    public Task(IN input, TaskObserver observer){
        this.input = input;
        this.observer = observer;
    }

    /**
     * Método executado pela thread da tarefa quando {@link #execute()} é chamada. Se alguma exceção é detectada, a
     * tarefa será terminada e o evento {@link #failed(String)} será enviado ao observador.
     */
    public void run(){
        try {
            beforeTask();
            onExecution();
            afterTask();
            if(!Thread.currentThread().isInterrupted())
                succeeded();
        } catch (Exception e) {
            failed("program failure, exception on execution!");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    /**
     * Retorna a saída da tarefa.
     * @return Retorna output, a saída da tarefa, pode retornar null ou saída inválida caso a tarefa não tenha sído
     *         executada ainda e não tenha sucedido.
     */
    public OUT getOutput() { return output; }

    /**
     * define um valor para o output, saída da tarefa.
     * @param output Saída, produto esperado pelo observador caso a tarefa tenha sucesso na execução. Imutabilidade
     *               não é garantida nem thread-safety.
     */
    protected void setOutput(OUT output) { this.output = output; }

    /**
     *  Quando este método é invocado, a tarefa é enviada para execução pelo {@link TaskController}, onde é executada
     *  quando possível de acordo com as condições de execução do controlador de tarefas.
     */
    public void execute() {
        TaskController.execute(this);
    }

    /**
     * Retorna a entrada da tarefa.
     * @return Retorna o input da tarefa, informações com as quais a tarefa irá trabalhar para produzir um output após
     *         sua execução
     */
    protected IN getInput() { return input; }

    /**
     * Método que deve ser invocado caso a tarefa falhe durante a execução. Como por exemplo, na ocorrencia de
     * excessões.
     * Se a thread da tarefa não tiver sido interrompida por outra falha, o observador será informado de que a tarefa
     * deve ser regeitada junto com uma mensagem de erro e a tarefa em sí. Posteriormente a tarefa será interrompida.
     * @param warning String contendo informações do motivo de falha da tarefa.
     */
    protected void failed(String warning)
    {
        if(!Thread.currentThread().isInterrupted()) {
             observer.regectTask(this, warning);
             Thread.currentThread().interrupt();
        }
    }

    /**
     * Método que deve ser invocado caso a tarefa tenha executado até o fim sem nenhum problema. O observador será
     * informado de que a tarefa pode ser aceita, enviando a tarefa em sí.
     */
    protected void succeeded() { observer.acceptTask(this); }

    /**
     * Este método não realiza nenhuma tarefa, pode ser sobrescrito para realizar algum procedimento antes de
     * {@link #onExecution()}.
     * @throws Exception qualquer excessão que possa ocorrer durante a execução da tarefa.
     */
    protected void beforeTask() throws Exception {}

    /**
     * método onde devem ser implementada a realização da tarefa.
     * @throws Exception qualquer excessão que possa ocorrer durante a execução da tarefa.
     */
    protected abstract void onExecution() throws Exception;

    /**
     * Este método não realiza nenhuma tarefa, pode ser sobrescrito para realizar algum procedimento depois de
     * {@link #onExecution()}.
     * @throws Exception qualquer excessão que possa ocorrer durante a execução da tarefa.
     */
    protected void afterTask() throws Exception {}

}
