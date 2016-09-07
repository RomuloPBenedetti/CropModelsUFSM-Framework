package cropModelsUFSM.task.abstractTasks;

import cropModelsUFSM.support.Util;
import cropModelsUFSM.task.Task;
import cropModelsUFSM.task.taskInterfaces.TaskObserver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public abstract class MeteorologicFileTask extends Task<File,String> {

    /**
     * {@link #data}
     * {@link #originalHeader}
     * {@link #referenceHeader}
     * {@link #readFirstDay}
     * {@link #lastDate}
     */
    public final List<String> data;
    private final List<String> originalHeader, referenceHeader;
    private Boolean readFirstDay = false;
    private LocalDate lastDate;

    /**
     * @param input
     * @param observer
     */
    public MeteorologicFileTask(File input, TaskObserver observer)
    {
        super(input, observer);
        data = new ArrayList<>();
        originalHeader = new ArrayList<>();
        referenceHeader = new ArrayList<>();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void onExecution() throws Exception {
        readMeteorologicData();
        validateHeader();
        validateData();
        getRangeAndStore();
    }

    /**
     * @throws IOException
     */
    private void readMeteorologicData() throws IOException
    {
        data.addAll(Util.readAfile(getInput()));
        referenceHeader.addAll(Arrays.asList(Util.getText(103).split("\\s+")));
        originalHeader.addAll(Arrays.asList(data.get(0).split("\\s+")));

    }

    /**
     *
     */
    private void validateHeader()
    {
        originalHeader.forEach(item -> {
            if (!referenceHeader.contains(item))
                failed(Util.getText(79) + Util.getText(103) + Util.getText(80) + data.get(0));
        });
    }

    /**
     * @throws IOException
     */
    private void getRangeAndStore() throws IOException
    {
        String first = data.get(1);
        String last = data.get(data.size() - 1);

        long dayOfYear = Long.parseLong(first.split("\\s+")[1]);
        long year = Long.parseLong( first.split("\\s+")[0]);
        LocalDate from = Util.getDate(dayOfYear, year);

        dayOfYear = Long.parseLong(last.split("\\s+")[1]);
        year = Long.parseLong(last.split("\\s+")[0]);
        LocalDate to =Util.getDate(dayOfYear, year);

        setOutput(Util.dateToString(from)+" "+Util.getText(100)+" "+Util.dateToString(to));

        Util.createFilesFolder();
        Util.writeAfile(new File(Util.meteorologicFile), data);
    }

    /**
     *
     */
    protected abstract void validateData();
}
