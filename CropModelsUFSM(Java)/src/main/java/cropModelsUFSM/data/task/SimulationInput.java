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
    private Integer cultivarType, cultivarNumber, plantingYears, cropPoint;
    private Boolean emergence;
    private LocalDate plantingStartDate, MinimalDate, maximumDate;
    private List<FortranInput> fortranInputs;

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
    public Integer getCropPoint() { return cropPoint; }

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
    public LocalDate getMaximumDate() { return maximumDate; }

    /**
     *
     * @return
     */
    public LocalDate getMinimalDate() { return MinimalDate; }

    /**
     *
     * @return
     */
    public List<FortranInput> getFortranInputs() {
        return fortranInputs;
    }

    /**
     *
     * @return
     */
    public void setCropPoint(Integer cropPoint) { this.cropPoint = cropPoint; }

    /**
     *
     * @return
     */
    public void setCultivarNumber(Integer cultivarNumber) { this.cultivarNumber = cultivarNumber; }

    /**
     *
     * @return
     */
    public void setCultivarType(Integer cultivarType) { this.cultivarType = cultivarType; }

    /**
     *
     * @return
     */
    public void setEmergence(Boolean emergence) { this.emergence = emergence; }

    /**
     *
     * @return
     */
    public void setFortranInputs(List<FortranInput> fortranInputs) { this.fortranInputs = fortranInputs; }

    /**
     *
     * @return
     */
    public void setMaximumDate(LocalDate maximumDate) { this.maximumDate = maximumDate; }

    /**
     *
     * @return
     */
    public void setMinimalDate(LocalDate minimalDate) { MinimalDate = minimalDate; }

    /**
     *
     * @return
     */
    public void setPlantingStartDate(LocalDate plantingStartDate) { this.plantingStartDate = plantingStartDate; }

    /**
     *
     * @return
     */
    public void setPlantingYears(Integer plantingYears) { this.plantingYears = plantingYears; }
}
