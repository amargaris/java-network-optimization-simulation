package network.optimization.sdn.opt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class RouletteRandom {
	
	private ArrayList<Integer> list;
	private ArrayList<Double> normalized;
	private Random rand;
	//private int sum;
	public RouletteRandom(ArrayList<Integer> thelist,int thesum){
		list=thelist;
		//sum=thesum;
		rand = new Random();
		normalized = new ArrayList<Double>();
		double sum = 0.0;
		for(int i=0;i<list.size();i++){
			sum = sum + list.get(i);
		}
		//System.out.println(sum);
		if(sum!=0){
			for(int i=0;i<list.size();i++){
				//System.out.println(list.get(i)+" / "+sum);
				normalized.add((double) list.get(i)/sum);
			}
		}
		else{
			normalized.add((double) 1/thelist.size()); //omoiomorfi
		}
		//System.out.println(normalized.toString());
	}
	public ArrayList<Integer> getNResults(int a){
		ArrayList<Integer> returnval = new ArrayList<Integer>();
		for(int i=0;i<a;i++){
			double offset = 0.0;
			double random = rand.nextDouble();
			for(int j=0;j<normalized.size();j++){
				if(random>=offset&&random<(offset+normalized.get(j))){
					returnval.add(j);
					break;
				}
				offset=offset+normalized.get(j);
			}
		}
		return returnval;
	}
	public static void main(String[] args){
		int num1 = 3;
		int num2 = 100;
		int num3 = 110;
		int num4 = 2;
		int sum = num1+num2+num3+num4;
		ArrayList<Integer> s = new ArrayList<Integer>();
		s.add(num3-num1);
		s.add(num3-num2);
		s.add(num3-num3);
		s.add(num3-num4);
		RouletteRandom r = new RouletteRandom(s,sum);
		ArrayList<Integer>f =r.getNResults(1000);
		int[] count = new int[4];
		for(Integer p:f){
			System.out.println(p);
			count[p]= count[p]+1;
		}
		System.out.println(Arrays.toString(count));
	}
}
