
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Scanner;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Prim {
	public  double [][] matriz;
	public  double [][] matrizConCentroide;
	
	
//	public static void main(String[] args){
//		PriorityQueue<Arco>colao=algoritmoPrim();
//		System.out.println(colao.poll().costo);
//	}
	public  PriorityQueue<Arco> algoritmoPrim(){
		PriorityQueue<Arco>cola=new PriorityQueue<Arco>();
		Workbook workbook;
		try {
			workbook = Workbook.getWorkbook(Prim.class.getResourceAsStream("darosCentroide.xls"));
		
		Sheet sheet = workbook.getSheet("s1");
		
		Cell a1 = sheet.getCell(0,1);
		int t=80;
		matriz = new double [t][t];
		matrizConCentroide = new double [t+1][t+1];
		int[] visitado = new int[t];
		double min = 9999999999999.0;
		int c1 = 0;
		int c2 = 0;
		int total = 0;
		
		for(int i = 0; i < t+1; i++){
			if(i<t)
			visitado[i] = 0;
			
			for(int j = 0; j < t+1; j++){
				if(j<t&&i<t){
					matriz[i][j] = Double.parseDouble(sheet.getCell(i,j).getContents().replace(",", "."));
					if(matriz[i][j]==0){
						matriz[i][j] = 9999999999999.0;

					}
				}
				
				matrizConCentroide[i][j] = Double.parseDouble(sheet.getCell(i,j).getContents().replace(",", "."));
				if(matrizConCentroide[i][j]==0){
					matrizConCentroide[i][j] = 9999999999999.0;
					
				}
			}
			
		}
		
		visitado[0] = 1;
		
		for(int counter = 0; counter < t-1 ; counter++){
			
			min = 9999.0;
			
			for(int i = 0; i < t; i++){
			
			if(visitado[i]==1){
			
				for(int j = 0; j < t; j++){
				
					if(visitado[j]==0){
						
						if(min > matriz[i][j]){
							
							min = matriz[i][j];
							c1 = i;
							c2 = j;
							
						}
						
					}	
					
				}	
				
			}
			
			}
		
			visitado[c2] = 1;
			total += min;
			matriz[c1][c2] = matriz[c1][c2] = 9999999999999.0;
			System.out.println("Nodos conectados: "+c1+" -> "+c2+" : "+min);
			Arco a=new Arco(c1,c2,-min);
			cola.add(a);
		
		}
		
		System.out.println("El Costo total del árbol es: "+ total);
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cola;
	}
}
