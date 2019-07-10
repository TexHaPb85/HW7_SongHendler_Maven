import java.util.Comparator;
import java.util.Map;


public class WordPeriodicityComparatorDesc<K,V extends Comparable<? super V>> implements Comparator<Map.Entry<K, V>>{
    @Override
    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        int res = o2.getValue().compareTo(o1.getValue());
        return res != 0 ? res : 1;//if method will return 0, sorting will delete elements with same values
    }
}
