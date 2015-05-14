import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class BaseGraph implements Graph {
    private static final int INF = (int) 1e9;

    public Map<Integer, Set<Integer>> getG() {
        return G;
    }

    private Map<Integer, Set<Integer>> G;
    private Set<Integer> vertexes;
    private Map<Pair, Integer> GW;
    private Map<Pair, Integer> c;
    private Map<Pair, Integer> f;
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

    public int maxFlow(int s, int t) {
        c = new HashMap<>();
        f = new HashMap<>();

        for (Integer u : G.keySet()) {
            for (Integer v : G.get(u)) {
                Pair edge = new Pair(u, v);
                Pair edgeRev = new Pair(v, u);

                f.putIfAbsent(edge, 0);
                f.putIfAbsent(edgeRev, 0);

                c.putIfAbsent(edge, 0);
                c.put(edge, c.get(edge) + GW.get(edge));
            }
        }

        int sum = 0;
        while(enlarge(s, t));

        for (Integer i : G.get(s)) {
            sum += f.get(new Pair(s, i));
        }
        return sum;
    }

    private boolean enlarge(int s, int t) {
        Queue<Integer> q = new LinkedList<>();
        Map<Integer, Integer> mf = new HashMap<>();
        Map<Integer, Integer> p = new HashMap<>();

        for (Integer v : vertexes) {
            mf.put(v, 0);
        }
        mf.put(s, INF);
        p.put(s, s);

        q.add(s);
        while(!q.isEmpty() && mf.get(t) == 0) {
            int u = q.poll();

            for (Integer v : G.get(u)) {
                Pair edge = new Pair(u, v);
                int val = c.get(edge) - f.get(edge);

                if(val > 0 && mf.get(v) == 0) {
                    mf.put(v, Math.min(mf.get(u), val));
                    q.add(v);
                    p.put(v, u);
                }
            }
        }

        if(mf.get(t) == 0) {
            return false;
        }

        int v = t;
        while(p.get(v) != v) {
            Pair fw = new Pair(p.get(v), v);
            Pair bw = new Pair(v, p.get(v));
            f.put(fw, f.get(fw) + mf.get(t));
            f.put(bw, f.get(bw) - mf.get(t));
            v = p.get(v);
        }

        return true;
    }

    public Map<Pair, Integer> floyd() {
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

        return d;
    }

    public Set<Pair> pairsWithPathInNegCycle() {
        Set<Pair> ans = new HashSet<>();
        Map<Pair, Integer> floyd = floyd();

        for (int i : vertexes) {
            for (int j : vertexes) {
                for (int t : vertexes) {
                    Pair p1 = new Pair(t, t);
                    Pair p2 = new Pair(i, t);
                    Pair p3 = new Pair(t, j);

                    if (floyd.get(p1) < 0 && floyd.get(p2) < INF && floyd.get(p3) < INF) {
                        Pair edge = new Pair(min(i, j), max(i, j));
                        ans.add(edge);
                    }
                }
            }
        }

        return ans;
    }

    public int dijkstra(int start, int end) {
        Map<Integer, Integer> d = new HashMap<>();
        Set<Integer> used = new HashSet<>();

        for (Integer vertex : vertexes) {
            d.put(vertex, INF);
        }
        d.put(start, 0);

        for (int i = 0; i < vertexes.size(); ++i) {
            int v = -1;

            for (int j : vertexes) {
                if (!used.contains(j) && (v == -1 || d.get(j) < d.get(v))) {
                    v = j;
                }
            }

            if (d.get(v) == INF) {
                break;
            }

            used.add(v);

            for (int to : G.get(v)) {
                Integer weight = GW.get(new Pair(v, to));

                if (d.get(v) + weight < d.get(to)) {
                    d.put(to, d.get(v) + weight);
                }
            }
        }

        return d.get(end);
    }

    public int radius() {
        Map<Pair, Integer> d = floyd();
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
                r = max(r, d.getOrDefault(new Pair(v, u), -1));
            }
            radius = Math.min(radius, r);
        }

        return radius;
    }

    public Map<Pair, Integer> MST() {
        Map<Integer, Set<Integer>> t = new HashMap<>();
        Map<Pair, Integer> ans = new HashMap<>();

        for (int u : vertexes) {
            t.put(u, new HashSet<>());
        }

        while (true) {
            List<Set<Integer>> components = components(t);
            if (components.size() == 1) {
                break;
            }
            System.out.println(components.size() + " " + components);

            int min = (int) 1e9;
            Pair candidate = null;

            List<Pair> edges = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();
            for (Set<Integer> component : components) {
                for (int u : component) {
                    if (!G.containsKey(u))
                        continue;
                    for (int v : G.get(u)) {
                        Pair edge = new Pair(u, v);
                        if (GW.get(edge) < min && !component.contains(v)) {
                            min = GW.get(edge);
                            candidate = edge;
                        }
                    }
                }

                edges.add(candidate);
                weights.add(min);
            }

            for (int i = 0; i < edges.size(); ++i) {
                Pair edge = edges.get(i);
                int weight = weights.get(i);

                ans.put(edge, weight);
                if (edge == null) {
                    continue;
                }

                if (!t.containsKey(edge.x)) {
                    t.put(edge.x, new HashSet<>());
                }

                t.get(edge.x).add(edge.y);
            }
        }


        Map<Pair, Integer> newAns = new HashMap<>();
        Set<Pair> toAdd = new HashSet<>();
        for (Pair pair : ans.keySet()) {
            Pair inv = new Pair(pair.y, pair.x);
            if (ans.containsKey(inv) && ans.get(inv).equals(ans.get(pair))) {
                inv = new Pair(min(pair.x, pair.y), max(pair.x, pair.y));
                toAdd.add(inv);
                continue;
            }

            newAns.put(pair, ans.get(pair));
        }

        for (Pair pair : toAdd) {
            newAns.put(pair, ans.get(pair));
        }

        return newAns;
    }

    static class Pair {
        int x, y;

        Pair(int x, int y) {
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

    public List<Set<Integer>> components(Map<Integer, Set<Integer>> G) {
        List<Set<Integer>> components = new ArrayList<>();
        Set<Integer> used = new HashSet<>();

        G.keySet().stream().filter(from -> !used.contains(from)).forEach(from -> {
            Set<Integer> curComponent = new HashSet<Integer>();
            Queue<Integer> q = new LinkedList<>();
            q.add(from);

            while (!q.isEmpty()) {
                int u = q.poll();
                curComponent.add(u);
                if (!G.containsKey(u)) continue;

                G.get(u).stream().filter(v -> !used.contains(v)).forEach(v -> {
                    used.add(v);
                    q.add(v);
                });
            }
            components.add(curComponent);
        });

        return components;
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
                int max = max(u, v);
                Pair p = new Pair(min, max);
                ans.add(p);
            }
        }

        return ans.size();
    }
}
