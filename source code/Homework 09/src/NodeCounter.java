public class NodeCounter {
    private int nodeId;
    private int count;

    public NodeCounter() {
        nodeId = 0;
        count = 0;
    }
    
    public NodeCounter(int nodeId) {
        this.nodeId = nodeId;
        count = 1;
    }
    
    public NodeCounter(int nodeId, int count) {
        this.nodeId = nodeId;
        this.count = count;
    }
    
    public boolean isNode(int nodeId) {
        return this.nodeId == nodeId;
    }
    
    public boolean isNull() {
        return count == 0;
    }
    
    public void decrease() {
        count--;
    }

    public void increase() {
        count++;
    }
}
