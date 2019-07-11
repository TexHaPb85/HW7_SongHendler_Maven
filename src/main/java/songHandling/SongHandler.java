package songHandling;

import comparators.WordPeriodicityComparatorDesc;
import util.FileWorker;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongHandler {
    private String song;
    private List<String> words;
    private List<String> exceptedWords;
    private Set<String> badWords = new HashSet<>();

    public SongHandler() {
        badWords = new HashSet<>();
        Collections.addAll(badWords, "fuck", "shit");
        File file = new File(getClass().getClassLoader().getResource("song.txt").getFile());
        this.song = FileWorker.readFile(file);
        handleTheSong();
    }

    private boolean isBadWord(String word) {
        for (String badWord : badWords) {
            if (word.toLowerCase().contains(badWord))
                return true;
        }
        return false;
    }

    private <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> sortEntriesByValue(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new WordPeriodicityComparatorDesc<K, V>());
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    private int arrayContains(String[] array, String containElement) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(containElement))
                return i;
        }
        return -1;
    }

    private void replaceAbbreviatedWords() {
        ListIterator<String> iterator = words.listIterator();
        String replaceWhat[] = new String[]{"ll", "m", "t", "re"};
        String replaceTo[] = new String[]{"will", "am", "it", "are"};
        while (iterator.hasNext()) {
            String next = iterator.next();
            int indexOfReplacing = arrayContains(replaceWhat, next);
            if (indexOfReplacing != -1) {
                iterator.set(replaceTo[indexOfReplacing]);
            }
        }
    }

    private void exceptUnsuitableWords() {
        exceptedWords = words.stream().filter(e -> e.length() <= 2 || isBadWord(e)).collect(Collectors.toList());
        words.removeIf(e -> isBadWord(e) || e.length() <= 2);
    }

    public void handleTheSong() {
        words = Stream.of(song.split("[ ,;':.)\n(!?\\\\]+")).collect(Collectors.toList());
        //words.replaceAll(String::toLowerCase);
        replaceAbbreviatedWords();
        exceptUnsuitableWords();
        System.out.println("excepted words:\n" + exceptedWords + "\n");
        System.out.println("matching words:\n" + words + "\n");
    }

    public List getOftenWords(int howMuchWords) {
        Map<String, Integer> wordPeriodicity = new TreeMap<>();
        for (String word : words) {
            if (wordPeriodicity.containsKey(word))
                wordPeriodicity.put(word, wordPeriodicity.get(word) + 1);
            else
                wordPeriodicity.put(word, 1);
        }
        System.out.println(sortEntriesByValue(wordPeriodicity));
        //I`m waiting for your advices about how can i optimize it all))
        return sortEntriesByValue(wordPeriodicity).stream().limit(howMuchWords).collect(Collectors.toList());
    }

    public String getSong() {
        return song;
    }

    @Override
    public String toString() {
        return "Words count: " + words.size()
                + "\nexcepted words: " + exceptedWords.stream().distinct().collect(Collectors.toList())
                + "\nnum of excepted words: " + exceptedWords.size()
                + "\n5 most often words: " + getOftenWords(5);
    }
}
