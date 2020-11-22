Class Graph_DS
This class represents an undirectional weighted graph based on a hashmap. 

public Graph_DS()
default constructor, create new empty graph

public Graph_DS(graph g)
Copy constructor. Copyis a graph with all its data(EdgeCount, ModeCount) and all its neighbor by value

public node_info getNode(int key)
returns a node from the graph represented with this key

public boolean hasEdge(int node1, int node2)
checks if there are edge between two nodes from the graph

public boolean hasEdge(int node1, int node2)
returns the weight between this two nodes(node1,node2)
if there is no edge returns -1

public void addNode(node_info n)
 adds a node to a graph 

public void connect(int node1, int node2, double w)
connects two nodes of specific graph with edge weight(w) between them

public Collection<node_info> getV()
returns a collection of all the nodes belongs the graph

public Collection<node_info> getV(int node_id)
returns collection of all the neighbor of specific node

public node_info removeNode(int key)
removes specific node from the graph and all the edges ends or begins in it

public void removeEdge(int node1, int node2)
removes a edge between two nodes in the graph

public int nodeSize()
returns a count of all the nodes in the graph



Class WGraph_Algo
This class represents a graph algorithms that support the following functions:

public WGraph_Algo()
Default constructor. Create new Graph_Algo with empty graph 

public void init(weighted_graph g)
Init the graph on which this set of algorithms operates on

public weighted_graph copy()
Compute a deep copy of this graph

public boolean isConnected()
Check if the graph is connected (there is a valid path from every node to each other node)

public double shortestPathDist(int src, int dest)
Receive two keys of nodes – sourc and destiny – and returns the shortest path weight between this two nodes.
If there is no path between this two returns  -1

public List<node_info> shortestPath(int src, int dest)
Receive two nodes represented by keys called src, dest and returns list's nodes of the shortest path from src to dest
If there is path between this two node returns empty list

