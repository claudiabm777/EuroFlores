import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Simulaciones {
	public void simulacion1K(int k){
		
		JFrame parentFrame = new JFrame();
		 
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Especifique el nombre del archivo .txt");   
		 
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    double time1= System.currentTimeMillis();
		    EuroFlores ef=new EuroFlores(k, fileToSave);
		    ef.simulacionK();
		    double time2= System.currentTimeMillis();
		    double time=(time2-time1)/1000.0;
		    ef.reportarResultados(time);
		    
		    JOptionPane.showMessageDialog (null, "Se llevó a cabo la simulación exitosamente. Consulte el archivo", "Succes!", JOptionPane.INFORMATION_MESSAGE);

		}else{
			JOptionPane.showMessageDialog (null, "No se llevó a cabo la simulación.", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	public void imprimirResultados(List<EuroFlores>listaEF,File archivoResultados,EuroFlores mejor,double time){
		try{
		FileWriter fw = new FileWriter(archivoResultados.getAbsoluteFile());
		
		BufferedWriter bw = new BufferedWriter(fw);
		for(int p=0;p<listaEF.size();p++){
			EuroFlores e=listaEF.get(p);
			
				
				bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				bw.newLine();
				bw.write("Archivo de resultados de la iteración con k = "+(p+1)+"");
				bw.newLine();
				bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				bw.newLine();
				bw.write("COMPONENTES: ");
				bw.newLine();
				for(int i=0;i<e.listaComponentes.size();i++){
					bw.write("  Componente "+i+": ");
					List<Integer>listaC=new ArrayList<Integer>(e.listaComponentes.get(i));
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
				for(int i=0;i<e.listasDeRutas.size();i++){
					bw.write("  *Ruta "+i+": ");
					List<Integer>listaC=new ArrayList<Integer>(e.listasDeRutas.get(i));
					for (int j = 0; j < listaC.size(); j++) {
						bw.write(listaC.get(j).toString());
						if(j!=listaC.size()-1){
							bw.write(",");
						}
					}
					bw.newLine();
					bw.write("    + Costo de la ruta "+i+": "+e.costos.get(i));
					bw.newLine();
					bw.write("    + Distancia de la ruta "+i+": "+e.distancias.get(i));
					bw.newLine();
				}
				bw.write("------------------------------------------------------------------");
				bw.newLine();
				bw.write("DISTANCIA: "+e.distanciaTotal);
				bw.newLine();
				bw.write("COSTO TRANSPORTE: "+e.costoGasolina);
				bw.newLine();
				bw.write("COSTO CONDUCTORES: "+e.costoConductores);
				bw.newLine();
				bw.write("COSTO TOTAL DE UNA JORNADA: "+e.costoTotal);
				bw.newLine();
				bw.write("COSTO DE OPERACIÓN MENSUAL: "+e.costosMensuales);
				bw.newLine();
				
			bw.write("**********************************************************************");
			bw.newLine();
			bw.write("**********************************************************************");
			bw.newLine();
			bw.write("**********************************************************************");
			bw.newLine();
			bw.write("  ");
			bw.newLine();
			
		}
		bw.write("#############################################################################");
		bw.newLine();
		bw.write("#############################################################################");
		bw.newLine();
		bw.write("-------------------------TIEMPO COMPUTACIONAL TOTAL--------------------------");
		bw.newLine();
		bw.write("Tiempo Computacional de todas las iteraciones: "+time);
		bw.newLine();
		bw.write("#############################################################################");
		bw.newLine();
		bw.write("#############################################################################");
		bw.newLine();
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("-----------------------LA MEJOR ITERACION-------------------------");
		bw.newLine();
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		bw.newLine();
		bw.write("Archivo de resultados con la mejor iteración k = "+(mejor.k)+"");
		bw.newLine();
		bw.write("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		bw.newLine();
		bw.write("COMPONENTES: ");
		bw.newLine();
		for(int i=0;i<mejor.listaComponentes.size();i++){
			bw.write("  Componente "+i+": ");
			List<Integer>listaC=new ArrayList<Integer>(mejor.listaComponentes.get(i));
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
		for(int i=0;i<mejor.listasDeRutas.size();i++){
			bw.write("  *Ruta "+i+": ");
			List<Integer>listaC=new ArrayList<Integer>(mejor.listasDeRutas.get(i));
			for (int j = 0; j < listaC.size(); j++) {
				bw.write(listaC.get(j).toString());
				if(j!=listaC.size()-1){
					bw.write(",");
				}
			}
			bw.newLine();
			bw.write("    + Costo de la ruta "+i+": "+mejor.costos.get(i));
			bw.newLine();
			bw.write("    + Distancia de la ruta "+i+": "+mejor.distancias.get(i));
			bw.newLine();
		}
		bw.write("------------------------------------------------------------------");
		bw.newLine();
		bw.write("DISTANCIA: "+mejor.distanciaTotal);
		bw.newLine();
		bw.write("COSTO TRANSPORTE: "+mejor.costoGasolina);
		bw.newLine();
		bw.write("COSTO CONDUCTORES: "+mejor.costoConductores);
		bw.newLine();
		bw.write("COSTO TOTAL DE UNA JORNADA: "+mejor.costoTotal);
		bw.newLine();
		bw.write("COSTO DE OPERACIÓN MENSUAL: "+mejor.costosMensuales);
		bw.newLine();
		
		bw.close();
		
		JOptionPane.showMessageDialog (null, "El archivo se guardo con los parametros satisfactoriamente.", "Archivo Guardado", JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception ee){
			JOptionPane.showMessageDialog (null, "No se llevó a cabo la simulación.", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	public void simulacionTodasLasK(){

		JFrame parentFrame = new JFrame();
		 
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Especifique el nombre del archivo .txt");   
		 
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		 
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    double time1= System.currentTimeMillis();
		    List<EuroFlores>listaEF=new ArrayList<EuroFlores>();
		    Prim prim=new Prim();
		    PriorityQueue<Arco>colaArbolExpMinimal=prim.algoritmoPrim();
		    EuroFlores mejor=new EuroFlores(1,null);
		    mejor.costoTotal=99999999999999999.99999999;
		    for(int i=1;i<80;i++){
			    EuroFlores ef=new EuroFlores(i, null);
			   // System.out.println("k: "+ef.k);
			    ef.colaArbolExpMinimal=new PriorityQueue<Arco>(colaArbolExpMinimal);
			    
			    
			    ef.simulacionKs(prim);
			    
			    listaEF.add(ef);
			    if(mejor.costoTotal>=ef.costoTotal){
			    	mejor=ef;
			    }
		    }
		    double time2= System.currentTimeMillis();
		    double time=(time2-time1)/1000.0;
		    imprimirResultados(listaEF, fileToSave,mejor,time);
		    JOptionPane.showMessageDialog (null, "Se llevó a cabo la simulación exitosamente. Consulte el archivo", "Succes!", JOptionPane.INFORMATION_MESSAGE);

		}else{
			JOptionPane.showMessageDialog (null, "No se llevó a cabo la simulación.", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}
}
