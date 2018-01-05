package shun.cmu.edu;

import java.util.*;

import static shun.cmu.edu.StopWordRemover.isStopWord;

public class SyntacticUnit implements Comparable<SyntacticUnit>
{
    public String text; // original sentence
    public List<String> token; // list of valid words
    public int index; // position of the sentence in the document
    public double newScore;
    public double score; // pagerank score
    public HashMap<String, Integer> freqMap = new HashMap<>(); // freq map of each word in the sentence
    public int numWords = 0; // number of words in this text

    public SyntacticUnit(String text)
    {
        this.text = text;
        this.token = Utility.splitWords(text);
        this.numWords = token.size();
        this.index = -1;
        this.score = 1;
        this.newScore = 0;
        this.freqMap = getWordFreqMap(token);
    }

    /**
     * COnstruct hashmap that maps each word to its frequency in this sentence
     * @param thisSentence
     * @return
     */
    public HashMap<String, Integer> getWordFreqMap(List<String> thisSentence)
    {
        HashMap<String, Integer> freqMap = new HashMap<>();
        for (String word : thisSentence)
        {
            Integer count = freqMap.getOrDefault(word, 0) + 1;
            freqMap.put(word, count);
        }
        return freqMap;
    }

    /**
     * Get cosine similarity with other syntactic unit
     * @param other
     * @return
     */
    public double sim(SyntacticUnit other)
    {
        double denominator = Math.sqrt(this.numWords * other.numWords);
        double numerator = 0;
        for (Map.Entry<String, Integer> entry : freqMap.entrySet())
        {
            String word = entry.getKey();
            Integer count1 = entry.getValue();
            Integer count2 = other.freqMap.getOrDefault(word, 0);
            numerator += count1 * count2;
        }
        return numerator / denominator;
    }


    @Override
    public int compareTo(SyntacticUnit other)
    {
        if (other.score > this.score)
        {
            return 1;
        }
        if (other.score == this.score)
        {
            return 0;
        }
        else return -1;
    }

}
