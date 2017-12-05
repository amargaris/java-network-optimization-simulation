package network.optimization.sourcecollector.bean;

public class NetworkDataBundle {
	
	public int[][][] finalgraph;
	public double[] cost;
	public NetworkDataBundle(int[][][] thefinalgraph,double[] thecost){
		finalgraph= new int[thefinalgraph.length][thefinalgraph[0].length][thefinalgraph[0][0].length];//=thefinalgraph;
		if(thecost!=null){
			cost= new double[thecost.length];//=thecost;
		}
		for(int i=0;i<finalgraph.length;i++){
			if(thecost!=null){
				cost[i] = thecost[i];
			}
			for( int j=0;j<finalgraph[0].length;j++){
				for(int k=0;k<finalgraph[0][0].length;k++){
					finalgraph[i][j][k] = thefinalgraph[i][j][k];
				}
			}
		}
	}
}
