package shun.cmu.edu;

import java.util.*;

public class TextRankSummary
{

    final int max_iter = 30;
    final double minDiff = 0.001f;
    final double minSim = 0.001f;
    int D; // number of docs
    List<SyntacticUnit> syntacticUnitList; //[sentence[word]]
    TreeMap<Double, Integer> top; // top scored docs (score -> index)
    private Map<String, SyntacticUnit> nameToNode = new HashMap<>();  // look up by name of node
    private Map<SyntacticUnit, Set<SyntacticUnit>> pageEdges = new HashMap<>(); // node to other nodes
    boolean converge = false;

    /**
     * Initialize parameters and apply pagerank
     * @param docs
     */
    public TextRankSummary(String docs)
    {
        List<String> sentenceList = Utility.splitSentence(docs);
        List<SyntacticUnit> syntacticUnitList = new ArrayList<>(sentenceList.size());
        D = sentenceList.size();
        for (int i = 0; i < D; i++)
        {
            SyntacticUnit synt = new SyntacticUnit(sentenceList.get(i));
            synt.index = i;
            synt.score = 1 / D;
            syntacticUnitList.add(synt);
        }
        this.syntacticUnitList = syntacticUnitList;

        top = new TreeMap<>(Collections.reverseOrder());
        initializeGraph();
        updateScore();
    }

    /**
     * Build graph
     */
    public void initializeGraph()
    {
        // create page edges
        for (int i = 0; i < D; i++)
        {
            SyntacticUnit currSentence = syntacticUnitList.get(i);
            for (int j = 0; j < D; j++)
            {
                if (i == j) continue;
                SyntacticUnit toNode = syntacticUnitList.get(j);
                Set<SyntacticUnit> currEdges = pageEdges.get(currSentence);

                if (currEdges == null)
                {
                    currEdges = new HashSet<>();
                }

                if (currEdges.contains(toNode))
                {
                    continue;
                }

                double cosSim = currSentence.sim(toNode);

                if (cosSim > minSim)
                {
                    currEdges.add(toNode); // add edge
                    Set<SyntacticUnit> toEdges = pageEdges.get(toNode);
                    if (toEdges == null)
                    {
                        toEdges = new HashSet<>();
                    }
                    toEdges.add(currSentence); // add edge in other direction
                    pageEdges.put(currSentence, currEdges);
                    pageEdges.put(toNode, toEdges);
                }
            }
        }
    }

    /**
     * Update PageRank scores. Not checking converge.
     */
    public void updateScore()
    {
        for (int i = 0; i < max_iter; i++)
        {
            for (SyntacticUnit origin : pageEdges.keySet())
            {
                for (SyntacticUnit dest : pageEdges.get(origin))
                {
                    dest.newScore += 0.85 * (origin.score / pageEdges.get(origin).size());
                }
            }

            double maxDiff = 1.0;
            for (SyntacticUnit sentence : pageEdges.keySet())
            {
                sentence.score = sentence.newScore + 0.15 / D;
                maxDiff = Math.max(maxDiff, Math.abs(sentence.newScore - sentence.score));
                if (maxDiff < minDiff)
                {
                    converge = true;
                }
                sentence.newScore = 0;
                System.out.println(sentence.score);
            }
            if (converge)
            {
                System.out.println("Converge");
                break;
            }
        }
    }

    /**
     * Get the top scored sentences after pagerank
     * @param size
     * @return
     */
    public int[] getTopSentence(int size)
    {
        Collection<Integer> values = top.values();
        size = Math.min(size, values.size());
        int[] indexArray = new int[size];
        Iterator<Integer> it = values.iterator();
        for (int i = 0; i < size; i++)
        {
            indexArray[i] = it.next();
        }
        return indexArray;
    }

    public static void main(String[] args)
    {
        String document = "dog cat monkey. dog sky ocean. sky earth fire.";
        TextRankSummary tr = new TextRankSummary(document);

        for (SyntacticUnit sentence : tr.syntacticUnitList)
        {
            System.out.println(sentence.token.toString());
            System.out.println(sentence.score);
        }
    }
}
