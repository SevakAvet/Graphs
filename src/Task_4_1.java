import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: AvetisyanSY
 * Date: 24.04.15
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class Task_4_1 {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"), true);

        Scanner s = new Scanner(System.in);
        System.out.println("Enter u and v: ");
        int u = s.nextInt();
        int v = s.nextInt();

        System.out.println("Enter L: ");
        int L = s.nextInt();

        Integer dist = g.floyd().get(new BaseGraph.Pair(u, v));
        if (dist <= L) {
            System.out.println("Distance between (u, v) <= L, dist = " + dist);
        } else {
            System.out.println("Distance between (u, v) > L, dist = " + dist);
        }
    }
}
