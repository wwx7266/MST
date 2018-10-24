import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class mst {

    public class Graph{
        int V; // number of nodes
        double[][] adj; //adj matrix
        Node[] nodes;


        Graph(int V){
            this.V = V;
            adj = new double[V][V];
            nodes = new Node[V];
            for(int i=0; i < V; i++){
                nodes[i] = new Node(i);
            }
            for(int i = 0; i < V; i++){
                for(int j = 0; j < V; j++){
                    if(i == j) adj[i][j]=0;
                    else
                        adj[i][j] = (new Edge(i, j)).weight;
                }
            }
        }

        public class Edge implements Comparable<Edge>{
            int from;
            int to;
            double weight;

            Edge(int from, int to){
                from = from;
                to = to;
                double dx = nodes[from].x - nodes[to].x;
                double dy = nodes[from].y - nodes[to].y;
                weight = Math.sqrt(dx*dx + dy*dy);
            }

            public int compareTo(Edge compareEdge)
            {
                return (int)(this.weight-compareEdge.weight);
            }
        }

        public class Node{
            int index;
            double x;
            double y;

            Node(int index){
                index = index;
                double max = 1;
                double min = 0;
                Random r = new Random();
                x = min +  (max - min) * r.nextDouble();
                y = min +  (max - min) * r.nextDouble();
            }
        }
    }

    private class Subset{
        int parent;
        int rank;

        Subset(int node){
            parent = node;
            rank = 0;
        }
    }

    private int find_set(Subset subsets[], int i){
        if(subsets[i].parent != i)
            subsets[i].parent = find_set(subsets, subsets[i].parent);
        return subsets[i].parent;
    }

    private void union_set(Subset subsets[], int i, int j){
        int root_1 = find_set(subsets, i);
        int root_2 = find_set(subsets, j);

        if(subsets[root_1].rank <  subsets[root_2].rank)
            subsets[root_1].parent = root_2;
        else if(subsets[root_1].rank >  subsets[root_2].rank)
            subsets[root_2].parent = root_1;
        else if(subsets[root_1].rank == subsets[root_2].rank){
            subsets[root_2].parent = root_1;
            subsets[root_1].rank++;
        }
    }

    public void KruskalMST(Graph g){
        Graph.Edge results[] = new Graph.Edge[g.V];
        Subset subsets[] = new Subset[g.V];
        for(Graph.Node n : g.nodes){

        }
    }


}
