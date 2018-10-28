import java.util.*;


public class mst {

    Graph g;
    long KruskalRuningTiem, PrimeRunningTime;

    mst(int size, boolean flag){
        KruskalRuningTiem = 0;
        PrimeRunningTime = 0;
        if(flag==false)
            g = new Graph(size);
        else
            g = new Graph(size, flag);
    }


    public double getKruskalMSTweight(){
        return g.KruskalMST();
    }

    public double getPrimsMSTweight(){
        return g.PrimMST();
    }

    public class Graph{
        int V; // number of nodes
        Set<Edge>[] adj; //adj matrix
        Node[] nodes;

        //Graph generation for (i)
        Graph(int V){
            this.V = V;
            adj = new Set[V];
            nodes = new Node[V];
            for(int i=0; i < V; i++){
                nodes[i] = new Node(i);
                adj[i] = new HashSet<>();
            }
            for(int i = 0; i < V; i++){
                for(int j = i+1; j < V; j++){
                    addEdge(new Edge(i, j));
                }
            }
        }

        //generate randomly connected graph
        Graph(int V, boolean flag){

            int bound = V;
            boolean added[] = new boolean[V];


            this.V = V;
            adj = new Set[V];

            ArrayList<Node> vetexs = new ArrayList<>();
            nodes = new Node[V];
            for(int i=0; i < V; i++){
                nodes[i] = new Node(i);
                adj[i] = new HashSet<>();
                added[i] = false;
                vetexs.add(nodes[i]);
            }

            int index = (int) ((Math.random() * vetexs.size()));
            Node pre = vetexs.get(index);
            vetexs.remove(index);

            index = (int) ((Math.random() * vetexs.size()));
            Node next = vetexs.get(index);
            vetexs.remove(index);

            addEdge(new Edge(pre.index, next.index));
            while(!vetexs.isEmpty()){
                index = (int) ((Math.random() * vetexs.size()));
                next = vetexs.get(index);
                vetexs.remove(index);
                addEdge(new Edge(pre.index, next.index));
                pre = next;
            }


        }



        public Set<Edge> getAllEdges(){
            Set<Edge> ret= new HashSet<>();
            for(int i =0; i < V; i++){
                ret.addAll(adj[i]);
            }
            return ret;
        }

        public void addEdge(Edge edge){
            adj[edge.from].add(edge);
            adj[edge.to].add(edge);
        }

        public class Edge implements Comparable<Edge>{
            int from;
            int to;
            double weight;

            Edge(int from, int to){
                this.from = from;
                this.to = to;
                double dx = nodes[from].x - nodes[to].x;
                double dy = nodes[from].y - nodes[to].y;
                weight = Math.sqrt(dx*dx + dy*dy);
            }

            double getWeight(){
                return weight;
            }

            public int compareTo(Edge compareEdge)
            {
                return (int)(this.weight-compareEdge.weight);
            }

            public int getOther(int node){
                if(this.from == node)
                    return to;
                else if(this.to == node)
                    return from;
                return -1;
            }
        }

        public class Node{
            int index;
            double x;
            double y;

            Node(int index){
                this.index = index;
                double max = 1;
                double min = 0;
                Random r = new Random();
                x = min +  (max - min) * r.nextDouble();
                y = min +  (max - min) * r.nextDouble();
            }
        }

        public double KruskalMST(){
            long startTime = System.currentTimeMillis();
            ArrayList<Edge> results = new ArrayList<>();
            Set<Edge> edges = getAllEdges();
            PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparing(Edge::getWeight));
            pq.addAll(edges);
            Subset sb = new Subset(V);

            while(!pq.isEmpty() && results.size() < V -1){
                Edge next = pq.poll();
                int x = next.from;
                int y = next.to;
                if(sb.isConnected(x,y)){
                    continue;
                }
                sb.union_set(x, y);
                results.add(next);
            }
            double sum = 0;
            for(Edge edge : results){
                sum += edge.weight;
            }
            long endTime = System.currentTimeMillis();
            KruskalRuningTiem = endTime - startTime;
            return sum;
        }

        public int findMinKey(double key[], boolean marked[]){
            double min = Double.MAX_VALUE;
            int index = -1;
            for(int i=0; i < V; i++){
                if(marked[i] == false && key[i] < min){
                    min = key[i];
                    index = i;
                }
            }
            return index;
        }

        public double findConnectedWeight(int from, int to){
            if(from == to) return 0;
            Set<Edge> edges = adj[from];
            for(Edge e : edges){
                if(e.getOther(from) == to)
                    return e.weight;
            }
            return -1;
        }

        public double PrimMST(){
            long startTime = System.currentTimeMillis();
            boolean marked[] = new boolean[V];
            int pi[] = new int[V];
            double key[] = new double[V];

            //init state
            for(int i =0; i < V; i++){
                key[i] = Double.MAX_VALUE;
                pi[i] = -1;
                marked[i] = false;
            }

            key[0] = 0;
            pi[0] = -1;

            for(int i =0; i < V-1; i++){
                int u = findMinKey(key, marked);
                marked[u] = true;

                for(int v=0; v < V; v++){
                    if(u == v) continue;
                    double weight = findConnectedWeight(u, v);
                    if(weight != -1 && marked[v] == false && weight < key[v]){
                        pi[v] = u;
                        key[v] = weight;
                    }
                }
            }
            double sum = 0;
            for(int i =1; i < V; i++){
                sum += findConnectedWeight(i, pi[i]);
            }
            long endTime = System.currentTimeMillis();
            PrimeRunningTime = endTime - startTime;
            return sum;
        }
    }

    private class Subset{
        int parent[];
        int rank[];
        int count;

        Subset(int n){
            count = n;
            parent = new int[n];
            rank = new int[n];
            for(int i = 0; i < n; i++){
                parent[i] = i;
                rank[i] = 0;
            }
        }

        private boolean isConnected(int p, int q){
            return find_set(p) == find_set(q);
        }

        private int find_set( int i){
            while (i != parent[i]){
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        private void union_set(int i, int j){
            int root_1 = find_set(i);
            int root_2 = find_set(j);

            if(rank[root_1]<  rank[root_2])
                parent[root_1] = root_2;
            else if(rank[root_1] >  rank[root_2])
                parent[root_2] = root_1;
            else if(rank[root_1] == rank[root_2]){
                parent[root_2] = root_1;
                rank[root_1]++;
            }
        }
    }




    public static void main(String[] args) {
        Random r = new Random();
        int p = 0;
        double sum = 0;
        double PrimSum = 0;

        long KruskalTime = 0;
        long PrimeTime = 0;

        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(100, false);
            sum += test.getKruskalMSTweight();
        }
        System.out.println("n=100 p=" + p + "\n" + sum/p);

        sum = 0;
        PrimSum = 0;
        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(500, false);
            sum += test.getKruskalMSTweight();

        }
        System.out.println("n=500 p=" + p + "\n" + sum/p);

        sum = 0;
        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(1000, false);
            sum += test.getKruskalMSTweight();
        }
        System.out.println("n=1000 p=" + p + "\n" + sum/p);

        sum = 0;
        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(5000, false);
            sum += test.getKruskalMSTweight();
        }
        System.out.println("n=5000 p=" + p + "\n" + sum/p);


        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(100, true);
            sum += test.getKruskalMSTweight();
            PrimSum += test.getPrimsMSTweight();

            KruskalTime += test.KruskalRuningTiem;
            PrimeTime += test.PrimeRunningTime;
        }
        System.out.println("n=100 p=" + p + "  Kruskal: " + sum/p + " VS Prime: " + PrimSum/p);
        System.out.println("Running Time compare  Kruskal: " + KruskalTime/p + "ms VS Prime: " + PrimeTime/p + "ms");

        sum = 0;
        PrimSum = 0;
        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(500, true);
            sum += test.getKruskalMSTweight();
            PrimSum += test.getPrimsMSTweight();
            KruskalTime += test.KruskalRuningTiem;
            PrimeTime += test.PrimeRunningTime;
        }
        System.out.println("n=100 p=" + p + "  Kruskal: " + sum/p + " VS Prime: " + PrimSum/p);
        System.out.println("Running Time compare  Kruskal: " + KruskalTime/p + "ms VS Prime: " + PrimeTime/p + "ms");


        sum = 0;
        PrimSum = 0;
        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(1000, true);
            sum += test.getKruskalMSTweight();
            PrimSum += test.getPrimsMSTweight();
            KruskalTime += test.KruskalRuningTiem;
            PrimeTime += test.PrimeRunningTime;
        }
        System.out.println("n=100 p=" + p + "  Kruskal: " + sum/p + " VS Prime: " + PrimSum/p);
        System.out.println("Running Time compare  Kruskal: " + KruskalTime/p + "ms VS Prime: " + PrimeTime/p + "ms");


        sum = 0;
        PrimSum = 0;
        p = 50 + r.nextInt(10);
        for(int i=0; i < p; i++){
            mst test = new mst(5000, true);
            sum += test.getKruskalMSTweight();
            PrimSum += test.getPrimsMSTweight();
            KruskalTime += test.KruskalRuningTiem;
            PrimeTime += test.PrimeRunningTime;
        }
        System.out.println("n=100 p=" + p + "  Kruskal: " + sum/p + " VS Prime: " + PrimSum/p);
        System.out.println("Running Time compare  Kruskal: " + KruskalTime/p + "ms VS Prime: " + PrimeTime/p + "ms");
    }

}
