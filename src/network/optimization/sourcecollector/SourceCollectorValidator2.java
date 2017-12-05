package network.optimization.sourcecollector;

import network.optimization.sourcecollector.iface.Validator;

public class SourceCollectorValidator2 implements Validator{
	private SourcesCollectorsProblemModel model;
	private int[] backcost;
	
	public SourceCollectorValidator2(SourcesCollectorsProblemModel mod){
		model=mod;	
	}
	@Override
	public boolean isGraphValid() {
		backcost = model.getCollectorLimit();
		for(int i=0;i<backcost.length;i++){
			if(model.calculateCostAt(0)>backcost[i]){
				return false;
			}
		}
		return true;
	}

}
