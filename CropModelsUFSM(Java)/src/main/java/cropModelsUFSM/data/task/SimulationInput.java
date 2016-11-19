package cropModelsUFSM.data.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public abstract class SimulationInput implements Serializable {

    /**
     *
     */
    private Integer cultivarType, cultivarNumber, plantingYears;
    private Boolean emergence;
    private LocalDate plantingStartDate;
    private List<FortranInput> fortranInputs;

    public SimulationInput(Integer cultivarType, Integer cultivarNumber, Integer plantingYears, Boolean emergence,
                           LocalDate plantingStartDate) {
        this.cultivarType = cultivarType;
        this.cultivarNumber = cultivarNumber;
        this.plantingYears = plantingYears;
        this.emergence = emergence;
        this.plantingStartDate = plantingStartDate;
    }

    /**
     *
     */
    public abstract void generateFortranInputs();

    /**
     * @return
     */
    public Integer getCultivarType() {
        return cultivarType;
    }

    /**
     *
     * @return
     */
    public Integer getCultivarNumber() {
        return cultivarNumber;
    }

    /**
     *
     * @return
     */
    public Integer getPlantingYears() {
        return plantingYears;
    }

    /**
     *
     * @return
     */
    public Boolean getEmergence() {
        return emergence;
    }


    /**
     *
     * @return
     */
    public LocalDate getPlantingStartDate() {
        return plantingStartDate;
    }


    /**
     *
     * @return
     */
    public List<FortranInput> getFortranInputs() {
        return fortranInputs;
    }

}
