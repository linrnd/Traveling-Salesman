
import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Projekt {

	public static void main(String[] args) throws FileNotFoundException {
		// SIMULATION PARAMETERS
		int populationNr = 50;
		int generationStopCount = 20000;
		int parentsNr = populationNr/2;
		float mutationRate = (float) 0.5;	//50% chance of mutation
		// END OF SIMULATION PARAMETERS
		
		//Einlesen
		double[][] koord = einlesenAus(new File("att532.tsp"));
		//auslesen(koord);
		
		//Verarbeitung
		//1. Deklaration von einem Array zur Speicherung einer Lösung (reihenfolge) und der Restriktionen (welche Orte sind schon beliefert) 
		int[] reihenfolge      = new int[koord.length];
		boolean[] besuchteOrte = new boolean[koord.length];
		
		int ort                = (int)(Math.random()*koord.length);
		reihenfolge[0]         = ort;
		besuchteOrte[ort]      = true;

		int[]order=new int [koord.length];
		
		// Initial Population
		List<Individual> population=new ArrayList<>();
		for(int i=0;i<populationNr;i++) {
			population.add(new Individual(makeRandomOrder(koord)));
		}
		
		//Simulation Loop	
		for(int i=0;i<generationStopCount;i++) {
			//Calculate Cost: give the cost to individual 
			for(Individual x:population) {
				if(x.getCost()==0) {
					x.setCost(calCost(x.getOrder(),koord));	
				}
			}
			//Selection 
			Collections.sort(population);
			for(int j=populationNr-1;j>parentsNr-1;j--) {
				population.remove(j);
			}
			
			//Crossover
			population.add(population.get(1).crossover(population.get(0)));
			for(int m=1;m<parentsNr;m++) {
				population.add(population.get(0).crossover(population.get(m))); //  children birth
			}
			
			//Mutation,only for children
			for(Individual x:population) {
				if(x.getCost()==0) {	//Newborns have cost=0
					x.mutate(mutationRate);
				}
			}
			System.out.println("Gen: "+ i + "\tBest Cost: "+ population.get(0).getCost());
		}
		
	}
		
	public static void auslesen(double[][] koord) {
		for (int i = 0; i < koord.length; i++)
			System.out.println(i + " " + koord[i][0] + " " + koord[i][1]);
	}
	
	public static double[][] einlesenAus(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		double[][] koord = new double[0][0];
		int index = 0;
				
		while(scanner.hasNext()) {
			
			String next = scanner.next();

			if (next.equals("DIMENSION:")) {
				int dim = scanner.nextInt();
				koord = new double[dim][2];
				continue;
			} 				
			try {
				int nummer = Integer.valueOf(next);
				if (scanner.hasNext()) {
					double x = Double.valueOf(scanner.next());
					if (scanner.hasNext()) {
						double y = Double.valueOf(scanner.next());
						koord[index][0] = x;
						koord[index][1] = y;
						//koord[index][2] = y;
						index++;
					}					
				}
			} catch (NumberFormatException e) {}			
		}	
		return koord;
	}
	
	public static double calCost(int [] order, double[][] koord) { // cost ist the fittness here
		double cost=0;
		for(int i=0;i<order.length-1;i++) {
			int citya=order[i];
			int cityb=order[i+1];
			
			double xa=koord[citya][0];
			double ya=koord[citya][1];
			double xb=koord[cityb][0];
			double yb=koord[cityb][1];
			
			cost+=Math.sqrt((xa-xb)*(xa-xb)+(ya-yb)*(ya-yb));
		}
		return cost;
	}
	
	
	public static int[] makeRandomOrder(double [][]koord) {
		boolean[] visited = new boolean[koord.length];
		int city;
		int[] order = new int[koord.length];
		for(int i=0;i<order.length;i++) {
			do {
				city=(int)(Math.random()*koord.length);
			}
			while(visited[city]!=false);
			order[i]=city;
			visited[city]=true;
		}
		return order;	
	}
}
