import java.util.HashMap;

import com.oocourse.specs3.models.PathNotFoundException;
import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.RailwaySystem;

public class MyRailwaySystem3 implements RailwaySystem {
    private HashMap<Integer, Path> paths1;//use id find path
    private HashMap<Path, Integer> paths2;//use path find id
    private int pidcounter;
    
    private VertexSet vertexSet;
    private EdgeSet edgeSet;
    
    public MyRailwaySystem3() {
        paths1 = new HashMap<>();
        paths2 = new HashMap<>();
        pidcounter = 1;
        
        vertexSet = new VertexSet();
        edgeSet = new EdgeSet();
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
                vertexSet.add(path, newId);
                edgeSet.add(path, newId);               
                return newId;
            }
        } else {
            return 0;
        }
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
    public int removePath(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            int pathId = paths2.get(path);
            paths1.remove(pathId);
            paths2.remove(path);            
            vertexSet.remove(path, pathId);
            edgeSet.remove(path, pathId);
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
            vertexSet.remove(path, pathId);
            edgeSet.remove(path, pathId);
        } else {
            throw new PathIdNotFoundException(pathId);
        }        
    }

    @Override
    public int size() {
        return paths1.size();
    }

    @Override
    public int getConnectedBlockCount() {
        return edgeSet.getConnectedBlockCount(vertexSet);
    }

    @Override
    public int getLeastTicketPrice(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            try {
                if (fromNodeId == toNodeId) {
                    return 0;
                }
                int price = edgeSet.getLeastTicketPrice(
                        fromNodeId, toNodeId, vertexSet);
                return price;
            } catch (NodeNotConnectedException e) {
                throw e;
            }            
        }
    }

    @Override
    public int getLeastTransferCount(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {      
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            try {
                if (fromNodeId == toNodeId) {
                    return 0;
                }
                int price = edgeSet.getLeastTransferCount(
                        fromNodeId, toNodeId, vertexSet);
                return price;
            } catch (NodeNotConnectedException e) {
                throw e;
            }            
        }
    }

    @Override
    public int getLeastUnpleasantValue(int fromNodeId, int toNodeId)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(fromNodeId)) {
            throw new NodeIdNotFoundException(fromNodeId);
        } else if (!containsNode(toNodeId)) {
            throw new NodeIdNotFoundException(toNodeId);
        } else {
            try {
                if (fromNodeId == toNodeId) {
                    return 0;
                }
                int price = edgeSet.getLeastUnPleasant(
                        fromNodeId, toNodeId, vertexSet);
                return price;
            } catch (NodeNotConnectedException e) {
                throw e;
            }            
        }
    }
    
    @Override
    public int getUnpleasantValue(Path arg0, int arg1, int arg2) {
        return 0;
    }

}
