package phenoglad.task.ConcreteTask;

import phenoglad.support.Util;
import phenoglad.task.Task;
import phenoglad.task.TaskInterfaces.TaskObserver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static phenoglad.support.Util.logger;
import static phenoglad.support.Util.loggingFile;

/**
 *
 */
public abstract class MeteorologicFileTask extends Task<File,String> {

    /**
     * {@link #logger}
     * {@link #data}
     * {@link #originalHeader}
     * {@link #referenceHeader}
     * {@link #readFirstDay}
     * {@link #lastDate}
     */
    private final List<String> data, originalHeader, referenceHeader;
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
     */
    @Override
    public void run() {
        try {
            readMeteorologicData();
            validateHeader();
            validateData();
            getRangeAndStore();
            if(!Thread.currentThread().isInterrupted()) succeeded();
        } catch (IOException e) {
            failed("program failure!");
            logger.log(Level.SEVERE, e.toString(), e);
        }
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
     *
     */
    protected abstract void validateData();

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
}