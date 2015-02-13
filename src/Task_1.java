import java.io.File;

public class Task_1 {
    public static void main(String[] args) throws Exception {
        Graph graph = new BaseGraph(new File("input.txt"));
        System.out.println(graph);

        System.out.println("Vertexes degrees:");
        for (int vertex : graph.getVertexes()) {
            System.out.println("Vertex " + vertex + ": " + graph.degree(vertex));
        }
    }
}
