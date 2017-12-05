package network.optimization.sourcecollector;
import java.awt.Component;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import network.optimization.sourcecollector.bean.NetworkDataBundle;
import network.optimization.sourcecollector.gfx.TableColumnAdjuster;
import network.optimization.sourcecollector.iface.ProblemModel;
import network.optimization.sourcecollector.iface.Validator;




public class SourcesCollectorsProblemModel implements ProblemModel{
	
	private int maxsources,maxcollectors,maxcost,maxlimit; //Oria problimatos
	private int sources,collectors;//Proswrines times
	private double spaceorder,possiblespaceorder;
	private Random rand;
	
	private int[][] graph;
	private int[][] costgraph;
	private int[] collectorlimit;
	private Validator[] validators;
	private int validatorcount=0;
	private NetworkDataBundle bd;
	
	private final int MAX_PER_PAGE=512;
	
	private ImageIcon success = new ImageIcon(getClass().getResource("images/success.png"));
	private ImageIcon failed = new ImageIcon(getClass().getResource("images/failed.png"));
	
	public SourcesCollectorsProblemModel(int themaxsources,int themaxcollectors,int themaxcost,int themaxlimit){
		maxsources=themaxsources;
		maxcollectors=themaxcollectors;
		maxcost = themaxcost;
		maxlimit = themaxlimit;
		
	}


	@Override
	public void initialize() {
		rand = new Random();
		
		sources = rand.nextInt(maxsources)+1;
		collectors = rand.nextInt(maxcollectors)+1;
		graph = new int[collectors][sources];
		
		costgraph = new int[collectors][sources];
		collectorlimit = new int[collectors];
		for(int i=0;i<costgraph.length;i++){
			collectorlimit[i]=rand.nextInt(maxlimit)+1;
			for(int j=0;j<costgraph[0].length;j++){
				costgraph[i][j]=rand.nextInt(maxcost)+1;
			}
		}
		spaceorder = Math.pow(2,collectors*sources);
		possiblespaceorder = Math.pow(collectors, sources);
		bd=null;
		clearValidators();
		addValidator(new SourceCollectorValidator1(this));
		addValidator(new SourceCollectorValidator2(this));
		randomizeGraph();
	}
	public int getX(){ //X diastash ( gia to JPanel)
		return collectors;
	}
	public int getY(){ //Y diastash ( gia to JPanel);
		return sources;
	}
	public double getSpaceOrder(){
		return spaceorder;
	}
	public double getPossibleSpaceOrder(){
		return possiblespaceorder;
	}
	public int[] getCollectorLimit(){
		return collectorlimit;
	}
	public int calculateCostAt(int collector){ //Methodos poy ypologizei to kostos sto sygkekrimeno collector
		int counter=0;
		for(int i=0;i<sources;i++){
				counter = counter + (graph[collector][i]*costgraph[collector][i]);
		}
		return counter;
	}
	public int getLimitAt(int collector){ //Epistrefei tin timi toy max capacity
		return collectorlimit[collector];
	}
	@Override
	public void randomizeGraph() {
		
		int counter=0;
		do{
			for(int j=0;j<sources;j++){
				for(int i=0;i<collectors;i++){
					graph[i][j]=0;
				}
			}
			for(int j=0;j<sources;j++){
				int a = rand.nextInt(collectors);
				graph[a][j] = 1;
			}
			counter=counter+1;
		}while((!isValid())&&counter<100);
	}
	public NetworkDataBundle getNGenes(int n){
		int[][][] finalgraph = new int[n][collectors][sources];
		double[] costs = new double[n];
		for(int i=0;i<n;i++){
			randomizeGraph();
			costs[i] = LinearToHash(matrixToLinear(graph));
			for(int k=0;k<collectors;k++){
				for(int j=0;j<sources;j++){
					finalgraph[i][k][j]=graph[k][j];
					
				}
			}
		}
		NetworkDataBundle bd = new NetworkDataBundle(finalgraph,costs);
		return bd;
	}
	public NetworkDataBundle getNextGeneration(NetworkDataBundle b){ //TODO edw epomenh genia
		int[][][] temp = b.finalgraph;
		double[] tempcost = b.cost;
		quickSort(tempcost,temp,0,tempcost.length-1);
		for(int i=0;i<tempcost.length;i++){
			System.out.println(""+tempcost[i]);
		}
		return null;
	}
	public static int partition(double arr[],int[][][] arr2, int left, int right) {//Boithitiki methodos toy quicksort
	      int i = left, j = right;
	      double tmp;
	      int[][] tmp2;
	      int pivot = (int)arr[(left + right) / 2];
	     
	      while (i <= j) {
	            while (arr[i] < pivot)
	                  i++;
	            while (arr[j] > pivot)
	                  j--;
	            if (i <= j) {
	                  tmp = arr[i];
	                  tmp2 = arr2[i];
	                  arr[i] = arr[j];
	                  arr2[i]=arr2[j];
	                  arr[j] = tmp;
	                  arr2[j]= tmp2;
	                  i++;
	                  j--;
	            }
	      };
	     
	      return i;
	} 
	public static void quickSort(double arr[],int[][][] arr2, int left, int right) { //QuickSort Algorithmos 
	      int index = partition(arr,arr2, left, right);
	      if (left < index - 1)
	            quickSort(arr,arr2, left, index - 1);
	      if (index < right)
	            quickSort(arr,arr2, index, right);
	}
	public JTable getTableWithGenes(NetworkDataBundle bd){
		String[] legend = {"Id","Hash","Gene","Status","Cost"};
		Object[][] theresults;
		int limit=bd.finalgraph.length;
		theresults = new Object[limit+1][legend.length];
		double counterforcost=0.0;
		for(double l=0.0;l<limit;l++){
			graph = bd.finalgraph[(int)l];
			theresults[(int)l][0]=(int)l;
			theresults[(int)l][1]=bd.cost[(int)l];
			theresults[(int)l][2]=linearToString(matrixToLinear(graph));
			theresults[(int)l][3]=isValid()?new JLabel(success):new JLabel(failed);
			theresults[(int)l][4]=new Double(calculateCost());
			counterforcost = counterforcost + calculateCost();
		}
		theresults[limit][0]="";
		theresults[limit][1]="";
		theresults[limit][2]="";
		theresults[limit][3]=new JLabel("Average");
								
		theresults[limit][4] = ""+counterforcost/limit;
		DefaultTableModel model = new DefaultTableModel(theresults,legend){
			
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				if (column == 4) { 
				return Integer.class;
				}
				if(column == 0){
					return Integer.class;
				}
				if(column == 1){
					return Integer.class;
				}
				return String.class;
				}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
				
		};
		
		JTable tab = new JTable(model);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(false);
		tab.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
		tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(tab);
		tca.adjustColumns();
		tab.setAutoCreateRowSorter(true);
		theresults=null;
		tab.setName(""+limit);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(true);
		return tab;
	}
	public int[] getType2Gene(){
		int[] returnvalue=new int[sources];
		for(int i=0;i<sources;i++){
			for (int j=0;j<collectors;j++){
				if(graph[j][i]==1){
					returnvalue[i]=j;
				}
			}
		}
		return returnvalue;
	}
	@Override
	public String linearToString(int[] gene) {
		String s="";
		for(int i=0;i<gene.length;i++){
			s = s+gene[i];
		}
		return s;
	}

	@Override
	public int[] matrixToLinear(int[][] graph) {
		int[] returnvalue = new int[graph.length*graph[0].length];
		int counter =0;
		for(int i=0;i<graph.length;i++){
			for(int j=0;j<graph[0].length;j++){
				returnvalue[counter]=graph[i][j];
				counter = counter +1;
			}
		}
		return returnvalue;
	}

	@Override
	public double LinearToHash(int[] gene) {
		int[] temp = matrixToLinear(graph);
		double count =0.0;
		for(int i=0;i<temp.length;i++){
			count = count + ((Math.pow(2, i)*temp[i]));
		}
		return count;
	}

	@Override
	public int[][] LinearToGraph(int[] gene) {
		int[][] result = new int[collectors][sources];
		for(int i=0;i<gene.length;i++){
			if(gene[i]==1){
				result[i/sources][i%sources]=1;
			}
			else {
				result[i/sources][i%sources]=0;
			}
		}
		return result;
	}

	@Override
	public boolean isValid() { 
		boolean flag =true;
		if(this.validatorcount==0){
			
		}
		else{
			for(int i=0;i<validatorcount;i++){
				flag = flag&validators[i].isGraphValid();
			}
		}
		return flag;
	}
	public boolean[] isValidVector() {
		boolean[] results = new boolean[validatorcount];
		for(int i =0;i<results.length;i++){
			results[i] = validators[i].isGraphValid();
		}
		return results;
	}

	@Override
	public void addValidator(Validator v) { //Methodos poy prosthetei enan validator sto Model
		if(validatorcount==0){
			validatorcount=validatorcount+1;
			validators = new Validator[validatorcount];
			validators[0]=v;
		}
		else{
			validatorcount=validatorcount+1;
			Validator[] newvalidators = new Validator[validatorcount];
			for(int i=0;i<validators.length;i++){
				newvalidators[i]=validators[i];
			}
			newvalidators[validatorcount-1]=v;
			validators=newvalidators;
		}
	}

	@Override
	public int calculateCost() { //Methodos poy ypologizei to kostos toy sygkekrimenoy grafoy
		int cost=0;
		for(int i=0;i<graph.length;i++){
			for(int j=0;j<graph[0].length;j++){
					cost = cost + (graph[i][j]*costgraph[i][j]);
			}
		}
		return cost;
	}


	@Override
	public int[][] getGraph() {
		return graph;
	}
	public int[][] getCostGraph(){
		return costgraph;
	}


	@Override
	public int[] hashToLinear(double val) {
		int[] thearray = new int[collectors*sources];
		for(int i=thearray.length;i>=0;i--){
			double key = Math.pow(2,i);
			if(val-key>=0){
				val = val-key;
				thearray[i]=1;
			}
			else {
				//continue;
			}
			if(val==0){
				break;
			}
		}
		return thearray;
	}
	public void runNTimes(int n){ //Methodos poy dokimazei n tyxaia deigmata kai krataei to kalytero apotelesma
		long s = System.currentTimeMillis();
		int score = 10000000;
		int where = -1;
		int[][][] saved= new int[n][collectors][sources]; 
		for(int i=0;i<n;i++){
			randomizeGraph();
			if(calculateCost()<score){
				score=calculateCost();
				where = i;
				for(int k=0;k<collectors;k++){
					for(int f=0;f<sources;f++){
						saved[i][k][f]=graph[k][f];
					}
				}
				continue;
			}
		}
		long k = System.currentTimeMillis();
		long time = k-s;
		graph = saved[where];
		score = calculateCost();
		JOptionPane.showMessageDialog(null,"Best result is "+score+" after "+n+" Iterations and "+time+" MilliSeconds");
	}
	public JTable getExhaustiveSearchResults(int n){
		String[] legend = {"Id","Hash","Gene","Status","Cost"};
		Object[][] theresults;
		if(spaceorder<MAX_PER_PAGE){
			theresults = new Object[(int)spaceorder][legend.length];
			for(double l=0.0;l<spaceorder;l++){
				graph = LinearToGraph(hashToLinear(l));
				theresults[(int)l][0]=(int)l;
				theresults[(int)l][1]=l;
				theresults[(int)l][2]=linearToString(hashToLinear(l));
				if(isValid()){
					theresults[(int)l][3]=new JLabel(success);
					theresults[(int)l][4]=calculateCost();//NetworkGraphicsPanel.calculateCost(temp, this.cost);
				}
				else{
					theresults[(int)l][3]=new JLabel(failed);//failed);
					((JLabel)theresults[(int)l][3]).setOpaque(false);
					theresults[(int)l][4]="N/A";
				}
			}
		}
		else {
			theresults = new Object[MAX_PER_PAGE][legend.length];
			for(double l=0.0;l<MAX_PER_PAGE;l++){
				graph = LinearToGraph(hashToLinear((n*MAX_PER_PAGE)+l));
				theresults[(int)l][0]=(int)((n*512.0)+l);
				theresults[(int)l][1]=(n*512.0)+l;
				theresults[(int)l][2]=linearToString(hashToLinear((n*MAX_PER_PAGE)+l));//geneToString(matrixToLinear(temp));
				if(isValid()){
					theresults[(int)l][3]=new JLabel(success);
					theresults[(int)l][4]=calculateCost();//NetworkGraphicsPanel.calculateCost(temp, this.cost);
				}
				else{
					theresults[(int)l][3]=new JLabel(failed);//failed);
					((JLabel)theresults[(int)l][3]).setOpaque(false);
					theresults[(int)l][4]="N/A";
				}
			}
		}
		DefaultTableModel model = new DefaultTableModel(theresults,legend){
			
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				if (column == 4) { 
				return Integer.class;
				}
				if(column == 0){
					return Integer.class;
				}
				if(column == 1){
					return Integer.class;
				}
				return String.class;
			}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
				
		};
		JTable tab = new JTable(model);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(false);
		tab.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
		tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(tab);
		tca.adjustColumns();
		tab.setAutoCreateRowSorter(true);
		theresults=null;
		tab.setName(""+spaceorder);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(false);
		return tab;
	}
	public JTable getExhaustiveSuccessfulSearchResults(int n){ 

		String[] legend = {"Id","Hash","Gene","Status","Cost"};
		//Elegxos
		/*
			Runnable run = new Runnable(){

				@Override
				public void run() {
					JFrame fram = new JFrame();
					fram.setSize(new Dimension(200,90));
					JProgressBar bar = new JProgressBar();
					bar.setStringPainted(true);
					bar.setPreferredSize(new Dimension(160,20));
					Container cont = fram.getContentPane();
					fram.setLocationRelativeTo(null);
					cont.setLayout(new FlowLayout());
					cont.add(bar);
					bar.setIndeterminate(false);
					fram.setVisible(true);
					getSuccessFullDataBundle(bar);
				}
				
			};
			new Thread(run).start();
			return null;
		*/
		//
		if(bd==null){
		   getSuccessFullDataBundle(null);
		}
		
		int limit = bd.finalgraph.length;
		Object[][] theresults;
		if(limit<MAX_PER_PAGE){
			theresults = new Object[limit][legend.length];
			for(double l=0.0;l<limit;l++){
				graph = bd.finalgraph[(int)l];
				theresults[(int)l][0]=(int)l;
				theresults[(int)l][1]=bd.cost[(int)l];
				theresults[(int)l][2]=linearToString(matrixToLinear(graph));
				theresults[(int)l][3]=new JLabel(success);
				theresults[(int)l][4]=new Double(calculateCost());
			}
		}
		else {
			if(limit-(n*MAX_PER_PAGE)<MAX_PER_PAGE){
				theresults = new Object[limit-(n*MAX_PER_PAGE)][legend.length];
				for(double l=0.0;l<theresults.length;l++){
					graph = bd.finalgraph[(int)((n*MAX_PER_PAGE)+l)];
					theresults[(int)l][0]=(int)((n*MAX_PER_PAGE)+l);
					theresults[(int)l][1]=bd.cost[(int)((n*MAX_PER_PAGE)+l)];
					theresults[(int)l][2]=linearToString(matrixToLinear(graph));
					theresults[(int)l][3]=new JLabel(success);
					theresults[(int)l][4]=new Double(calculateCost());
				}
			}
			else{
				theresults = new Object[MAX_PER_PAGE][legend.length];
				for(double l=0.0;l<theresults.length;l++){
					graph = bd.finalgraph[(int)((n*MAX_PER_PAGE)+l)];
					theresults[(int)l][0]=(int)((n*MAX_PER_PAGE)+l);
					theresults[(int)l][1]=bd.cost[(int)((n*MAX_PER_PAGE)+l)];
					theresults[(int)l][2]=linearToString(matrixToLinear(graph));
					theresults[(int)l][3]=new JLabel(success);
					theresults[(int)l][4]=new Double(calculateCost());
				}
			}
		}
		DefaultTableModel model = new DefaultTableModel(theresults,legend){
			
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				if (column == 4) { 
				return Integer.class;
				}
				if(column == 0){
					return Integer.class;
				}
				if(column == 1){
					return Integer.class;
				}
				return String.class;
				}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
				
		};
		
		JTable tab = new JTable(model);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(false);
		tab.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
		tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnAdjuster tca = new TableColumnAdjuster(tab);
		tca.adjustColumns();
		tab.setAutoCreateRowSorter(true);
		theresults=null;
		tab.setName(""+limit);
		tab.setCellSelectionEnabled(false);
		tab.setColumnSelectionAllowed(false);
		tab.setRowSelectionAllowed(false);
		return tab;
	}
	
	class ImageRenderer extends DefaultTableCellRenderer {//Klasi gia na zwgrafizetai to ImageIcon stin stili 4
	
		private static final long serialVersionUID = 1L;
		private JLabel lbl = new JLabel();
	
		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) {
	    	try{
	    		lbl=(JLabel)value;
	    		return lbl;
	    	}catch(Exception e){
	    		return (Component)value;
	    	}
	    }
	}
	public void getSuccessFullDataBundle(JProgressBar bar){

		int limit = (int)possiblespaceorder;
		int[][][] finalgraph = new int[limit][collectors][sources];
		double[] costs = new double[limit];
		int counter =0;
		System.out.println("Entering validation loop");
		for(double l=0.0;l<spaceorder;l++){
			if(bar!=null){
				
				double k = l/spaceorder *100.0;
				int s = (int) k;
				bar.setValue(s);
				bar.setString(""+l+" of "+spaceorder);
					
				
			}
			System.out.println(""+l+" of "+spaceorder);
			graph = LinearToGraph(hashToLinear(l));
			if(isValid()){
				finalgraph[counter]=graph;
				costs[counter]=l;
				counter=counter+1;
				if(counter==finalgraph.length){
					break;
				}
			}
		}
		if(bar!=null){
			double k = 100.0;
			int s = (int) k;
			bar.setValue(s);
			bar.setString(""+spaceorder+" of "+spaceorder);
		}
		
		if(counter==finalgraph.length){
			System.out.println("Successful results: "+finalgraph.length);
			bd= new NetworkDataBundle(finalgraph,costs);
		}
		else{
			System.out.println("Successful results: "+counter);
			int[][][]finalgraph2 = new int[counter][collectors][sources];
			for(int i=0;i<finalgraph2.length;i++){
				finalgraph2[i]=finalgraph[i];
			}
			bd = new NetworkDataBundle(finalgraph2,costs);
		}
	}
	public int[] getBundleMask(){ //TODO edw eimastan palia
		int[] result = null;
		if(bd!=null){
			
		}
		return result;
	}
//HL2fndNBTs

	@Override
	public void clearValidators() {
		validatorcount=0;
		validators=null;
		
	}


	@Override
	public char[] linearToATGC(int[] gene) {
		char[] dna = new char[gene.length/2];
		for(int i=0;i<dna.length;i++){
			if(gene[i*2]==0){
				if(gene[(i*2)+1]==0){
					dna[i]='A';
				}
				else{
					dna[i]='T';
				}
			}
			else{
				if(gene[(i*2)+1]==0){
					dna[i]='C';
				}
				else{
					dna[i]='G';
				}
			}
		}
		return dna;
	}

}
