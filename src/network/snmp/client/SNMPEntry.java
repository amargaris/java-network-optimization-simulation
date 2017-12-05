package network.snmp.client;
import java.io.Serializable;
import java.util.ArrayList;


public class SNMPEntry implements Serializable{
	

	private static final long serialVersionUID = -6177031967009995351L;
	
	public String name;
	public String hostname;
	public String port;
	public String community;
	public int snmptype;
	
	public transient int ifcount;
	public transient ArrayList<Integer> ifindexes;
	public transient ArrayList<Integer> ipindexes1,ipindexes2,ipindexes3;
	public transient int iptab1count;
	public transient int iptab2count;
	public transient int iptab3count;
	public transient int tcptabcount;
	
	public static final int SNMP_VER1 = 0;
	public static final int SNMP_VER2C = 1;
	public static final int SNMP_VER3 = 2;
	
	public SNMPEntry(String theentryname,String thehostname,String theport,int thesnmptype,String thecommunity){
		//ifindexes = new ArrayList<Integer>();
		init();
		name=theentryname;
		hostname = thehostname;
		port = theport;
		snmptype=thesnmptype;
		community=thecommunity;
	}
	public void init(){
		ifindexes = new ArrayList<Integer>();
		ipindexes1=new ArrayList<Integer>();
		ipindexes2=new ArrayList<Integer>();
		ipindexes3=new ArrayList<Integer>();
	}
	public String toString(){
		return ""+name +" - "+hostname+" - "+port+" - "+SNMPEntry.typeToString(snmptype)+" - "+community;
	}
	public static String typeToString(int a){
		if(a==0){
			return "Version 1";
		}
		else if(a==1){
			return "Version 2c";
		}
		else{
			return "Version 3";
		}
	}
}
