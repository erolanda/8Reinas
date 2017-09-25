import java.util.Random;
import java.util.Comparator;

public class Solucion {
	private int fitness;
	private int [] reinas = new int[8];
    
    public void setReinasRdm(){
    	Random rgen = new Random();
    	for(int i=0;i<8;i++)
    		reinas[i] = i;
    	for (int i=0; i<reinas.length; i++) {
		    int rP = rgen.nextInt(reinas.length);
		    int temp = reinas[i];
		    reinas[i] = reinas[rP];
		    reinas[rP] = temp;
		}
    }
    
    public void impRep(){
    	for (int i=0; i<8;i++)
    		System.out.print (reinas[i]+"|");
    	System.out.println ();
    }
    
    public int encuentraAtaque(){
    	int cont=0;
    	int actVal,i;
    	for(i=0; i<reinas.length;i++){
    		actVal = reinas[i];
			for (int ii=i-1; ii>= 0; ii--){
				if((actVal - (i - ii)) == reinas[ii])
					cont++;
			}
			for (int id=i+1; id < 8; id++){
				if((actVal - (id - i)) == reinas[id])
					cont++;
			}
    	}
    	fitness = cont;
    	return cont;
    }
    
    public int getFitness(){
    	return fitness;
    }
    
    public void setReinas(int [] reinas){
    	this.reinas = reinas;
    }
    
    public int [] getReinas(){
    	return reinas;
    }
}

class fitnessComparator implements Comparator{
	public int compare(Object o1, Object o2){
		int apO1 = ((Solucion)o1).getFitness();
		int apO2 = ((Solucion)o2).getFitness();
		if(apO1 > apO2)
			return 1;
		else if(apO1 < apO2)
			return -1;
		else
			return 0;
	}
}