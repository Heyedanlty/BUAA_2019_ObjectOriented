import java.util.HashMap;

import com.oocourse.specs2.models.Graph;
import com.oocourse.specs2.models.NodeIdNotFoundException;
import com.oocourse.specs2.models.NodeNotConnectedException;
import com.oocourse.specs2.models.Path;
import com.oocourse.specs2.models.PathIdNotFoundException;
import com.oocourse.specs2.models.PathNotFoundException;

public class MyGraph2 implements Graph {
    private HashMap<Integer, Path> paths1;//use id find path
    private HashMap<Path, Integer> paths2;//use path find id
    private int pidcounter;
    
    private VertexSet vertexSet;
    private EdgeSet edgeSet;
    
    public MyGraph2() {
        paths1 = new HashMap<>();
        paths2 = new HashMap<>();
        pidcounter = 1;
        
        vertexSet = new VertexSet();
        edgeSet = new EdgeSet();
    }

    @Override
    public boolean containsPath(Path path) {
        if (path != null) {
            return paths1.containsValue(path);
        }
        return false;
    }

    @Override
    public boolean containsPathId(int pathId) {
        return paths1.containsKey(pathId);
    }

    @Override
    public int getDistinctNodeCount() {
        return vertexSet.getDistinctNodeCount();
    }
    
    @Override
    public Path getPathById(int pathId) throws Exception {
        if (paths1.containsKey(pathId)) {
            return paths1.get(pathId);
        } else {
            throw new PathIdNotFoundException(pathId);
        }        
    }

    @Override
    public int getPathId(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            return paths2.get(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public int addPath(Path path) throws Exception {
        if (path != null && path.isValid()) {
            if (containsPath(path)) {
                int id = paths2.get(path);
                return id;
            } else {
                int newId = pidcounter++;
                paths1.put(newId, path);
                paths2.put(path, newId);                
                vertexSet.add(path);
                edgeSet.add(path);               
                return newId;
            }
        } else {
            return 0;
        }
    }
    
    public int removePath(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            int pathId = paths2.get(path);
            paths1.remove(pathId);
            paths2.remove(path);            
            vertexSet.remove(path);
            edgeSet.remove(path);
            return pathId;
        } else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            Path path = paths1.get(pathId);
            paths1.remove(pathId);
            paths2.remove(path);
            vertexSet.remove(path);
            edgeSet.remove(path);
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    @Override
    public int size() {
        return paths1.size();
    }

    @Override
    public boolean containsEdge(int fromNodeId, int toNodeId) {
        return edgeSet.containsEdge(fromNodeId, toNodeId);
    }

    @Override
    public boolean containsNode(int nodeId) {
        return vertexSet.containsNode(nodeId);
    }
    
    @Override
    public int getShortestPathLength(int fromNodeId, int toNodeId)
        throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!vertexSet.containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!vertexSet.containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            try {
                if (fromNodeId == toNodeId) {
                    return 0;
                }
                int distance = 
                        edgeSet.getShortestPathLength(
                                fromNodeId, toNodeId, vertexSet);
                return distance;
            } catch (NodeNotConnectedException e) {
                throw e;
            }
            
        }
    }

    @Override
    public boolean isConnected(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException {
        if (!vertexSet.containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!vertexSet.containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            if (fromNodeId == toNodeId) {
                return true;
            }
            return edgeSet.isConnected(fromNodeId, toNodeId, vertexSet);
        }
    }

}
