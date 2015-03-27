import java.io.File;
import java.io.IOException;

public class Task_2_33 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"), true);
        System.out.println(g);
        int radius = g.radius();
        System.out.println(radius == (int) 1e9 ? "Есть цикл отрицательного веса" : radius);
    }
}
