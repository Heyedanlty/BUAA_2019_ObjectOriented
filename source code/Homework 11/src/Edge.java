
public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private String name;
    private int hash;
    
    private int unPleasant;
    
    public Edge(Vertex v1, Vertex v2) {
        if (v1.getNodeId() < v2.getNodeId()) {
            vertex1 = v1;
            vertex2 = v2;
        } else if (v1.getLine() < v2.getLine()) {
            vertex1 = v1;
            vertex2 = v2;
        } else {
            vertex1 = v2;
            vertex2 = v1;
        }
        name = vertex1.toString() + "_" + vertex2;
        hash = name.hashCode();
        
        if (vertex1.getNodeId() == vertex2.getNodeId()) {
            unPleasant = 16;
        } else {
            int u1 = vertex1.getUnPleasant();
            int u2 = vertex2.getUnPleasant();
            if (u1 >= u2) {
                unPleasant = (int)Math.pow(4,u1);
            } else {
                unPleasant = (int)Math.pow(4,u2);
            }
        }
    }

    public Vertex getV1() {
        return vertex1;
    }
    
    public Vertex getV2() {
        return vertex2;
    }
    
    public int getShortestDistance() {
        if (vertex1.getNodeId() == vertex2.getNodeId()) {
            return 0;
        }
        return 1;
    }
    
    public int getLeastTicket() {
        return 1;
    }
    
    public int getLeastUnpleasant() {
        return unPleasant;
    }
    
    public int getLeastTransfer() {
        if (vertex1.getNodeId() == vertex2.getNodeId()) {
            return 1;
        } else {
            return 0;
        }
    }
    
    @Override 
    public int hashCode() {
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Edge) {
            Edge edge2 = (Edge)o;
            if (vertex1.equals(edge2.getV1()) 
                    && vertex2.equals(edge2.getV2())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
