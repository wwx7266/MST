import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class mst {

    Graph graphs[];

    mst(int number, int graphSize){
        graphs = new Graph[number];
        for(int i = 0; i < number; i++){
            graphs[i] = new Graph(graphSize);
        }
//        for(Graph g : graphs){
//            g = new Graph(graphSize);
//        }
    }

    public class Graph{
        int V; // number of nodes
        double[][] adj; //adj matrix
        Node[] nodes;


        Graph(int V){
            System.out.println("graph size is = "+ V);
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
                double dx = nodes[from].x - nodes[to].x;System.out.println();
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

        public double KruskalMST(){
            Edge results[] = new Graph.Edge[V];
            Edge edges[] = new Graph.Edge[(V-1)*(V)/2];
            Subset subsets[] = new Subset[V];
            int index = 0;
            for(int i = 0; i < V; i++){
                subsets[i] = new Subset(i);
                for(int j=0; j < V; j++){
                    edges[index] = new Edge(i, j);
                    index++;
                }
            }

            Arrays.sort(edges);

            int e=0;
            int i = 0;
            while(e < V -1){
                Edge next = edges[i++];
                int x = find_set(subsets, next.from);
                int y = find_set(subsets, next.to);
                if(x != y){
                    results[e++] = next;
                    union_set(subsets, x, y);
                }
            }
            int sum = 0;
            for(Edge edge : results){
                sum += edge.weight;
            }
            return sum;
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


    public static void main(String[] args) {
        Random r = new Random();
        double sum_size_100 = 0;
        int p_size_100 = r.nextInt(50)+50;
        System.out.println(p_size_100);
        mst test_size_100 = new mst(p_size_100, 2);
        System.out.println("size=" + test_size_100.graphs[0].V);
        for(Graph g: test_size_100.graphs){
            sum_size_100 += g.KruskalMST();
        }
        System.out.println(sum_size_100/p_size_100);

    }

}
