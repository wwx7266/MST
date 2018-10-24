import java.util.ArrayList;
import java.util.Random;

public class MST {

    private double getWeight(Node from, Node to){
        double dx = from.x - to.x;
        double dy = from.y - to.y;
        return Math.sqrt(dx*dx + dy*dy);
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

    private class Graph{
        int V;
        ArrayList<Node> nodes;
        ArrayList<Edge> E;

        Graph(int size){
            V = size;
            nodes = new ArrayList<>();
            E = new ArrayList<>();
            double max = 1;
            double min = 0;
            Random r = new Random();
            double randomValue = min +  (max - min) * r.nextDouble();
            for(int i= 0; i < V; i++){
                double x = min +  (max - min) * r.nextDouble();
                double y = min +  (max - min) * r.nextDouble();
                nodes.add(new Node(i, x, y));
            }
            generatEdge();
        }

        public void addEdge(Node from, Node to){
            E.add(new Edge(from, to));
        }

        public boolean addNode(Node node){
            if(nodes.contains(node))
                return false;
            nodes.add(node);
            return true;
        }

        public void generatEdge(){
          for(int i = 0; i < V-1; i++){
              for(int j=i+1; j < V-1; j++){
                  E.add(new Edge(nodes.get(i), nodes.get(j)));
              }
          }
        }
    }

    private class Edge implements Comparable<Edge> {
        Node from;
        Node to;
        Double weight;
        Edge(Node from, Node to){
            from = from;
            to = to;
            weight = getWeight(from, to);
        }
        Edge(){}
        public int compareTo(Edge compareEdge)
        {
            return (int)(this.weight-compareEdge.weight);
        }

    }

    private class Node{
        int index;
        double x;
        double y;

        Node(int index, double x, double y){
            index = index;
            x = x;
            y = y;
        }
    }

    void find_MST(Graph g){
        int e = 0;
        Edge result[] = new Edge[g.V];
        for(int i = 0; i < g.V; i++){
            result[i] = new Edge();
        }

        g.E.sort(Edge::compareTo);

        Subset subsets[] = new Subset[g.nodes.size()];
        for(int i = 0; i < g.nodes.size(); i++){
            subsets[i] = new Subset(i);
        }
        int i = 0;
        while (e < g.V -1){
            Edge next = g.E.get(i++);

            int x = find_set(subsets, next.from.index);
            int y = find_set(subsets, next.to.index);

            if(x != y){
                result[e++] = next;
                union_set(subsets, x, y);
            }
        }
        
    }


}
