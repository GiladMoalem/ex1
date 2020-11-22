package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class Tester_WGraph {
    private Random rand = new Random();
    @Test
    void nodeSize() {
        weighted_graph g = new WGraph_DS();
        Assertions.assertEquals(0, g.nodeSize());
        g.addNode(2);
        g.addNode(1);
        g.addNode(1);
        Assertions.assertEquals(2,g.nodeSize());
        g.removeNode(3);
        g.removeNode(1);
        g.removeNode(1);
        int s = g.nodeSize();
        Assertions.assertEquals(1,s);
    }

    @Test
    void edgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(1,0,1.5);
        g.connect(0,0,4);
        g.connect(0,6,2);
        int e_size =  g.edgeSize();
        Assertions.assertEquals(3, e_size);
        double w01 = g.getEdge(0,1);
        double w10 = g.getEdge(1,0);
        Assertions.assertEquals(w01, w10);
        Assertions.assertEquals(w01, 1.5);
    }
    @Test
    void getV() {
        weighted_graph g = new WGraph_DS();
        createGraph(g,10,45);

//        System.out.println(g.getV()+"\nedgeSize: "+g.edgeSize());
        Collection<node_info> v = g.getV();
        Iterator<node_info> iter = v.iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            Assertions.assertNotNull(n);
            Assertions.assertEquals(g.getNode(n.getKey()),n);
        }
    }
    @Test
    void copyGraph(){
        weighted_graph g = new WGraph_DS();
        createGraph(g,10, 20);
        weighted_graph g1 = new WGraph_DS(g);
        LinkedList<node_info> verG = new LinkedList<>(g.getV());
        LinkedList<node_info> verG1 = new LinkedList<>(g1.getV());
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
    }
    @Test
    void remove(){
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.addNode(6);
        g.addNode(7);
        g.connect(0,1,1.1);
        g.connect(0,2,-1);
        g.connect(0,3,1);
        g.connect(0,2,8);
        g.connect(1,0,1.1);
        g.connect(3,4,12);
        g.connect(7,1,9);
        Assertions.assertEquals(8,g.nodeSize());
        Assertions.assertEquals(5,g.edgeSize());
        Assertions.assertEquals(13,g.getMC());
        g.removeNode(0);
        Assertions.assertEquals(7,g.nodeSize());
        Assertions.assertEquals(2,g.edgeSize());
        Assertions.assertEquals(14,g.getMC());

    }

    void createGraph(weighted_graph g,Integer v, int e){
        for (int i = 0; i < v; i++ )
            g.addNode(new WGraph_DS.NodeInfo().getKey());

        node_info[] vertex = new node_info[g.nodeSize()];
        LinkedList<node_info> ll = new LinkedList<node_info>(g.getV());

        for (int i = 0; i < vertex.length; i++) {
            vertex[i] = ll.pollFirst();
        }
        int num1, num2;
        for (int i=0; i < e; i++){
            num1 = rand.nextInt(vertex.length);
            num2 = rand.nextInt(vertex.length);
            while(num1==num2)
                num2 = rand.nextInt(vertex.length);
            if(!g.hasEdge(num1,num2))
                g.connect(num1,num2, rand.nextDouble());
            else
                i--;
        }
    }

}
