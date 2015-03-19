import java.io.File;
import java.io.IOException;

public class Task_1_23 {
    public static void main(String[] args) throws IOException {
        Graph graph = new BaseGraph(new File("input.txt"));

        System.out.println("Old graph");
        System.out.println(graph);

        System.out.println("New graph");
        graph.addVertex(1);
        graph.addVertex(400);
        System.out.println(graph);
    }
}
