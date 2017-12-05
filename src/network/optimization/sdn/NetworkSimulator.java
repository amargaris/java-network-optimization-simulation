package network.optimization.sdn;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import network.optimization.sdn.gfx.MessageConsole;
import network.optimization.sdn.opt.RouletteRandom;



public class NetworkSimulator implements ActionListener{
	private JFrame fram,fram2;
	private Network net;
	private JButton start,refreshstart,next,settings,routetables;
	private JSlider slide;
	private JCheckBox showinfocheck,showlogcheck;
	private NetPanel pan;
	private Timer time;
	private ImageIcon logoicon;
	private int interval =1000;
	private int state=0;
	
	public NetworkSimulator(){
		try{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		}catch(Exception e){
			
		}
		logoicon = new ImageIcon(getClass().getResource("images/logo.png"));
		fram = new JFrame("Simulate a Network");
		fram.setSize(725,750);
		fram.setIconImage(logoicon.getImage());
		fram.setLocationRelativeTo(null);
		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cont = fram.getContentPane();
		cont.setLayout(new FlowLayout());
		net = new Network("Net 1");
		net.createRandomNetwork();
		pan = new NetPanel(net);
		pan.startTimer();
		cont.add(pan);
		cont.setBackground(Color.white);
		start = new JButton("Start");
		start.addActionListener(this);
		refreshstart = new JButton("Add network");
		refreshstart.addActionListener(this);
		ImageIcon ic = new ImageIcon(getClass().getResource("images/Settings.png"));
		settings = new JButton(ic);
		settings.addActionListener(this);
		next = new JButton("Next");
		settings.setSize(settings.getSize().width,next.getHeight());
		next.addActionListener(this);
		showinfocheck = new JCheckBox("Show Info");
		showinfocheck.addActionListener(this);
		showlogcheck = new JCheckBox("Show Log");
		showlogcheck.addActionListener(this);
		routetables = new JButton("Route");
		routetables.addActionListener(this);
		slide = new JSlider( SwingConstants.HORIZONTAL, 0, 0, 0 );
		slide.setMajorTickSpacing( 1 );
		slide.setMinorTickSpacing(  1 );
		slide.setPaintTicks ( true);
		slide.setPaintLabels(true);
		slide.setSnapToTicks( true);
		slide.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				pan.selectednetwork=slide.getValue();
				pan.repaint();
			}
			
		});
		JPanel sliderpan = new JPanel(new FlowLayout());
		sliderpan.add(slide);
		sliderpan.setOpaque(false);
		sliderpan.setBorder(BorderFactory.createTitledBorder("Network"));
		cont.add(sliderpan);
		JPanel buttonspan = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		buttonspan.setPreferredSize(new Dimension(330,100));
		buttonspan.setOpaque(false);
		//buttonspan.add(slide);
		buttonspan.add(start);
		buttonspan.add(refreshstart);
		buttonspan.add(next);
		buttonspan.add(settings);
		buttonspan.add(routetables);
		buttonspan.add(showinfocheck);
		buttonspan.add(showlogcheck);
		buttonspan.setBorder(BorderFactory.createTitledBorder("Controls"));
		cont.add(buttonspan);
		time = new Timer(interval,this);
		time.setRepeats(true);
		time.setCoalesce(true);
		fram.setVisible(true);
		fram2 = new JFrame();
		fram2.setSize(400,400);
		fram2.setLocationRelativeTo(null);
		
		JTextArea textComponent = new JTextArea();
		fram2.add( new JScrollPane( textComponent ) );
		MessageConsole mc = new MessageConsole(textComponent);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);

		fram2.setTitle("Log:");
		fram2.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				showlogcheck.setSelected(false);
			}
		});
		fram2.setIconImage(logoicon.getImage());
		//Telos me grafika arxi leitoyrgikwn pragmatwn
		
		//net.routingReport();
		net.setAllNodesEnabled(true);
		net.networkStatus();
		net.generateAllRoutingTables();
		//net.routingReport();
	}	
	public void startEvents(){
		time.start();
	}
	public void stopEvents(){
		time.stop();
	}
	public void openRoutingTableFrame(){
		JFrame fram = new JFrame("Routing tables");
		fram.setSize(500,500);
		fram.setLocationRelativeTo(null);
		JTabbedPane pan = new JTabbedPane();
		pan.setPreferredSize(new Dimension(400,400));
		for(int i=0;i<net.getNodes().size();i++){
			int a=net.getNodes().get(i).getRouteTable().size();
			String[][] data = new String[a][3];
			for(int p=0;p<a;p++){
				data[p][0] = "192.168.2."+net.getNodes().get(i).getRouteTable().get(p).id+"";
				data[p][1] = "eth"+net.getNodes().get(i).getRouteTable().get(p).link+"";
				data[p][2] = net.getNodes().get(i).getRouteTable().get(p).hops+"";
			}
			String[] datahead = {"To","Interface","Hops"};
			JTable table = new JTable(data,datahead);
			JScrollPane paned = new JScrollPane(table);
			paned.setPreferredSize(pan.getPreferredSize());
			pan.addTab("Node "+(i+1), paned);
		}
		Container con = fram.getContentPane();
		con.add(pan);
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fram.setVisible(true);
	}
	public void initializeNetwork(){ //unused
		net = new Network("Net 1");
		net.createRandomNetwork();
		net.routingReport();
		net.setAllNodesEnabled(true);
		
	}
	public void beginAnnealing(int length,int count){
		Random rand = new Random();
		double temperature =4.0; //Thermokrasia Tweaking
		System.out.println("first sample:");
		ArrayList<int[]> results = new ArrayList<int[]>();
		int[] offset =generateRandomState(length,count); //metabliti offset einai h kyria thesi
		if(offset==null){
			JOptionPane.showMessageDialog(null,"Rejected fron Annealing!");
			return;
		}
		results.add(offset);
		int cost = calculateCost(offset); //ypologize to cost function tis katastasis
		System.out.println("Cost:"+cost);
		int[][] neighbourhood = generateNeighbourhood(offset,length); //dhmioyrgei tin geitonia me oles tis enallages
		int neighoff =0; //metabliti poy tyxaiopoieitai gia tin epomeni lysi entos tis geitonias
		int internalcount=0;
		do{
			neighoff=rand.nextInt(neighbourhood.length); //tyxaio epomeno deigma
			System.out.println("Candidate:"+Arrays.toString(neighbourhood[neighoff]));
			int cost2 = calculateCost(neighbourhood[neighoff]); //cost toy epomenoy deigmatos
			System.out.println("Candidate Cost "+cost2);
			if(cost2<cost){ //periptwsi kalyteroy deigmatos
				System.out.println("case 1");
				offset=neighbourhood[neighoff]; //h kyria thesi allazei
				results.add(offset);
				cost=cost2; //sto cost ekxwroyme to kainoyrgio cost
				neighoff = 0; //epanarxikopoioyme tin neighoff
				neighbourhood = generateNeighbourhood(offset,length); //ksanadhmioyrgoyme tin geitonia		
			}
			else { //periptwsh xeiroteroy deigmatos elegxos deyteris eykairias->
				System.out.println("case 2");
				int dcost = cost-cost2; //ypologismos diaforas
				double exp = (double) - (dcost/temperature);
				double d = Math.exp(exp); //ypologismos pithanotitas dektis i akyris lysis
				System.out.println(d);
				double s = rand.nextDouble(); //tyxaio deigma gia elegxo
				System.out.println(s);
				if(s<d){ //se periptwsi poy petyxainei to peirama
					System.out.println("case 2.1");
					offset=neighbourhood[neighoff]; //ginetai dekti i xeiroteri lysi
					results.add(offset);
					neighbourhood = generateNeighbourhood(offset,length); //ksanadhmioyrgeite i geitonia
					cost=cost2; //ekxwreite to neo cost
				}
				else{ //apotygxanei to peirmana
					System.out.println("case 2.2");
					offset = generateRandomState(length,count); //epanatyxaiopoiisi toy deigmatos
					results.add(offset);
					cost = calculateCost(offset); //ypologismos toy kostoys
					neighbourhood = generateNeighbourhood(offset,length); //dimioyrgia tis geitonias
				}
				//temperature = temperature-1;
			}
			internalcount = internalcount+1;
			if(internalcount==4){
				temperature = temperature-1;
				internalcount=0;
			}
			 //meiwnoyme ti thermokrasia
		}while(temperature>0);
		int max=Integer.MAX_VALUE;
		int maxpos=-1;
		for(int i=0;i<results.size();i++){
			int[] temp = results.get(i);
			int tempcount = calculateCost(temp);
			if(tempcount<max){
				max=tempcount;
				maxpos=i;
			}
		}
		System.out.println("Result:" +Arrays.toString(results.get(maxpos))+" With Score:"+max);
	}
	public int[] beginAnnealing(VirtualNetworkRequest v,Network n){
		Random rand = new Random();
		double temperature =25.0; //Thermokrasia Tweaking
		//System.out.println("first sample:");
		float mincost = Float.MAX_VALUE;
		int[] maxpos = null;
		int[] offset =generateRandomState2(v,n); //metabliti offset einai h kyria thesi
		System.out.println("first sample:"+Arrays.toString(offset));
		
		System.out.println("offset length"+offset.length);
		float cost = calculateCost(offset,v,n); //ypologize to cost function tis katastasis
		System.out.println("Cost:"+cost);
		int[][] neighbourhood = generateNeighbourhood(offset,n.getNodes().size()); //dhmioyrgei tin geitonia me oles tis enallages
		int neighoff =0; //metabliti poy tyxaiopoieitai gia tin epomeni lysi entos tis geitonias
		int internalcount=0;
		outerloop:do{
			int counter=0;
			do{
				counter= counter+1;
				if(counter==500){
					JOptionPane.showMessageDialog(null, "Rejected from Annealing Tethe");
					//return null;
					break outerloop;
				}
				neighoff=rand.nextInt(neighbourhood.length); //tyxaio epomeno deigma
				System.out.println("Candidate:"+Arrays.toString(neighbourhood[neighoff]));
			}while(!isValid(neighbourhood[neighoff], v, n));
			float cost2 = calculateCost(neighbourhood[neighoff],v,n); //cost toy epomenoy deigmatos
			System.out.println("Candidate Cost "+cost2);
			if(cost2<cost){ //periptwsi kalyteroy deigmatos
				System.out.println("case 1");
				offset=neighbourhood[neighoff]; //h kyria thesi allazei
				cost=cost2; //sto cost ekxwroyme to kainoyrgio cost
				neighoff = 0; //epanarxikopoioyme tin neighoff
				neighbourhood = generateNeighbourhood(offset,n.getNodes().size()); //ksanadhmioyrgoyme tin geitonia		
			}
			else { //periptwsh xeiroteroy deigmatos elegxos deyteris eykairias->
				System.out.println("case 2");
				float dcostt = cost-cost2; //ypologismos diaforas
				double dcost = Math.abs(dcostt);
				double exp = (double) - (dcost/temperature);
				double d = Math.exp(exp); //ypologismos pithanotitas dektis i akyris lysis
				System.out.println(d);
				double s = rand.nextDouble(); //tyxaio deigma gia elegxo
				System.out.println(s);
				if(s<d){ //se periptwsi poy petyxainei to peirama
					System.out.println("case 2.1");
					offset=(int[])Node.deepClone(neighbourhood[neighoff]); //ginetai dekti i xeiroteri lysi
					neighbourhood = generateNeighbourhood(offset,n.getNodes().size()); //ksanadhmioyrgeite i geitonia
					cost=cost2; //ekxwreite to neo cost
				}
				else{ //apotygxanei to peirmana
					System.out.println("case 2.2");
					counter=0;
					do{
						counter= counter+1;
						if(counter==500){
							JOptionPane.showMessageDialog(null, "Rejected from Annealing Tethe");
							break outerloop;
						}
						offset = generateRandomState2(v,n); //epanatyxaiopoiisi toy deigmatos
					}while(!isValid(offset,v,n));
					cost = calculateCost(offset,v,n); //ypologismos toy kostoys
					neighbourhood = generateNeighbourhood(offset,n.getNodes().size()); //dimioyrgia tis geitonias
				}
			}
			internalcount = internalcount+1;
			if(internalcount==4){
				temperature = temperature-1;
				internalcount=0;
			}
			if(cost<mincost){
				mincost=cost;
				maxpos=offset;
			}
			 //meiwnoyme ti thermokrasia
		}while(temperature>0);
		System.out.println("Result:" +Arrays.toString(maxpos)+" With Score:"+mincost);
		return maxpos;
	}
	public void beginGenetic(int length,int count){
		int chromcount =10;
		int generation =0;
		int generationlimit=20;
		int[][] first = generateNChromosomes(length,count,chromcount); //random generate
		int[] scores = getChromosomeScore(first);//evaluate
		System.out.println("------FIRST-----");
		for(int i=0;i<first.length;i++){
			System.out.println(Arrays.toString(first[i])+" score :"+scores[i]);
		}
		int[][] selected = new int[first.length][length];
		while(generation<generationlimit){
			System.out.println("@@@@@@@@@@@@@@");
			System.out.println("generation "+generation);
			if(selected!=null){
				System.out.println("------LISTED-----");
				for(int i=0;i<selected.length;i++){
					System.out.println(Arrays.toString(first[i])+" score :"+scores[i]);
				}
			}
			int maxscore=0;
			for(int i=0;i<scores.length;i++){
				if(scores[i]>maxscore){
					maxscore=scores[i];
				}
			}
			ArrayList<Integer> input = new ArrayList<Integer>();
			for(int i=0;i<scores.length;i++){
				input.add(new Integer(maxscore-scores[i]));
			}
			//rouleta me syblirwma ws prow to megalytero
			RouletteRandom roulette = new RouletteRandom(input,0);
			selected = new int[first.length][first[0].length];
			ArrayList<Integer> roul = roulette.getNResults(first.length);
			for(int i=0;i<roul.size();i++){
				int[] temp = new int[first[0].length];
				for(int f=0;f<temp.length;f++){
					temp[f]=first[roul.get(i)][f];
				}
				selected[i]=(int[])Node.deepClone(temp);
			}
			//~rouleta
			System.out.println("------Selection-----");
			for(int i=0;i<selected.length;i++){
				System.out.println(Arrays.toString(selected[i]));
			}
			breedSelected(selected); //anaparagwgh
			System.out.println("------After Breed-----");
			for(int i=0;i<selected.length;i++){
				System.out.println(Arrays.toString(selected[i]));
			}
			mutateSelected(selected,length); //metallaksi
			scores = getChromosomeScore(selected);//evaluate
			System.out.println("------After Mutation-----");
			for(int i=0;i<selected.length;i++){
				System.out.println(Arrays.toString(selected[i])+" score "+scores[i]);
			}
			
			generation = generation +1; //allagi genias
			first=(int[][])Node.deepClone(selected);
		}
		int max = Integer.MAX_VALUE;
		int maxpos=-1;
		for(int i=0;i<scores.length;i++){
			if(scores[i]<max){
				max=scores[i];
				maxpos=i;
			}
		}
		System.out.println("Result:" +Arrays.toString(selected[maxpos])+" With Score:"+max);
	}
	public void breedSelected(int[][] selected){
		for(int i=0;i<selected.length;i=i+2){
			for(int a=0;a<selected[0].length/2;a++){
				int temp = selected[i][a];
				selected[i][a]=(Integer)Node.deepClone(selected[i+1][a]);
				selected[i+1][a]=(Integer)Node.deepClone(temp);
			}
		}
	}
	public void mutateSelected(int[][] selected,int length){
		Random rand = new Random();
		float check = 0.05f;
		for(int i=0;i<selected.length;i++){
			for(int j=0;j<selected[0].length;j++){
				float temp = rand.nextFloat();
				if(temp<check){
					if(selected[i][j]>0){
					selected[i][j]=selected[i][j]-1;//rand.nextInt(length);
					}
					else{
						selected[i][j]=selected[i][j] +1;
					}
				}
			}
			
		}
	}
	public int[] generateRandomState(int length,int count){
		Random rand = new Random();
		int[] array = new int[count];
		int attempts=0;
		int limit=40;
		do{
			for(int i=0;i<array.length;i++){
				array[i]=rand.nextInt(length);
			}
			if(attempts>limit){
				return null;
			}
			attempts=attempts+1;
		}while(!isValid(array));

		return array;
	}
	public int[] generateRandomState2(VirtualNetworkRequest r,Network n){
		Random rand = new Random();
		int[] results = new int[r.numOfNodes];
		for(int i=0;i<results.length;i++){
			results[i] = rand.nextInt(n.getNodes().size());
		}	
		return results;
	}
	public int[][] generateNChromosomes(int length,int count,int n){
		int[][] returnval= new int[n][count];
		for(int i=0;i<n;i++){
			returnval[i]=generateRandomState(length,count);
		}
		return returnval;
	}
	public boolean isValid(int[] vector){
		ArrayList<Integer> passed = new ArrayList<Integer>();
		for(int a:vector){
			for(Integer s:passed){
				if(s==a){
					//return false;
				}
			}
			passed.add(a);
		}
		return true;
	}
	public boolean isValid(int[] vector,VirtualNetworkRequest r,Network n){
		//elegxos gia 1 node / Vnode
		ArrayList<Integer> passed = new ArrayList<Integer>();
		for(int a:vector){
			for(Integer s:passed){
				if(s==a){
					System.out.println("Failed constraint 1");
					return false;
				}
			}
			passed.add(a);
		}
		passed.clear();
		passed=null;
		//elegxos gia cpu capacity kai ram
		for(int i=0;i<vector.length;i++){
			float total =r.cpus[i]*r.freq[i];
			if(n.getNodes().get(vector[i]).getUsedCpu()+total>n.getNodes().get(vector[i]).getMaxCpu()){
				System.out.println("Failed constraint 2");
				return false;
			}
			if(n.getNodes().get(vector[i]).getUsedRam()*1024+r.ram[i]>n.getNodes().get(vector[i]).getRam()*1024){
				System.out.println("Failed constraint 3");
				return false;
			}
		}
		//elegxos gia link
		for(int i=0;i<r.linked.length;i++){
			for(int j=0;j<r.linked[0].length;j++){
				if(r.linked[i][j][0]==1){
					ArrayList<Link> links =n.getPath(i,j);
					for(Link l:links){
						if(l.getUsedSpeed()*1000+r.linked[i][j][1]>l.getSpeed()*1000){
							System.out.println("Failed constraint 4");
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	public int[] getChromosomeScore(int[][] chrome){
		int[] scores = new int[chrome.length];
		for(int i=0;i<chrome.length;i++){
			scores[i]=calculateCost(chrome[i]);
		}
		return scores;
	}
	public int calculateCost(int[] input){
		if(!isValid(input)){
			return 0;
		}
		int count=0;
		for(int i=0;i<input.length;i++){
			count = count + input[i];
		}
		return count;
	}
	public float calculateCost(int[] input,VirtualNetworkRequest r,Network n){ //synarthsi kostoys
		//gia kathe node
		float cpucost=0;
		float ramcost=0;
		//System.out.println("R nodes = "+r.numOfNodes);
		for(int i=0;i<r.numOfNodes;i++){
			cpucost = cpucost + (r.cpus[i]*r.freq[i]/n.getNodes().get(input[i]).getMaxCpu());
			ramcost = ramcost + r.ram[i]/n.getNodes().get(input[i]).getRam();
		}
		
		//gia kathe link
		float linkcost=0.0f;
		
		for(int i=0;i<r.linked.length;i++){
			for(int j=0;j<r.linked[0].length;j++){
				if(r.linked[i][j][0]==1){
					ArrayList<Link>links=n.getPath(i,j);
					for(Link l:links){
						System.out.println(l);
						linkcost = linkcost+(r.linked[i][j][1]/l.getSpeed()*1000);
					}
				}
			}
		}
		//System.out.println(cpucost+ramcost+linkcost);
		return cpucost+ramcost+linkcost;//linkcost;
	}
	public int[][] generateNeighbourhood(int[] array,int length){
		
		int point=0;
		int[][] finalresult = new int[array.length*length-1][array.length];
		for(int i=0;i<array.length;i++){
			for(int j=0;j<length;j++){
				int[] temp = new int[array.length];
				if (array[i]==j){
					continue;
				}
				for(int k=0;k<array.length;k++){
					if(k==i){
						continue;
					}
					int a = array[k];
					temp[k] =a;
				}
				temp[i]=j;
				finalresult[point]=temp;
				//System.out.println(Arrays.toString(finalresult[point]));
				point = point +1;
				
			}
		}
		return finalresult;
	}
	public static void main(String... args){
		@SuppressWarnings("unused")
		NetworkSimulator sim = new NetworkSimulator();

	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(showinfocheck)){
			
			pan.infopainted=showinfocheck.isSelected();
		}
		else if(arg0.getSource().equals(next)){
			net.generateRandomMessage();
		}
		else if(arg0.getSource().equals(settings)){
			
		}
		else if(arg0.getSource().equals(routetables)){
			openRoutingTableFrame();
		}
		else if(arg0.getSource().equals(showlogcheck)){
			fram2.setVisible(showlogcheck.isSelected());
		}
		else if(arg0.getSource().equals(start)){
			if(state==0){
				startEvents();
				state=1;
				((JButton)arg0.getSource()).setText("Stop");
			}
			else{
				stopEvents();
				state=0;
				((JButton)arg0.getSource()).setText("Start");
			}
		}
		else if(arg0.getSource().equals(time)){
			net.generateRandomMessage();
		}
		else if(arg0.getSource().equals(refreshstart)){
			Random rand = new Random();
			int length = rand.nextInt(10)+2; //synolo timwn poy pairnei to vector 2 -> {0,1}
			int count = rand.nextInt(10) +1; //megethos toy vector 3 -> {x,y,z}
			VirtualNetworkRequest r = new VirtualNetworkRequest(net);
			System.out.println(r.toString());
			//int[] result = generateRandomState2(r,this.net);
			//System.out.println(Arrays.toString(result));
			//System.out.println(isValid(result,r,net));
			//beginAnnealing(length,count);
			int[] result=beginAnnealing(r,net);
			if(result==null){
				return;
			}
			Network s = r.tethe(net, result);
			pan.addNetwork(s);
			//beginGenetic(length,count);
			slide.setMaximum(slide.getMaximum()+1);
		}
	}
}
