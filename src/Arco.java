
public class Arco implements Comparable<Arco>{
	public Integer origen;
	public Integer destino;
	public Double costo;
	public Arco(Integer origenN,Integer destinoN,Double costoN){
		origen=origenN;
		destino=destinoN;
		costo=costoN;
	}
    public int compareTo(Arco other)
    {
        return Double.compare(costo, other.costo);
    }
}
