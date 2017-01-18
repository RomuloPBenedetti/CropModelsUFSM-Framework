package cropModelsUFSM.task.TaskInterfaces;

import cropModelsUFSM.task.Task;

public interface TaskObserver {

    void acceptTask(Task validTask);

    void regectTask(Task validTask, String warning);
    
}
