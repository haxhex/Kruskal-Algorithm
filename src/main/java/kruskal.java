/*
* Minimum Spanning Tree : Kruskal’s algorithm
* Helia Ghorbani
* 9824353
* samples : inputs & outputs at the end
* image of samples in folder samples
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class kruskal {

// edge class (create class for edges)
// implement Comparable interface for this class to sort edges base on their weights by using compareTo function
    public static class Edge implements Comparable<Edge> {
// parameters of edge class
// each edges has two vertices & a weight
        int node1;
        int node2;
        int weight;
// constructor edge: (class : edge) , get three parameters to creat a new edge
        public Edge(int node1, int node2, int weight) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }


// use toString function to display result (mst)
        public String toString() {
            return this.node1 + "\t" + this.node2 + "\t" + this.weight;
        }

// comparator function used for sorting edges based on their weight
        @Override
        public int compareTo(Edge edge) {
            return this.weight - edge.weight;
        }
    }

// create a hashmap to store vertices and assign each vertex an integer to find parent
    static HashMap<String, Integer> vertex = new HashMap<>();

    public static void kruskalAlgorithm(int numOfNodes, Edge[] Edges, int[][] graph) {

// create a 2D array mst for storing result -> formed minimum spanning tree
        int[][] mst = new int[graph.length][graph.length];
// sort edges base on their assigning weights : ascending (increasing order)
        Arrays.sort(Edges);

// label each node with main root (parent) & size
        int[] parents = new int[numOfNodes + 1];
        int[] size = new int[numOfNodes + 1];
// initialize parent and size arrays in this for loop
// At start of Kruskal every node is put into own cloud
// Decorates every vertex with its parent ptr & rank
        for (int vertex = 1; vertex < graph.length; vertex++) {
// each nodes parent set its own value
            parents[vertex] = vertex;
// each nodes size set 1
            size[vertex] = 1;
        }

        int edgeNumber = 0;
        int usedEdge = 1;

// for connecting all the nodes need to have at least ((number of nodes) - 1) edges
        while (usedEdge <= numOfNodes - 1) {
// creat a new edge and store value of edges array with index edgeNumber in it
            Edge edge = Edges[edgeNumber];
// increment index of edges array - update it
            edgeNumber++;
// check if adding current edge to minimum spanning tree  make cycle and if true ignore this edge and don't add to mst
            if (checkCycle(edge.node1, edge.node2, parents))
                continue;
// for adding edge to mst :
// find the parents of both nodes (vertices) -> node1, node2
// merge (combine) smaller set (means set with smaller size) with larger set (means set with larger size)
// smaller link to larger
// inputs of merge functions :
// parentFinder(edge.node1, parents) -> find parent of first node (node1)
// parentFinder(edge.node2, parents) -> find parent of second node (node2)
// parents -> at first equal to each vertex then updated with larger size
// size -> at first all of them equal 1 then updated after connecting nodes
            merge(parentFinder(edge.node1, parents), parentFinder(edge.node2, parents), parents, size);
// after merging smaller with larger adding edge to mst (node1, node2 -> edge) => wight
            mst[edge.node1][edge.node2] = edge.weight;
            usedEdge++;
        }

// displaying mst
// HashMap <String, Integer> vertex -> <key, value>
// Using Entry to find the key of vertex HashMap according to Integer value (assigned each node integer value) -> find corresponding node by this integer value

// find corresponding node by this integer value -> node1
        String node1 = null, node2 = null;
        for (int n1 = 1; n1 < mst.length; n1++) {
            for (int n2 = 0; n2 < mst.length; n2++) {
                if (mst[n1][n2] != 0) {
                    for(Entry<String, Integer> entry: vertex.entrySet()) {
                        if(entry.getValue() == n1) {
                            node1 = entry.getKey();
                            break;
                        }
                    }

// find corresponding node by this integer value -> node2
                    for(Entry<String, Integer> entry: vertex.entrySet()) {
                        if(entry.getValue() == n2) {
                            node2 = entry.getKey();
                            break;
                        }
                    }

// print mst -> (node1(corresponding integer value = n1), node2(corresponding integer value = n2)) -> edge (which added to spanning tree)
// weight of edge : store in 2D array mst with index n1(corresponding node1) and n2(corresponding node2)
                    System.out.println(node1 + " " + node2 + " " + mst[n1][n2]);
                }
            }
        }

    }

    // this function check if the parent(main root) of node1 and node2 are same this means they are connected to common vertex so
// if we add this edge to MST a cycle will be created.
    public static boolean checkCycle(int node1, int node2, int[] parents) {
        return parentFinder(node1, parents) == parentFinder(node2, parents);
    }
// Implementing Union-Find
// this function find the parents of input nodes (node1, node2 -> edge) (vertices) and merge lager set with smaller set (base on size)
    public static void merge(int node1, int node2, int[] parents, int[] size) {
// find the parent of node1 (first vertex)
        node1 = parentFinder(node1, parents);
// find the parent of node2 (second vertex)
        node2 = parentFinder(node2, parents);
// merge lager set with smaller set (base on size)
// if size of node1 lager than size of node2 set parent of node2 parent of node1 and increment size of node1
        if (size[node1] > size[node2]) {
            parents[node2] = node1;
            size[node1] += size[node2];
        }
// if size of node2 lager than size of node1 set parent of node1 parent of node2 and increment size of node2
        else {
            parents[node1] = node2;
            size[node2] += size[node1];
        }
    }

    public static int parentFinder(int node, int[] parents) {
// if the parent of input node (vertex) itself return the input node as parent
        if (parents[node] == node) {
            return node;
        }
// if the parent of input node doesn't equal itself traverse a path by recursion
// to reach the parent (a node which parent equal itself)
        else {
            parents[node] = parentFinder(parents[node], parents);
            return parents[node];
        }
    }
    public static void main(String[] args) {
// define scanner to get input from user
        Scanner scanner = new Scanner(System.in);
// assign a large number as a number of nodes
        int nodes = 10_000;

// creat adjacency matrix (dimensions: number of nodes)
       int[][] graph = new int[nodes][nodes];

// assign a large number as a number of edges
        int numEdges =10_000;
// creat array of type edge to store edges
        Edge[] Edges = new Edge[numEdges];
// set number of nodes 1 and number of edge 0 (at least one vertex)
        int numOfNodes = 1;
        int numOfEdges = 0;
// this for loop stop when input equals (-1)  (numEdges is too large)
        for (int edge = 0; edge < numEdges; edge++) {
// get first node from user and store it in node1
            String node1 = scanner.next();
// if first input of a line equals (-1) stop
            if (node1.equalsIgnoreCase("-1"))
                break;
// get first node from user and store it in node2
            String node2 = scanner.next();
// store nodes(vertices) in a hashmap and ignore repetitious nodes (avoid storing one vertex with two assigning integer)
// increment number of  nodes if the condition is true
            if (!vertex.containsKey(node1)){
                vertex.put(node1, numOfNodes);
                numOfNodes++;
            }
            if (!vertex.containsKey(node2)){
                vertex.put(node2, numOfNodes);
                numOfNodes++;
            }
// get the weight of current edge
            int weight = scanner.nextInt();
// assume graph will be bidirectional so node1 is neighbour of node2 and vice-versa
// and assign weight to index [node1][node2] = [node2][node1]
            graph[vertex.get(node1)][vertex.get(node2)] = graph[vertex.get(node2)][vertex.get(node1)] = weight;

// add generated edge (node1, node2, weight) to array edges
// using a constructor with three inputs (node1(assigned integer), node2(assigned integer), weight)
// increment number of  edges at each step
            Edges[edge] = new Edge(vertex.get(node1), vertex.get(node2), weight);
            numOfEdges++;
        }

// the size of current edges array is not true (some of them are null)
// so creat new array of edges with size number of arrays and copy edges to it
        Edge[] newEdges = new Edge[numOfEdges];
        if (numOfEdges >= 0) System.arraycopy(Edges, 0, newEdges, 0, numOfEdges);

// the size of current graph is not true (some of them are null)
// so creat new adjacency matrix dimensions number of nodes and copy elements to it
        int[][] newGraph = new int[numOfNodes][numOfNodes];
        for (int j = 0; j < newGraph.length; j++) {
            System.arraycopy(graph[j], 0, newGraph[j], 0, newGraph.length);

        }

// applying kruskal algorithm with kruskalAlgorithm with inputs number of nodes (vertices), array of edges, adjacency graph
        kruskalAlgorithm(numOfNodes-1, newEdges, newGraph);

    }

}

/*
* samples:
input 1:
A B 6
A E 7
A F 3
B C 9
B E 5
B F 4
C D 8
C E 8
D E 1
E F 2
-1
output 1:
A F 3
B F 4
E F 2
C D 8
D E 1
********
input 2:
A B 1
A C 4
A D 1
B C 2
D C 2
C E 3
C G 4
E G 2
G F 1
-1
output 2:
A B 1
A D 1
B C 2
C E 3
E G 2
G F 1
********
input 3:
A B 4
A C 2
C B 1
C E 4
B D 8
E D 2
E F 7
D F 1
-1
output 3:
A C 2
C B 1
C E 4
E D 2
D F 1
*******
input 4:
A B 1
A D 3
B D 5
B E 1
B C 6
C E 5
C F 2
D E 1
F E 4
-1
output 4:
A B 1
B E 1
D E 1
C F 2
F E 4
********
input 5:
a b 4
b c 8
a h 8
b h 11
h i 7
h g 1
i g 6
i c 2
c d 7
c f 4
d e 9
d f 14
e f 10
g f 2
-1
output 5:
a b 4
b c 8
c d 7
c f 4
h g 1
i c 2
g f 2
d e 9
*******
input 6:
A B 6
A F 3
A G 6
A C 10
B F 2
C G 1
C D 7
D G 5
D H 4
D E 3
E H 4
F G 1
G H 9
-1
output 6:
A F 3
B F 2
F G 1
C G 1
D G 5
D H 4
D E 3
*******
input 7:
C E 2
D F 3
B C 4
E F 4
B D 4
A B 4
A D 5
B E 6
B F 8
-1
output 7:
C E 2
E F 4
D F 3
B C 4
A B 4

 */
