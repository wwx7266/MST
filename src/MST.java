import java.util.ArrayList;

public class MST {

    private double getWeight(Node from, Node to){
        double dx = from.x - to.x;
        double dy = from.y - to.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    private class Graph{
        ArrayList<Node> V;
        ArrayList<Edge> E;

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
        public int compareTo(Edge compareEdge)
        {
            return (int)(this.weight-compareEdge.weight);
        }

    }

    private class Node{
        double x;
        double y;

        Node(double x, double y){
            x = x;
            y = y;
        }
    }

}
