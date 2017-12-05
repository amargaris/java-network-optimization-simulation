package network.snmp.simulation;

import java.net.*;

import javax.swing.JOptionPane;

class SimSNMPD
{
	private int delay;
	private String ip="177.33.22.1";
	private String traffic="731273bytes";
	private boolean control = false;
	private int port;
	private String[] data={ip,traffic,Integer.toString(port)};
	
public SimSNMPD (int porttest,int delaytest){
	this.port = porttest;
	this.data[2]=Integer.toString(this.port);
	this.delay=delaytest;
}
   public void main() throws Exception
      {
         DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            while(true)
               {
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String sentence = new String( receivePacket.getData(),0,receivePacket.getLength());
                 
                  InetAddress IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
                  String capitalizedSentence = decide(sentence);
                  
                  sendData = capitalizedSentence.getBytes();
                  DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  double t0 = System.currentTimeMillis();
                  	double t1=0;
                  	do {
                  		 t1= System.currentTimeMillis();
                  	}while((t1-t0)<this.delay*1000);
                  serverSocket.send(sendPacket);
                  receiveData = new byte[1024];
                  sendData = new byte[1024];
                  if(control) break;
               }
            serverSocket.close();
            
           
      }
   public String decide( String sentence) {
		
		String decision = new String(sentence);
	    String[] stAr =  decision.split("-");
		String response = null;
	
		if (stAr.length==1)	{
			if (stAr[0].equals("list")) {
				response ="Agent:ip,traffic,port";
			} else if(stAr[0].equals("quit")){
				this.control = true; 
				response = "Agent:quit";
				
			} else if(stAr[0].equals("connect")){
				response="Agent:connected";
			}else {
				response = "Agent:invalid command :";
			}
			
		}else if (stAr.length==2){
			if (stAr[0].equals("get")){
				if (stAr[1].equals("ip")) {
						response="Agent:"+getParameter(stAr[1]);//this.ip;
					}else if ( stAr[1].equals("traffic")){
						response="Agent:"+getParameter(stAr[1]);//this.traffic;
					}else if ( stAr[1].equals("port")){
						response="Agent:"+getParameter(stAr[1]);//Integer.toString(this.port);
					}else {
						response="Agent:invalid attribute ";
					}
			    }else {
				 response = "Agent:invalid get statement";
			 }
			 
		} else if (stAr.length==3){
			 if (stAr[0].equals("set")){
				if (stAr[1].equals("ip")) {
					setParameter(0,stAr[2]);//this.ip = stAr[2];
					response="Agent: Ip changed to: "+getParameter(stAr[1]);
				}else if ( stAr[1].equals("traffic")){
					setParameter(1,stAr[2]);//this.traffic = stAr[2];
					response="Agent: traffic changed to: "+getParameter(stAr[1]);
				}else {
					response="Agent:invalid set attribute";
				}
			 }else {
				 response ="Agent:invalid set statement";
			 }
					
		
		
		}else {
			response ="Agent invalid command syntaxis";
		}
   
			
			return response;
	}
   public String getParameter(String key) {
	   int num=0;
	   if (key.equalsIgnoreCase("ip")){
		   num=0;
	   } else if(key.equalsIgnoreCase("traffic")){
		   num=1;
	   } else if(key.equalsIgnoreCase("port")) {
		   num=2;
	   }
		return this.data[num];
   }
   public void setParameter(int key,String value){
	   this.data[key]=value;
   }
   public static void main(String[] args) throws Exception {
		JOptionPane.showMessageDialog(null , "This is Aristotelis's SNMP Agent");
		String theport = JOptionPane.showInputDialog("ENTER UDP PORT");
		int port = Integer.parseInt(theport);
		String thedelay = JOptionPane.showInputDialog("ENTER THE SERVER DELAY:(s)");
		int delay = Integer.parseInt(thedelay);
		SimSNMPD abc = new SimSNMPD(port,delay);
		JOptionPane.showMessageDialog(null , "SNMP AGENT CREATED AT:"+theport+" port");
		abc.main();
		
	}
}