import java.io.File;
import java.io.IOException;

public class Task_2_19 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"));
        System.out.println(g);

        System.out.println(g.hasCycles() ? "Graph is cyclic" : "Graph is acyclic");
    }
}
