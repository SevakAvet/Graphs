import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;

public class BaseGraph 
        implements Graph {
    private Set<Integer> vertexes;
    private Map<Integer, List<Integer>> G;

    public Graph clone() {
        BaseGraph g = new BaseGraph();

        Set<Integer> vertexes = new HashSet<>();
        for (Integer vertex : this.vertexes) {
            vertexes.add(vertex);
        }

        Map<Integer, List<Integer>> G = new HashMap<>();
        for (Integer vertex : this.G.keySet()) {
            G.put(vertex, new ArrayList<Integer>());

            for (Integer to : this.G.get(vertex)) {
                G.get(vertex).add(to);
            }
        }

        g.vertexes =  vertexes;
        g.G = G;

        return g;
    }

    @Override
    public void addVertex(int vertex) {
        if (!vertexes.contains(vertex)) {
            vertexes.add(vertex);
            G.put(vertex, new ArrayList<Integer>());
        } else {

        }
    }

    @Override
    public void removeVertex(int vertex) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Integer> getVertexes() {
        return this.vertexes;
    }

    private BaseGraph() {

    }

    public BaseGraph(BufferedReader reader) throws IOException {
        G = new HashMap<>();
        vertexes = new HashSet<>();

        int n = parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            String[] splited = line.split(" ");

            int from = parseInt(splited[0]);
            vertexes.add(from);
            G.put(from, new ArrayList<Integer>());

            for (int j = 1; j < splited.length; j++) {
                int to = parseInt(splited[j]);
                addArc(from, to);
            }
        }
    }

    @Override
    public boolean isAdjacent(int from, int to) throws Exception {
        if (!G.containsKey(from)) {
            throw new Exception("Vertex " + from + " not found!");
        }

        if (!vertexes.contains(to)) {
            throw new Exception("Vertex " + to + " not found!");
        }

        return G.get(from).contains(to);
    }

    @Override
    public List<Integer> getList(int vertex) throws Exception {
        if (!G.containsKey(vertex)) {
            throw new Exception("Vertex " + vertex + " not found!");
        }

        return G.get(vertex);
    }

    @Override
    public void addArc(int from, int to) {
        if (!G.containsKey(from)) {
            G.put(from, new ArrayList<Integer>());
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

        if (!vertexes.contains(to)) {
            throw new Exception("Vertex " + to + " not found!");
        }

        if (!G.get(from).contains(to)) {
            throw new Exception("Arc " + from + " -> " + to + " not found!");
        }

        while (G.get(from).remove((Integer) to)) ;
    }

    @Override
    public void removeEdge(int from, int to) throws Exception {
        this.removeArc(from, to);
        this.removeArc(to, from);
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
        for (int u : vertexes) {
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

    public int radius() {
        int radius = -1;

        int n = G.keySet().size();
        int INF = (int) 1e9;
        List<Integer> vertexList = new ArrayList<>(G.keySet());
        Map<Pair, Integer> d = new HashMap<>();

        for(int u : vertexList) {
            for(int v : vertexList) {
                if(G.containsKey(u) && G.get(u).contains(v) ||
                   G.containsKey(v) && G.get(v).contains(u)) {
                    Pair p1 = new Pair(u, v);
                    Pair p2 = new Pair(v, u);

                    d.put(p1, 1);
                }

            }
        }



        return radius;
    }

    private class Pair {
        int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (x != pair.x) return false;
            if (y != pair.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
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
