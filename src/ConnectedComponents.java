import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ConnectedComponents
{
    static final int MAXV         = 100;
    static boolean   processed[]  = new boolean[MAXV];
    static boolean   discovered[] = new boolean[MAXV];
    static int       parent[]     = new int[MAXV];
    public static List<List<Integer>>listaComponentes=new ArrayList<List<Integer>>();
 
    static void bfs(CCGraph g, int start,List<Integer>componente)
    {
        Queue<Integer> q = new LinkedList<Integer>();
        int i, v;
        q.offer(start);
        discovered[start] = true;
        while (!q.isEmpty())
        {
            v = q.remove();
            process_vertex(v,componente);
            processed[v] = true;
            for (i = g.degree[v] - 1; i >= 0; i--)
            {
                if (!discovered[g.edges[v][i]])
                {
                    q.offer(g.edges[v][i]);
                    discovered[g.edges[v][i]] = true;
                    parent[g.edges[v][i]] = v;
                }
            }
        }
    }
 
    static void initialize_search(CCGraph g)
    {
        for (int i = 1; i <= g.nvertices; i++)
        {
            processed[i] = discovered[i] = false;
            parent[i] = -1;
        }
    }
 
    static void process_vertex(int v,List<Integer>componente)
    {
    	componente.add(v-1);
        System.out.printf(" %d", v-1);
    }
 
    static void connected_components(CCGraph g)
    {
        int c;
        initialize_search(g);
        c = 0;
        for (int i = 1; i <= g.nvertices; i++)
        {
            if (!discovered[i])
            {
            	List<Integer>componente=new ArrayList<Integer>();;
            	
                c++;
                System.out.printf("Component %d:", c);
                bfs(g, i,componente);
                System.out.printf("\n");
                listaComponentes.add(componente);
            }
        }
    }
 
//    static public void main(String[] args)
//    {
//        
//    }
}
