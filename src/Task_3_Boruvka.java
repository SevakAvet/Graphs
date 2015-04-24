import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: AvetisyanSY
 * Date: 10.04.15
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class Task_3_Boruvka {
    public static void main(String[] args) throws IOException {
        BaseGraph g = new BaseGraph(new File("input.txt"), true);

        System.out.println(g.MST());
        System.out.println(g.MST().size());
    }
}
