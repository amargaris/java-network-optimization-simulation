package network.optimization.sourcecollector.iface;

public interface ProblemModel {
	
	
	public void initialize();//Arxikopoiiseis
	public void randomizeGraph();//Tyxaia timi toy graph
	public String linearToString(int[] gene);//Gonidio se String
	public char[] linearToATGC(int[] gene);
	public int[] matrixToLinear(int[][] graph);//Grafos se gonidio
	public int[] hashToLinear(double l);
	public double LinearToHash(int[] gene);//Gonidio se Hash
	public int[][] LinearToGraph(int[] gene);//Gonidio se Grafo
	public boolean isValid();//Elegxei olous toy validators
	public void addValidator(Validator v);//Prosthetei neo validator
	public int calculateCost();//Epistrefei to kostos tis sygkekrimenis graph
	public int[][] getGraph();
	public void clearValidators();//Katharizei ola ta validators
}
