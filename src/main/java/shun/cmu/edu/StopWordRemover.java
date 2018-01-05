package shun.cmu.edu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class StopWordRemover
{
    private static String[] stopWords;
    private static Set<String> stopWordSet = null;

    public static void initialize()
    {
        stopWordSet = new HashSet<>();
        String stopWordFilePath = "/Users/shunyao/Documents/study/CMU/opennlp/corenlp_stopwords.txt";
        String line = null;

        try {
            FileReader fileReader = new FileReader(stopWordFilePath);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                stopWordSet.add(line.trim());
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            stopWordFilePath + "'");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isStopWord(String word)
    {
        if (stopWordSet == null)
        {
            initialize();
        }
        return (stopWordSet.contains(word.toLowerCase()));
    }

    public static void main(String[] args)
    {
        initialize();
        System.out.println(stopWordSet.contains("I've".toLowerCase()));
    }
}
