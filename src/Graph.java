import java.util.Set;

public interface Graph extends Cloneable {
    public boolean isAdjacent(int from, int to) throws Exception;

    public void addArc(int from, int to);

    public void addEdge(int from, int to);

    public void removeArc(int from, int to) throws Exception;

    public void removeEdge(int from, int to) throws Exception;

    public int degree(int vertex) throws Exception;

    public int orientedDegree(int vertex) throws Exception;

    public void addVertex(int vertex);

    public void removeVertex(int vertex) throws Exception;

    public Set<Integer> getVertexes();

    public Graph clone();
}

// 1 список 1 23
