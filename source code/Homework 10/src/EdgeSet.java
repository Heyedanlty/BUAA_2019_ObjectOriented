import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.oocourse.specs2.models.NodeNotConnectedException;
import com.oocourse.specs2.models.Path;

public class EdgeSet {
    private HashMap<Edge, Integer> edges1;//edge & its count
    private HashMap<Integer, HashSet<Integer>> edges2;//another set of edge
    private HashMap<Edge, Integer> buffer;//buffer of least length (1~...)
    
    EdgeSet() {
        edges1 = new HashMap<>();
        edges2 = new HashMap<>();
        buffer = new HashMap<>();
    }
    
    void add(Path path) {
        for (int i = 0; i < path.size() - 1; i++) {
            int j = i + 1;
            int v1 = path.getNode(i);
            int v2 = path.getNode(j);
            Edge tmpEdge = new Edge(v1, v2);
            if (edges1.containsKey(tmpEdge)) {
                int count = edges1.get(tmpEdge);
                edges1.replace(tmpEdge, count, count + 1);
            } else {
                edges1.put(tmpEdge, 1);
            }
            if (v1 != v2) {                
                if (edges2.containsKey(v1)) {
                    HashSet<Integer> tmpSet = edges2.get(v1);
                    tmpSet.add(v2);
                } else {
                    HashSet<Integer> tmpSet = new HashSet<>();
                    tmpSet.add(v2);
                    edges2.put(v1,tmpSet);
                }
                
                if (edges2.containsKey(v2)) {
                    HashSet<Integer> tmpSet = edges2.get(v2);
                    tmpSet.add(v1);
                } else {
                    HashSet<Integer> tmpSet = new HashSet<>();
                    tmpSet.add(v1);
                    edges2.put(v2,tmpSet);
                }
            }
        }
        buffer.clear();
    }
    
    void remove(Path path) {
        for (int i = 0; i < path.size() - 1; i++) {
            int j = i + 1;
            int v1 = path.getNode(i);
            int v2 = path.getNode(j);
            Edge tmpEdge = new Edge(v1, v2);
            int count = edges1.get(tmpEdge);
            if (count == 1) {
                edges1.remove(tmpEdge);
                if (v1 != v2) {
                    edges2.get(v1).remove(v2);
                    edges2.get(v2).remove(v1);
                }                
            } else {
                edges1.replace(tmpEdge, count, count - 1);
            }         
        }
        buffer.clear();
    }
    
    boolean containsEdge(int fromNodeId, int toNodeId) {
        return edges1.containsKey(new Edge(fromNodeId, toNodeId));
    }
   
    int getShortestPathLength(int from, int to, VertexSet vs) 
            throws NodeNotConnectedException {
        Edge edgetmp = new Edge(from, to);
        if (edges1.containsKey(edgetmp)) {
            return 1;
        } else if (buffer.containsKey(edgetmp)) {
            return buffer.get(edgetmp);
        }
        try {
            int length = getShortLen(from, to, vs);
            return length;
        } catch (NodeNotConnectedException e) {
            throw e;
        }
    }
    
    boolean isConnected(int from, int to, VertexSet vs) {
        Edge edgetmp = new Edge(from, to);
        if (buffer.containsKey(edgetmp) || edges1.containsKey(edgetmp)) {
            return true;
        }
        //find whether it's connected
        try {
            getShortLen(from, to, vs);
            return true;
        } catch (NodeNotConnectedException e) {
            return false;
        }
    }

    private int getShortLen(int from, int to, VertexSet vs) 
            throws NodeNotConnectedException {
        HashMap<Integer, Vertex> vers = new HashMap<>();
        Iterator<Integer> it = vs.iterator();
        while (it.hasNext()) {
            int ver = it.next();
            vers.put(ver, new Vertex(ver));            
        }
        vers.get(from).setVisited();
        LinkedList<Integer> llist = new LinkedList<>();
        llist.add(from);
        while (!llist.isEmpty()) {
            int nodeNow = llist.removeFirst();
            int distanceNow = vers.get(nodeNow).getDistance() + 1;
            HashSet<Integer> connectNode = edges2.get(nodeNow);
            for (int i : connectNode) {
                Vertex v = vers.get(i);
                if (!v.wasVisited()) {
                    if (i == to) {
                        insertBuffer(from, to, distanceNow);
                        return distanceNow;
                    }
                    v.setDistance(distanceNow);
                    v.setVisited();
                    llist.add(i);
                    insertBuffer(from, i, distanceNow);
                }
            }
        }
        throw new NodeNotConnectedException(from, to);
    }
    
    private void insertBuffer(int from, int to, int distance) {
        if (distance == 1) {
            return;
        }        
        Edge edge = new Edge(from,to);
        if (!buffer.containsKey(edge)) {
            buffer.put(edge, distance);
        }
        
    }
    
}
