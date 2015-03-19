import java.io.File;
import java.io.IOException;

public class Task_2_22 {
    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");
        BaseGraph g = new BaseGraph(file);

        boolean ans = false;
        int v = -1;

        for(int u : g.getVertexes()) {
            BaseGraph gCopy = new BaseGraph(file);
            gCopy.removeVertex(u);

            if(!gCopy.hasCycles() && gCopy.componentsCount() == 1) {
                ans = true;
                v = u;
                break;
            }
        }

        if(ans) {
            System.out.println("Можно получить дерево из графа G, удалив вершину " + v);
        } else {
            System.out.println("Нельзя получить дерево из графа G, удалив какую-либо вершину");
        }
    }
}
