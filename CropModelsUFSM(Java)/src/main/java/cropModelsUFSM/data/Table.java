package cropModelsUFSM.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * {@link Table} is a class that well represent data Tables, a common, simple and efficient
 * data structure for meteorologic models that generally work on "n" values (columns) that
 * change over time (lines).
 *
 * The data is stored as text strings, on universal readable interface and it can be serialized
 * or written as plain text given an adequated separator.
 * </pre>
 *
 * @author romulo
 * @see phenoglad
 */
public final class Table extends ArrayList<Column> implements Serializable {

    /**
     * Create a table, where each column has a <b>header</b> and <b>lines.size()<b/>
     * length, storing all <b>lines.get()<b/> data in each column. @param headers columns names
     * , the number of headers should be equal to the number of elements per line.
     * @param lines A list of data lines, each line should contain sufficient elements so it
     *              can fill all columns.
     */
    Table(List<String> headers , List<List<String>> lines)
    {
        for(int i=0 ; i< headers.size(); i++){
            this.add(new Column(headers.get(i)));
        }
        lines.forEach(line -> {
            for(int i=0 ; i< line.size(); i++){
                this.get(i).add(line.get(i));
            }
        });
    }

    /** get an element of the table, based on the column header and line index of the the
     * element, casting it to the specific type requested. If <b>index</b> is equal to <b>
     * Integer.MAX_VALUE</b>, index will be the last line of the table.
     * @param header the header of the column.
     * @param index the line number, if value equal to <b>Integer.MAX_VALUE</b>, the last
     *              line.
     * @param aType the type to witch the string element should be cast.
     * @param <T> the type Class to witch the element, a string, should be converted.
     * @return the converted element
     */
    public <T> T get(String header, Integer index, Class<T> aType)
    {
        if(index.equals(Integer.MAX_VALUE)) index = this.get(0).size()-1;
        String value = this.stream().filter(column -> column.getHeader().equals(header)).
                findFirst().orElse(null).get(index).get();
        if(aType.equals(Integer.class))
            return (T) Integer.valueOf(Double.valueOf(value).intValue());
        if(aType.equals(Double.class))
            return (T) Double.valueOf(value);
        if(aType.equals(Long.class))
            return (T) Long.valueOf(value);
        if(aType.equals(Number.class))
            return (T) Long.valueOf(value);
        return (T) value;
    }

    /** set an element of the table, based on the column header and line index of the
     * element. If <b>index</b> is equal to <b>Integer.MAX_VALUE</b>, index will be the
     * last line of the table.
     * @param header the header of the column.
     * @param index the line number, if value equal to <b>Integer.MAX_VALUE</b>, the last
     *              line.
     * @param data The data to be stored on the requested position.
     */
    public void set(String header, Integer index, String data)
    {
        if(index.equals(Integer.MAX_VALUE)) index = this.get(0).size();
        this.stream().filter(column -> column.getHeader().equals(header)).
                findFirst().orElse(null).get(index).set(data);
    }
}