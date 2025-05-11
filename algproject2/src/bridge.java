import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class bridge {
    public static int time;
    public static void main(String[] args) {
        int num;
        int gettemp=0;
        int addtemp=0;
        ArrayList<ArrayList<Integer>> vertex = new ArrayList<>();

        Scanner scan = new Scanner(System.in);

        System.out.print("정점개수를 입력:>");
        num = scan.nextInt();
        for(int i = 0; i<=num;i++){
            vertex.add(new ArrayList<>());
        }
        while(true){
             System.out.print("간선의 양 끝점을 입력하고 엔터를 누르시오:>");
             gettemp = scan.nextInt();
             addtemp = scan.nextInt();
             if(gettemp == -1 && addtemp == -1){
                 break;
             }
             vertex.get(gettemp).add(addtemp);
             vertex.get(addtemp).add(gettemp);
        }
        findBridges(vertex, num);
        System.out.println("정점 두개를 입력하여 브릿지인지 판별하시오 (-1 -1 입력 시 종료):");
        while (true) {
            int u = scan.nextInt();
            int v = scan.nextInt();
            if (u == -1 && v == -1) break;
            if (isBridge(vertex, num, u, v)) {
                System.out.println(u + " - " + v + " is a bridge.");
            } else {
                System.out.println(u + " - " + v + " is not a bridge.");
            }
        }
    }
    private static void findBridges(ArrayList<ArrayList<Integer>> graph, int vertices) {
        boolean visited[] = new boolean[vertices];
        int discoveryTime[] = new int[vertices];
        int low[] = new int[vertices];
        int parent[] = new int[vertices];
        Arrays.fill(parent, -1);
        time = 0;

        System.out.println("Bridges in the graph:");
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                dfs(i, visited, discoveryTime, low, parent, graph);
            }
        }
    }
    private static void dfs(int u, boolean[] visited, int[] discoveryTime, int[] low, int[] parent, ArrayList<ArrayList<Integer>> graph) {
        visited[u] = true;
        discoveryTime[u] = low[u] = ++time;
        int v;
        for (int i = 0; i< graph.get(u).size();i++) {
            v = graph.get(u).get(i);
            if (!visited[v]) {
                parent[v] = u;
                dfs(v, visited, discoveryTime, low, parent, graph);

                low[u] = Math.min(low[u], low[v]);
                if (low[v] > discoveryTime[u]) {
                    System.out.println(u + " - " + v);
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], discoveryTime[v]);
            }
        }
    }
    private static boolean isBridge(ArrayList<ArrayList<Integer>> graph, int vertices, int u, int v) {
        graph.get(u).remove((Integer) v);
        graph.get(v).remove((Integer) u);

        boolean[] visited = new boolean[vertices];
        dfsConnectivity(0, visited, graph);

        graph.get(u).add(v);
        graph.get(v).add(u);

        return (!visited[v]||!visited[u]);
    }
    private static void dfsConnectivity(int u, boolean[] visited, ArrayList<ArrayList<Integer>> graph) {
        visited[u] = true;
        int v;
        for (int i = 0; i< graph.get(u).size();i++) {
            v = graph.get(u).get(i);
            if (!visited[v]) {
                dfsConnectivity(v, visited, graph);
            }
        }
    }
}
