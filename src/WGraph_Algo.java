package ex1.src;

import java.io.*;
import java.util.*;

/**
 * this class WGraph_Algo represents graph algorithms based on a graph
 */
public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph g;

    public WGraph_Algo(){
        g = new WGraph_DS();
    }

    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    @Override
    public weighted_graph getGraph() {
        return g;
    }

//	this method used the copy constructor of graph(WGraph_DS) and return new graph.
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.g);
    }

//	this method return true if graph g is connected
    @Override
    public boolean isConnected() {
        if(g==null||g.nodeSize()<2) return true;
        HashMap<Integer, node_info> spare = new HashMap<Integer, node_info>();
        LinkedList<node_info> nei = new LinkedList<node_info>(g.getV());
//		create hashmap called spare of all the nodes in the graph selected by key.
//		all nodes we got to will remove from that hashmap
//		if that hashmap is empty we got all the nodes in this graph
        while(!nei.isEmpty()) {
            node_info n = nei.pollFirst();
            spare.put(n.getKey(),n);
        }
//      nei = "waiting list" of nodes, that represented nodes we need to check their neighbor
        node_info nd = g.getV().iterator().next();
        nei.add(nd);

        /**
         * while the waiting list(nei) not empty and spare nodes in spare poll the first
         * node from nei and check all its neighbors - if we wont passed this node
         * already we remove this node from spare and add its to list nei.
         */
        while(!spare.isEmpty() && !nei.isEmpty()){
            nd = nei.pollFirst();
            if(spare.containsKey(nd.getKey())){
                spare.remove(nd.getKey());
                for (node_info node: g.getV(nd.getKey()))
                    if(spare.containsKey(node.getKey()))
                        nei.add(node);
            }
        }
        return spare.isEmpty();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        LinkedList<node_info> result = (LinkedList<node_info>) shortestPath(src, dest);
        if(result==null) return -1;
        if(result.size()<2) return 0;
        double path = 0;
        node_info node1 = result.pollFirst(),node2;
        while(!result.isEmpty()){
            node2 = result.pollFirst();
            path += g.getEdge (node1.getKey(), node2.getKey());
            node1 = node2;
        }
        return path;
    }
    /**
     * this method get two nodes represented by keys called src, dest
     * return list's nodes of the shortest path from src to dest
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {////need to fix - edge's weight (not just 1)!
        if (g == null || g.getNode(src) == null || g.getNode(dest) == null) return null;
        if (src == dest) {
            LinkedList<node_info> result = new LinkedList<node_info>();
            result.add(g.getNode(src));
            return result;
        }
        //Characteristics
        HashMap<Integer, Integer> father = new HashMap<Integer, Integer>(); //  [key = node's key | value = father's key]
        HashMap<Integer, LinkedList<Integer>> children = new HashMap<Integer, LinkedList<Integer>>();//  [key = father's key | value = children's keys]
        LinkedList<node_info> waittingList = new LinkedList<node_info>(); //waitting list for node needed checking
        //put src and starting
        father.put(src, -1);
        children.put(src, new LinkedList<>());
        g.getNode(src).setTag(0);
        waittingList.add(g.getNode(src));

        /**
         * while waiting list isn't empty:
         * poll first waiting list's node and do for all is neighbors:
         * if(first time past trough this node)||the distance trough the first polled waiting node to this node < the distance trough the old node's father to this node)
         *   add this node to wiating list
         *   update father and children lists
         *   update node's tag and all the nodes it is father of
         */
        while(!waittingList.isEmpty()){
            node_info nd = waittingList.pollFirst();
            for (node_info node: g.getV(nd.getKey())){// for all nd's neighbor
                if(g.getNode(dest).getTag()==0 || g.getNode(dest).getTag()>nd.getTag()+g.getEdge(nd.getKey(),node.getKey()) ){// if(didnt pasted dest)||(dest.dist > node.newDist)
                    if(!father.containsKey(node.getKey())){//fist time past trough this node
                        father.put(node.getKey(), nd.getKey());
                        children.put(node.getKey(), new LinkedList<>() );// create children list for node
                        children.get(nd.getKey()).add(node.getKey());// add node to be nd's sun

                        node.setTag(nd.getTag()+g.getEdge(nd.getKey(),node.getKey()));//update node's tag
                        waittingList.add(node);
                    }else if(nd.getTag()+g.getEdge(nd.getKey(),node.getKey())<node.getTag()) {// if(node.newTag < node.oldTag)
                        updateChildTag(children,node.getKey(), node.getTag()-(nd.getTag()+g.getEdge(nd.getKey(),node.getKey())));//update SHOSHELET
                        children.get(father.get(node.getKey())).remove(node);// remove node from the old father
                        father.put(node.getKey(),nd.getKey());
                        children.get(nd.getKey()).add(node.getKey());//add node to be children to new father
                    }
                }
            }
        }
        //      to update all the tags to 0!!
        for(int node: father.keySet())
            g.getNode(node).setTag(0);

        if(father.containsKey(dest)){//dest found. create path's list
            LinkedList<node_info> result = new LinkedList<node_info>();
            while (dest != -1) {
                result.addFirst(g.getNode(dest));
                dest = father.get(dest);
            }
            return result;
        }else //no path from src to dest
            return null;
    }
//  this method updates the tag of node's key and all is SHOSHELET(nodes which pasted truogh it) to be (tag-dist)
    private void updateChildTag(HashMap<Integer,LinkedList<Integer>> children,int key,  double dist){
        g.getNode(key).setTag(g.getNode(key).getTag()-dist);
        if(children==null||!children.containsKey(key))return;
        if(children.get(key)==null)return;
        for(int node: children.get(key))
            updateChildTag(children, node, dist);
    }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream fileIn = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileIn);
            out.writeObject(g);
            out.close();
            fileIn.close();
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        try{
            FileInputStream fileN = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(fileN);
            g = (WGraph_DS) input.readObject();
            fileN.close();
            input.close();
//            this.init(g);
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("this is the exception");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(g, that.g);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g);
    }
}
