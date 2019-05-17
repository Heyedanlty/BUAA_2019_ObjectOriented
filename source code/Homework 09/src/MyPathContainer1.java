import java.util.ArrayList;

import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

public class MyPathContainer1 implements PathContainer {
    private ArrayList<MyPath1> pathList;
    private ArrayList<Integer> pidList;
    private ArrayList<NodeCounter> nodeList;
    private int pidCounter;
    
    public MyPathContainer1() {
        pathList = new ArrayList<MyPath1>();
        pidList = new ArrayList<Integer>();
        nodeList = new ArrayList<NodeCounter>();
        pidCounter = 1;
    }
    
    @Override
    public int addPath(Path path) throws Exception {
        if (path != null && path.isValid()) {
            if (containsPath(path)) {
                // return pathId of this path
                for (int i = 0; i < size(); i++) {
                    if (pathList.get(i).equals(path)) {
                        return pidList.get(i);
                    }
                }
                return 0;
            } else {
                // insert this path to pathList
                pathList.add((MyPath1)path);
                pidList.add(pidCounter++);
                // update node count of this path
                for (int node : path) {
                    boolean newNc = true;
                    for (NodeCounter nc : nodeList) {
                        if (nc.isNode(node)) {
                            nc.increase();
                            newNc = false;
                            break;
                        }
                    }
                    if (newNc == true) {
                        NodeCounter nc = new NodeCounter(node);
                        nodeList.add(nc);
                    }
                }
                return pidList.get(size() - 1);
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean containsPath(Path path) {
        if (path != null) {
            for (int i = 0; i < pathList.size(); i++) {
                if (pathList.get(i).equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsPathId(int pathId) {
        for (int i = 0; i < pidList.size(); i++) {
            if (pathId == pidList.get(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getDistinctNodeCount() {
        return nodeList.size();
    }

    @Override
    public Path getPathById(int pathId) throws Exception {
        if (containsPathId(pathId)) {
            for (int i = 0; i < size(); i++) {
                if (pathId == pidList.get(i)) {
                    return pathList.get(i);
                }
            }            
        } else {
            throw new PathIdNotFoundException(pathId);
        }
        return null;
    }

    @Override
    public int getPathId(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            for (int i = 0; i < size(); i++) {
                if (pathList.get(i).equals(path)) {
                    return pidList.get(i);
                }
            }
        } else {
            throw new PathNotFoundException(path);
        }
        return 0;
    }
    
    public int removePath(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            int retvalue = 0;
            MyPath1 tmp = null;
            for (int i = 0; i < size(); i++) {
                if (pathList.get(i).equals(path)) {
                    retvalue = pidList.get(i);
                    tmp = pathList.remove(i);
                    pidList.remove(i);
                    break;
                }
            }
            
            // delete node count of this path
            for (int node : tmp) {
                for (NodeCounter nc : nodeList) {
                    if (nc.isNode(node)) {
                        nc.decrease();
                        break;
                    }
                }
            }
            updateNodeList();
            
            return retvalue;
        } else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            int index = 0;
            for (int i = 0; i < size(); i++) {
                if (pidList.get(i) == pathId) {
                    index = i;
                }
            }
            MyPath1 tmp = pathList.remove(index);
            pidList.remove(index);
            
            // delete node count of this path
            for (int node : tmp) {
                for (NodeCounter nc : nodeList) {
                    if (nc.isNode(node)) {
                        nc.decrease();
                        break;
                    }
                }
            }
            updateNodeList();
            
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    @Override
    public int size() {
        return pathList.size();
    }

    private void updateNodeList() {
        for (int i = nodeList.size() - 1; i >= 0; i--) {
            if (nodeList.get(i).isNull()) {
                nodeList.remove(i);
            }
        }
    }

}
