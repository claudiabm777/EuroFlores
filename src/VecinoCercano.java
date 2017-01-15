
import java.util.ArrayList;
import java.util.List;


public class VecinoCercano {

	public List<Integer> VecinoC(Double [][] costosTLS){

		int[] visitado = new int[costosTLS.length];
		int[] agregado = new int[costosTLS.length];
		List<Integer>agregados = new ArrayList<>();
		double min=10000000000000.0 ;
		List<Integer>respuesta=new ArrayList<Integer>();
		double total=0;
		for(int i = 0; i < costosTLS.length; i++){

			for(int j = 0; j < costosTLS.length; j++){
				//System.out.println(costosTLS[costosTLS.length-1][j]);
				if(costosTLS[i][j]==0){
					
					costosTLS[i][j] = 99999999999999999.0; //para que no esté conectado con el mismo nodo
				}
			}
		}

		//Inicial 
		int inicial= costosTLS.length-1;
		int mi=costosTLS.length-1;
		agregado[costosTLS.length-1]=1;
		agregados.add(costosTLS.length-1);
	
		while(agregados.size()!=costosTLS.length){
			for(int j=0;j<costosTLS.length;j++){
				//System.out.println("costo:"+costosTLS[inicial][j]+"     min:"+min );
				if (agregado[j]!=1){
					min=costosTLS[inicial][j];
					mi=j;
				}
			}
			
			visitado[mi] = 1;
			agregado[mi]=1;
			agregados.add(mi);
			inicial=mi;
		}


//
		agregados.add(costosTLS.length-1);
//
		return agregados;
	}

}
