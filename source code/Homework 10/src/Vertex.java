
class Vertex {
    //Vertex Class for shortest path
    private int vertex;
    private int distance;
    private boolean visited;
    
    Vertex(int vertex) {
        this(vertex, 0, false);
    }
    
    Vertex(int vertex, int distance) {
        this(vertex, distance, false);
    }
    
    Vertex(int vertex, int distance, boolean visited) {
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
        return vertex;
    }
}
