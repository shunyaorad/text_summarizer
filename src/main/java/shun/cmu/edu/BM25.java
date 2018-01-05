package shun.cmu.edu;

import java.util.*;

public class BM25
{
    int D; // number of documents
    List<SyntacticUnit> syntacticUnitList; // list of syntactic unit (sentence)

    public BM25(String text)
    {
        List<String> sentenceList = Utility.splitSentence(text);
        List<SyntacticUnit> syntacticUnitList = new ArrayList<>(sentenceList.size());
        for (int i = 0; i < sentenceList.size(); i++)
        {
            SyntacticUnit synt = new SyntacticUnit(sentenceList.get(i));
            synt.index = i;
            syntacticUnitList.add(synt);
        }
        this.syntacticUnitList = syntacticUnitList;
        D = syntacticUnitList.size();
    }

    public double[] simAll(SyntacticUnit sentence)
    {
        double[] similarityArray = new double[D];
        for (int i = 0; i < D; i++)
        {
            if (sentence.index == i) continue; // similarity with itself is ignored
            similarityArray[i] = sentence.sim(syntacticUnitList.get(i));
        }
        return similarityArray;
    }

    public static void main(String[] args)
    {
        String testDoc1 = "This is first sentence to test. This is first sentence to test. This is third sentence to test";
        BM25 bm25 = new BM25(testDoc1);
        double[] scores = bm25.simAll(bm25.syntacticUnitList.get(0));
        System.out.println(Arrays.toString(scores));
    }
}
