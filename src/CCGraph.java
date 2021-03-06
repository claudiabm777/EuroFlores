 
// Sample program to find connected components of undirected graph

 
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
 
class CCGraph
{
    static final int MAXV      = 100;
    static final int MAXDEGREE = 50;
    public int       edges[][] = new int[MAXV + 1][MAXDEGREE];
    public int       degree[]  = new int[MAXV + 1];
    public int       nvertices;
    public int       nedges;
 
    CCGraph()
    {
        nvertices = nedges = 0;
        for (int i = 1; i <= MAXV; i++)
            degree[i] = 0;
    }
 
    void read_CCGraph(boolean directed,int numVertices, PriorityQueue<Arco>arcosBosques)
    {
        int x, y;
        //Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of vertices: ");
        nvertices = numVertices;
        System.out.println("Enter the number of edges: ");
        int m = arcosBosques.size();
        System.out.println("Enter the edges: <from> <to>");
        for (int i = 1; i <= m; i++)
        {
        	Arco a=arcosBosques.poll();
            x = a.origen+1;
            y = a.destino+1;
            insert_edge(x, y, directed);
        }
        //sc.close();
    }
 
    void insert_edge(int x, int y, boolean directed)
    {
        if (degree[x] > MAXDEGREE)
            System.out.printf(
                    "Warning: insertion (%d, %d) exceeds max degree\n", x, y);
        edges[x][degree[x]] = y;
        degree[x]++;
        if (!directed)
            insert_edge(y, x, true);
        else
            nedges++;
    }
 
    void print_CCGraph()
    {
        for (int i = 1; i <= nvertices; i++)
        {
            System.out.printf("%d: ", i);
            for (int j = degree[i] - 1; j >= 0; j--)
                System.out.printf(" %d", edges[i][j]);
            System.out.printf("\n");
        }
    }
}
 
