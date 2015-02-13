import java.io.File;
import java.io.IOException;

public class Task_23 {
    public static void main(String[] args) throws IOException {
        Graph graph = new BaseGraph(new File("input.txt"));

        System.out.println("Old graph");
        System.out.println(graph);
    }
}
