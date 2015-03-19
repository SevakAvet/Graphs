import java.io.File;
import java.io.IOException;

public class Task_2_33 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"));
        System.out.println(g);

        g.radius();
    }
}
