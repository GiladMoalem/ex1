package ex1.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

/**
 *  this class represents weighted graph based on:
 *   nodes list - based on hashmap selected by key.
 *   mode counter
 *   edge size
 */
public class WGraph_DS implements weighted_graph,java.io.Serializable{

    private HashMap<Integer, node_info> nodes;
    private int modeCounter;
    private int edgeSize;

    public WGraph_DS(){
        nodes = new HashMap<Integer, node_info>();
    }
// deep copy constructor
    public WGraph_DS(weighted_graph g){
        modeCounter = 0;
        this.edgeSize = g.edgeSize();
        this.nodes = new HashMap<Integer, node_info>();
        for (Integer node: ((WGraph_DS)g).nodes.keySet()){
            this.nodes.put(node,new NodeInfo(g.getNode(node)));
        }
    }
    @Override
    public node_info getNode(int key) {
        return this.nodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if(nodes.containsKey(node1)&&nodes.containsKey(node2))
            return ((NodeInfo)nodes.get(node1)).hasNi(node2);
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if(nodes.containsKey(node1)&&nodes.containsKey(node2))
        return ((NodeInfo)nodes.get(node1)).getEdge(node2);
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(!nodes.containsKey(key)) {
            nodes.put(key, new NodeInfo(key));
            modeCounter++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if(nodes.containsKey(node1) && nodes.containsKey(node2) && node1 != node2 && w>=0) {
            if(w!=getEdge(node1,node2)){
                if (!hasEdge(node1, node2))
                    edgeSize++;
                modeCounter++;
                ((NodeInfo) nodes.get(node1)).addNi(nodes.get(node2), w);
                ((NodeInfo) nodes.get(node2)).addNi(nodes.get(node1), w);
            }
        }
    }

    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        LinkedList<Integer> nei = new LinkedList<Integer>(((NodeInfo)nodes.get(node_id)).getNi()); //create list with keys of node_id's neighbors
        LinkedList<node_info> result = new LinkedList<node_info>();
        while(!nei.isEmpty()){
            result.add(nodes.get(nei.pollFirst()));
        }
        return result;
    }

    @Override
    public node_info removeNode(int key) {
        if(!nodes.containsKey(key)) return null; //if node key dost agsist - return null
        node_info n = nodes.get(key);
        LinkedList<node_info> nei = new LinkedList<node_info>(getV(key));
        while(!nei.isEmpty()){ //delete all edges
            node_info p = nei.pollFirst();
            ((NodeInfo)n).removeNi(p.getKey());
            ((NodeInfo)p).removeNi(n.getKey());
            edgeSize--;
        }
        modeCounter++;
        nodes.remove(key);
        return n;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)) return; //if node1 or node2 dost agsist
        if(hasEdge(node1,node2)) {
            modeCounter++;
            edgeSize--;
            ((NodeInfo) nodes.get(node1)).removeNi(node2);
            ((NodeInfo) nodes.get(node2)).removeNi(node1);
        }
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return modeCounter;
    }
    @Override
    public String toString(){
        return "Graph: "+ nodes.keySet();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return
                edgeSize == wGraph_ds.edgeSize &&
                Objects.equals(nodes, wGraph_ds.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, modeCounter, edgeSize);
    }

    /**
     * this class represents a node based on
     *  key
     *  info
     *  tag
     *  neighbor's list based on HashMap [key-neigbor's key|value-edge's weight between]
     */
    public static class NodeInfo implements node_info, java.io.Serializable{
        private String info;
        private double tag;
        private int key;
        private static int count=0;
        private HashMap<Integer, Double> neighbor;

        public NodeInfo(){
            this.key = count;
            neighbor = new HashMap<Integer, Double>();
            count++;
            info = "";
        }
// key's copy constructor without neighbors
        public NodeInfo(int key){
            this.key= key;
            neighbor = new HashMap<Integer , Double>();
            info = "";
        }
// copy constructor with neighbors
        public NodeInfo(node_info node){
            this.info = ((NodeInfo)node).info+"";
            this.tag = ((NodeInfo)node).tag;
            this.key = ((NodeInfo)node).key;
            this.neighbor = new HashMap<Integer, Double> (((NodeInfo) node).neighbor);
        }
        public double getEdge(int key){//return the weight of the edge berween this.key, key if there is no edge return -1
            if(neighbor.containsKey(key))
                return neighbor.get(key);
            else return -1;
        }
        public boolean hasNi(int key) {
            return neighbor.containsKey(key);
        }
        public void addNi(node_info t,double edge) {
            if(t.getKey()!=this.key)
                neighbor.put(t.getKey(), edge);
        }
        public void removeNi(int node) {
            neighbor.remove(node);
        }
        public Collection<Integer> getNi(){    //check it!
            return this.neighbor.keySet();
        }
        @Override
        public String toString(){
            return "NODE:"+key+" "+getNi();
        }


        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s + "";
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return Double.compare(nodeInfo.tag, tag) == 0 &&
                    key == nodeInfo.key &&
                    Objects.equals(info, nodeInfo.info) &&
                    Objects.equals(neighbor, nodeInfo.neighbor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

}