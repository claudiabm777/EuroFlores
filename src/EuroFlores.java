import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;


public class EuroFlores {
	public final static double COSTO_CONDUCTORES=500000.0;
	public final static double COSTO_GASOLINA=1000.0;
	public final static int JORNADAS=7;
	public Integer k;
	public PriorityQueue<Arco>colaArbolExpMinimal;
	public List<Double>distancias=new ArrayList<Double>();
	public double distanciaTotal=0.0;
	public List<Double>costos=new ArrayList<Double>();
	public double costoTotal=0.0;
	public List<List<Integer>>listasDeRutas;
	public List<List<Integer>>listaComponentes;
	public Double costoConductores;
	public Double costoGasolina;
	public Double costosMensuales;
	public double [][] matrizConCentroide;
	public File archivoResultados;
	
	public EuroFlores(int k,File archivoResultados){
		colaArbolExpMinimal=null;
		distancias=new ArrayList<Double>();
		distanciaTotal=0.0;
		costos=new ArrayList<Double>();
		costoTotal=0.0;
		listasDeRutas=null;
		listaComponentes=null;
		costoGasolina=0.0;
		costosMensuales=0.0;
		matrizConCentroide=null;
		
		this.k=k;
		costoConductores=(k+1)*COSTO_CONDUCTORES;
		this.archivoResultados=archivoResultados;
	}
	
	public void simulacionK(){
		Prim prim=new Prim();
		//Ejecutar el algoritmo de prim. El lee el archivo .xls de los datos.
		colaArbolExpMinimal=prim.algoritmoPrim();
		
		//Remover de la cola de prioridad los k primeros arcos
		PriorityQueue<Arco>cola=new PriorityQueue<Arco>(colaArbolExpMinimal);
		for (int i = 0; i < k; i++) {
			cola.poll();
		}
		
		//Calcular componentes conexas del bosque
		CCGraph g = new CCGraph();
        g.read_CCGraph(false,80,cola);
        g.print_CCGraph();
        ConnectedComponents cc=new ConnectedComponents();
        cc.connected_components(g,cc.listaComponentes);
        
        listaComponentes=cc.listaComponentes;
        matrizConCentroide=prim.matrizConCentroide;
        
        //llamar al procedimiento de TSP para cada componente
        listasDeRutas=new ArrayList<List<Integer>>();
        for (int i = 0; i < listaComponentes.size(); i++) {
        	
        	List<Integer>comp=new ArrayList<Integer>(listaComponentes.get(i));
        	comp.add(80);
        	Double [][] costosTLS=new Double[comp.size()][comp.size()];
        	for(int j=0;j<comp.size();j++){
        		for(int m=0;m<comp.size();m++){
        			costosTLS[j][m]=matrizConCentroide[comp.get(j)][comp.get(m)];
        		}
        	}
        	VRP e= new VRP(costosTLS);
        	List<Integer>ruta=e.generateModel(costosTLS.length-1);
        	List<Integer>rutaF=new ArrayList<Integer>();
        	for (int j = 0; j < ruta.size(); j++) {
				rutaF.add(comp.get(ruta.get(j)));
			}
        	listasDeRutas.add(rutaF);
		} 
         //Calcular los costos y distancias
        distanciaTotal=distanciasTotales();
        costoGasolina=costosGasolinaTotales();
        costoTotal=costoGasolina+costoConductores;
        costosMensuales=costoTotal*JORNADAS;
       	}
	
	public void simulacionKs(Prim prim){
		//Ejecutar el algoritmo de prim. El lee el archivo .xls de los datos.
		
		
		//Remover de la cola de prioridad los k primeros arcos
		PriorityQueue<Arco>cola=new PriorityQueue<Arco>(colaArbolExpMinimal);
		for (int i = 0; i < k; i++) {
			cola.poll();
		}
		
		//Calcular componentes conexas del bosque
		CCGraph g = new CCGraph();
        g.read_CCGraph(false,80,cola);
        g.print_CCGraph();
        ConnectedComponents cc=new ConnectedComponents();
        cc.connected_components(g,cc.listaComponentes);
        
        listaComponentes=cc.listaComponentes;
        matrizConCentroide=prim.matrizConCentroide;
        
        //llamar al procedimiento de TSP para cada componente
        listasDeRutas=new ArrayList<List<Integer>>();
        for (int i = 0; i < listaComponentes.size(); i++) {
        	
        	List<Integer>comp=new ArrayList<Integer>(listaComponentes.get(i));
        	comp.add(80);
        	Double [][] costosTLS=new Double[comp.size()][comp.size()];
        	for(int j=0;j<comp.size();j++){
        		for(int m=0;m<comp.size();m++){
        			costosTLS[j][m]=matrizConCentroide[comp.get(j)][comp.get(m)];
        		}
        	}
        	VRP e= new VRP(costosTLS);
        	List<Integer>ruta=e.generateModel(costosTLS.length-1);
        	List<Integer>rutaF=new ArrayList<Integer>();
        	for (int j = 0; j < ruta.size(); j++) {
				rutaF.add(comp.get(ruta.get(j)));
			}
        	listasDeRutas.add(rutaF);
		} 
         //Calcular los costos y distancias
        distanciaTotal=distanciasTotales();
        costoGasolina=costosGasolinaTotales();
        costoTotal=costoGasolina+costoConductores;
        costosMensuales=costoTotal*JORNADAS;
       	}
	
	
	public void reportarResultados(Double time){
		try{
		FileWriter fw = new FileWriter(archivoResultados.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		bw.newLine();
		bw.write("Archivo de resultados de la iteración con k = "+k+"");
		bw.newLine();
		bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		bw.newLine();
		bw.write("COMPONENTES: ");
		bw.newLine();
		for(int i=0;i<listaComponentes.size();i++){
			bw.write("  Componente "+i+": ");
			List<Integer>listaC=new ArrayList<Integer>(listaComponentes.get(i));
			 for (int j = 0; j < listaC.size(); j++) {
				bw.write(listaC.get(j).toString());
				if(j!=listaC.size()-1){
					bw.write(",");
				}
			}
			bw.newLine();
		}
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("RUTAS: ");
		bw.newLine();
		for(int i=0;i<listasDeRutas.size();i++){
			bw.write("  *Ruta "+i+": ");
			List<Integer>listaC=new ArrayList<Integer>(listasDeRutas.get(i));
			for (int j = 0; j < listaC.size(); j++) {
				bw.write(listaC.get(j).toString());
				if(j!=listaC.size()-1){
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("    + Costo de la ruta "+i+": "+costos.get(i));
			bw.newLine();
			bw.write("    + Distancia de la ruta "+i+": "+distancias.get(i));
			bw.newLine();
		}
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("DISTANCIA: "+distanciaTotal);
		bw.newLine();
		bw.write("COSTO TRANSPORTE: "+costoGasolina);
		bw.newLine();
		bw.write("COSTO CONDUCTORES: "+costoConductores);
		bw.newLine();
		bw.write("COSTO TOTAL DE UNA JORNADA: "+costoTotal);
		bw.newLine();
		bw.write("COSTO DE OPERACIÓN MENSUAL: "+costosMensuales);
		bw.newLine();
		bw.write("TIEMPO COMPUTACIONAL (segundos): "+time);
		bw.newLine();
		bw.close();
		JOptionPane.showMessageDialog (null, "El archivo se guardo con los parametros satisfactoriamente.", "Archivo Guardado", JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception e){
			JOptionPane.showMessageDialog (null, "El archivo no se pudo guardar. Intentelo de nuevo", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	public Double distanciasTotales(){
		Double distancia=0.0;
		for (int i = 0; i < listasDeRutas.size(); i++) {
			List<Integer>ruta=new ArrayList<Integer>(listasDeRutas.get(i));
			Double distRuta=0.0;
			for (int j = 0; j < ruta.size()-1; j++) {
				
				distRuta+=matrizConCentroide[ruta.get(j)][ruta.get(j+1)];
			}
			distancias.add(distRuta);
			distancia+=distRuta;
		}
		return distancia;
	}
	public Double costosGasolinaTotales(){
		Double costoT=0.0;
		List<Double>distanciasRutas=distancias;
		for (int i = 0; i < distanciasRutas.size(); i++) {
			Double dist=distanciasRutas.get(i);
			Double c=COSTO_GASOLINA*(Math.pow(dist,2));
			costos.add(c);
			costoT+=c;
		}
		return costoT;
	}
//	public List<Integer> metodoPipe(Double [][] costosTLS){
//		List<Integer>respuesta=new ArrayList<Integer>();
//		//inventado
//		respuesta.add(costosTLS.length-1);
//		for(int i=0;i<costosTLS.length-1;i++){
//			respuesta.add(i);
//		}
//		respuesta.add(costosTLS.length-1);
//		//fin de lo inventado
//		return respuesta;
//	}
	
//	public static void main(String[] args) {
//		EuroFlores f=new EuroFlores(1,null);
//		f.simulacionK();
//	}
}
