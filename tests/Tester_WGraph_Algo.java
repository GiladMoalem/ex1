package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tester_WGraph_Algo {
    private Tester_WGraph TWD = new Tester_WGraph();
    private Random rand = new Random();


    @Test
    void copy(){
        weighted_graph g = new WGraph_DS();
        TWD.createGraph(g,10,20);
        weighted_graph_algorithms G1 = new WGraph_Algo();
        G1.init(g);
        weighted_graph_algorithms G2 = new WGraph_Algo();
        G2.init (G1.copy());

        weighted_graph g1 = G1.getGraph();
        weighted_graph g2 = G2.getGraph();

        LinkedList<node_info> verG = new LinkedList<>(g1.getV());
        LinkedList<node_info> verG1 = new LinkedList<>(g2.getV());
        while(!verG.isEmpty() && !verG1.isEmpty()){
            node_info node1 = verG.pollFirst();
            node_info node2 = verG1.pollFirst();
            Assertions.assertEquals(node1.getKey(), node2.getKey());
            LinkedList<node_info> nei1 =new LinkedList<>(g.getV(node1.getKey()));//neighbors of node1
            LinkedList<node_info> nei2 =new LinkedList<>(g1.getV(node2.getKey()));//neighbors of node2
            for(node_info node: nei1){
                boolean plag =false;
                for (node_info node3: nei2)
                    if(node.getKey()==node3.getKey()){
                        plag=true;
                        Assertions.assertEquals(g.getEdge(node1.getKey(),node.getKey()) , g1.getEdge(node2.getKey(),node3.getKey()));// the edge's weight the same
                    }
                Assertions.assertTrue(plag);
            }
            for(node_info node: nei2){
                boolean plag =false;
                for (node_info node3: nei1)
                    if(node.getKey()==node3.getKey())
                        plag=true;
                Assertions.assertTrue(plag);
            }
        }
        Assertions.assertEquals(verG.isEmpty(),verG1.isEmpty());
        Assertions.assertEquals(G1.isConnected(),G2.isConnected());
        int v1 = rand.nextInt(g.nodeSize());
        int v2 = rand.nextInt(g.nodeSize());

        Assertions.assertEquals(G1.shortestPathDist(v1,v2),G2.shortestPathDist(v1,v2));
    }
    @Test
    void shortestPath(){
        weighted_graph_algorithms G1 = new WGraph_Algo();//null graph
        Assertions.assertEquals(true,G1.isConnected());
        Assertions.assertEquals(null,G1.shortestPath(0,1));
        Assertions.assertEquals(-1,G1.shortestPathDist(0,1));

        G1.init(new WGraph_DS());//one vertex graph
        G1.getGraph().addNode(0);
        Assertions.assertEquals(true,G1.isConnected());
        Assertions.assertEquals(null,G1.shortestPath(0,1));
        Assertions.assertEquals(0,G1.shortestPath(0,0).get(0).getKey());
        Assertions.assertEquals(1,G1.shortestPath(0,0).size());
        Assertions.assertEquals(0,G1.shortestPathDist(0,0));
        Assertions.assertEquals(-1,G1.shortestPathDist(1,1));
        long start = System.currentTimeMillis();
        weighted_graph g = new WGraph_DS();//connected graph
        createConnectedGraph(g,4000);
        long after = System.currentTimeMillis();
        System.out.println("tack for to mack the graph: " + (after-start)/1000.0+"s'");
        G1.init(g);
        Assertions.assertEquals(true, G1.isConnected());
        System.out.println("tack for check connection: "+ (System.currentTimeMillis()-after)/1000.0+"s'");
    }
    void createConnectedGraph(weighted_graph g, int v){
        int  [] vertex = new int[v];
        for (int i = 0; i < v; i++ ) {
            node_info n = new WGraph_DS.NodeInfo();
            g.addNode(n.getKey());
            vertex[i] = n.getKey();
        }
        for(int i=0;i<v;i++){
            for(int j =0;j<v;j++)
                if(i!=j)
                    g.connect(i,j,rand.nextDouble());
        }

    }
}
