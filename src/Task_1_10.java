import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Task_1_10 {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Graph origin = new BaseGraph(reader, false);
        Graph graph = origin.clone();

        List<Integer> even = new ArrayList<>();
        List<Integer> odd = new ArrayList<>();

        for (int u : graph.getVertexes()) {
            int cur = graph.orientedDegree(u);
            if(cur % 2 == 0) {
                even.add(u);
            } else {
                odd.add(u);
            }
        }

        for (int u : even) {
            for (int v : odd) {
                if(graph.isAdjacent(u, v)) {
                    graph.removeArc(u, v);
                }
                if(graph.isAdjacent(v, u)) {
                    graph.removeArc(v, u);
                }
            }
        }

        System.out.println(origin);
        System.out.println(graph);
    }
}
