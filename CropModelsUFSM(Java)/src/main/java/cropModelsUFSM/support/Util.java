package cropModelsUFSM.support;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.security.CodeSource;
import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public abstract class Util {

    public static final String s = File.separator;

    private static final List<String> fontsString =
        new ArrayList<>( Arrays.asList(new String[]{
                "fonts/Roboto-Thin.ttf",
                "fonts/Roboto-Black.ttf",
                "fonts/Roboto-Regular.ttf",
                "fonts/Roboto-Medium.ttf",
                "fonts/Roboto-Light.ttf"}
    ));
    public static List<InputStream> fonts = new ArrayList<>();

    public static final String loggingFile = "logfile.log";
    public static final Logger logger= Logger.getLogger(Util.class.getName());

    private static final String cssString ="Gui.css";
    private static final String fxmlString = "Gui.fxml";
    private static final String helpFxmlString = "WebPane.fxml";
    private static final String warningFxmlString = "Warning.fxml";
    private static final String configFxmlString = "Config.fxml";

    public static final ClassLoader loader = Util.class.getClassLoader();
    public static final FXMLLoader GuiFxmlLoader = new FXMLLoader();
    public static final FXMLLoader HelpFxmlLoader = new FXMLLoader();
    public static final FXMLLoader aboutFxmlLoader = new FXMLLoader();
    public static final FXMLLoader fxmlLoaderL = new FXMLLoader();
    public static final FXMLLoader WarningFxmlLoader = new FXMLLoader();
    public static final FXMLLoader fxmlLoaderC = new FXMLLoader();
    public static InputStream fxml, helpFxml, configFxml, warningFxml,
    legendFxml, aboutFxml, css;

    public static List<String> imagesString;
    public static List<InputStream> images = new ArrayList<>();
    private static final String iconString = "images/icons/icon.png";
    public static Image icon ;

    private static final Locale ptBR = new Locale("pt", "BR");
    private static final ResourceBundle pt = ResourceBundle.getBundle("Gui_pt", ptBR);
    public static final ResourceBundle en =  ResourceBundle.getBundle("Gui_en");
    public static ResourceBundle rb;

    public static FileHandler fileHandler;
    public static final String ioFolder = "files";
    public static final String meteorologicFile = "files"+s+"meteorologicFile.txt";
    public static final String resultFolderpath = "results";

    public static final String helpHtml_EN = "help/EN/help.xhtml";
    public static final String helpHtml_PT = "help/PT/help.xhtml";
    public static final String aboutHtml_EN = "about/EN/about.xhtml";
    public static final String aboutHtml_PT = "about/PT/about.xhtml";
    public static final String legendHtml_EN = "legend/EN/legend.xhtml";
    public static final String legendHtml_PT = "legend/PT/legend.xhtml";

    public static ZipFile zip;
    public static String currentJarPath;

    public static String winString, nixString;
    private static InputStream winIn, nixIn;

    public static UtilVirtualMethods virtualSimulationNameMethodObject;

    public static void loadRessources()
    {

        resourcesLogging();

        try{
            zip = new ZipFile(new File(currentJarPath));
            imagesString.forEach(i -> images.add(getISFromZip(i)));
            icon = new Image(getISFromZip(iconString));
            fontsString.forEach(f -> fonts.add(getISFromZip(f)));
            fxml = getISFromZip(fxmlString);
            helpFxml = getISFromZip(helpFxmlString);
            legendFxml = getISFromZip(helpFxmlString);
            aboutFxml = getISFromZip(helpFxmlString);
            warningFxml = getISFromZip(warningFxmlString);
            configFxml = getISFromZip(configFxmlString);
            css = getISFromZip(cssString);
            winIn = getISFromZip(winString);
            nixIn = getISFromZip(nixString);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally
        {
            if(zip == null)
            {
                imagesString.forEach(
                    i -> images.add(loader.getResourceAsStream(i)));
                icon = new Image(loader.getResourceAsStream(iconString));
                fontsString.forEach(f -> fonts.add(loader.getResourceAsStream(f)));
                fxml = loader.getResourceAsStream(fxmlString);
                helpFxml = loader.getResourceAsStream(helpFxmlString);
                legendFxml = loader.getResourceAsStream(helpFxmlString);
                aboutFxml = loader.getResourceAsStream(helpFxmlString);
                warningFxml = loader.getResourceAsStream(warningFxmlString);
                configFxml = loader.getResourceAsStream(configFxmlString);
                css = loader.getResourceAsStream(cssString);
                winIn = loader.getResourceAsStream(winString);
                nixIn = loader.getResourceAsStream(nixString);
            }
        }
        loadExecutables();
        loadLocale();
        loadFonts();
        loadLogger();
    }

    private static void resourcesLogging() {
        if(imagesString == null){
            logger.log(Level.SEVERE, "CropModelsUFSM -> No animation image list, " +
                    "refere to cropModelsUFSM.support.util documentation for help.");
        }
        if(currentJarPath == null){
            logger.log(Level.SEVERE, "CropModelsUFSM -> No jar path, " +
                    "refere to cropModelsUFSM.support.util documentation for help.");
        }

        if(nixString == null || winString == null){
            logger.log(Level.SEVERE, "CropModelsUFSM -> No modelExecutables, " +
                    "refere to cropModelsUFSM.support.util documentation for help.");
        }
    }

    private static void loadExecutables() {
        FileOutputStream nixOut, winOut;

        try {
            nixOut = new FileOutputStream(new File(nixString));
            winOut = new FileOutputStream(new File(winString));

            int read = 0;
            byte[] bytes = new byte[1024];

            String OS = System.getProperty("os.name").toLowerCase();
            if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
                while ((read = nixIn.read(bytes)) != -1) {
                    nixOut.write(bytes, 0, read);
                }
                Set set = new HashSet<PosixFilePermission> ();
                set.add(PosixFilePermission.OWNER_EXECUTE);
                Files.setPosixFilePermissions((new File(nixString)).toPath(), set);
            } else {
                while ((read = winIn.read(bytes)) != -1) {
                    winOut.write(bytes, 0, read);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unloadExecutables(){
        (new File (nixString)).delete();
        (new File (winString)).delete();
    }

    public static void loadLocale ()
    {
        if (Locale.getDefault().getLanguage().equals("en")) {
            GuiFxmlLoader.setResources(en); rb = en;
        }
        else { GuiFxmlLoader.setResources(pt); rb = pt; }
    }

    public static void loadFonts ()
    {
        fonts.forEach(f -> Font.loadFont(f, 15));
    }

    private static InputStream getISFromZip (String name)
    {
        try
        {
            return zip.getInputStream(zip.getEntry(name));
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private static void loadLogger() {
        try {
            fileHandler = new FileHandler(loggingFile,true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public static void unloadLogger()
    {
        fileHandler.close();
    }

    public static String[] fortranModelCommand(String parameters)
    {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            String[] linx =
                {"/bin/sh", "-c", "./" + nixString + " < " + parameters};
            return linx;
        } else {
            String[] win =
                {"cmd.exe", "/c", ".\\" + winString + " < .\\" + parameters};
            return win;
        }
    }

    public static LocalDate getDate (Long dayOfYear, Long year){
        Map<TemporalField, Long> dayOfYear_Year_date = new HashMap<>();
        dayOfYear_Year_date.put(ChronoField.DAY_OF_YEAR, dayOfYear);
        dayOfYear_Year_date.put(ChronoField.YEAR, year);
        return IsoChronology.INSTANCE.
            resolveDate(dayOfYear_Year_date, ResolverStyle.SMART);
    }

    public static LocalDate StringToDate (String date){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d MMM uuuu");
        return LocalDate.parse(date, dateFormat);
    }

    public static String dateToString (LocalDate date){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d MMM uuuu");
        return date.format(dateFormat);
    }

    public static String dateToString (Long dayOfYear, Long year){
        return dateToString(getDate(dayOfYear,year));
    }

    public static String getText (Integer key){
        return rb.getString("key"+String.valueOf(key));
    }

    public static List<String> readAfile (File file) throws IOException {
        LineNumberReader fileReader;
        fileReader = new LineNumberReader(new FileReader(file));
        List<String> lines = fileReader.lines().collect(Collectors.toList());
        fileReader.close();
        return lines;
    }

    public static void writeAfile (File file, List<String> data) throws IOException {
        FileWriter writer;
        writer = new FileWriter(file);
        for (String line : data)
            writer.write(line + "\n");
        writer.close();
    }

    public static void createFilesFolder(){
        File IOfolder = new File(Util.ioFolder);
        if (!IOfolder.exists() || !IOfolder.isDirectory())
            IOfolder.mkdir();
    }

    public static void createResultsFolder() {
        File IOfolder = new File(Util.resultFolderpath);
        if (!IOfolder.exists() || !IOfolder.isDirectory())
            IOfolder.mkdir();
    }

    public static void DeleteDirectory(File path) {
        if (path.exists()) {
            for (File d : path.listFiles()) {
                if (d.isDirectory())
                    for (File f : d.listFiles())
                        f.delete();
                d.delete();
            }
        }
    }

    public static String generateSimulationName(List<String> input) {
        return virtualSimulationNameMethodObject.generateSimulationName(input);
    }

}


