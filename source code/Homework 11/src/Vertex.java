
public class Vertex {
    private int nodeId;
    private int line;
    private String name;
    private int hash;
    
    private int unPleasant;
    
    public Vertex(int nodeId) {
        this(nodeId,0);
    }
    
    public Vertex(int nodeId, int line) {
        this.nodeId = nodeId;
        this.line = line;
        this.name = nodeId + "_" + line;
        hash = name.hashCode();
        unPleasant = (nodeId % 5 + 5) % 5;
    }
    
    public int getNodeId() {
        return nodeId;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getUnPleasant() {
        return unPleasant;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Vertex) {
            Vertex vertex2 = (Vertex)o;
            if (nodeId == vertex2.getNodeId() && line == vertex2.getLine()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return hash;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
