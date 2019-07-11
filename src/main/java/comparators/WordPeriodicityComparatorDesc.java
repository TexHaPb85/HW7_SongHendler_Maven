package comparators;

import java.util.Comparator;
import java.util.Map;

/**
 * WordPeriodicityComparatorDesc.
 *
 * @param <K>
 * @param <V>
 */
public class WordPeriodicityComparatorDesc<K, V extends Comparable<? super V>>
        implements Comparator<Map.Entry<K, V>> {
    /**
     * compare method implementation.
     *
     * @param o1 first map entry.
     * @param o2 second map entry.
     * @return if method will return 0, sorting will delete elements with same
     * values.
     */
    @Override
    public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
        int res = o2.getValue().compareTo(o1.getValue());
        return res != 0 ? res : 1;
    }
}
