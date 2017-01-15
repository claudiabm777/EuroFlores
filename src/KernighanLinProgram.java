

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class KernighanLinProgram {
  static public void main(String[] args) {
    Double[][] weights = {
//            {0.0, 10000.0, 0.0, 0.0},
//            {10000.0, 0.0, 1000.0, 4.0},
//            {0.0, 1000.0, 0.0, 300000.0},
//            {0.0, 994.0, 300000000000.0, 0.0}
    		 {0.0, 1000.0, 0.0, 0.0, 0.0, 0.0},
             {1000.0, 0.0, 1.0, 4.0, 0.0, 0.0},
             {0.0, 10.0, 0.0, 30.0, 0.0, 0.0},
             {5.0, 9.0, 30.0, 0.0, 0.0, 0.0},
             {5.0, 9.0, 30.0, 0.0, 0.0, 989890.0},
             {977775.0, 9.0, 30.0, 0.0, 0.0, 0.0}
    };

    List<List<Integer>> ans = KernighanLinProgram(weights);
    List<Integer> groupA = ans.get(0);
    List<Integer> groupB = ans.get(1);

    System.out.println(groupA.toString());

    System.out.println(groupB.toString());
    
  }
  
  public static List<List<Integer>> KernighanLinProgram(Double[][] weights) {



    KernighanLin k = KernighanLin.process(graphFromMatrix(weights));

    List<Integer> groupA = new ArrayList<Integer>();
    List<Integer> groupB = new ArrayList<Integer>();
    List<List<Integer>> ans = new ArrayList<>();


    for (Vertex x : k.getGroupA())
      groupA.add(x.name);
    for (Vertex x : k.getGroupB())
      groupB.add(x.name);

    ans.add(groupA);
    ans.add(groupB);
   //System.out.println("Cut cost: "+k.getCutCost());

    return ans;
  }
  
  public static Graph graphFromFile(String filename) throws IOException {
    FileReader fileReader = new FileReader(filename);
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    Graph g = fromReadable(bufferedReader);
    bufferedReader.close();

    return g;
  }

  public static Graph graphFromMatrix(Double[][] weights) {
    Graph graph = new Graph();
    HashMap<Integer, Vertex> names = new HashMap<Integer, Vertex>();

    int len = weights.length;

    for(int i = 0; i < len; i++) {
      Vertex v = new Vertex(i);
      graph.addVertex(v);
      names.put(i, v);
    }

    for(int i = 0; i < len; i++) {
      for(int j = 0; j < len; j++) {
    	 // System.out.println(weights.toString());
        double weight = weights[i][j];
        graph.addEdge(new Edge(weight), new Vertex(i), new Vertex(j));
      }
    }

    return graph;
  }
  
  public static Graph fromReadable(Readable readable) {
    Graph graph = new Graph();
    HashMap<String, Vertex> names = new HashMap<String, Vertex>();
    
    Scanner s = new Scanner(readable);

    while(s.hasNext("\r|\n")) s.next("\r|\n");
    
    s.skip("vertices:");
    while (s.findInLine("([1-9]+)") != null) {
      MatchResult match = s.match();
      
      String name = match.group(1);
      Vertex v = new Vertex(Integer.parseInt(name));
      graph.addVertex(v);
      names.put(name, v);
    }

    s.skip("\nedges:");
    while (s.findInLine("([A-Z])([A-Z])\\(([0-9]+(?:\\.[0-9]+)?)\\)") != null) {
      MatchResult match = s.match();
      
      Vertex first = names.get(match.group(1));
      Vertex second = names.get(match.group(2));
      Double weight = Double.parseDouble(match.group(3));
      graph.addEdge(new Edge(weight), first, second);
    }
    return graph;
  }
  
  /** Adds a random vertex on an edge if the number of 
   *  vertices in the given graph isn't even */
  public static void makeVerticesEven(Graph g) {
    if (g.getVertices().size() % 2 == 0) return;
    
    ArrayList<Vertex> vlist = new ArrayList<Vertex>();
    for (Vertex v : g.getVertices()) vlist.add(v);
    Random r = new Random();
    Vertex randomV = vlist.get(r.nextInt(vlist.size()));
    Vertex newV = new Vertex(-1);
    Edge newE = new Edge(0);
    
    g.addVertex(newV);
    g.addEdge(newE, newV, randomV);
  }
  
  public static void printGraph(Graph g) {
    for (Vertex v : g.getVertices())
      System.out.print(v+" ");
    System.out.println();
    
    for (Edge e : g.getEdges()) {
      Pair<Vertex> endpoints = g.getEndpoints(e);
      System.out.print(endpoints.first+""+endpoints.second+"("+e.weight+") ");
    }
    System.out.println();
    
  }
  
}
