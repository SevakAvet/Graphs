import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;

public class BaseGraph implements Graph {
    private static final int INF = (int) 1e9;
    private Map<Integer, Set<Integer>> G;
    private Set<Integer> vertexes;
    private Map<Pair, Integer> GW;
    private boolean isWeighted;

    private BaseGraph() {
    }

    public BaseGraph(BufferedReader reader, boolean isWeighted) throws IOException {
        this.isWeighted = isWeighted;
        vertexes = new HashSet<>();
        G = new HashMap<>();
        GW = new HashMap<>();

        if (isWeighted) {
            int n = parseInt(reader.readLine());
            for (int i = 0; i < n; i++) {
                String line = reader.readLine();
                String[] splited = line.split(" ");

                int from = parseInt(splited[0]);
                vertexes.add(from);

                for (int j = 1; j < splited.length; j += 2) {
                    int to = parseInt(splited[j]);
                    int w = parseInt(splited[j + 1]);

                    GW.put(new Pair(from, to), w);
                    vertexes.add(to);
                    addArc(from, to);
                }
            }
        } else {
            int n = parseInt(reader.readLine());
            for (int i = 0; i < n; i++) {
                String line = reader.readLine();
                String[] splited = line.split(" ");

                int from = parseInt(splited[0]);
                vertexes.add(from);
                G.put(from, new HashSet<>());

                for (int j = 1; j < splited.length; j++) {
                    int to = parseInt(splited[j]);
                    vertexes.add(to);
                    addArc(from, to);
                }
            }
        }

    }

    public BaseGraph(File file, boolean isWeighted) throws IOException {
        this(new BufferedReader(new FileReader(file)), isWeighted);
    }

    @Override
    public void addVertex(int vertex) {
        if (!G.containsKey(vertex)) {
            G.put(vertex, new HashSet<>());
            vertexes.add(vertex);
        }
    }

    @Override
    public void removeVertex(int vertex) throws Exception {
        if (!G.containsKey(vertex)) {
            throw new Exception("Vertex " + vertex + " not found!");
        }

        G.remove(vertex);
        vertexes.remove(vertex);

        for (int u : G.keySet()) {
            if (G.get(u).contains(vertex)) {
                G.get(u).remove(vertex);
            }
        }
    }


    public Set<Integer> getVertexes() {
        return vertexes;
    }

    @Override
    public void addArc(int from, int to) {
        if (!G.containsKey(from)) {
            G.put(from, new HashSet<>());
        }

        vertexes.add(from);
        vertexes.add(to);
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

        vertexes.remove(from);
        vertexes.remove(to);
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
        Map<Integer, Integer> colors = new HashMap<>();

        for (int from : G.keySet()) {
            if (hasCycles(colors, from)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycles(Map<Integer, Integer> colors, int from) {
        colors.put(from, 1);

        for (int to : G.get(from)) {
            if (!colors.containsKey(to)) {
                colors.put(to, 0);
            }

            if (colors.get(to) == 0) {
                if (hasCycles(colors, to)) {
                    return true;
                }
            } else if (colors.get(to) == 1) {
                return true;
            }
        }

        colors.put(from, 2);
        return false;
    }

    public int radius() {
        int INF = (int) 1e9;
        Map<Pair, Integer> d = new HashMap<>();

        for (int u : vertexes) {
            for (int v : vertexes) {
                Pair uu = new Pair(u, u);
                Pair vv = new Pair(v, v);
                Pair uv = new Pair(u, v);

                d.put(uu, 0);
                d.put(vv, 0);

                if (GW.containsKey(uu) && GW.get(uu) < 0) {
                    d.put(uu, GW.get(uu));
                }

                if (GW.containsKey(vv) && GW.get(vv) < 0) {
                    d.put(vv, GW.get(vv));
                }

                if (GW.containsKey(uv)) {
                    d.put(uv, GW.get(uv));
                }
            }
        }

        for (int k : vertexes) {
            for (int i : vertexes) {
                for (int j : vertexes) {
                    Pair p1 = new Pair(i, j);
                    Pair p2 = new Pair(i, k);
                    Pair p3 = new Pair(k, j);

                    d.putIfAbsent(p1, INF);
                    d.putIfAbsent(p2, INF);
                    d.putIfAbsent(p3, INF);

                    if (d.get(p2) < INF / 2 && d.get(p3) < INF / 2 && d.get(p1) > d.get(p2) + d.get(p3)) {
                        d.put(p1, d.get(p2) + d.get(p3));
                    }
                }
            }
        }


        System.out.println(d);

        for (int t : vertexes) {
            if (d.get(new Pair(t, t)) < 0) {
                return INF;
            }
        }

        int radius = INF;
        for (int u : vertexes) {
            int r = -1;
            for (int v : vertexes) {
                r = Math.max(r, d.getOrDefault(new Pair(v, u), -1));
            }
            radius = Math.min(radius, r);
        }

        return radius;
    }

    public Map<Pair, Integer> MST() {
        Map<Pair, Integer> d = new HashMap<>();
        int[] id = new int[vertexes.size()];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
        }

        int n = vertexes.size();
        int m = 0;

        while (m < n - 1) {
            for (int u : vertexes) {
                int min = INF;
                int curId = -1;
                for (int v : G.get(u)) {
                    Pair key = new Pair(u, v);

                    if(GW.get(key) < min) {
                        min = GW.get(key);
                    }
                }
            }
        }

        return d;
    }


    private class Pair {
        int x, y;

        private Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public int componentsCount() {
        return componentsCount(new HashSet<>());
    }

    public int componentsCount(Set<Integer> used) {
        int ans = 0;
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
            G.put(vertex, new HashSet<>());

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
        sb.append(vertexes.size()).append("\n");

        if (isWeighted) {
            for (Pair pair : GW.keySet()) {
                sb.append(pair.x).append(" -> ").append(pair.y).append(", weight = ").append(GW.get(pair));
                sb.append("\n");
            }
        } else {
            for (Integer from : G.keySet()) {
                sb.append(from).append(": ");

                for (int to : G.get(from)) {
                    sb.append(to).append(" ");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public int edgesCount(Set<Integer> used) {
        Set<Pair> ans = new HashSet<>();
        for (int u : vertexes) {
            if (used.contains(u)) continue;
            for (int v : G.get(u)) {
                if (used.contains(v)) continue;

                int min = Math.min(u, v);
                int max = Math.max(u, v);
                Pair p = new Pair(min, max);
                ans.add(p);
            }
        }

        return ans.size();
    }
}
