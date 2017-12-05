package network.optimization.sdn;

import java.util.Random;

public class VirtualNetworkRequest {
	public int id;
	public int numOfNodes;
	public int numOfLinks;
	public int[][][] linked; // an 1 <-> 2 tote linked[1][2][0]==1 kai link speed [1][2][1]=2048
	public int[] cpus;
	public float[] freq;
	public int[] ram;
	
	public VirtualNetworkRequest(Network n){
		Random rand = new Random();
		id=rand.nextInt();
		int maxnodes=n.getNodes().size();
		numOfNodes = rand.nextInt(maxnodes)+2;
		linked = new int[numOfNodes][numOfNodes][2];
		cpus = new int[numOfNodes];
		freq = new float[numOfNodes];
		ram = new int[numOfNodes];
		for(int i=0;i<numOfNodes;i++){
			cpus[i] = (rand.nextInt(3)+1);
			freq[i] = (rand.nextInt(10)+1)*0.2f;
			ram[i] = (rand.nextInt(3)+1)*Node.RAMSIZE;
		}
		int whostar = rand.nextInt(numOfNodes); //poios einai o star node
		numOfLinks=numOfNodes-1;
		for(int i=0;i<linked.length;i++){
			for(int j=0;j<linked[0].length;j++){
				if(i==whostar){
					if(j!=i){
						linked[i][j][0]=1;
						linked[i][j][1]=(rand.nextInt(10)+1)*512;
					}
				}
			}
		}
		
	}
	public Network tethe(Network sub,int[] solution){
		Network net = new Network("Virtual Network "+id);
		for(int i=0;i<solution.length;i++){
			net.getNodes().add(sub.getNodes().get(solution[i]));
		}
		for(int j=0;j<linked.length;j++){
			for(int k=0;k<linked[0].length;k++){
				if(linked[j][k][0]==1){
					Link temp = new Link(linked[j][k][1],"link",net.getNodes().get(j),net.getNodes().get(k));
					net.getLinks().add(temp);
				}
			}
		}
		return net;
	}
	public String toString(){
		String s ="VNet Request:"+id+" nodes: "+numOfNodes+" links:"+numOfLinks+"\n";
		for(int i=0;i<numOfNodes;i++){
			s = s +"\nNode "+(i+1)+": Cores: "+cpus[i]+" Frequency: "+freq[i]+" Ram: "+ram[i];
		}
		s= s+"\nLinks:";
		for(int i=0;i<linked.length;i++){
			for(int j=0;j<linked[0].length;j++){
				if(linked[i][j][0]==1){
					s = s+"\nLink between "+i+" and "+j+" bandwidth: "+linked[i][j][1];
				}
			}
		}
		return s;
	}
}
