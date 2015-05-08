import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by ����� on 25.04.2015.
 */
public class Task_4_19 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"), true);

        Set<BaseGraph.Pair> pairs = g.pairsWithPathInNegCycle();
        List<BaseGraph.Pair> pairsList = new ArrayList<>();
        pairsList.addAll(pairs);
        Collections.sort(pairsList, (o1, o2) -> Integer.compare(o1.x, o2.x));

        System.out.println(pairsList);
    }
}
