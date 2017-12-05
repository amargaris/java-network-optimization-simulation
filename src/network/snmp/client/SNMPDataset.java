package network.snmp.client;
import java.util.ArrayList;


public class SNMPDataset {
	public ArrayList<ArrayList<Integer>> data;
	public int stepperset;
	public SNMPDataset(int thestep){
		stepperset =thestep;
		data= new ArrayList<ArrayList<Integer>>();
	}
	public void addRow(ArrayList<Integer> input){
		data.add(input);
	}
	public void addRow(Integer input){
		ArrayList<Integer> finalized =new ArrayList<Integer>();
		finalized.add(input);
		data.add(finalized);
	}
}
