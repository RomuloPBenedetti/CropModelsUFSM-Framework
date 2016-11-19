package cropModelsUFSM.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <pre>
 * {@link Column} is a List that store both a header and data as text, an
 * universally intelligible interface. It's an sub-element of {@link Table}.
 * </pre>
 *
 * @author romulo Pulcinelli Benedetti
 * @see cropModelsUFSM
 */
public final class Column extends ArrayList<StringProperty> implements Serializable {

    /** the column header value */
    private String header = "HEADER";

    /** Create an empty column with a <b>header</b> defined
     * @param header
     */
    public Column(String header) { this.header = header; }

    /** Return the Header of this column.
     * @return a string that name this column.
     */
    public String getHeader() { return header; }

    /** Add a new {@link SimpleStringProperty} to the column.
     * @param data value stored in {@link SimpleStringProperty}.
     */
    public void add(String data) { super.add(new SimpleStringProperty(data)); }
}