import java.util.ArrayList;
import java.util.Iterator;

import com.oocourse.specs1.models.Path;

public class MyPath1 implements Path {
    private ArrayList<Integer> nodes;
    private int distinctNodeCount;

    public MyPath1(int... group) {
        int i;
        int j;
        distinctNodeCount = 0;
        nodes = new ArrayList<Integer>();
        for (i = 0; i < group.length; i++) {
            nodes.add(group[i]);
            for (j = 0; j < i; j++) {
                if (getNode(j) == getNode(i)) {
                    break;
                }
            }
            if (j == i) {
                distinctNodeCount++;
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
        for (int i = 0; i < size(); i++) {
            if (getNode(i) == node) {
                return true;
            }
        }
        return false;
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
    
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MyPath1) {
            if (size() == ((MyPath1)obj).size()) {
                for (int i = 0; i < size(); i++) {
                    if (getNode(i) != ((MyPath1)obj).getNode(i)) {
                        return false;                        
                    }
                }
                return true;
            }
        }
        return false;
    }
    
}
