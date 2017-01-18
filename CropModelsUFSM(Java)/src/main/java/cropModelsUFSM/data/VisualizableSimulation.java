package cropModelsUFSM.data;

import javafx.scene.paint.Color;
import java.io.Serializable;
import java.util.List;

public class VisualizableSimulation implements Serializable {

    private Table simulationTable;
    private String warnText = null;
    private Boolean plantIsDead = false;

    public void setSimulationTable(List<String> headers, List<List<String>> lines)
    {
        this.simulationTable = new Table(headers, lines);
    }

    public Table getSimulationTable()
    {
        return simulationTable;
    }

    public void setPlantIsDead(Boolean plantIsDead)
    {
        this.plantIsDead = plantIsDead;
    }

    public Boolean getPlantIsDead()
    {
        return plantIsDead;
    }

    public void setWarning(String warning) {
        warnText = warning;
    }

    public String getWarnMessege ()
    {
        return warnText;
    }

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