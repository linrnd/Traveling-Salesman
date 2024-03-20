import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Individual implements Comparable<Individual>{
	

	private int [] order; //one of the solution
	private double cost; //fitness 
	
	public Individual(int[] order) {
		super();
		this.order = order;
	
	}

	public int[] getOrder() {
		return order;
	}

	public void setOrder(int[] order) {
		this.order = order;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public int compareTo(Individual o) {
		
		if(this.cost>o.getCost()) {
			return 1;
			
		}
		else if(this.cost<o.getCost()){
			return -1;
			
		}
		return 0;
	}
	
	//Um gleich Ort nicht 2 mals zu besuchen, benutzt man Partially Mapped Crossover(PMX),
	//PMX tauschen nicht nur Elemente aus 2 Eltern, auch tauschen die Elemente in den Eltern.
	//Hier benutzt man Collections.swap() Methode. Man bekommt am Ende ein Kind. 

	public  Individual crossover(Individual partner) {
		int breakpoint=(int)(Math.random()*this.order.length);
		
		Integer[] convertThidOrder = new Integer[this.order.length];
		Integer[] convertPartnerOrder = new Integer[partner.getOrder().length];
		int i=0;
		for(int value:this.order) {
			convertThidOrder[i++] = Integer.valueOf(value);
		}
		i = 0;
		for(int value:partner.getOrder()) {
			convertPartnerOrder[i++] = Integer.valueOf(value);
		}
		
		List<Integer> parent1=Arrays.asList(convertThidOrder);
		List<Integer> parent2=Arrays.asList(convertPartnerOrder);
		
		for(int j=0;j<breakpoint;j++) {
			int newValue;
			newValue=parent2.get(j);
			Collections.swap(parent1,parent1.indexOf(newValue),j);
		}
		int [] result=parent1.stream().mapToInt(Integer::intValue).toArray();
		return new Individual(result);
	}
	
	public void mutate(float mutationChances) {
		//zu mutieren zufällig benutzt man auch Collections.swap(),
		//die 2 getauschte Orte werden werden mit random.nextInt() erzeugt. 
		//Dann am Ende bekommt die Eltern ein Mutationskind. 
		Random random=new Random();
		float mutatepoint=random.nextFloat();
		if(mutatepoint<=mutationChances) {
			Integer[] convertThidOrder = new Integer[this.order.length];
			int i=0;
			for(int value:this.order) {
				convertThidOrder[i++] = Integer.valueOf(value);
			}
			List<Integer> ThidOrderList=Arrays.asList(convertThidOrder);	
			Collections.swap(ThidOrderList, random.nextInt(ThidOrderList.size()),random.nextInt(ThidOrderList.size()) );
			this.order=ThidOrderList.stream().mapToInt(Integer::intValue).toArray();
		}
		
	}

	@Override
	public String toString() {
		return "Individual [order=" + Arrays.toString(order) + ", cost=" + cost + "]";
	}
	
	
}
