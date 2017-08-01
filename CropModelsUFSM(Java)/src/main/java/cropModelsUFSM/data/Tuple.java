package cropModelsUFSM.data;

/**
 * Created by rbenedetti on 29/07/17.
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}