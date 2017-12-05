package network.optimization.sdn.bean;

public class RouteEntry {
	public int id;
	public int link;
	public int hops;
	public RouteEntry(int destinationid,int thelink,int thehops){
		id=destinationid;
		link =thelink;
		hops=thehops;
	}
	public String toString(){
		return"|"+id+"|"+link+"|"+hops+"|";
	}
}
