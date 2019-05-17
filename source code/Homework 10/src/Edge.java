
class Edge {
    private int vertex1;
    private int vertex2;
    private String name;
    private int hash;
    //vertex1 < vertex2
    
    Edge(int v1, int v2) {
        if (v1 <= v2) {
            vertex1 = v1;
            vertex2 = v2;
        } else {
            vertex1 = v2;
            vertex2 = v1;
        }
        name += vertex1 + "_" + vertex2;
        hash = name.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Edge) {
            if (vertex1 == ((Edge)o).getV1() && vertex2 == ((Edge)o).getV2()) {
                return true;
            }
        }
        return false;
    }
    
    int getV1() {
        return vertex1;
    }
    
    int getV2() {
        return vertex2;
    }
    
    @Override
    public int hashCode() {
        return hash;
    }
}
