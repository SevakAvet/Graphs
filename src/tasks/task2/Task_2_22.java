package tasks.task2;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Task_2_22 {
    public static void main(String[] args) throws Exception {
        File file = new File("input.txt");
        BaseGraph g = new BaseGraph(file, false);

        boolean ans = false;
        int v = -1;

        System.out.println(g);

        for(int u : g.getVertexes()) {
            Set<Integer> used = new HashSet<>();
            used.add(u);

            int vertexesCount = g.getVertexes().size() - 1;
            int edgesCount = g.edgesCount(used);

            if(edgesCount == vertexesCount - 1 && g.componentsCount(used) == 1) {
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
