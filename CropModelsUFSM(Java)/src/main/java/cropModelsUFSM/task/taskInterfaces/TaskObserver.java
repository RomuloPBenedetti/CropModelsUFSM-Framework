package cropModelsUFSM.task.taskInterfaces;

import cropModelsUFSM.task.Task;

/**
 * Interface que deve ser implementada pelos observadores de {@link Task}. As tarefas informam seu estado aos
 * observadores por meio destes métodos, permitindo a execução assincrona de tarefas, sem recorrer a bloqueios.
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public interface TaskObserver {

    /**
     * Método chamado pela tarefa para informar ao observador que a execução terminou com sucesso.
     * @param thisTask Referência de sí propria que a tarefa envia ao observador.
     */
    void acceptTask(Task thisTask);

    /**
     * Método chamado pela tarefa para informar ao observador que algo não ocorreu corretamente durante a execução da
     * tarefa.
     * @param thisTask Referência de sí propria que a tarefa envia ao observador.
     * @param warning String contendo uma mensagem para informar ao observador qual foi o erro que provocou a
     *                regeição da tarefa.
     */
    void regectTask(Task thisTask, String warning);
    
}
