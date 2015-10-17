
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;
import java.util.Scanner;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Prim {

	public final static InputStream archivoDatos1=Prim.class.getResourceAsStream("datos.xls");
	
	
	public static void main(String[] args){
		PriorityQueue<Arco>colao=algoritmoPrim();
		System.out.println(colao.poll().costo);
	}
	public static PriorityQueue<Arco> algoritmoPrim(){
		PriorityQueue<Arco>cola=new PriorityQueue<Arco>();
		Workbook workbook;
		try {
			workbook = Workbook.getWorkbook(archivoDatos1);
		
		Sheet sheet = workbook.getSheet("datos");
		
		Cell a1 = sheet.getCell(1,1);
		System.out.println(a1.getContents());
		int t=80;
		double [][] matriz = new double [t][t];
		int[] visitado = new int[t];
		double min = 9999.0;
		int c1 = 0;
		int c2 = 0;
		int total = 0;
		
		for(int i = 0; i < t; i++){
			
			visitado[i] = 0;
			
			for(int j = 0; j < t; j++){
				matriz[i][j] = Double.parseDouble(sheet.getCell(i,j).getContents().replace(",", "."));
				if(matriz[i][j]==0){
					
					matriz[i][j] = 9999.0;
					
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
			matriz[c1][c2] = matriz[c1][c2] = 9999.0; 
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
