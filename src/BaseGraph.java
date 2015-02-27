import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;

public class BaseGraph implements Graph {
    private Set<Integer> vertexes;
    private Map<Integer, List<Integer>> G;

    @Override
    public void addVertex(int vertex) {
        if(!vertexes.contains(vertex)) {
            vertexes.add(vertex);
            G.put(vertex, new ArrayList<Integer>());
        } else {

        }
    }

    public Set<Integer> getVertexes() {
        return this.vertexes;
    }

    public BaseGraph(File file) throws IOException {
        G = new HashMap<>();
        vertexes = new HashSet<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));

        int n = parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            String[] splited = line.split(" ");

            int from = parseInt(splited[0]);
            vertexes.add(from);

            for (int j = 1; j < splited.length; j++) {
                int to = parseInt(splited[j]);
                addEdge(from, to);
            }
        }
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

        while(G.get(from).remove((Integer) to));
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
