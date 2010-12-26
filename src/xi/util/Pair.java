package xi.util;

/**
 * A pair of arguments.
 * 
 * @author Joschi
 * 
 * @param <S>
 *            The first arguments type.
 * @param <T>
 *            The second arguments type.
 */
public class Pair<S, T> {

    public S first;

    public T second;

    public Pair(final S first, final T second) {
        this.first = first;
        this.second = second;
    }
}
