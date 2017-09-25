import javax.swing.JOptionPane;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Random;

public class Principal {
	static int nComparaciones = 0;
	
	private static Object resizeArray (Object oldArray, int newSize) {
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType,newSize);
		int preserveLength = Math.min(oldSize,newSize);
		if (preserveLength > 0)
			System.arraycopy (oldArray,0,newArray,0,preserveLength);
		return newArray; 
   	}
	
    public static Solucion[] seleccionaPadres(Solucion[] poblacion, int tamPob){
		Solucion [] padres = new Solucion[2];
		Solucion [] padresComp = new Solucion[5];
		int cont2=0, r=0;
		while (cont2 < 5){
			r = (int)Math.floor((Math.random()*tamPob));
			padresComp[cont2] = poblacion[r];
			if (padresComp[cont2] != null)
				cont2++;
		}
		cont2 = 0;
		Arrays.sort(padresComp,new fitnessComparator());
		padres[0] = padresComp[0];
		padres[1] = padresComp[1];
		return padres;
	}
	
	public static boolean encuentraNum(int[] rep, int n){
		for(int i=0; i < rep.length; i++){
			if (rep[i] == n)
				return true;
		}
		return false;
	}
	
	public static Solucion[] cruza(Solucion p, Solucion m){
		//Cruza, obtengo 4 reinas de P y 4 reinas de m, las pongo en el hijo.
		Random rgen = new Random();
		Solucion[] hijos = new Solucion[2];
		Solucion hijo1 = new Solucion();
		Solucion hijo2 = new Solucion();
		int cont = 0, r=0;
		int[] reinasP1 = p.getReinas();
		int[] reinasP2 = m.getReinas();
		int[] reinasH1 = new int[8];
		int[] reinasH2 = new int[8];
		int[] tmp = new int[8];
		int x,y;
		int pCorte = rgen.nextInt(reinasP1.length);
		if(pCorte==0|pCorte>6){
			reinasH1 = reinasP1;
			reinasH2 = reinasP2;
		}else{
			for(int i=0;i<pCorte;i++){
				reinasH1[i] = reinasP1[i];
				reinasH2[i] = reinasP2[i];
			}
			for(int i=pCorte; i<8;i++){
				x=reinasP1[i];
				y=reinasP2[i];
				if (encuentraNum(reinasH1,y)==false)
					reinasH1[i] = y;
				else
					reinasH1[i] = 99;
				if (encuentraNum(reinasH2,x)==false)
					reinasH2[i] = x;
				else
					reinasH2[i] = 99;	
			}
			for(int i=pCorte; i < reinasH1.length; i++){
				if(reinasH1[i] == 99){
					while(reinasH1[i] == 99){
						x = rgen.nextInt(reinasH1.length);
						if (!encuentraNum(reinasH1,x)){
							reinasH1[i] = x;
						}
					}
				}
			}
			for(int i=pCorte; i < reinasH2.length; i++){
				if(reinasH2[i] == 99){
					while(reinasH2[i] == 99){
						x = rgen.nextInt(reinasH2.length);
						if (!encuentraNum(reinasH2,x)){
							reinasH2[i] = x;
						}
					}
				}
			}
		}
		hijo1.setReinas(reinasH1);
		hijo2.setReinas(reinasH2);
		nComparaciones++;
		hijo1.encuentraAtaque();
		nComparaciones++;
		hijo2.encuentraAtaque();
		hijos[0] = hijo1;
		hijos[1] = hijo2;
		return hijos;
	}
    
    public static Solucion mutacion(Solucion sujeto){
    	int [] reinasS = sujeto.getReinas();
    	int x1, x2;
    	x1 = (int)Math.floor((Math.random()*reinasS.length));
    	x2 = (int)Math.floor((Math.random()*reinasS.length));
    	int tmp = reinasS[x1];
    	reinasS[x1] = reinasS[x2];
    	reinasS[x2] = tmp;
    	sujeto.setReinas(reinasS);
    	nComparaciones++;
    	sujeto.encuentraAtaque();
    	return sujeto;
    }
    
    public int genetico (int n, double umutacion, int nCorrida){
    	boolean listo = false;
    	Solucion [] poblacion = new Solucion[n];
    	//Poblacion Inicial
    	for(int i=0; i < n; i++){
			poblacion[i] = new Solucion();
			poblacion[i].setReinasRdm();
			nComparaciones++;
			poblacion[i].encuentraAtaque();
			if(poblacion[i].getFitness() == 0){
				listo = true;
				poblacion[i].impRep();
				return nComparaciones;
			}
		}
		int nGeneraciones = 0;
		int limite = 10000;
		int x,p,m;
		while(!listo && nComparaciones < limite && nGeneraciones < 500){
			x=0;
			Solucion [] padres = seleccionaPadres(poblacion,n);
			Solucion [] hijos = new Solucion[2];
			hijos = cruza(padres[0],padres[1]);
			for(int i=0; i<hijos.length; i++){
				if((Math.random()*1) < umutacion){
					hijos[i] = mutacion(hijos[i]);
				}
			}
			poblacion = (Solucion[])resizeArray(poblacion,(poblacion.length+(hijos.length)));
			poblacion[100] = hijos[0];
			poblacion[101] = hijos[1];
			Arrays.sort(poblacion,new fitnessComparator());
			poblacion = (Solucion[])resizeArray(poblacion,n);
			if(poblacion[0].getFitness() == 0){
				listo = true;
			}
			nGeneraciones++;
		}
		if(nComparaciones > limite | nGeneraciones > 500){
			System.out.println ("Me rindo");
			return nComparaciones;
		}
		poblacion[0].impRep();
		return nComparaciones;
	}
	
	public static void main (String[] args) {
		Principal r = new Principal();
    	int n = Integer.parseInt(JOptionPane.showInputDialog(null,"Dame el tamano de la poblacion","Poblacion",JOptionPane.QUESTION_MESSAGE));
    	Double umutacion = Double.parseDouble(JOptionPane.showInputDialog(null,"Dame un umbral para la mutacion",JOptionPane.QUESTION_MESSAGE));
    	int g=0;
		g = r.genetico(n,umutacion,1);
    	System.out.println ("Caso terminado en "+g+ " evaluaciones");
	}
}
