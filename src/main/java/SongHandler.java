import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongHandler {
    private String song;
    private List<String> words;
    private List<String> exceptedWords;
    private final String badWords[] = new String[]{"fuck", "shit",};

    public SongHandler(String song) {
        this.song = song;
    }

    public SongHandler() {
        File file = new File(getClass().getClassLoader().getResource("song.txt").getFile());
        this.song = FileWorker.readFile(file);
        handleTheSong();
    }

    private boolean isBadWord(String word){
        for (String badWord : badWords) {
            if(word.toLowerCase().contains(badWord))
                return true;
        }
        return false;
    }

    <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(new WordPeriodicityComparatorDesc<K,V>());
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public void handleTheSong() {
        words = Stream.of(song.split("[ ,;':.)\n(!?\\\\]+")).collect(Collectors.toList());
        exceptedWords= words.stream().filter(e -> e.length() <= 2 || isBadWord(e)).collect(Collectors.toList());
        words.removeIf(e->isBadWord(e) || e.length() <= 2);
        System.out.println("bad:\n"+exceptedWords+"\n");
        System.out.println("good:\n"+ words+"\n");
    }

    public List getOftenWords(int howMuchWords){
        Map<String,Integer> wordPeriodicity = new TreeMap<>();
        for (String word : words) {
            if(wordPeriodicity.containsKey(word))
                wordPeriodicity.put(word,wordPeriodicity.get(word)+1);
            else
                wordPeriodicity.put(word,1);
        }
        //System.out.println(entriesSortedByValues(wordPeriodicity));
        //I`m waiting for your advices about how can i optimize it))
        return entriesSortedByValues(wordPeriodicity).stream().limit(howMuchWords).collect(Collectors.toList());
    }

    public String getSong() {
        return song;
    }

    @Override
    public String toString() {
        return "Words count: "+words.size()
                +"\nexcepted words: "+ exceptedWords.stream().distinct().collect(Collectors.toList())
                +"\nnum of excepted words: "+exceptedWords.size()
                +"\n5 most often words: "+getOftenWords(5);
    }
}
