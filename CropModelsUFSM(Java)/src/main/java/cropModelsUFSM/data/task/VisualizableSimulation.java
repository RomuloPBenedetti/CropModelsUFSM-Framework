package cropModelsUFSM.data.task;

import cropModelsUFSM.data.Table;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.List;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public class VisualizableSimulation implements Serializable {

    /**
     *
     */
    private Table simulationTable;
    private String warnText = null;
    private Boolean plantIsDead = false;

    /**
     *
     * @param headers
     * @param lines
     */
    public void setSimulationTable(List<String> headers, List<List<String>> lines)
    {
        this.simulationTable = new Table(headers, lines);
    }

    /**
     *
     * @return
     */
    public Table getSimulationTable()
    {
        return simulationTable;
    }

    /**
     *
     * @param plantIsDead
     */
    public void setPlantIsDead(Boolean plantIsDead)
    {
        this.plantIsDead = plantIsDead;
    }

    /**
     *
     * @return
     */
    public Boolean getPlantIsDead()
    {
        return plantIsDead;
    }

    /**
     *
     * @param warning
     */
    public void setWarning(String warning) {
        warnText = warning;
    }

    /**
     *
     * @return
     */
    public String getWarnMessege ()
    {
        return warnText;
    }

    /**
     *
     * @param index
     * @return
     */
    public Color getColor(int index)
    {
        if (index == 1) {
            if(plantIsDead) return Color.web("#D6462F");
            else            return Color.web("#d6bb2f");
        }
        if (index == 2){
            if(plantIsDead) return Color.web("#ff9f9f");
            else            return Color.web("#ffed8e");
        }
        return null;
    }
}