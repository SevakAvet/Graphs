import java.io.File;
import java.io.IOException;

/**
 * Created by Avetisyan Sevak
 * Date: 14.05.2015
 * Time: 23:41
 */
public class Task_5 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"), true);
        System.out.println("Max flow: " + g.maxFlow(1, 8));
    }
}
