package network.optimization.sdn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import network.optimization.sdn.bean.Message;
import network.optimization.sdn.bean.RouteEntry;

public class Node {
	private String name; //node name
	private int id; //obsoletE?
	private int cores; //count
	private float frequency; //Ghz
	private int ram;//MB
	private ArrayList<Link> links ;
	private ArrayList<RouteEntry> routetable= new ArrayList<RouteEntry>();
	private ArrayList<VirtualNode> vnodes = new ArrayList<VirtualNode>();
	public static final int RAMSIZE=512;
	public static final float CPUFREQ=2.2f;
	public static final int CORE =1;

	private ArrayList<Message> received = new ArrayList<Message>();
	private ArrayList<Message> process = new ArrayList<Message>();
	private ActionListener act;
	private int delay=25;//gia to timer (1 praksi / 0.1 s)
	private Timer time;
	public Node(String thename,int theid,int thecores,float thefrequency,int theram){
		links = new ArrayList<Link>();
		name=thename;
		id=theid;
		cores=thecores;
		frequency=thefrequency;
		ram=theram;
	}
	public Node(){
		links = new ArrayList<Link>();
		Random rand = new Random();
		id = rand.nextInt();
		cores = (rand.nextInt(5)+1)*2*CORE;
		frequency = rand.nextFloat()*CPUFREQ;
		ram = (rand.nextInt(5)+1)*RAMSIZE;
		name = "Node"+id;
	}
	public Node(int theid){
		links = new ArrayList<Link>();
		Random rand = new Random();

		id=theid;
		cores = (rand.nextInt(5)+1)*2;
		frequency = CPUFREQ+rand.nextFloat()*CPUFREQ;
		ram = (rand.nextInt(12)+4)*RAMSIZE;
		name = "Node"+id;
	}
	public int getId(){
		return id;
	}
	public int getCores(){
		return cores;
	}
	public ArrayList<RouteEntry> getRouteTable(){
		return routetable;
	}
	public float getFrequency(){
		float temp = frequency*100.0f;
		int a = (int) temp;
		int b = a/100;
		return (float) b;
	}
	public float getRam(){
		return (float)ram/1024; 
	}
	public void setEnabled(boolean input){
		if(input){
			act= new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					function();
				}
				
			};
			time = new Timer(delay,act);
			time.setRepeats(true);
			time.setCoalesce(true);
			time.start();
		}
		else{
			time.stop();
		}
	}
	public boolean isConnectedTo(Node to){
		ArrayList<Node> check = getConnectedLinks();
		for(Node n:check){
			if(n.id==to.id){
				return true;
			}
		}
		return false;
	}
	public void generateRoutingTable(Network n){ 
		int destinations = n.getNodes().size();
		//routetable = new ArrayList<RouteEntry>();
		for(int i=1;i<=destinations;i++){
			if(i==id){
				continue;
			}
			boolean flag=false;
			for(RouteEntry r:routetable){
				if(r.id==i){
					//System.out.println(""+r.id+" // "+i);
					//continue;
					flag=true;
				}
			}
			if(flag){
				continue;
			}
			System.out.println("Update Route for: "+i);
			Message temp = new Message(""+id,""+i,id+"");
			temp.setType(Message.MSG_ROUTE_1);
			System.out.println(temp.toString());
			ArrayList<Node> links = getConnectedLinks();
			for(Node f:links){
				Message tempor = (Message)deepClone(temp);
				f.receive(tempor);
			}
		}
	}
	public void getConnectedLinksReport(){
		for(Link l:links){
			ArrayList<Node> nod=l.getNodes();
			for(Node n:nod){
				if(!n.name.equalsIgnoreCase(name)){
					System.out.println("Connected to :"+n.name);
					System.out.println(n.toString());
				}
			}
		}
	}
	public ArrayList<Node> getConnectedLinks(){
		ArrayList<Node> returnval= new ArrayList<Node>();
		for(Link l:links){
			ArrayList<Node> nod=l.getNodes();
			for(Node n:nod){
				if(!n.name.equalsIgnoreCase(name)){
					boolean flag=true;
					for(Node f:returnval){
						if(f.name.equalsIgnoreCase(n.name)){
							flag=false;
						}
					}
					if(flag){
						returnval.add(n);
					}
				}
			}
		}
		return returnval;
	}
	public String getName(){
		return name;
	}
	public void setLink(Link l){
		links.add(l);
		ArrayList<Node> from = l.getNodes();
		Node s=new Node();
		for(Node n:from){
			if(n.id!=id){
				s=n;
			}
		}
		RouteEntry ent = new RouteEntry(s.id,links.indexOf(l),1);
		routetable.add(ent);
	}
	public String toString(){
		String s = "Name: "+name+" Id: "+id+" Cores: "+cores+" frequency "+frequency+" Ram: "+ram+" Links: "+links.size()+" {";
		for(Link l:links){
			ArrayList<Node> d = l.getNodes();
			String f = d.get(0).name+"<-->"+d.get(1).name+"@"+l.getSpeed()+",";
			s = s+f;
		}
		s= s+"}";
		return s;
	}
	public void function(){
		if(process.size()!=0){
			Message m=process.get(0);
			process(m);
			process.remove(0);
		}
		
	}
	public void receive(Message m){
		process.add(m);
	}
	public boolean hasMessage(){
		if(process.size()!=0){
			return true;
		}
		return false;
	}
	public void process(Message m){
		//basikos elegxos gia duplicate
		for(Message mm:received){
			if(mm.serial==m.serial){
				System.out.println("Duplicate: rejected");
				return;
			}
		}
		//perase ton elegxo
		received.add(m);
		if(m.type==Message.MSG_ROUTE_1){
			int tempfrom =Integer.parseInt(m.tempfrom);
			int tempdest = Integer.parseInt(m.destination);
			if(id==tempdest){
				Network.received=Network.received+1;
				System.out.println("Messages:"+Network.received+"/"+Network.send);
				System.out.println("reached destination");
				System.out.println("--------->"+m.toString()+"<---------");
				generateRouteResponse(m);
			}
			else{
				Node n = this.getRouteDecision(m.destination);
				if(n==null){
					System.out.println("null");
					for(Node nnn:this.getConnectedLinks()){
						if(nnn.id==id){
							continue;
						}
						if(nnn.id==tempfrom){
							continue;
						}
						System.out.println(m.toString()+" forwarded to "+nnn.id);
						Message temp = (Message)deepClone(m);
						temp.tempfrom=id+"";
						temp.message=temp.message+"@"+id;
						nnn.receive(temp);
					}
				}
				else{
					System.out.println(m.toString()+" forwarded to "+n.id);
					m.tempfrom=id+"";
					m.message=m.message+"@"+id;
					Message temp = (Message)deepClone(m);
					n.receive(temp);
				}
			}
		}
		else if(m.type==Message.MSG_ROUTE_2){
			System.out.println("Received Trace Route Message!");
			int dest=Integer.parseInt(m.destination);
			if(dest==id){
				updateRouteTable(m);
			}
			else{ //anoigei to path to opoio einai 1@3@2@4 kai briskei to epomeno hop
				System.out.println(m.toString());
				String s = m.message;
				String[] a = s.split("@");
				int i;
				for(i=a.length-1;i>-1;i--){
					int temper = Integer.parseInt(a[i]);
					if(temper==id){
						break;
					}
				}
				int to = Integer.parseInt(a[i-1]);
				ArrayList<Node> target = getConnectedLinks();
				for(Node n:target){
					if(n.id==to){
						m.tempfrom=id+"";
						Message temp = (Message)deepClone(m);
						n.receive(temp);
					}
				}
			}
			
		}
		else if(m.type==Message.MSG_PLAIN){ //aplo mhnyma thewrei oti yparxei routetable kai pairnei mia apokrisi apo ti methodo getRoute...
			int tempdest=Integer.parseInt(m.destination);
			if(tempdest==id){
				Network.received=Network.received+1;
				System.out.println("Messages:"+Network.received+"/"+Network.send);
				System.out.println("reached destination");
				System.out.println("--------->"+m.toString()+"<---------");
			}
			else{
				Node n = getRouteDecision(m.destination);
				Message temp = (Message)deepClone(m);
				temp.tempfrom=""+id;
				if(n==null){
					System.out.println("null");
					return;
				}
				n.receive(temp);
			}
			
		}
	}
	public void updateRouteTable(Message m){
		System.out.println("inside update Route table");
		System.out.println(m.toString());
		String s = m.message;
		String[] a=s.split("@");
		String from = m.source;
		int ak=Integer.parseInt(from);
		int tempfrom = Integer.parseInt(m.tempfrom);
		int interfaceint=0;
		for(int i=0;i<links.size();i++){
			ArrayList<Node> nodes=links.get(i).getNodes();
			for(Node n:nodes){
				if(n.id==id){
					continue;
				}
				if(n.id==tempfrom){
					interfaceint=i;
				}
			}
		}
		for(RouteEntry r:routetable){
			if(r.id==ak){
				if(r.hops>a.length){
					routetable.remove(r);
					RouteEntry ne = new RouteEntry(ak,interfaceint,a.length);
					routetable.add(ne);
					return;
				}
			}
		}
		RouteEntry ne = new RouteEntry(ak,interfaceint,a.length);
		routetable.add(ne);
	}
	public void generateRouteResponse(Message m){ 
		Message response = new Message(id+"",m.source,m.message);
		response.setType(Message.MSG_ROUTE_2);
		System.out.println("Generation response: "+response.toString() );
		ArrayList<Node> nodecom = this.getConnectedLinks();
		int targid=Integer.parseInt(m.tempfrom);
		for(Node n:nodecom){
			if(n.id==targid){
				n.receive(response);
			}
		}
				
	}
	public Node getRouteDecision(String dest){
		if(routetable.size()==0){
			return null;
		}
		else if(routetable.size()==1){
			ArrayList<Node> results=links.get(routetable.get(0).link).getNodes();
			for(Node n:results){
				if(n.id!=id){
					return n;
				}
			}
			return null;
		}
		else{
			for(RouteEntry r:routetable){
				if(r.id==Integer.parseInt(dest)){
					//ArrayList<Node> to = getConnectedLinks();
					//for(Node n:to){
						//if(n.id==r.id){
						//	return n;
						//}
					//}
					ArrayList<Node> results=links.get(r.link).getNodes();
					for(Node n:results){
						if(n.id!=id){
							return n;
						}
					}
				}
				
			}
			return null;
		}
		
	}
	public static Object deepClone(Object object) {
	    try {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream(baos);
	      oos.writeObject(object);
	      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	      ObjectInputStream ois = new ObjectInputStream(bais);
	      return ois.readObject();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	public float getUsedCpu(){
		float total = 0.0f;
		for(VirtualNode n:vnodes){
			total = total + (n.cpucount*n.cpufreq);
		}
		return total;
	}
	public float getMaxCpu(){
		return cores*frequency;
	}
	public int getUsedRam(){
		int total =0;
		for(VirtualNode n:vnodes){
			total = total+n.ram;
		}
		return total;
	}
	public Link getLinkFor(Node n){
		for(RouteEntry r:routetable){
			if(r.id==n.id){
				return links.get(r.link);
			}
		}
		return null;
	}
}
