import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Task_1_1 {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Graph graph = new BaseGraph(reader, false);
        System.out.println(graph);

        System.out.println("Vertexes degrees:");
        for (int vertex : graph.getVertexes()) {
            System.out.println("Vertex " + vertex + ": " + graph.degree(vertex));
        }
    }
}
