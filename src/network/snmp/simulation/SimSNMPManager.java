package network.snmp.simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.*;

import javax.swing.JOptionPane;

class SimSNMPManager
{
	
   public static void main(String args[]) throws Exception
   {
	   String bot ="fake";
	   boolean functioncontrol=true;
	   boolean ack=true;
	   int themanagerport=0;
	   String managerport="";
	   int theserverport=0;
	   String thelog="";
	   int timeoutlocal=-1;
	   String testtimeoutlocal="";
	   String server="localhost";
	   do {
	    managerport = JOptionPane.showInputDialog("Select Manager Port");
	    themanagerport = Integer.parseInt(managerport);
	   }while(themanagerport<9000||themanagerport>10000);
	   do {
		   testtimeoutlocal =JOptionPane.showInputDialog("Enter Timeout Value");
		   timeoutlocal=Integer.parseInt(testtimeoutlocal);
	   }while(timeoutlocal<0||timeoutlocal>10);
	   
	   DatagramSocket clientSocket = new DatagramSocket(themanagerport);
	   clientSocket.setSoTimeout(timeoutlocal*1000);//setSoTimeout();
	   JOptionPane.showMessageDialog(null , "SNMP Manager created At:"+themanagerport);
	   String serverport="";
	   
	   do {
		   	  serverport = JOptionPane.showInputDialog("Select Agent Port");
		      theserverport=Integer.parseInt(serverport);
		      server=JOptionPane.showInputDialog("Enter Server Ip");
		      InetAddress IPAddress = InetAddress.getByName(server);
		      byte[] sendData = new byte[1024];
		      byte[] receiveData = new byte[1024];
		      String connect="connect";
		      thelog=thelog+"\n"+connect+" to: "+serverport;
		      sendData=connect.getBytes();
		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,theserverport );
		      clientSocket.send(sendPacket);
		      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		      try {
		    	  
		    	  clientSocket.receive(receivePacket) ;
		      String connectionsentence = new String(receivePacket.getData(),0,receivePacket.getLength());
		      	 //if (connectionsentence.equalsIgnoreCase("Agent:connected")) {
		    	  ack=false;
		         //}
		      }
		      catch (SocketTimeoutException iioe){
		    	  clientSocket.close();
		    	  JOptionPane.showMessageDialog(null , "Connection Timeout");
		    	  clientSocket =new DatagramSocket(themanagerport);
		    	  clientSocket.setSoTimeout(timeoutlocal*1000);
		      }
		      //if(receivePacket.getData()!=null) ack=false;		      		      
	   } while(ack);
	   		  
	   		  String sentence="";
	   		  thelog=thelog+"\nConnected Successfuly";
	   		  JOptionPane.showMessageDialog(null , "Connected At: "+serverport);
	   		  while(functioncontrol){
	   			  byte[] sendData = new byte[1024];
 				  byte[] receiveData = new byte[1024];
 				 InetAddress IPAddress = InetAddress.getByName(server);
 				  if(bot.equalsIgnoreCase("fake")){
	   				  
	   				   sendData = new byte[1024];
	   				   receiveData = new byte[1024];
	   				   sentence = JOptionPane.showInputDialog("Command?");
	   				  thelog=thelog+"\nManager: "+sentence;
	   				  sendData = sentence.getBytes();
	   			  }
	   			  else {
	   				   sendData = new byte[1024];
	   				   receiveData = new byte[1024];
	   				  sendData =bot.getBytes();
	   				JOptionPane.showMessageDialog(null , "Resent: "+bot);
	   			  }
	   				  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, theserverport);
	   				  clientSocket.send(sendPacket);
	   				  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	   			  try{
	   				  clientSocket.receive(receivePacket);
	   				  String modifiedSentence = new String(receivePacket.getData(),0,receivePacket.getLength());
		   			  thelog=thelog+"\n "+modifiedSentence;
		   			  JOptionPane.showMessageDialog(null,modifiedSentence);
		   			  String connue = new String(modifiedSentence);	
	   			  	    if (connue.equalsIgnoreCase("Agent:quit")){
	   			  	    	functioncontrol=false;
	   			  	    }
	   			  	    bot="fake";
	   			  } 
	   				catch(SocketTimeoutException iaa) {
	   					  bot=sentence;
	   					  thelog=thelog+"\n"+bot;
	   					JOptionPane.showMessageDialog(null,"Resending previous Statement"+bot);
	   				  }
	   			  //String modifiedSentence = new String(receivePacket.getData(),0,receivePacket.getLength());
	   			  //thelog=thelog+"\n "+modifiedSentence;
	   			  //JOptionPane.showMessageDialog(null,modifiedSentence);
	   			 // String connue = new String(modifiedSentence);	
	   			  	//if (connue.equalsIgnoreCase("Agent:quit")){
	   			  		//functioncontrol=false;
	   			  	//}
	   		  }
	  JOptionPane.showMessageDialog(null , "CLIENT EXITED");
	  JOptionPane.showMessageDialog(null , thelog,"History",JOptionPane.INFORMATION_MESSAGE);
	  File file = new File("write.txt");
	  BufferedWriter output = new BufferedWriter(new FileWriter(file));
	  output.write(thelog);
	  output.close();
	 
	  clientSocket.close();
	
   }
   
}
