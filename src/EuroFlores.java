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
		this.k=k;
		costoConductores=k*COSTO_CONDUCTORES;
		this.archivoResultados=archivoResultados;
	}
	
	public void simulacionK(){
		//Ejecutar el algoritmo de prim. El lee el archivo .xls de los datos.
		colaArbolExpMinimal=Prim.algoritmoPrim();
		
		//Remover de la cola de prioridad los k primeros arcos
		PriorityQueue<Arco>cola=new PriorityQueue<Arco>(colaArbolExpMinimal);
		for (int i = 0; i < k; i++) {
			cola.poll();
		}
		
		//Calcular componentes conexas del bosque
		CCGraph g = new CCGraph();
        g.read_CCGraph(false,80,cola);
        g.print_CCGraph();
        ConnectedComponents.connected_components(g);
        listaComponentes=ConnectedComponents.listaComponentes;
        matrizConCentroide=Prim.matrizConCentroide;
        
        //llamar al procedimiento de TSP para cada componente
        listasDeRutas=new ArrayList<List<Integer>>();
        for (int i = 0; i < listaComponentes.size(); i++) {
        	listaComponentes.get(i).add(80);
        	List<Integer>comp=listaComponentes.get(i);
        	Double [][] costosTLS=new Double[comp.size()][comp.size()];
        	for(int j=0;j<comp.size();j++){
        		for(int m=0;m<comp.size();m++){
        			costosTLS[j][m]=matrizConCentroide[comp.get(j)][comp.get(m)];
        		}
        	}
        	List<Integer>ruta=metodoPipe(costosTLS);
        	List<Integer>rutaF=new ArrayList<Integer>();
        	for (int j = 0; j < ruta.size(); j++) {
				rutaF.add(comp.get(ruta.get(j)));
			}
        	listasDeRutas.add(rutaF);
		} 
        //Calcular los costos y distancias
        distanciaTotal=distanciasTotales(listasDeRutas);
        costoGasolina=costosGasolinaTotales();
        costoTotal=costoGasolina+costoConductores;
        costosMensuales=costoTotal*JORNADAS;
	}
	
	public void reportarResultados(){
		try{
		FileWriter fw = new FileWriter(archivoResultados.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		bw.newLine();
		bw.write("Archivo de resultados de la iteración con "+k+" zonas");
		bw.newLine();
		bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		bw.newLine();
		bw.write("COMPONENTES: ");
		bw.newLine();
		for(int i=0;i<listaComponentes.size();i++){
			bw.write("  Componente "+i+": ");
			List<Integer>listaC=listaComponentes.get(i);
			for (int j = 0; j < listaC.size(); j++) {
				bw.write(listaC.get(j));
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
			bw.write("  Ruta "+i+": ");
			List<Integer>listaC=listasDeRutas.get(i);
			for (int j = 0; j < listaC.size(); j++) {
				bw.write(listaC.get(j));
				if(j!=listaC.size()-1){
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("  Costo de la ruta "+i+": "+costos.get(i));
			bw.newLine();
		}
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("COSTO TRANSPORTE: "+costoGasolina);
		bw.newLine();
		bw.write("COSTO CONDUCTORES: "+costoConductores);
		bw.newLine();
		bw.write("COSTO TOTAL DE UNA JORNADA: "+costoTotal);
		bw.newLine();
		bw.write("COSTO DE OPERACIÓN MENSUAL: "+costosMensuales);
		bw.newLine();
		bw.write("TIEMPO COMPUTACIONAL: ");
		bw.newLine();
		bw.close();
		JOptionPane.showMessageDialog (null, "El archivo se guardo con los parametros satisfactoriamente.", "Archivo Guardado", JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception e){
			JOptionPane.showMessageDialog (null, "El archivo no se pudo guardar. Intentelo de nuevo", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	public Double distanciasTotales(List<List<Integer>>listasDeRutas){
		Double distancia=0.0;
		for (int i = 0; i < listasDeRutas.size(); i++) {
			List<Integer>ruta=listasDeRutas.get(i);
			Double distRuta=0.0;
			for (int j = 0; j < ruta.size()-1; j++) {
				distRuta+=matrizConCentroide[ruta.get(i)][ruta.get(i)+1];
			}
			costos.add(distRuta);
			distancia+=distRuta;
		}
		return distancia;
	}
	public Double costosGasolinaTotales(){
		Double costoT=0.0;
		List<Double>distanciasRutas=distancias;
		for (int i = 0; i < distanciasRutas.size(); i++) {
			Double dist=distanciasRutas.get(i);
			Double c=COSTO_GASOLINA*(Math.sqrt(dist));
			costos.add(c);
			costoT+=c;
		}
		return costoT;
	}
	public List<Integer> metodoPipe(Double [][] costosTLS){
		List<Integer>respuesta=new ArrayList<Integer>();
		//contenido
		return respuesta;
	}
	
	public static void main(String[] args) {
		EuroFlores f=new EuroFlores(9,null);
		f.simulacionK();
	}
}
