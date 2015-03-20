import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;

public class BaseGraph implements Graph {
    private Map<Integer, Set<Integer>> G;

    private BaseGraph() {

    }

    public BaseGraph(BufferedReader reader) throws IOException {
        G = new HashMap<>();

        int n = parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            String[] splited = line.split(" ");

            int from = parseInt(splited[0]);
            G.put(from, new HashSet<Integer>());

            for (int j = 1; j < splited.length; j++) {
                int to = parseInt(splited[j]);
                addArc(from, to);
            }
        }
    }

    public BaseGraph(File file) throws IOException {
        this(new BufferedReader(new FileReader(file)));
    }

    @Override
    public void addVertex(int vertex) {
        if (!G.containsKey(vertex)) {
            G.put(vertex, new HashSet<Integer>());
        }
    }

    @Override
    public void removeVertex(int vertex) throws Exception {
        if (!G.containsKey(vertex)) {
            throw new Exception("Vertex " + vertex + " not found!");
        }

        G.remove(vertex);
        for (int u : G.keySet()) {
            if (G.get(u).contains(vertex)) {
                G.get(u).remove(vertex);
            }
        }
    }


    public Set<Integer> getVertexes() {
        return G.keySet();
    }

    @Override
    public void addArc(int from, int to) {
        if (!G.containsKey(from)) {
            G.put(from, new HashSet<Integer>());
        }

        G.get(from).add(to);
    }


    @Override
    public void addEdge(int from, int to) {
        this.addArc(from, to);
        this.addArc(to, from);
    }

    @Override
    public void removeArc(int from, int to) throws Exception {
        if (!G.containsKey(from)) {
            throw new Exception("Vertex " + from + " not found!");
        }

        if (!G.get(from).contains(to)) {
            throw new Exception("Arc " + from + " -> " + to + " not found!");
        }

        while (G.get(from).remove(to)) ;
    }

    @Override
    public void removeEdge(int from, int to) throws Exception {
        this.removeArc(from, to);
        this.removeArc(to, from);
    }

    @Override
    public boolean isAdjacent(int from, int to) throws Exception {
        if (!G.containsKey(from)) {
            throw new Exception("Vertex " + from + " not found!");
        }
        return G.get(from).contains(to);
    }

    @Override
    public int degree(int vertex) throws Exception {
        if (!G.containsKey(vertex)) {
            throw new Exception("Vertex " + vertex + " not found!");
        }

        return G.get(vertex).size() / 2;
    }

    @Override
    public int orientedDegree(int vertex) throws Exception {
        if (!G.containsKey(vertex)) {
            throw new Exception("Vertex " + vertex + " not found!");
        }

        int ans = G.get(vertex).size();
        for (int u : G.keySet()) {
            if (u == vertex)
                continue;

            if (G.containsKey(u) && G.get(u).contains(vertex)) {
                ++ans;
            }
        }

        return ans;
    }

    public boolean hasCycles() {
        for (int from : G.keySet()) {
            if (hasCycles(from)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycles(int from) {
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> used = new HashSet<>();

        q.add(from);
        while (!q.isEmpty()) {
            int u = q.poll();
            if (!G.containsKey(u)) continue;

            for (int v : G.get(u)) {
                if (!used.contains(v)) {
                    used.add(v);
                    q.add(v);
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public int radius() {
        int radius = -1;
        return radius;
    }

    public int componentsCount() {
        int ans = 0;
        Set<Integer> used = new HashSet<>();

        for (int from : G.keySet()) {
            if (!used.contains(from)) {
                Queue<Integer> q = new LinkedList<>();
                q.add(from);

                while (!q.isEmpty()) {
                    int u = q.poll();
                    if (!G.containsKey(u)) continue;

                    for (int v : G.get(u)) {
                        if (!used.contains(v)) {
                            used.add(v);
                            q.add(v);
                        }
                    }
                }
                ans++;
            }
        }

        return ans;
    }

    public BaseGraph clone() {
        BaseGraph g = new BaseGraph();

        Map<Integer, Set<Integer>> G = new HashMap<>();
        for (Integer vertex : this.G.keySet()) {
            G.put(vertex, new HashSet<Integer>());

            for (Integer to : this.G.get(vertex)) {
                G.get(vertex).add(to);
            }
        }

        g.G = G;
        return g;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(G.size()).append("\n");

        for (Integer from : G.keySet()) {
            sb.append(from).append(": ");

            for (int to : new HashSet<>(G.get(from))) {
                sb.append(to).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
