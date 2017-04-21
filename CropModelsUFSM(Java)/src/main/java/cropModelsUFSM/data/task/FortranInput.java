package cropModelsUFSM.data.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public interface FortranInput extends Serializable {

    /**
     *
     * @return
     */
    List<String> getInputList();

    /**
     *
     * @return
     */
    String getInput();

    /**
     *
     * @return
     */
    Integer getYear();

    /**
     *
     * @return
     */
    LocalDate getPlantingDate();

    /**
     *
     * @return
     */
    Integer getCultivarType();

    /**
     *
     * @return
     */
    Integer getCultivar();
}
