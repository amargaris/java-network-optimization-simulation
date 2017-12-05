package network.optimization.sdn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import network.optimization.sdn.bean.Message;
import network.optimization.sdn.bean.VirtualLink;

public class Link {
	private ArrayList<Node> nodes;
	private ArrayList<VirtualLink> vlink =new ArrayList<VirtualLink>();
	private String descr;
	private int cap;//capacity in Mbps
	public Link(int thecap,String thedescr,Node... nod){
		nodes = new ArrayList<Node>(Arrays.asList(nod));
		cap=thecap;
		descr=thedescr;
		for (Node n:nodes){
			n.setLink(this);
		}
	}
	public Link(Node... nod){
		Random rand = new Random();
		int randcap = rand.nextInt(5)+1;
		cap = randcap*100;
		descr = "random generated link";
		nodes = new ArrayList<Node>(Arrays.asList(nod));
		for (Node n:nodes){
			n.setLink(this);
		}
	}
	public int getSpeed(){
		return cap;
	}
	public ArrayList<Node> getNodes(){
		return nodes;
	}
	public void pushVLink(VirtualLink input){
		vlink.add(input);
	}
	public String toString(){
		String s ="";//descr+" - speed: "+cap+" nodes:";
		for (Node b:nodes){
			s = s + b.getId();
		}
		return s;
	}
	public void forward(Node nn,Message m){
		for(Node n:nodes){
			if(n.getId()==nn.getId()){
				continue;
			}
			n.receive(m);
		}
	}
	public int getUsedSpeed(){
		int result=0;
		for(VirtualLink v:vlink){
			result = result+v.vspeed;
		}
		return result;
	}
}
