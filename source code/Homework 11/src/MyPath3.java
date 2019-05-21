import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.oocourse.specs3.models.Path;

public class MyPath3 implements Path {
    private ArrayList<Integer> nodes;
    private HashSet<Integer> nodeSet;
    private int distinctNodeCount;
    private int hash;
    
    public MyPath3(int... group) {
        distinctNodeCount = 0;
        nodes = new ArrayList<Integer>();
        nodeSet = new HashSet<Integer>();
        int i;
        int j;        
        for (i = 0; i < group.length; i++) {
            nodes.add(group[i]);
            for (j = 0; j < i; j++) {
                if (getNode(j) == getNode(i)) {
                    break;
                }
            }
            if (j == i) {
                distinctNodeCount++;
                nodeSet.add(group[i]);
            }
        }
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }

    @Override
    public int compareTo(Path o) {
        if (this.equals(o)) {
            return 0;
        } else {
            int thislen = size();
            int olen = o.size();
            for (int i = 0; i < thislen || i < olen; i++) {
                if (i == thislen) {
                    return -1;
                }
                if (i == olen) {
                    return 1;
                }
                if (getNode(i) < o.getNode(i)) {
                    return -1;
                }
                if (getNode(i) > o.getNode(i)) {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean containsNode(int node) {
        if (nodeSet.contains(node)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getDistinctNodeCount() {
        return distinctNodeCount;
    }

    @Override
    public int getNode(int index) {
        if (index >= 0 && index < size()) {
            return nodes.get(index); 
        }
        return 0;
    }

    @Override
    public boolean isValid() {
        return size() >= 2;
    }

    @Override
    public int size() {
        return nodes.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MyPath3) {
            if (size() == ((MyPath3)obj).size()) {
                for (int i = 0; i < size(); i++) {
                    if (getNode(i) != ((MyPath3)obj).getNode(i)) {
                        return false;                        
                    }
                }
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
    public int getUnpleasantValue(int nodeId) {
        if (containsNode(nodeId)) {
            return (int)Math.pow(4, (nodeId % 5 + 5) % 5);
        } else {
            return 0;
        }
    }
}
