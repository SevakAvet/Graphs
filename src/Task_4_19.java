import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Севак on 25.04.2015.
 */
public class Task_4_19 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"), true);

        Set<BaseGraph.Pair> pairs = g.pairsWithPathInNegCycle();
        System.out.println(pairs);
    }
}
