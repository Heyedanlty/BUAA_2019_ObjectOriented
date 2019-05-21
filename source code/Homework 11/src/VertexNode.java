
public class VertexNode {
    //this class is for bfs || transfer || un
    private Vertex vertex;
    private int distance;
    private boolean visited;
    
    public VertexNode(Vertex vertex) {
        this(vertex, 0, false);
    }
    
    public VertexNode(Vertex vertex, int distance) {
        this(vertex, distance, false);
    }
    
    public VertexNode(Vertex vertex, int distance, boolean visited) {
        this.vertex = vertex;
        this.distance = distance;
        this.visited = visited;
    }
    
    public int getDistance() {
        return distance;
    }
    
    public void setDistance(int i) {
        distance = i;
    }
    
    public boolean wasVisited() {
        return visited;
    }
    
    public void setVisited() {
        visited = true;
    }
    
    @Override
    public int hashCode() {
        return vertex.hashCode();
    }

    public Vertex getVertex() {
        return vertex;
    }
}
