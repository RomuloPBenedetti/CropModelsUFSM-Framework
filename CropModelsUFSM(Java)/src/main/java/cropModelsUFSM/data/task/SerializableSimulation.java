package cropModelsUFSM.data.task;

import java.io.Serializable;
import java.util.List;

/**
 *  classe que representa uma simulação serializável. A simulação consiste da simulação de uma safra ou ciclo (tamanho
 *  máximo de um ano), podendo fazer parte de um conjunto de diversas simulações (uma simulação envolvendo
 *  multiplos anos).
 *
 *  A simulação armazena o resultado bruto da simulação e os inputs da tarefa de simulação {@link SimulationInput}.
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public class SerializableSimulation implements Serializable{

    /**
     * year - ano da simulação.
     * result - resultado produzido pelo modelo fortran.
     * simulationInput - entrada da tarefa de simulação que produziou esta simulação.
     */
    private final Integer year;
    private final List<String> result;
    private final SimulationInput simulationInput;

    /**
     * Construtor da classe.
     * @param result resultado produzido pelo modelo fortran, uma lista de linhas obtidas do arquivo de resultado.
     * @param simulationInputs entrada da tarefa de simulação, representa as entradas inseridas na interface, junto do
     *                         conjunto de inputs para o modelo em fortran, que pode ser mais de um caso a tarefa de
     *                         simulação envolva múltiplos anos, a dependender do valor de
     *                         {@link SimulationInput#getPlantingYears()}
     */
    public SerializableSimulation(List<String> result, SimulationInput simulationInputs, Integer year)
    {
        this.result = result;
        this.simulationInput = simulationInputs;
        this.year = year;
    }

    /**
     * Retorna o resultado produzido pelo modelo fortran
     * @return resultado produzido pelo modelo fortran, uma lista de linhas obtidas do arquivo de resultado.
     */
    public List<String> getResult() {
        return result;
    }

    /**
     * Retorna a Entrada da tarefa de simulação que produziu esta simulação.
     * @return entrada da tarefa de simulação, representa as entradas inseridas na interface, junto do conjunto de
     *         inputs para o modelo em fortran, que pode ser mais de um caso a tarefa de simulação envolva múltiplos
     *         anos, a dependender do valor de {@link SimulationInput#getPlantingYears()}.
     */
    public SimulationInput getSimulationInput() { return simulationInput; }

    /**
     * Retorna o ano desta simulação.
     * @return ano desta simulação em particular.
     */
    public Integer getYear() {
        return year;
    }
}
