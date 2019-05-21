import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;

public class EdgeSet {
    private HashMap<Edge, Integer> edges1;
    private HashMap<Vertex, HashSet<Vertex>> edges2;
    private HashMap<Edge, Integer> edges3;
    private HashMap<Vertex, HashSet<Vertex>> edges4;
    private HashMap<Edge, Integer> buffer1;//distance
    private HashMap<Edge, Integer> buffer2;//ticketPrice
    private HashMap<Edge, Integer> buffer3;//unpleasant
    private HashMap<Edge, Integer> buffer4;//leastTransfer
    private HashSet<Edge> buffer5;//not connected
    private int connectCount;    
    private HashMap<Integer, Integer> father;
    
    EdgeSet() {
        edges1 = new HashMap<>();
        edges2 = new HashMap<>();        
        edges3 = new HashMap<>();
        edges4 = new HashMap<>();
        buffer1 = new HashMap<>();
        buffer2 = new HashMap<>();
        buffer3 = new HashMap<>();
        buffer4 = new HashMap<>();
        buffer5 = new HashSet<>();
        connectCount = 0;
        father = new HashMap<>();
    }
    
    void add(Path path, int pathId) {
        for (int i = 0; i < path.size() - 1; i++) {
            int node1 = path.getNode(i);
            int node2 = path.getNode(i + 1);
            Vertex v10 = new Vertex(node1);
            Vertex v20 = new Vertex(node2);
            Vertex v1i = new Vertex(node1, pathId);
            Vertex v2i = new Vertex(node2, pathId);
            Edge tmpEdge1 = new Edge(v10, v20);
            Edge tmpEdge2 = new Edge(v1i, v2i);
            Edge tmpEdge3 = new Edge(v10, v1i);
            Edge tmpEdge4 = new Edge(v20, v2i);
            if (edges1.containsKey(tmpEdge1)) {
                int count = edges1.get(tmpEdge1);
                edges1.replace(tmpEdge1, count, count + 1);
            } else {
                edges1.put(tmpEdge1, 1);
            }
            if (node1 != node2) {                
                if (edges2.containsKey(v10)) {
                    HashSet<Vertex> tmpSet = edges2.get(v10);
                    tmpSet.add(v20);
                } else {
                    HashSet<Vertex> tmpSet = new HashSet<>();
                    tmpSet.add(v20);
                    edges2.put(v10, tmpSet);
                }                
                if (edges2.containsKey(v20)) {
                    HashSet<Vertex> tmpSet = edges2.get(v20);
                    tmpSet.add(v10);
                } else {
                    HashSet<Vertex> tmpSet = new HashSet<>();
                    tmpSet.add(v10);
                    edges2.put(v20,tmpSet);
                }
            }
            if (node1 != node2) {
                insertEdge(tmpEdge2);
                insertEdge(tmpEdge3);
                insertEdge(tmpEdge4);
            } else {
                insertEdge(tmpEdge3);
            }
        }
        cleanBuffers();
    }
    
    void remove(Path path, int pathId) {
        for (int i = 0; i < path.size() - 1; i++) {
            int node1 = path.getNode(i);
            int node2 = path.getNode(i + 1);
            Vertex v10 = new Vertex(node1);
            Vertex v20 = new Vertex(node2);
            Vertex v1i = new Vertex(node1, pathId);
            Vertex v2i = new Vertex(node2, pathId);
            Edge tmpEdge1 = new Edge(v10, v20);
            Edge tmpEdge2 = new Edge(v1i, v2i);
            Edge tmpEdge3 = new Edge(v10, v1i);
            Edge tmpEdge4 = new Edge(v20, v2i);
            int count = edges1.get(tmpEdge1);
            if (count == 1) {
                edges1.remove(tmpEdge1);
                if (node1 != node2) {
                    edges2.get(v10).remove(v20);
                    edges2.get(v20).remove(v10);
                }
            } else {
                edges1.replace(tmpEdge1, count, count - 1);
            }
            if (node1 != node2) {
                removeEdge(tmpEdge2);
                removeEdge(tmpEdge3);
                removeEdge(tmpEdge4);
            } else {
                removeEdge(tmpEdge3);
            }            
        }
        cleanBuffers();
    }
    
    private void cleanBuffers() {
        buffer1.clear();
        buffer2.clear();
        buffer3.clear();
        buffer4.clear();
        buffer5.clear();
        connectCount = 0;
        father.clear();
    }
    
    boolean containsEdge(int fromNodeId, int toNodeId) {
        return edges1.containsKey(
                new Edge(new Vertex(fromNodeId), new Vertex(toNodeId)));
    }
    
    boolean isConnected(int from, int to, VertexSet vs) {
        Edge edge = new Edge(new Vertex(from), new Vertex(to));
        if (edges1.containsKey(edge) || buffer1.containsKey(edge) ||
                buffer2.containsKey(edge) || buffer3.containsKey(edge) ||
                buffer4.containsKey(edge)) {
            return true;
        } else if (buffer5.contains(edge)) {
            return false;
        }
        try {
            getShortestPathLength(from, to, vs);
            return true;
        } catch (NodeNotConnectedException e) {
            buffer5.add(edge);
            return false;
        }
    }
    
    public int getShortestPathLength(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        Edge edge = new Edge(new Vertex(from), new Vertex(to));
        if (edges1.containsKey(edge)) {
            return 1;
        } else if (buffer1.containsKey(edge)) {
            return buffer1.get(edge);
        }
        int lenth = shortLen(from, to, vs);
        return lenth;        
    }
    
    private int shortLen(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        HashMap<Integer, VertexNode> vers = new HashMap<>();
        Iterator<Vertex> it = vs.cleanIterator();
        while (it.hasNext()) {
            Vertex tmpV = it.next();
            VertexNode tmpVn = new VertexNode(tmpV);
            vers.put(tmpV.getNodeId(), tmpVn);
        }
        vers.get(from).setVisited();
        LinkedList<Integer> llist = new LinkedList<>();
        llist.add(from);
        while (!llist.isEmpty()) {
            int nodeNow = llist.removeFirst();
            int distanceNow = vers.get(nodeNow).getDistance() + 1;
            HashSet<Vertex> connectedNode = edges2.get(new Vertex(nodeNow));
            for (Vertex v : connectedNode) {
                VertexNode vn = vers.get(v.getNodeId());
                if (!vn.wasVisited()) {
                    insertBuffer(from, v.getNodeId(), distanceNow, buffer1);
                    vn.setDistance(distanceNow);
                    vn.setVisited();
                    llist.add(v.getNodeId());
                }
            }        
        }
        Edge tmpEdge = new Edge(new Vertex(from), new Vertex(to));
        if (buffer1.containsKey(tmpEdge)) {
            return buffer1.get(tmpEdge);
        }
        throw new NodeNotConnectedException(from, to);
    }
    
    public int getLeastTicketPrice(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        Edge edge = new Edge(new Vertex(from), new Vertex(to));
        if (edges1.containsKey(edge)) {
            return 1;
        } else if (buffer2.containsKey(edge)) {
            return buffer2.get(edge);
        }
        int price = leastPrice(from, to, vs);
        return price;        
    }
    
    private int leastPrice(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        HashMap<Vertex, VertexNode> vers = new HashMap<>();
        Iterator<Vertex> it = vs.iterator();
        while (it.hasNext()) {
            Vertex tmpV = it.next();
            VertexNode tmpVn = new VertexNode(tmpV);
            vers.put(tmpV, tmpVn);
        }
        VertexNode fromVn = vers.get(new Vertex(from));
        PriorityQueue<VertexNode> plist = new PriorityQueue<>(
                Comparator.comparingInt(VertexNode::getDistance));
        plist.add(fromVn);
        while (!plist.isEmpty()) {
            VertexNode vnNow = plist.remove();         
            Vertex vertexNow = vnNow.getVertex();
            int distanceNow = vnNow.getDistance();
            if (vertexNow.getLine() == 0) {
                insertBuffer(from, vertexNow.getNodeId(),
                        distanceNow - 2, buffer2);
            }
            HashSet<Vertex> connectedNode = edges4.get(vertexNow);
            for (Vertex v : connectedNode) {
                Edge edge = new Edge(vertexNow, v);
                VertexNode newVn = vers.get(v);
                /*------------------------------------*/
                int price = edge.getLeastTicket();
                int newDistance = distanceNow + price;
                /*------------------------------------*/
                if (!newVn.wasVisited()) {   
                    newVn.setDistance(newDistance);
                    newVn.setVisited();
                    plist.add(newVn);
                } else if (newVn.getDistance() > newDistance) {
                    plist.remove(newVn);
                    newVn.setDistance(newDistance);
                    plist.add(newVn);
                }
            }            
        }
        Edge tmpEdge = new Edge(new Vertex(from), new Vertex(to));
        if (buffer2.containsKey(tmpEdge)) {
            return buffer2.get(tmpEdge);
        } else {
            throw new NodeNotConnectedException(from, to);
        }
    }
    
    public int getLeastUnPleasant(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        Edge edge = new Edge(new Vertex(from), new Vertex(to));
        if (edges1.containsKey(edge)) {
            return edge.getLeastUnpleasant();
        } else if (buffer3.containsKey(edge)) {
            return buffer3.get(edge);
        }
        int unpleasant = leastUnpleasant(from, to, vs);
        return unpleasant;
    }
    
    private int leastUnpleasant(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        HashMap<Vertex, VertexNode> vers = new HashMap<>();
        Iterator<Vertex> it = vs.iterator();
        while (it.hasNext()) {
            Vertex tmpV = it.next();
            VertexNode tmpVn = new VertexNode(tmpV);
            vers.put(tmpV, tmpVn);
        }
        VertexNode fromVn = vers.get(new Vertex(from));
        PriorityQueue<VertexNode> plist = new PriorityQueue<>(
                Comparator.comparingInt(VertexNode::getDistance));
        plist.add(fromVn);
        while (!plist.isEmpty()) {
            VertexNode vnNow = plist.remove();
            Vertex vertexNow = vnNow.getVertex();
            int distanceNow = vnNow.getDistance();
            if (vertexNow.getLine() == 0) {
                insertBuffer(from, vertexNow.getNodeId(),
                        distanceNow - 32, buffer3);
            }
            HashSet<Vertex> connectedNode = edges4.get(vertexNow);
            for (Vertex v : connectedNode) {                
                Edge edge = new Edge(vertexNow, v);
                VertexNode newVn = vers.get(v);
                /*------------------------------------*/
                int unpleasant = edge.getLeastUnpleasant();
                int newDistance = distanceNow + unpleasant;
                /*------------------------------------*/
                if (!newVn.wasVisited()) {   
                    newVn.setDistance(newDistance);
                    newVn.setVisited();
                    plist.add(newVn);
                } else if (newVn.getDistance() > newDistance) {
                    plist.remove(newVn);
                    newVn.setDistance(newDistance);
                    plist.add(newVn);
                }
            }            
        }
        Edge tmpEdge = new Edge(new Vertex(from), new Vertex(to));
        if (buffer3.containsKey(tmpEdge)) {
            return buffer3.get(tmpEdge);
        } else {
            throw new NodeNotConnectedException(from, to);
        }
    }
    
    public int getLeastTransferCount(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        Edge edge = new Edge(new Vertex(from), new Vertex(to));
        if (edges1.containsKey(edge)) {
            return 0;
        } else if (buffer4.containsKey(edge)) {
            return buffer4.get(edge);
        }
        int trans = leastTransfer(from, to, vs);
        return trans;
    }
    
    private int leastTransfer(int from, int to, VertexSet vs)
            throws NodeNotConnectedException {
        HashMap<Vertex, VertexNode> vers = new HashMap<>();
        Iterator<Vertex> it = vs.iterator();
        while (it.hasNext()) {
            Vertex tmpV = it.next();
            VertexNode tmpVn = new VertexNode(tmpV);
            vers.put(tmpV, tmpVn);
        }
        VertexNode fromVn = vers.get(new Vertex(from));
        PriorityQueue<VertexNode> plist = new PriorityQueue<>(
                Comparator.comparingInt(VertexNode::getDistance));
        plist.add(fromVn);
        while (!plist.isEmpty()) {
            VertexNode vnNow = plist.remove();         
            Vertex vertexNow = vnNow.getVertex();
            int distanceNow = vnNow.getDistance();
            if (vertexNow.getLine() == 0) {
                insertBuffer(from, vertexNow.getNodeId(),
                        (distanceNow - 2) / 2, buffer4);
            }
            HashSet<Vertex> connectedNode = edges4.get(vertexNow);
            for (Vertex v : connectedNode) {
                Edge edge = new Edge(vertexNow, v);
                VertexNode newVn = vers.get(v);
                /*------------------------------------*/
                int trans = edge.getLeastTransfer();
                int newDistance = distanceNow + trans;
                /*------------------------------------*/
                if (!newVn.wasVisited()) {   
                    newVn.setDistance(newDistance);
                    newVn.setVisited();
                    plist.add(newVn);
                } else if (newVn.getDistance() > newDistance) {
                    plist.remove(newVn);
                    newVn.setDistance(newDistance);
                    plist.add(newVn);
                }
            }            
        }
        Edge tmpEdge = new Edge(new Vertex(from), new Vertex(to));
        if (buffer4.containsKey(tmpEdge)) {
            return buffer4.get(tmpEdge);
        } else {
            throw new NodeNotConnectedException(from, to);
        }
    }
    
    public int getConnectedBlockCount(VertexSet vs) {
        if (connectCount == 0) {
            connectCount = getCount(vs);            
        } 
        return connectCount;        
    }
    
    private int getCount(VertexSet vs) {
        Iterator<Vertex> it = vs.cleanIterator();
        while (it.hasNext()) {
            Vertex v = it.next();
            int nodeId = v.getNodeId();
            father.put(nodeId,nodeId);
        }
        Set<Edge> keySet = edges1.keySet();
        Iterator<Edge> edgeIt = keySet.iterator();
        while (edgeIt.hasNext()) {
            Edge edge = edgeIt.next();
            join(edge.getV1().getNodeId(), edge.getV2().getNodeId());
        }        
        Iterator<Entry<Integer, Integer>> entryIt = 
                father.entrySet().iterator();
        int count = 0;
        while (entryIt.hasNext()) {
            Entry<Integer, Integer> entry = entryIt.next();
            int a = entry.getKey();
            int b = entry.getValue();
            if (a == b) {
                count++;
            }
        }
        return count;
    }
    
    private void join(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx != fy) {
            father.replace(fy, father.get(fy), fx);
        }
    }
    
    private int find(int x) {
        int fatherNode = father.get(x); 
        if (fatherNode != x) {
            int truefather = find(fatherNode);
            father.replace(x, fatherNode, truefather);
        }
        return father.get(x);
    }    
    
    private void insertBuffer(
            int v1, int v2, int value, HashMap<Edge,Integer> buffername) {
        Edge edge = new Edge(new Vertex(v1), new Vertex(v2));
        if (!buffername.containsKey(edge)) {
            buffername.put(edge, value);
        }
    }
    
    private void insertEdge(Edge edge) {
        if (edges3.containsKey(edge)) {
            int count = edges3.get(edge);
            edges3.replace(edge, count, count + 1);
        } else {
            edges3.put(edge, 1);
            Vertex v1 = edge.getV1();
            Vertex v2 = edge.getV2();
            if (edges4.containsKey(v1)) {
                HashSet<Vertex> tmpSet = edges4.get(v1);
                tmpSet.add(v2);
            } else {
                HashSet<Vertex> tmpSet = new HashSet<>();
                tmpSet.add(v2);
                edges4.put(v1, tmpSet);
            }
            if (edges4.containsKey(v2)) {
                HashSet<Vertex> tmpSet = edges4.get(v2);
                tmpSet.add(v1);
            } else {
                HashSet<Vertex> tmpSet = new HashSet<>();
                tmpSet.add(v1);
                edges4.put(v2, tmpSet);
            }
        }
    }
    
    private void removeEdge(Edge edge) {
        Vertex v1 = edge.getV1();
        Vertex v2 = edge.getV2();
        int count = edges3.get(edge);
        if (count == 1) {
            edges3.remove(edge);
            HashSet<Vertex> tmpSet;
            tmpSet = edges4.get(v1);
            tmpSet.remove(v2);
            tmpSet = edges4.get(v2);
            tmpSet.remove(v1);            
        } else {
            edges3.replace(edge, count, count - 1);
        }
    }
}