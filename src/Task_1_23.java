import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Task_1_23 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Graph graph = new BaseGraph(reader, false);

        System.out.println("Old graph");
        System.out.println(graph);

        System.out.print("Enter count of vertexes: ");
        int n = Integer.parseInt(reader.readLine());
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.print("Vertex " + (i + 1) + " = ");
            graph.addVertex(Integer.parseInt(reader.readLine()));
        }

        System.out.println("New graph");
        System.out.println(graph);
    }
}
