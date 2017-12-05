package network.optimization.sdn;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import network.optimization.sdn.bean.Message;
import network.optimization.sdn.bean.RouteEntry;

public class Network {
	
	private ArrayList<Node> nodes;
	private ArrayList<Link> links;
	@SuppressWarnings("unused")
	private String name;
	private static final int MAX_NODE=10;
	private static final int MIN_NODE=5;
	
	public static int received=0;
	public static int send=0;
	
	public Network(String thename){
		name=thename;
		nodes=  new ArrayList<Node>();
		links = new ArrayList<Link>();
	}
	/**
	 * Constructor της κλάσης Network
	 */
	public void setLinks(ArrayList<Link> thelinks){
		links=thelinks;
		//Kai edw eisagwgi twn nodes;
	}
	/**
	 * Επιστρέφει το ArrayList(Link) της δομής
	 */
	public ArrayList<Link> getLinks(){
		return links;
	}
	/**
	 * Επιστρέφει το ArrayList(Node) της δομής
	 */
	public ArrayList<Node>getNodes(){
		return nodes;
		
	}
	/**
	 * Δημιουργεί τυχαίο δίκτυο
	 */
	public void createRandomNetwork(){
		Random rand = new Random();
		int count = rand.nextInt(MAX_NODE)+MIN_NODE;
		nodes= new ArrayList<Node>();
		for(int i=1;i<=count;i++){
			nodes.add(new Node(i));
		}
		for(int j=0;j<nodes.size();j++){
			int towho;
			do{
				towho = rand.nextInt(nodes.size());
			}while(towho==j||nodes.get(j).isConnectedTo(nodes.get(towho)));
			links.add(new Link(nodes.get(j),nodes.get(towho)));
		}
	}
	/**
	 * Δίνει ένα report για το routing table του κάθε στοιχείου
	 */
	public void routingReport(){
		for(Node n:nodes){
			ArrayList<RouteEntry> routes = n.getRouteTable();
			System.out.println(""+n.getName()+" route table :");
			for(RouteEntry r:routes){
				System.out.println(r.toString());
			}
			System.out.println("________________");
		}
	}
	/**
	 * Δίνει μία αναφορά σχετικά με το δίκτυο
	 */
	public void networkReport(){
		if(nodes.size()!=0){
			for(int i=0;i<nodes.size();i++){
				System.out.println("Node "+i);
				nodes.get(i).getConnectedLinksReport();
			}
		}
		else{
			System.out.println("Report: Empty Network");
		}
	}
	/**
	 * Δημιουργεί ένα τυχαίο μήνυμα σε ένα τυχαίο Node με στόχο ένα άλλο τυχαίο Node του δικτύου
	 */
	public void generateRandomMessage(){
		Random rand = new Random();
		Node sourcenode = nodes.get(rand.nextInt(nodes.size()));
		Node destnode;
		do{
			destnode = nodes.get(rand.nextInt(nodes.size()));
		}while(destnode.getName().equalsIgnoreCase(sourcenode.getName()));
		
		Message randommessage = new Message(sourcenode.getId()+"",destnode.getId()+"","This is a dummy message!");
		send= send+1;
		System.out.println("<---------"+randommessage.toString()+"--------->");
		sourcenode.receive(randommessage);
	}
	/**
	 * Ενεργοποιεί όλα τα Nodes που υπάρχουν στο δίκτυο
	 */
	public void setAllNodesEnabled(boolean flag){
		for(Node n:nodes){
			n.setEnabled(flag);
		}
		
	}
	/**
	 * Καλεί την generate Routing Table σε κάθε κόμβο του δικτύου
	 */
	public void generateAllRoutingTables(){
		for(Node n:nodes){
			System.out.println("in node"+n.getId());
			n.generateRoutingTable(this);
		}
	}
	/**
	 * 
	 * Δημιουργεί μία τυχαία τοπολογία στο χώρο
	 */
	public Point[] randomGraph(Point center, int radi,int n){
		    double alpha = Math.PI * 2 / n;
		    Point[] points = new Point[n];
		    
		   	int i = -1;
		    while( ++i < n )
		    {
		        double theta = alpha * i;
		        double tempx = Math.cos(theta)* radi;
		        double tempy =  Math.sin(theta)*radi;
		        Point pointoncircle = new Point((int)tempx+center.x,(int) tempy+center.y);
		        points[ i ] = pointoncircle;
		    }
		    
		    return points; 
	}
	/**
	 * Δημιουργεί ένα τυχαίο Virtual Network βασισμένο στο ήδη υπάρχον δίκτυο
	 */
	public Network generateRandomVirtualNetwork(){
		return null;
	}
	/**
	 * Δίνει αναφορά για την χρησιμοποίηση των πόρων του δικτύου
	 */
	public void networkStatus(){
		int cpucount =0;
		float freqcount =0.0f;
		float frequsedcount =0.0f;
		float ramcount=0.0f;
		float ramusecount=0.0f;
	
		for(Node n:nodes){
			cpucount = cpucount+n.getCores();
			freqcount = freqcount+n.getCores()*n.getFrequency();
			frequsedcount = frequsedcount +n.getUsedCpu();
			ramcount = ramcount+n.getRam();
			ramusecount = ramusecount +n.getUsedRam();
		}
		int totalcapacity=0;
		ArrayList<Float> percentage4= new ArrayList<Float>(); //speed
		for(Link l:links){
			totalcapacity = totalcapacity+l.getSpeed();
			percentage4.add((float)l.getUsedSpeed()/l.getSpeed());
		}
		System.out.println("Total CPU's: "+cpucount);
		System.out.println("Total CPU frequency: "+freqcount+" / "+frequsedcount);
		System.out.println("Total Ram : "+ramcount+" / "+ramusecount);
		System.out.println("Total BandWidth: "+totalcapacity);
	}
	/**
	 * Επιστρέφει την ακολουθία των συνδέσμων για την επιλεγμένη σύνδεση
	 */
	public ArrayList<Link> getPath(int node1,int node2){
		System.out.println("getting path for "+(node1+1)+" to "+(node2+1));
		ArrayList<Link> results = new ArrayList<Link>();
		Node start = nodes.get(node1);
		boolean flag=true;
		while(flag){
			Link temp=start.getLinkFor(nodes.get(node2));
			//System.out.println("adding "+temp.getSpeed());
			results.add(temp);
			ArrayList<Node> f= temp.getNodes();
			Node temper=null;
			for(Node n:f){
				//System.out.println(n.getId()+" start is"+start.getId());
				if(!(n.getId()==start.getId())){
					//start=n;
					//System.out.println("in not equals");
					temper=n;
				}
				if(n.getId()==nodes.get(node2).getId()){
					flag=false;
					break;
				}
				
			}
			start=temper;
		}
		Link[] what=new Link[results.size()];
		int a=0;
		for(Link l:results){
			what[a]=l;
			a=a+1;
		}
		if(what.length==1){
			System.out.println(what[0].getNodes().get(0)+"<---->"+what[0].getNodes().get(1));
		}
		else{
			ArrayList<String> nodenames = new ArrayList<String>();
			ArrayList<Node> nodes = what[0].getNodes();
			Node starting =null;
			for (Node n:nodes){ //prwto
				if(n.getId()==node1){
					starting=n;
					nodenames.add(""+n.getId());
				}
			}
			
			for (Node n:nodes){//deytero
				if(n.getId()!=node1){
					nodenames.add(""+n.getId());
					starting=n;
				}
			}
			
		}
		return results;
	}
}
