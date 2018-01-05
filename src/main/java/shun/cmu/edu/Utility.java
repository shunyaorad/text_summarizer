package shun.cmu.edu;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.server.ExportException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static shun.cmu.edu.StopWordRemover.isStopWord;

public class Utility
{
    private static SentenceDetectorME sentenceDetector;
    private static PorterStemmer stemmer;
    private static TokenizerME tokenizer;
    private static String endRegex = "[.,:;/]$";
    private static Pattern endPattern = Pattern.compile(endRegex);


    /**
     * Split document into sentences
     * @param document
     * @return
     */
    public static List<String> splitSentence(String document)
    {
        List<String> sentences = new ArrayList<String>();
        if (document == null) return sentences;
        String[] raw_sentences = detectSentence(document);
        for (String sentence : raw_sentences)
        {
            if (sentence.length() != 0)
            {
                sentences.add(sentence);
            }

        }
        return sentences;
    }

    public static String[] detectSentence(String document)
    {
        if (sentenceDetector == null)
        {
            try
            {
                InputStream inputStream = new FileInputStream("/Users/shunyao/Documents/study/CMU/opennlp/models/en-sent.bin");
                SentenceModel model = new SentenceModel(inputStream);
                sentenceDetector = new SentenceDetectorME(model);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return sentenceDetector.sentDetect(document);
    }

    public static List<String> getTopSentenceList(String document, int size)
    {
        TextRankSummary textRankSummary = new TextRankSummary(document);
        int[] topSentence = textRankSummary.getTopSentence(size);
        List<String> resultList = new LinkedList<String>();
        return resultList;
    }

    public static List<String> splitWords(String sentence)
    {
        //TODO: tokenize based on white space
        String[] tokens = tokenizeWord(sentence);
        List<String> wordList = new ArrayList<String>();
        for (String term : tokens)
        {
            // Remove stop words, and stem words
            if (!isStopWord(term))
            {
                Matcher endMatcher = endPattern.matcher(term);
                if (endMatcher.find())
                {
                    term = term.substring(0,term.length()-1);
                }
                wordList.add(stemWord(term));
            }
        }
        return wordList;
    }

    public static String[] tokenizeWord(String text)
    {
       if (tokenizer == null)
       {
           try
           {
               InputStream inputStream = new FileInputStream("/Users/shunyao/Documents/study/CMU/opennlp/models/en-token.bin");
               TokenizerModel tokenModel = new TokenizerModel(inputStream);
               tokenizer = new TokenizerME(tokenModel);
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }
       }
       return tokenizer.tokenize(text);
    }

    public static String stemWord(String word)
    {
        if (stemmer == null)
        {
            stemmer = new PorterStemmer();
        }
        return stemmer.stem(word);
    }
}
