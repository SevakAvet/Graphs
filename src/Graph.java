import java.util.Set;

public interface Graph {
    public void addArc(int from, int to);

    public void addEdge(int from, int to);

    public void removeArc(int from, int to) throws Exception;

    public void removeEdge(int from, int to) throws Exception;

    public int degree(int vertex) throws Exception;

    public void addVertex(int vertex);

    public Set<Integer> getVertexes();
}

// 1 список 1 23
