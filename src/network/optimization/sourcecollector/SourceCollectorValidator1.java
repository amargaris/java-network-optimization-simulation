package network.optimization.sourcecollector;

import network.optimization.sourcecollector.iface.Validator;

public class SourceCollectorValidator1 implements Validator{
	private SourcesCollectorsProblemModel mod;
	private int sources,collectors;
	private int[][] graph;
	
	public SourceCollectorValidator1(SourcesCollectorsProblemModel model){
		mod=model;
		collectors = mod.getX();
		sources = mod.getY();
		graph = mod.getGraph();
	}
	@Override
	public boolean isGraphValid() {
		graph = mod.getGraph();
		for(int j=0;j<sources;j++){
			int counter=0;
			for(int i=0;i<collectors;i++){
					counter = counter+graph[i][j];
				if(counter>1){
					return false;
				}
			}
			if(counter==0){
				return false;
			}
		}
		return true;
	}
}


