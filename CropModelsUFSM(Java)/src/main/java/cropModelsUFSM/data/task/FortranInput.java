package cropModelsUFSM.data.task;

import java.io.Serializable;
import java.util.List;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public class FortranInput implements Serializable {

    List<String> inputList;
    Integer year;

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setInputList(List<String> inputList) {
        this.inputList = inputList;
    }

    public Integer getYear() {
        return year;
    }

    public List<String> getInputList() {
        return inputList;
    }
}
