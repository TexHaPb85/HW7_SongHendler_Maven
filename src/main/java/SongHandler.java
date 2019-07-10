import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SongHandler {
    private String song;
    private List<String> words;
    private List<String> exceptedWords;
    private int goodWordCount;
    private int badWordCount;
    private final String badWords[] = new String[]{"fuck", "shit",};

    public SongHandler(String song) {
        this.song = song;
    }

    public SongHandler() {
        File file = new File(getClass().getClassLoader().getResource("song.txt").getFile());
        this.song = FileWorker.readFile(file);
    }

    private boolean isBadWord(String word){
        for (String badWord : badWords) {
            if(word.contains(badWord))
                return true;
        }
        return false;
    }

    public void handle() {
        words = Stream.of(song.split("[ ,;':.)\n(!?\\\\]+")).collect(Collectors.toList());
        exceptedWords= words.stream().filter(e -> e.length() <= 2 && isBadWord(e)).collect(Collectors.toList());
        badWordCount=exceptedWords.size();
        goodWordCount=words.size()-badWordCount;
    }

    public String getSong() {
        return song;
    }
}
