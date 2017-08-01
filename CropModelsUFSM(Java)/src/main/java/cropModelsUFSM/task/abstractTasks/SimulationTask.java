package cropModelsUFSM.task.abstractTasks;

import cropModelsUFSM.data.task.FortranInput;
import cropModelsUFSM.data.task.SerializableSimulation;
import cropModelsUFSM.data.task.SimulationInput;
import cropModelsUFSM.support.Util;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.taskInterfaces.TaskObserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

import static cropModelsUFSM.support.Util.*;

/**
 * SimulationTask é uma {@link Task} especializada na execução de simulações para modelos dentro dos padrões
 * que atualmente estão definidos em {@link SimulationInput}. Você deve fazer a adequada validação da entrada no
 * método {@link #validateInput()} a fim de garantir uma simulação adequada de acordo com as necessidades do modelo.
 * <p>
 * Esta tarefa recebe {@link SimulationInput} como ({@link #input}) e produz uma
 * {@link java.awt.List<SerializableSimulation>} como saída ({@link #output}).
 * <p>
 * Esta classe ira paralelizar a simulação se {@link SimulationInput#getFortranInputs()} retornar uma lista maior que
 * um, resultados serão ordenados pelos anos, armazenados em uma pasta <b>results\%FOLDER%</b> no diretório raiz do
 * programa. A pasta <b>%FOLDER%</b> é gerada com base na entrada da simulação ({@link #input}) pelo método
 * {@link Util#generateSimulationName(SimulationInput)}.
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public abstract class SimulationTask extends Task<SimulationInput, List<SerializableSimulation>> {

    /**
     * Cria a tarefa de simulação, são recebidos o input e o observer da tarefa. Também é alocada uma
     * {@link java.util.Collections.SynchronizedList} segura para paralelização dos anos de simulação.
     *
     * @param input    um {@link SimulationInput}, a entrada da tarefa de simulação.
     * @param observer Observador esperando {@link #failed(String)} ou {@link #succeeded()}, eventos que essa tarefa
     *                 pode gerar.
     */
    public SimulationTask(SimulationInput input, TaskObserver observer) {
        super(input, observer);
        setOutput(Collections.synchronizedList(new LinkedList<>()));
    }

    /**
     * metodo chamado por {@link Task#run()} durante a execução para realizar a tarefa. caso
     * {@link SimulationInput#getFortranInputs()} contenha mais de um imput, a tarefa é paralelizada em um stream.
     */
    @Override
    protected void onExecution() throws Exception {
        validateInput();
        if(!Thread.currentThread().isInterrupted()) {
            getInput().getFortranInputs().parallelStream().forEach(fortranInput -> {
                try {
                    executeModel(fortranInput);
                } catch (IOException | InterruptedException e) {
                    failed("Severe failure, interruption in model execution.");
                    logger.log(Level.SEVERE, e.toString(), e);
                }
            });
            orderOutput();
            storeSimulation();
        }
    }

    /**
     * Escreve os parametros no arquivo <b>parameters.txt</b> na pasta {@link Util#ioFolder}, executando o modelo com
     * a entrada redirecionada para este arquivo, posteriormente lendo os resultados do modelo para um
     * {@link SerializableSimulation}.
     *
     * @param fortranInput entrada para um modelo matemático fortran.
     * @throws IOException          caso algum dos dos arquivos necessários não possa ser criado/aberto.
     * @throws InterruptedException caso a execução do modelo seja interrompida durante a espera pela execução.
     */
    private void executeModel(FortranInput fortranInput)
            throws IOException, InterruptedException {
        Integer year = fortranInput.getYear();
        String inputPathName = ioFolder + s + year + "parameters.txt";
        String outputPathName = ioFolder + s + year + "result.txt";
        Util.writeAfile(new File(inputPathName), fortranInput.getInputList());

        String[] command = Util.fortranModelCommand(inputPathName);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        String line;
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while((line = error.readLine()) != null){
            System.out.println(line);
        }
        error.close();

        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while((line=input.readLine()) != null){
            System.out.println(line);
        }

        input.close();

        OutputStream outputStream = process.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println();
        printStream.flush();
        printStream.close();

        List<String> result = Util.readAfile(new File(outputPathName));
        result.set(0, Util.getText(104));

        getOutput().add(new SerializableSimulation(result, getInput(), year));
    }

    /**
     * Ordena as {@link SerializableSimulation} de acordo com {@link SerializableSimulation#getYear()}.
     */
    private void orderOutput() {
        Comparator<SerializableSimulation> simulationComparator =
                (first, second) -> first.getYear().compareTo(second.getYear());
        (getOutput()).sort(simulationComparator);
    }

    /**
     * Cria a hierarquia de diretórios para armazenar e organizar as simulações:
     * 1. Movendo resultados e parametros em texto das pastas convencionais do modelo Fortran.
     * 2. Traduzindo o cabeçalho do arquivo de resultados a depender do locale.
     * 3. chama {@link #serializeSimulation(String)} para armazenar uma versão serializada da simulação, com o objetivo
     *    de tornar a navegação entre as simulações mais rápida ao permitir que os dados sejam passados diretamente para
     *    a tarefa @{@link VisualizationTask}.
     *
     * @throws Exception se não conseguir mover os arquivos ou criar o arquivo de serialização.
     */
    private void storeSimulation() throws Exception {
        String simulationName = Util.generateSimulationName(getInput());
        String simulationPathString = resultFolderpath + s + simulationName;
        File simulationPath = new File(simulationPathString);

        Util.createResultsFolder();
        Util.DeleteDirectory(simulationPath);
        simulationPath.mkdir();

        for (File f : new File(ioFolder).listFiles()) {
            if (f.getName().contains("result")) {
                Path newPath = new File(simulationPath.getPath() + s + f.getName()).toPath();
                Files.move(f.toPath(), newPath);
                List<String> fileContent = new ArrayList<>(Files.readAllLines(newPath));
                fileContent.set(0, Util.getText(104));
                Files.write(newPath, fileContent);
            }
            if (f.getName().contains("parameters"))
                f.renameTo(new File(simulationPath.getPath() + s + f.getName()));
        }

        serializeSimulation(simulationPathString);
    }

    /**
     * Serializa a saida da simulação {@link #output}, a {@link java.awt.List<SerializableSimulation>}
     * e armazena no caminho dado.
     *
     * @param path Caminho onde a simulação serializada deve ser armazenada.
     * @throws Exception se não conseguir encontrar o caminho ou criar o arquivo de serialização, ou ainda se algum
     *                   destes recursos estiver bloqueado.
     */
    private void serializeSimulation(String path) throws Exception {
        FileOutputStream fileOut;
        fileOut = new FileOutputStream(path + s + "SimulationObjects.srz");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(getOutput());
        out.close();
    }

    /**
     * Valida a entrada do modelo, verifica a existência de um arquivo meteorológico de entrada e realiza algumas
     * validações básicas. É recomendável complementar este método.
     */
    protected void validateInput() {
        if (!(new File(Util.meteorologicFile)).exists()) {
            failed(Util.getText(90));
            logger.log(Level.WARNING, Util.getText(90));
        }
    }
}
