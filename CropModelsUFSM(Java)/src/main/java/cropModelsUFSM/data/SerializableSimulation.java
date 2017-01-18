package cropModelsUFSM.data;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class SerializableSimulation implements Serializable{

    /**
     *
     */
    private List<String> result, parameter;

    /**
     * @param result
     * @param parameter
     */
    public SerializableSimulation(List<String> result, List<String> parameter)
    {
        this.result = result;
        this.parameter = parameter;
    }

    /**
     * @return
     */
    public List<String> getResult() {
        return result;
    }

    /**
     * @return
     */
    public List<String> getParameter() { return parameter; }

}
