package song.handling;

import comparators.WordPeriodicityComparatorDesc;
import utilities.FileWorker;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongHandler {
    private String song;
    private List<String> words;
    private List<String> exceptedWords;

    private int howMuchWordsShow = 5;
    private int minLengthOfWord = 3;

    public SongHandler() {
        File file = new File(getClass().getClassLoader().getResource("song.txt").getFile());
        this.song = FileWorker.readFile(file);
        handleTheSong();
    }

    public SongHandler(File file) {
        this.song = FileWorker.readFile(file);
        handleTheSong();
    }

    public SongHandler(String song) {
        this.song = song;
        handleTheSong();
    }

    private boolean isBadWord(String word) {
        return Arrays.stream(WordsWorker.BAD_WORDS).anyMatch(e -> word.toLowerCase().contains(e));
    }

    private <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> sortEntriesByValue(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(new WordPeriodicityComparatorDesc<>());
        sortedEntries.addAll(map.entrySet());

        return sortedEntries;
    }

    /**
     * This method using for abbreviated words replacing.
     * '-1' - is not a magic number, it`s a negative result
     * which returns when "REPLACE_WHAT" array doesn`t contain
     * inputWord and it is not necessary to replace it.
     *
     * @param inputWord - word which we want to replace.
     * @return index of word in "REPLACE_TO[]" array
     * which should be set instead of the given word,
     * or -1 if "REPLACE_WHAT" array does not contains
     * this word, and it doesn`t need to be replaced.
     */
    private int getIndexOfReplacing(String inputWord) {
        for (int i = 0; i < WordsWorker.REPLACE_WHAT.length; i++) {
            if (WordsWorker.REPLACE_WHAT[i].equals(inputWord))
                return i;
        }

        return -1;
    }

    private void replaceAbbreviatedWords() {
        ListIterator<String> iterator = words.listIterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            int indexOfReplacing = getIndexOfReplacing(next);
            if (indexOfReplacing != -1) {
                iterator.set(WordsWorker.REPLACE_TO[indexOfReplacing]);
            }
        }
    }

    //I`m waiting for your advices about how can i optimize it
    private void exceptUnsuitableWords() {
        exceptedWords = words.stream().filter(e -> e.length() < minLengthOfWord || isBadWord(e)).collect(Collectors.toList());
        words.removeIf(e -> isBadWord(e) || e.length() < minLengthOfWord);
    }

    private void handleTheSong() {
        words = Stream.of(song.split("[ ,;':.)\n(!?\\\\]+")).collect(Collectors.toList());
        replaceAbbreviatedWords();
        exceptUnsuitableWords();
        System.out.println("excepted words:\n" + exceptedWords + "\n");
        System.out.println("matching words:\n" + words + "\n");
    }

    //I`m waiting for your advices about how can i optimize it
    private List getOftenWords() {
        Map<String, Integer> wordPeriodicity = new TreeMap<>();
        for (String word : words) {
            if (wordPeriodicity.containsKey(word))
                wordPeriodicity.put(word, wordPeriodicity.get(word) + 1);
            else
                wordPeriodicity.put(word, 1);
        }
        System.out.println(sortEntriesByValue(wordPeriodicity));

        return sortEntriesByValue(wordPeriodicity).stream().limit(howMuchWordsShow).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Words count: " + words.size()
                + "\nexcepted words: " + exceptedWords.stream().distinct().collect(Collectors.toList())
                + "\nnum of excepted words: " + exceptedWords.size()
                + "\n5 most often words: " + getOftenWords();
    }
}
