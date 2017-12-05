package network.snmp.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
//import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;


public class GUISNMP {
	
	//Swing Components ~ Graphics
	private JFrame fram;
	private JFrame settingsframe;	
	private JButton butt,tablebutt,checkbutt,debugbutt,returnbutt,getthis,addnetbutt,monitorbutt;
	private JButton applybutt,defaultbutt,tabbutt;
	private JPanel mainpan;
	private JTextArea lab;
	private JButton start;
	private JTextArea results;
	private JScrollPane scroll;
	private JComboBox box,box2;
	private JMenuBar menu;
	private JPanel localpan;
	private JLabel lab1,lab0,lab2 ,lab3,lab4 ;
	private JTextArea text1,text0,text2,text3;
	private JTable tab;	
	private JComboBox selectversion;
	private JComboBox type,type2,type3;
	private JPopupMenu pop;
	private JComboBox specialcombo;
	private JComboBox sampleratecombo;
	private JProgressBar progress;
	private JTable tabbb;
	private JComboBox tabbox;
	
	//PLOT
	private JFrame panfram;
	private GraphicalPanel pan;
	private JButton zoomin,zoomout,moveright,moveleft;
	private ImageIcon zoominic,zoomoutic,right,left;
	
	//HardCoded
	private static final String[] tables ={
		"ifTable",
		"ipAddrTable",
		"ipRouteTable",
		"ipNetToMediaTable",
		"tcpConnTable",
		"udpTable"
	};
	private static final String[] typelist = {
		"System",
		"Interface Scalar",
		"Interface Table",
		"Ip Scalar",
		"Ip Adrr Table",
		"Ip Route Table",
		"Ip Media Table",
		"Icmp Scalar"
		};
	private static final String[] sysscalar = {
		"sysDescr",
		"sysObjectID",
		"sysUpTime",
		"sysContact",
		"sysName",
		"sysLocation",
		"sysServices"
		};
	private static final String[] ifscalar = {"ifNumber"};
	private static final String[] ifnames = {
		"ifIndex",
		"ifDescr",
		"ifType",
		"ifMtu",
		"ifSpeed",
		"ifPhysAddress",
		"ifAdminStatus",
		"ifOperStatus",
		"ifLastChange",
		"ifInOctets",
		"ifInUcastPkts",
		"ifInNUcastPkts",
		"ifInDiscards",
		"ifInErrors",
		"ifInUnknownProtos",
		"ifOutOctets",
		"ifOutUcastPkts",
		"ifOutNUcastPkts",
		"ifOutDiscards",
		"ifOutErrors",
		"ifOutQLen",
		"ifSpecific"
		};
	private static final String[] ipscalar = {
		"ipForwarding",
		"ipDefaultTTL",
		"ipInReceives",
		"ipInHdrErrors",
		"ipInAddrErrors",
		"ipForwDatagrams",
		"ipInUnknownProtos",
		"ipInDiscards",
		"ipInDelivers", 
		"ipOutRequests",
		"ipOutDiscards", 
		"ipOutNoRoutes", 
		"ipReasmTimeout", 
		"ipReasmReqds", 
		"ipReasmOKs",
		"ipReasmFails", 
		"ipFragOKs", 
		"ipFragFails", 
		"ipFragCreates",
		"ipRoutingDiscards"
		};
	private static final String[] ipaddrtable = {
		"ipAdEntAddr",
		"ipAdEntIfIndex", 
		"ipAdEntNetMask", 
		"ipAdEntBcastAddr",
		"ipAdEntReasmMaxSize" 
		};
	private static final String[] iproutetable = {
		"ipRouteDest", 
		"ipRouteIfIndex", 
		"ipRouteMetric1", 
		"ipRouteMetric2", 
		"ipRouteMetric3", 
		"ipRouteMetric4", 
		"ipRouteNextHop", 
		"ipRouteType", 
		"ipRouteProto", 
		"ipRouteAge", 
		"ipRouteMask", 
		"ipRouteMetric5", 
		"ipRouteInfo", 
		};
	private static final String[] ipnettomediatable ={
		"ipNetToMediaIfIndex", 
		"ipNetToMediaPhysAddress", 
		"ipNetToMediaNetAddress", 
		"ipNetToMediaType" 
		};
	private static final String[] icmpscalar ={
		"icmpInMsgs", 
		"icmpInErrors",
		"icmpInDestUnreachs", 
		"icmpInTimeExcds", 
		"icmpInParmProbs", 
		"icmpInSrcQuenchs", 
		"icmpInRedirects", 
		"icmpInEchos", 
		"icmpInEchoReps", 
		"icmpInTimestamps", 
		"icmpInTimestampReps", 
		"icmpInAddrMasks", 
		"icmpInAddrMaskReps", 
		"icmpOutMsgs", 
		"icmpOutErrors", 
		"icmpOutDestUnreachs", 
		"icmpOutTimeExcds", 
		"icmpOutParmProbs", 
		"icmpOutSrcQuenchs", 
		"icmpOutRedirects", 
		"icmpOutEchos", 
		"icmpOutEchoReps", 
		"icmpOutTimestamps", 
		"icmpOutTimestampReps", 
		"icmpOutAddrMasks", 
		"icmpOutAddrMaskReps" 
		};
	//Eikones
	private ImageIcon tablebackic,getit,startic,stopic,resetic,offline,yes,no,checkic,debug,back,get,logo,tableic,returnic,addnet,monicon,seticon,graphic;  

	//System Variables
	private static final int TA_ROWS = 1;
	private static final int TA_COLS = 15;
	private ArrayList<SNMPEntry> list = new ArrayList<SNMPEntry>();
	private String defaultport = "161";
	private String defaultaddress = "127.0.0.1";
	private String defaultcommunity = "public";
	private int selectedindex=0;
	private int selectedindexifcount=0;
	private int selectedtablemode=0;
	//private Timer timed;
	private SNMPDataset dataset;
	private int a1;
	private Timer timer;
	private ActionListener taskPerformer;
	private boolean iftablechecked =false;
	
	//shmeiwseis snmpget -v2c -c public #ip{"83.212.238.231",".129"} #name1 #name2 #name3.0
	
	public GUISNMP(){ //constructor
		try{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		}catch(Exception e){
			
		}
		loadImages();
		displayMainWindow();
		//unloadImages();
	}
	public void loadImages(){ //fortwnei tis fwtografies
		offline = new ImageIcon(getClass().getResource("images/network_offline.png"));
		yes = new ImageIcon(getClass().getResource("images/y.png"));
		no = new ImageIcon(getClass().getResource("images/n.png"));
		checkic = new ImageIcon(getClass().getResource("images/check.png"));
		debug = new ImageIcon(getClass().getResource("images/debug.png"));
		back = new ImageIcon(getClass().getResource("images/back.jpg"));
		get = new ImageIcon(getClass().getResource("images/get.png"));
		logo = new ImageIcon(getClass().getResource("images/logo.png"));
		tableic = new ImageIcon(getClass().getResource("images/table.png"));
		returnic = new ImageIcon(getClass().getResource("images/return.png"));
		addnet = new ImageIcon(getClass().getResource("images/add.png"));
		monicon = new ImageIcon(getClass().getResource("images/monitor.png"));
		seticon = new ImageIcon(getClass().getResource("images/set.png"));
		graphic = new ImageIcon(getClass().getResource("images/graph.jpg"));
		startic = new ImageIcon(getClass().getResource("images/start.png"));
		stopic = new ImageIcon(getClass().getResource("images/stop.png"));
		resetic = new ImageIcon(getClass().getResource("images/reset.png"));
		getit = new ImageIcon(getClass().getResource("images/getit.png"));
		tablebackic = new ImageIcon(getClass().getResource("images/tableback.png"));
	}
	public void displayMainWindow(){ //Kyriws parathyro toy programmatos
	
		fram = new JFrame("Aristotelis Margaris's SNMP Program");
		fram.setSize(520,500);
		fram.setIconImage(logo.getImage());
		fram.setLocationRelativeTo(null);
		fram.setResizable(false);
		fram.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "Thank you for using our Software.See you later!");
            }
		});
		Container cont = fram.getContentPane();

		mainpan = new JPanel(new FlowLayout(FlowLayout.CENTER)){
			
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(back.getImage(),0,0,null);
				
			}
		};
		cont.add(mainpan);
		
		

		lab = new JTextArea("Here we print results");
		lab.setLineWrap(true);
		lab.setWrapStyleWord(true);
		lab.setFont(new Font("Serif", Font.PLAIN, 20));
		lab.setPreferredSize(new Dimension(250,100));
		lab.setOpaque(true);
		lab.setBackground(Color.white);
		//lab.setPreferredSize(new Dimension(530,20));
		//lab.setHorizontalAlignment( SwingConstants.CENTER );

		results = new JTextArea();
		results.append("Log:");
		results.setEditable(false);
		
		scroll = new JScrollPane(results);
		scroll.setPreferredSize(new Dimension(470,240));
		
		menu = new JMenuBar();
		JMenu themenu = new JMenu("Options");
		
		JMenuItem optionitem = new JMenuItem("Network");
		optionitem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openSettings();
			}
			
		});
		themenu.add(optionitem);
		JMenuItem importitem = new JMenuItem("Import..");
		importitem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				readListFromFile();
			}
			
		});
		themenu.add(importitem);
		JMenuItem exportitem = new JMenuItem("Export..");
		exportitem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				writeListToFile();
			}
			
		});
		themenu.add(exportitem);
		JMenuItem exititem = new JMenuItem("Exit");
		exititem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Thank you for using our Software.See you later!");
				System.exit(0);
			}
			
		});
		themenu.add(exititem);
		JMenu themenu2 = new JMenu("Help");
		JMenuItem aboutitem = new JMenuItem("About");
		aboutitem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Αριστοτέλης Μάργαρης - ME12067\n  Εργασία για Διαχείριση Δικτύων\nΥλοποίηση με Java και SNMP4J API");
			}
			
		});
		themenu2.add(aboutitem);
		menu.add(themenu);
		menu.add(themenu2);
		
		addnetbutt = new JButton("Add +",addnet);
		addnetbutt.setFocusPainted(false);
		addnetbutt.setPreferredSize(new Dimension(147,70));
		addnetbutt.setToolTipText("Press this button to open network settings and add a new machine");
		addnetbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				openSettings();
			}
			
		});
		checkbutt = new JButton("Debug",debug);
		checkbutt.setFocusPainted(false);
		checkbutt.setPreferredSize(new Dimension(147,70));
		checkbutt.setToolTipText("Press this button to open the debug mode of the Program.");
		checkbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(list.size()>0){
					displayDebugMode();
				}
				else{
					JOptionPane.showMessageDialog(null, "No Machines Have been Added!");
				}
			}
			
		});
		debugbutt = new JButton("Check",checkic);
		debugbutt.setFocusPainted(false);
		debugbutt.setPreferredSize(new Dimension(147,70));
		debugbutt.setToolTipText("Press this button to check if all registered snmp demons are active.");
		debugbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showConnectivityCheck();
			}
			
		});
		returnbutt = new JButton("Return",returnic);
		returnbutt.setFocusPainted(false);
		returnbutt.setToolTipText("Press this button to return to main menu");
		returnbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				fram.dispose();
				displayMainWindow();
			}
			
		});
		monitorbutt = new JButton("Monitor",monicon);
		monitorbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				displayMonitorMode();
			}
			
		});
		monitorbutt.setFocusPainted(false);
		monitorbutt.setPreferredSize(new Dimension(147,70));
		monitorbutt.setToolTipText("Press this button to begin monitoring a curtain parameter of the network");
		butt = new JButton("Get",get);
		butt.setFocusPainted(false);
		butt.setPreferredSize(new Dimension(147,70));
		butt.setToolTipText("Press this button to query scalars from MIB databases of registered Agents");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(list.size()>0){
					displayGetMode();
				}
				else{
					JOptionPane.showMessageDialog(null, "No Machines Have been Added!");
				}
			}
			
		});
		tablebutt = new JButton("Table",tableic);
		tablebutt.setFocusPainted(false);
		tablebutt.setPreferredSize(new Dimension(147,70));
		tablebutt.setToolTipText("Press this to Get a specific SNMP table from any Registered Agent");
		tablebutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(list.size()>0){
					displayTableMode();
					
				}
				else{
					JOptionPane.showMessageDialog(null, "No Machines Have been Added!");
				}
			}
			
		});
		fram.setJMenuBar(menu);
		JPanel padd = new JPanel();
		padd.setPreferredSize(new Dimension(500,145));
		padd.setLayout(new FlowLayout());
		progress = new JProgressBar();
		progress.setPreferredSize(new Dimension(100,40));
		progress.setVisible(false);
		padd.add(progress);
		padd.setOpaque(false);
		mainpan.add(padd);
		mainpan.add(addnetbutt);
		mainpan.add(butt);
		mainpan.add(tablebutt);
		mainpan.add(checkbutt);
		mainpan.add(debugbutt);
		mainpan.add(monitorbutt);

		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fram.setVisible(true);
	}
	public void displayTableMode(){ //Allazei to display mode se get Table mode
		
		mainpan.invalidate();
		mainpan.removeAll();
		selectedindex=0;
		//int selectedtablemode=0;
		ArrayList<String> templist = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			templist.add(list.get(i).name);
		}
		box2 = new JComboBox(templist.toArray());//Onomata twn eggrafwn
		box2.setSelectedIndex(0);
		tabbox = new JComboBox(tables);
		tabbox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedtablemode =tabbox.getSelectedIndex();
			}
			
		});
		tabbox.setSelectedIndex(0);
		tabbutt = new JButton("Get Table");
		tabbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				List lit=getSNMPTable(list.get(box2.getSelectedIndex()),(String)tabbox.getSelectedItem());//TODO debug
				createIfTableWindow(lit);
			}
			
		});
		JPanel padding = new JPanel();
		JPanel buttpantab = new JPanel(new GridLayout(3,2));
		buttpantab.setPreferredSize(new Dimension(400,100));
		JLabel logolab = new JLabel(tablebackic);
		logolab.setPreferredSize(new Dimension(400,100));
		JTextArea text1=new JTextArea();
		text1.setText("Entry");
		text1.setEditable(false);
		JTextArea text2 = new JTextArea();
		text2.setText("Table:");
		text2.setEditable(false);
		JTextArea text3 = new JTextArea();
		text3.setEditable(false);
		text3.setText("Press");
		buttpantab.add(text1);
		buttpantab.add(box2);
		buttpantab.add(text2);
		buttpantab.add(tabbox);
		buttpantab.add(text3);
		buttpantab.add(tabbutt);
		padding.setPreferredSize(new Dimension(460,100));
		padding.setOpaque(false);
		mainpan.add(padding);
		//mainpan.add(box2);
		//mainpan.add(tabbox);
		//mainpan.add(tabbutt);
		mainpan.add(logolab);
		mainpan.add(buttpantab);
		mainpan.add(returnbutt);
		mainpan.validate();
		mainpan.repaint();
		
	}
	public void displayMonitorMode(){ //Allazei to display mode se Monitor mode
	
		updateInterfaceCount();
		mainpan.invalidate();
		mainpan.removeAll();
		selectedindex=0;
		
		ArrayList<String> templist = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			templist.add(list.get(i).name);
		}
		box2 = new JComboBox(templist.toArray());//Onomata twn eggrafwn
		box2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				selectedindex=box2.getSelectedIndex();
				//if(type.getSelectedIndex()==0||type.getSelectedIndex()==-1){
					//selectedindexifcount=0;
				//}
				String[] interfacestring = new String[list.get(selectedindex).ifindexes.size()];//[selectedindexifcount];
				for(int i=0;i<interfacestring.length;i++){
					interfacestring[i]=""+list.get(selectedindex).ifindexes.get(i);
				}
				//type3 = new JComboBox(interfacestring);
				type3.invalidate();
				type3.removeAllItems();
				for(int i=0;i<interfacestring.length;i++){
					type3.addItem(interfacestring[i]);
				}
				type3.validate();
				type3.repaint();
				type3.setSelectedIndex(0);
			}
			
		});
		
		String[] interfacestring = new String[list.get(selectedindex).ifindexes.size()];//[selectedindexifcount];
		for(int i=0;i<interfacestring.length;i++){
			interfacestring[i]=Integer.toString(list.get(selectedindex).ifindexes.get(i));
			//System.out.println(interfacestring[i]);
		}
		type3 = new JComboBox(interfacestring);
		type3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(type3.getSelectedItem()!=null)
				selectedindexifcount = Integer.parseInt(type3.getSelectedItem().toString());//type3.getSelectedIndex()+1;
			}
			
		});
		
		type3.setSelectedIndex(0);
		//box2.setSelectedIndex(0);
		//type3.setSelectedIndex(0);
		String[] specialstring = {"Bytes","Throughtput","Utilization","Packets","Packet Rate","Packet Error Rate"};
		String[] sampleratestring = {"1","5","10","20","60"};
		specialcombo = new JComboBox(specialstring);
		specialcombo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(specialcombo.getSelectedIndex()==0){
					taskPerformer = new ActionListener() {
			            public void actionPerformed(ActionEvent evt) {
			                //...Perform a task...
			            	ArrayList<String> input = new ArrayList<String>();
			            	input.add("ifInOctets");
			            	input.add("ifOutOctets");
			            	ArrayList<String> output = getSNMP(list.get(selectedindex),input,"."+selectedindexifcount);
			            	
			                if(output!=null){
				            	System.out.println(output.get(0));
				            	int a2 = Integer.parseInt(output.get(0))+Integer.parseInt(output.get(1));
				            	dataset.addRow(a2);
				                resolvePlotButtons();
				                panfram.repaint();
			                }
			                else{
			                	System.out.println("null");
				                dataset.addRow(0);
				                resolvePlotButtons();
				                panfram.repaint();
			                }
			            }
			        };
				}
				else if(specialcombo.getSelectedIndex()==1){
					taskPerformer = new ActionListener() {
			            int counter =0;
						public void actionPerformed(ActionEvent evt) {
			                //...Perform a task...
							if(counter==0){
								ArrayList<String> input = new ArrayList<String>();
						    	input.add("ifInOctets");
						    	input.add("ifOutOctets");
						    	ArrayList<String> output = getSNMP(list.get(selectedindex),input,"."+selectedindexifcount);
						    	a1 = Integer.parseInt(output.get(0))+Integer.parseInt(output.get(1));
						    	counter =1;
							}
							else{
								ArrayList<String> input = new ArrayList<String>();
				            	input.add("ifInOctets");
				            	input.add("ifOutOctets");
				            	ArrayList<String> output = getSNMP(list.get(selectedindex),input,"."+selectedindexifcount);
				                if(output!=null){
					            	//System.out.println(output.get(0));
					            	int a2 = Integer.parseInt(output.get(0))+Integer.parseInt(output.get(1));
					            	int through = (a2-a1)*8/dataset.stepperset;
					            	System.out.println(through);
					            	a1=a2;
					            	dataset.addRow(through);
					                resolvePlotButtons();
					                panfram.repaint();
				                }
				                else{
				                	System.out.println("null");
					                dataset.addRow(Integer.parseInt("0"));
					                resolvePlotButtons();
					                panfram.repaint();
				                }
							}
			            	
			            }
			        };
				}
				else if(specialcombo.getSelectedIndex()==2){
					taskPerformer = new ActionListener() {
			            int counter=0;
						public void actionPerformed(ActionEvent evt) {
			                //...Perform a task...
			            	if(counter==0){
								ArrayList<String> input = new ArrayList<String>();
						    	input.add("ifInOctets");
						    	input.add("ifOutOctets");
						    	ArrayList<String> output = getSNMP(list.get(selectedindex),input,"."+selectedindexifcount);
						    	a1 = Integer.parseInt(output.get(0))+Integer.parseInt(output.get(1));
						    	counter =1;
							}
			            	else{
			            		ArrayList<String> input = new ArrayList<String>();
				            	input.add("ifInOctets");
				            	input.add("ifOutOctets");
				            	input.add("ifSpeed");
				            	ArrayList<String> output = getSNMP(list.get(selectedindex),input,"."+selectedindexifcount);
				                if(output!=null){
					            	int a2 = Integer.parseInt(output.get(0))+Integer.parseInt(output.get(1));
					            	int through = (a2-a1)*8;
					            	int cap = Integer.parseInt(output.get(2));
					            	if(cap==0){
					            		System.out.println("Capacity 0");
					            		dataset.addRow(0);
					            	}
					            	else{
					            		double res =((double)through / (double) cap)*100;
						            	int finalres = (int) res;
						            	a1=a2;
						            	System.out.println(finalres);
						            	dataset.addRow(finalres);
					            	}
					            	
					                resolvePlotButtons();
					                panfram.repaint();
				                }
				                else{
				                	System.out.println("null");
					                dataset.addRow(Integer.parseInt("0"));
					                resolvePlotButtons();
					                panfram.repaint();
				                }
			            	}
			            	
			            }
			        };
				}
				else if(specialcombo.getSelectedIndex()==3){
					
				}
				else if(specialcombo.getSelectedIndex()==4){
					
				}
				else if(specialcombo.getSelectedIndex()==5){
					//TODO
				}
			}
			
		});
		specialcombo.setSelectedIndex(0);
		sampleratecombo = new JComboBox(sampleratestring);
		sampleratecombo.setSelectedIndex(0);
		sampleratecombo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer!=null)
				timer.setDelay(Integer.parseInt((String)sampleratecombo.getSelectedItem())*1000);
			}
			
		});
		start = new JButton("Start");
		start.setIcon(startic);
		start.addActionListener(new ActionListener(){
			int status=0;
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(status==0){
					sampleratecombo.setEnabled(false);
					box2.setEnabled(false);
					type3.setEnabled(false);
					specialcombo.setEnabled(false);
					dataset = new SNMPDataset(Integer.parseInt((String)sampleratecombo.getSelectedItem()));
					dataset.addRow(0);
					if(specialcombo.getSelectedIndex()==0){
						createPlotFrame("Bits","Time (s)",5);
					}else if(specialcombo.getSelectedIndex()==1){
						createPlotFrame("Bit per Second","Time (s)",5);
					}else if(specialcombo.getSelectedIndex()==2){
						createPlotFrame("Usage ( x% )","Time (s)",5);
					}else if(specialcombo.getSelectedIndex()==3){
						createPlotFrame("Packets","Time (s)",5);
					}else if(specialcombo.getSelectedIndex()==4){
						createPlotFrame("Packets per Sec","Time (s)",5);
					}
					timer = new Timer(Integer.parseInt((String)sampleratecombo.getSelectedItem())*1000,taskPerformer);
					timer.setCoalesce(true);
					timer.setRepeats(true);
					status=1;
					timer.start();
					start.setText("Stop!");
					start.setIcon(stopic);
				}
				else if(status==1){
					status=2;
					timer.stop();
					start.setText("Reset");
					start.setIcon(resetic);
				}
				else{
					panfram.dispose();
					sampleratecombo.setEnabled(true);
					box2.setEnabled(true);
					type3.setEnabled(true);
					specialcombo.setEnabled(true);
					status=0;
					timer=null;//.restart();
					System.gc();
					start.setText("Start");
					start.setIcon(startic);
					box2.setSelectedIndex(0);
					type3.setSelectedIndex(0);
					specialcombo.setSelectedIndex(0);
					sampleratecombo.setSelectedIndex(0);
				}
			}
			
		});
		JPanel monitorpan = new JPanel(new GridLayout(4,2));
		monitorpan.setPreferredSize(new Dimension(200,100));
		monitorpan.setOpaque(false);
		JTextArea entrylab = new JTextArea("Target:");
		entrylab.setEditable(false);
		entrylab.setOpaque(true);
		JTextArea typelab = new JTextArea("Measure:");
		typelab.setEditable(false);
		JTextArea interfacelab = new JTextArea("Interface:");
		interfacelab.setEditable(false);
		JTextArea samplelab = new JTextArea("Sample Rate:");
		samplelab.setEditable(false);
		monitorpan.add(entrylab);
		monitorpan.add(box2);
		monitorpan.add(typelab);
		monitorpan.add(specialcombo);
		monitorpan.add(interfacelab);
		monitorpan.add(type3);
		monitorpan.add(samplelab);
		monitorpan.add(sampleratecombo);
		JLabel iconlab = new JLabel(graphic);
		iconlab.setPreferredSize(new Dimension(300,140));
		JPanel paddpanmon = new JPanel(new FlowLayout());
		paddpanmon.setPreferredSize(new Dimension(400,340));
		paddpanmon.setOpaque(false);
		paddpanmon.add(iconlab);
		paddpanmon.add(monitorpan);
		paddpanmon.add(start);
		mainpan.add(paddpanmon);
		mainpan.add(returnbutt);
		mainpan.revalidate();
		mainpan.repaint();
	}
	public void displayGetMode(){ //Allazei to display mode se general get mode
		mainpan.invalidate();
		mainpan.removeAll();
		JLabel logolab = new JLabel(getit);
		logolab.setPreferredSize(new Dimension(300,100));
		mainpan.add(logolab);
		mainpan.add(lab);
		localpan = new JPanel(new FlowLayout());
		localpan.setPreferredSize(new Dimension(450,150));
		localpan.setOpaque(false);
		updateInterfaceCount();
		String[] interfacestring = new String[list.get(selectedindex).ifcount];//[selectedindexifcount];
		for(int i=0;i<interfacestring.length;i++){
			interfacestring[i]=""+(i+1);
		}
		type3 = new JComboBox(interfacestring);
		type3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(type3.getSelectedItem()!=null)
				selectedindexifcount = Integer.parseInt((String)type3.getSelectedItem());//type3.getSelectedIndex()+1;
				else
					selectedindexifcount=0;
			}
			
		});
		selectedindexifcount=0;
		
		type = new JComboBox(typelist); //Onomata typoy metablitwn
		type.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int a = type.getSelectedIndex();
				if(a==0){ //system
					localpan.invalidate();
					localpan.remove(type3);
					selectedindexifcount=0;
					localpan.validate();
					type2.invalidate();
					type2.removeAllItems();
					for(int i=0;i<sysscalar.length;i++){
						type2.addItem(sysscalar[i]);
					}
					type2.validate();
					type2.repaint();
				}
				else if(a==1){ //ifscalar
					selectedindexifcount=0;
					localpan.invalidate();
					type2.invalidate();
					type2.removeAllItems();
					for(int i=0;i<ifscalar.length;i++){
						type2.addItem(ifscalar[i]);
					}
					type2.validate();
					type2.repaint();
					localpan.remove(type3);
					localpan.validate();
				}
				else if(a==2){ //iftable
					selectedindexifcount=1;
					type2.invalidate();
					type2.removeAllItems();
					for(int i=0;i<ifnames.length;i++){
						type2.addItem(ifnames[i]);
					}
					type2.validate();
					type2.repaint();
					localpan.remove(getthis);
					type3.invalidate();
					type3.removeAllItems();
					String[] interfacestring = new String[list.get(selectedindex).ifindexes.size()];
					for(int i=0;i<interfacestring.length;i++){
						interfacestring[i]=""+list.get(selectedindex).ifindexes.get(i);
					}
					for(int i=0;i<interfacestring.length;i++){
						type3.addItem(interfacestring[i]);
					}
					type3.validate();
					type3.repaint();
					localpan.add(type3);
					localpan.add(getthis);
				}
				else if(a==3){ //ip scalar
					selectedindexifcount=0;
					type2.invalidate();
					type2.removeAllItems();
					for(int i=0;i<ipscalar.length;i++){
						type2.addItem(ipscalar[i]);
					}
					type2.validate();
					type2.repaint();
					localpan.remove(type3);
					localpan.remove(getthis);
					localpan.add(getthis);
				}
				else if(a==4){
					selectedindexifcount=1;
				}
				else if(a==7){ //icmp scalar (only)
					selectedindexifcount=0;
					type2.invalidate();
					type2.removeAllItems();
					for(int i=0;i<icmpscalar.length;i++){
						type2.addItem(icmpscalar[i]);
					}
					type2.validate();
					type2.repaint();
					localpan.remove(getthis);
					localpan.remove(type3);
					localpan.add(getthis);
				}
			}
			
		});
		type2 = new JComboBox(sysscalar); //Onomata metablitwn
		
		ArrayList<String> templist = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			templist.add(list.get(i).name);
		}
		box2 = new JComboBox(templist.toArray());//Onomata twn eggrafwn
		box2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				selectedindex=box2.getSelectedIndex();
				//if(type.getSelectedIndex()==0||type.getSelectedIndex()==-1){
					selectedindexifcount=0;
				//}
				//updateInterfaceCount(selectedindex+1);
				String[] interfacestring = new String[list.get(selectedindex).ifindexes.size()];//[selectedindexifcount];
				for(int i=0;i<interfacestring.length;i++){
					interfacestring[i]=""+list.get(selectedindex).ifindexes.get(i);
				}
				//type3 = new JComboBox(interfacestring);
				type3.invalidate();
				type3.removeAllItems();
				for(int i=0;i<interfacestring.length;i++){
					type3.addItem(interfacestring[i]);
				}
				type3.validate();
				type3.repaint();
				type.setSelectedIndex(0);
				type2.setSelectedIndex(0);
				type3.setSelectedIndex(0);
				selectedindexifcount=0;
			}
			
		});
		getthis = new JButton ("GET");
		getthis.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> variables = new ArrayList<String>();
				variables.add((String)type2.getSelectedItem());
				getSNMP(list.get(selectedindex),variables,"."+selectedindexifcount);
				
			}
			
		});
		lab.setText("(Results)");
		localpan.add(box2);
		localpan.add(type);
		localpan.add(type2);
		localpan.add(getthis);
		mainpan.add(localpan);
		mainpan.add(returnbutt);
		mainpan.validate();
		mainpan.repaint();
	}
	public void displayDebugMode(){ //Allazei to display mode se debug get mode
		mainpan.invalidate();
		mainpan.removeAll();
		box = new JComboBox(sysscalar);
		ArrayList<String> templist = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			templist.add(list.get(i).name);
		}
		box2 = new JComboBox(templist.toArray());
		mainpan.add(lab);
		mainpan.add(box2);
		mainpan.add(box);
		mainpan.add(butt);
		mainpan.add(debugbutt);
		mainpan.add(scroll);
		mainpan.add(returnbutt);
		mainpan.validate();
		mainpan.repaint();
	}
	public void openSettings(){ //Anoigei to frame me ta network settings
		settingsframe = new JFrame("Network Settings");
		settingsframe.setIconImage(seticon.getImage());
		lab0 = new JLabel("Entry Name:");
		text0 = new JTextArea(TA_ROWS,TA_COLS);
		text0.setWrapStyleWord(true);
		text0.setLineWrap(true);
		lab1 = new JLabel("Target IP:");

		if(list.size()>0){
			String[] header = {"Name","Hostname","Port","Community","Version"};
			Object[][] data = new Object[list.size()][5];
			for(int i=0;i<list.size();i++){
				data[i][0]=list.get(i).name;
				data[i][1]=list.get(i).hostname;
				data[i][2]=list.get(i).port;
				data[i][4]=SNMPEntry.typeToString(list.get(i).snmptype);
				data[i][3]=list.get(i).community;
			}
			tab = new JTable(data,header);
			pop = new JPopupMenu();
			JMenuItem remove = new JMenuItem("remove");
			remove.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(tab.getSelectedRow()>-1){
						list.remove(tab.getSelectedRow());
						settingsframe.dispose();
						openSettings();
					}
				}
				
			});
			pop.add(remove);
			tab.setComponentPopupMenu(pop);
	
			JScrollPane scroll = new JScrollPane(tab);
			scroll.setPreferredSize(new Dimension(330,160));
			settingsframe.add(scroll);
		}
		else{
			JLabel offlinelab = new JLabel(offline);
			settingsframe.add(offlinelab);
		}
		String[] verlist = {"Version 1","Version 2c","Version 3"};
		selectversion = new JComboBox(verlist);
		lab3 = new JLabel("Version:");
		text1 = new JTextArea(TA_ROWS,TA_COLS);
		text1.setWrapStyleWord(true);
		text1.setLineWrap(true);
		lab2 = new JLabel("Target Port:");
		text2 = new JTextArea(TA_ROWS,5);
		text2.setWrapStyleWord(true);
		text2.setLineWrap(true);
		lab4 = new JLabel("Community");
		text3 = new JTextArea(TA_ROWS,TA_COLS);
		text3.setWrapStyleWord(true);
		text3.setLineWrap(true);
		applybutt = new JButton("Apply");
		applybutt.setPreferredSize(new Dimension(100,20));
		applybutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				if(text1.getText().length()>1&&text2.getText().length()>1&&text0.getText().length()>1&&text3.getText().length()>1){
					list.add(new SNMPEntry(text0.getText(),text1.getText(),text2.getText(),selectversion.getSelectedIndex(),text3.getText()));
					iftablechecked =false;
					settingsframe.dispose();
					openSettings();
				}
				else{
					JOptionPane.showMessageDialog(null, "Please enter all Fields");
				}
			}
			
		});
		defaultbutt = new JButton("Defaults");
		defaultbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				text2.setText(defaultport);
				text1.setText(defaultaddress);
				text0.setText("entry"+list.size());
				text3.setText(defaultcommunity);
				selectversion.setSelectedIndex(0);
			}
			
		});
		defaultbutt.setPreferredSize(new Dimension(70,20));
		settingsframe.setLayout(new FlowLayout());//new GridLayout(3,2));
		GridLayout greedy =new GridLayout(6,2);
		greedy.setHgap(5);
		JPanel pan = new JPanel(greedy);
		settingsframe.setSize(400, 400);
		settingsframe.setLocationRelativeTo(null);
		pan.add(lab0);
		pan.add(text0);
		pan.add(lab1);
		pan.add(text1);
		pan.add(lab2);
		pan.add(text2);
		pan.add(lab4);
		pan.add(text3);
		pan.add(lab3);
		pan.add(selectversion);
		pan.add(applybutt);
		pan.add(defaultbutt);
		settingsframe.add(pan);
		settingsframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsframe.setVisible(true);
	}
	public void showConnectivityCheck(){ //Anoigei to frame me ton elegxo energwn nodes
		JFrame fram = new JFrame("Connection Status");
		fram.setIconImage(seticon.getImage());
		if(list.size()<6){
			fram.setSize(200,100+50*list.size());
		}
		else{
			fram.setSize(200,100+50*6);
		}
		fram.setLocationRelativeTo(null);
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fram.setResizable(false);
		boolean[] temp = checkConnectivity();
		if(temp==null){
			return;
		}
		JLabel name = new JLabel("Name:");
		JLabel status = new JLabel("Status:");
		JPanel statpan = new JPanel(new GridLayout(list.size()+1,2));
		statpan.setBackground(Color.white);
		JScrollPane pan = new JScrollPane(statpan);
		pan.setPreferredSize(new Dimension(195,90));
		fram.add(pan);

		statpan.add(name);
		statpan.add(status);
		for(int i=0;i<list.size();i++){
			statpan.add(new JLabel(list.get(i).name));
			statpan.add(temp[i]?new JLabel(yes):new JLabel(no));
		}
		Component[] tempcomp = statpan.getComponents();
		for(int i=0;i<tempcomp.length;i++){
			((JLabel)tempcomp[i]).setBorder(BorderFactory.createLineBorder(Color.black));
			((JLabel)tempcomp[i]).setHorizontalAlignment( SwingConstants.CENTER );
		}
		fram.setVisible(true);
	}
	public void createPlotFrame(String yaxis,String xaxis,int samples){
		//Gia metafora
		zoominic = new ImageIcon(getClass().getResource("images/zoomin.png"));
		zoomoutic = new ImageIcon(getClass().getResource("images/zoomout.png"));
		left = new ImageIcon(getClass().getResource("images/left.png"));
		right = new ImageIcon(getClass().getResource("images/right.png"));
		//~
		pan = new GraphicalPanel(dataset,yaxis,xaxis,10);
		pan.setPreferredSize(new Dimension(400,400));
		panfram = new JFrame("Ploting");
		Container con = panfram.getContentPane();
		con.setLayout(new FlowLayout());
		con.add(pan);
		panfram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panfram.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0){
				pan=null;
			}
		});
		JPanel control = new JPanel();
		control.setOpaque(false);
		control.setPreferredSize(new Dimension(350,60));
		zoomin = new JButton(zoominic);
		zoomin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int temphowmany = pan.getCount();
				int tempstart = pan.getOffset();
				pan.setPanelOffsetAndLimit(tempstart, temphowmany-1);
				resolvePlotButtons();
			}
			
		});
		zoomout = new JButton(zoomoutic);
		zoomout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int tempmaxcount=pan.getMaxCount();
				int temphowmany = pan.getCount();
				int tempstart = pan.getOffset();
				if(tempstart+temphowmany<tempmaxcount){
					pan.setPanelOffsetAndLimit(tempstart, temphowmany+1);
				}
				else{
					pan.setPanelOffsetAndLimit(tempstart-1, temphowmany+1);
				}
				resolvePlotButtons();
			}
			
		});
		moveright = new JButton(right);
		moveright.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int tempoffset = pan.getOffset();
				int temphowmany = pan.getCount();
				pan.setPanelOffsetAndLimit(tempoffset+1, temphowmany);
				resolvePlotButtons();
			}
			
		});
		moveleft = new JButton(left);
		moveleft.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int tempoffset = pan.getOffset();
				int temphowmany = pan.getCount();
				pan.setPanelOffsetAndLimit(tempoffset-1, temphowmany);
				resolvePlotButtons();
			}
		});
		control.add(zoomin);
		control.add(zoomout);
		control.add(moveleft);
		control.add(moveright);
		con.add(control);
		panfram.setSize(410,500);
		panfram.setLocationRelativeTo(fram);
		panfram.setVisible(true);
		resolvePlotButtons();
	}
	public void resolvePlotButtons(){
		int offset = pan.getOffset();
		int count = pan.getCount();

		if(offset==0){
			moveleft.setEnabled(false);
		}
		else {
			moveleft.setEnabled(true);
		}
		if(offset+count==pan.getMaxCount()){
			moveright.setEnabled(false);
		}
		else{
			moveright.setEnabled(true);
		}
		if(count==1){
			zoomin.setEnabled(false);
		}
		else{
			zoomin.setEnabled(true);
		}
		if(count==pan.getMaxCount()){
			zoomout.setEnabled(false);
		}
		else{
			zoomout.setEnabled(true);
		}
	}
	@SuppressWarnings("unchecked")
	public void readListFromFile(){ //Fortwnei lista me eggrafes typoy SNMPEntry apo arxeio
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {
	        @Override
	        public boolean accept(File f) {
	            if (f.isDirectory()) {
	                return true;
	            }
	            final String name = f.getName();
	            return name.endsWith(".ndf");
	        }

	        @Override
	        public String getDescription() {
	            return "*.ndf";
	        }
	    });
		chooser.setCurrentDirectory(new File("."));
		File f;

		int returnVal = chooser.showOpenDialog(fram);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			f=chooser.getSelectedFile();
			try{
			FileInputStream in = new FileInputStream(f);
			ObjectInputStream ob = new ObjectInputStream(in);
			Object s =ob.readObject();
			list =(ArrayList<SNMPEntry>)s;
			iftablechecked =false;
			ob.close();
			in.close();
			JOptionPane.showMessageDialog(null, "Importing Ended! "+list.size()+" entries");
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Importing Failed!");
			}
		}
	}
	public void writeListToFile(){ //Grafei ti lista me tis eggrafes typoy SNMPEntry se arxeio
		String s = JOptionPane.showInputDialog("Enter file name:");
		if(s==null){
			return;
		}
		else{
			try{
			FileOutputStream out = new FileOutputStream(s+".ndf");
			ObjectOutputStream obout = new ObjectOutputStream(out);
			obout.writeObject(list);
			obout.close();
			out.close();
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "failed!");
			}
		}
	}
	public String getOIDfromName(String name){ //metafrazei to String se akoloythia integer TODO ta ypoloipa apo mibs
		if(name.indexOf("Table")!=-1){
			if(name.indexOf("ifTable")!=-1){
				return ".1.3.6.1.2.1.2.2";
			}
			else if(name.indexOf("ipAddrTable")!=-1){
				return ".1.3.6.1.2.1.4.20";
			}
			else if(name.indexOf("ipRouteTable")!=-1){
				return ".1.3.6.1.2.1.4.21.";
			}
			else if(name.indexOf("ipNetToMediaTable")!=-1){
				return ".1.3.6.1.2.1.4.22.";
			}
			else{
				return null;
			}
		}
		else{
			//System
			if(name.equalsIgnoreCase("sysDescr")){
				return ".1.3.6.1.2.1.1.1";
			}
			else if(name.equalsIgnoreCase("sysObjectID")){
				return ".1.3.6.1.2.1.1.2";
			}
			else if(name.equalsIgnoreCase("sysUpTime")){
				return ".1.3.6.1.2.1.1.3";
			}
			else if(name.equalsIgnoreCase("sysContact")){
				return ".1.3.6.1.2.1.1.4";
			}
			else if(name.equalsIgnoreCase("sysName")){
				return ".1.3.6.1.2.1.1.5";
			}
			else if(name.equalsIgnoreCase("sysLocation")){
				return ".1.3.6.1.2.1.1.6";
			}
			else if(name.equalsIgnoreCase("sysServices")){
				return ".1.3.6.1.2.1.1.7";
			}
			//Interfaces
			else if(name.equalsIgnoreCase("ifNumber")){
				return ".1.3.6.1.2.1.2.1.";
			}
			else if(name.equalsIgnoreCase("ifIndex")){
				return "1.3.6.1.2.1.2.2.1.1.";
			}
			else if(name.equalsIgnoreCase("ifDescr")){
				return "1.3.6.1.2.1.2.2.1.2.";
			}
			else if(name.equalsIgnoreCase("ifType")){
				return "1.3.6.1.2.1.2.2.1.3.";
			}
			else if(name.equalsIgnoreCase("ifMtu")){
				return "1.3.6.1.2.1.2.2.1.4.";
			}
			else if(name.equalsIgnoreCase("ifSpeed")){
				return "1.3.6.1.2.1.2.2.1.5.";
			}
			else if(name.equalsIgnoreCase("ifPhysAddress")){
				return "1.3.6.1.2.1.2.2.1.6.";
			}
			else if(name.equalsIgnoreCase("ifAdminStatus")){
				return "1.3.6.1.2.1.2.2.1.7.";
			}
			else if(name.equalsIgnoreCase("ifOperStatus")){
				return "1.3.6.1.2.1.2.2.1.8.";
			}
			else if(name.equalsIgnoreCase("ifLastChange")){
				return "1.3.6.1.2.1.2.2.1.9.";
			}
			else if(name.equalsIgnoreCase("ifInOctets")){
				return "1.3.6.1.2.1.2.2.1.10.";
			}
			else if(name.equalsIgnoreCase("ifInUcastPkts")){
				return "1.3.6.1.2.1.2.2.1.11.";
			}
			else if(name.equalsIgnoreCase("ifInNUcastPkts")){
				return "1.3.6.1.2.1.2.2.1.12.";
			}
			else if(name.equalsIgnoreCase("ifInDiscards")){
				return "1.3.6.1.2.1.2.2.1.13.";
			}
			else if(name.equalsIgnoreCase("ifInErrors")){
				return "1.3.6.1.2.1.2.2.1.14.";
			}
			else if(name.equalsIgnoreCase("ifInUnknownProtos")){
				return "1.3.6.1.2.1.2.2.1.15.";
			}
			else if(name.equalsIgnoreCase("ifOutOctets")){
				return "1.3.6.1.2.1.2.2.1.16.";
			}
			else if(name.equalsIgnoreCase("ifOutUcastPkts")){
				return "1.3.6.1.2.1.2.2.1.17.";
			}
			else if(name.equalsIgnoreCase("ifOutNUcastPkts")){
				return "1.3.6.1.2.1.2.2.1.18.";
			}
			else if(name.equalsIgnoreCase("ifOutDiscards")){
				return "1.3.6.1.2.1.2.2.1.19.";
			}
			else if(name.equalsIgnoreCase("ifOutErrors")){
				return "1.3.6.1.2.1.2.2.1.20.";
			}
			else if(name.equalsIgnoreCase("ifOutQLen")){
				return "1.3.6.1.2.1.2.2.1.21.";
			}
			else if(name.equalsIgnoreCase("ifSpecific")){
				return "1.3.6.1.2.1.2.2.1.22.";
			}
			else if(name.indexOf("ip")!=-1){
				if(name.indexOf("ipAd")==-1){
					if(name.indexOf("ipRoute")==-1){
						if(name.indexOf("NetToMedia")==-1){
							int count = -1;
							for(int i=0;i<ipscalar.length-1;i++){
								if(ipscalar[i].equalsIgnoreCase(name)){
									count = i+1;
									break;
								}
							}
							if(count==-1){
								return "1.3.6.1.2.1.4.23.";
							}
							else{
								return "1.3.6.1.2.1.4."+count+".";
							}
						}
						else{
							int count = -1;
							for(int i=0;i<ipnettomediatable.length;i++){
								if(ipnettomediatable[i].equalsIgnoreCase(name)){
									count = i+1;
									break;
								}
							}
							return "1.3.6.1.2.1.4.22.1."+count+".";
						}	
					}
					else{
						int count = -1;
						for(int i=0;i<iproutetable.length;i++){
							if(iproutetable[i].equalsIgnoreCase(name)){
								count = i+1;
								break;
							}
						}
						return "1.3.6.1.2.1.4.21.1."+count+".";
					}
				}
				else{
					int count = -1;
					for(int i=0;i<ipaddrtable.length;i++){
						if(ipaddrtable[i].equalsIgnoreCase(name)){
							count = i+1;
							break;
						}
					}
					return "1.3.6.1.2.1.4.20.1."+count+".";
				}
				
			}
			else if(name.indexOf("icmp")!=-1){
				int counter=-1;
				for(int i=0;i<icmpscalar.length;i++){
					if(icmpscalar[i].equalsIgnoreCase(name)){
						counter = i+1;
						break;
					}
				}
				return"1.3.6.1.2.1.5."+counter+".";
			}
			
			else{
				return null;
			}
		}
	}
	public void updateInterfaceCount(){ //enhmerwnei to plithos twn interfaces gia to a TODO OBSOLETE lysh mesw table
		if(!iftablechecked ){
			for(int i=0;i<list.size();i++){
				List temp = getSNMPTable(list.get(i),"ifTable");
				ArrayList<Integer> templist = new ArrayList<Integer>();
				int counter2= 0;
		        for ( int j = 0; j < temp.size();j++){
		        	TableEvent templ =(TableEvent)temp.get(j);
		        	VariableBinding[] temp2 =templ.getColumns();
		        	if(temp2[0].toString().indexOf("1.3.6.1.2.1.2.2.1.1")!=-1){
		        		templist.add(Integer.parseInt(temp2[0].toValueString()));
		        		counter2=counter2+1;
		        	}
		        	else{
		        		break;
		        	}
		        }
		        list.get(i).ifcount=counter2;
		        list.get(i).ifindexes=templist;
		        System.out.println(counter2+" rows");
			}
			/*
			//progress.setPreferredSize(new Dimension(150,30));
		//progress.setValue(0);
		//progress.setStringPainted(true);
		//progress.setString("0%");
		//int globalcount = list.size()*100;
		//progress.setVisible(true);

		for(int i=0;i<list.size();i++){
			//Iftable length
			list.get(i).init();
			
			try{
				ArrayList<String> variables = new ArrayList<String>();
				variables.add("ifNumber");
				ArrayList<String> result = getSNMP(list.get(i),variables,".0");
				//selectedindexifcount = Integer.parseInt(result.get(0));
				list.get(i).ifcount=Integer.parseInt(result.get(0));
			}catch(Exception e){
				System.out.println(""+e.toString());
				list.get(i).ifcount=0;
			}
			int counter = list.get(i).ifcount;
			int index =1;
			while(counter>0&&index<=100){
				//double dob1 = i*100 +index;
				//double dob2 = (dob1 /(double)globalcount)*100;
				//int a = (int) dob2;
				//progress.setValue(a);
				//progress.setString(a+"%");
				
				ArrayList<String> variables = new ArrayList<String>();
				variables.add("ifIndex");
				ArrayList<String> result;
				result = getSNMP(list.get(i),variables,"."+index);
				
				if(result==null){
					index = index+1;
					
				}
				else{
					System.out.println("IF Entry :"+" added: "+index);
					list.get(i).ifindexes.add(index);
					counter = counter-1;
					index = index+1;
				}
			}
			index =1;
			while(index<=100){
				ArrayList<String> variables = new ArrayList<String>();
				variables.add("ipAdEntAddr");
				ArrayList<String> result;
				result = getSNMP(list.get(i),variables,"."+index);
				
				if(result==null){
					index = index+1;
					
				}
				else{
					System.out.println("IP Entry :"+" added: "+index);
					list.get(i).ipindexes1.add(index);
					index = index+1;
				}
			}
			/*
			try{TODO problima me tin aneyresi table
				int counter =0;
				ArrayList<String> result;
				do{
					ArrayList<String> variables = new ArrayList<String>();
					variables.add("ifLastChange");
					result = getSNMP(list.get(i),variables,"."+(counter+1));
					counter = counter +1;
				}while(result!=null);
				list.get(i).ifcount=counter -1;
			}catch(Exception e){
				System.out.println(""+e.toString());
				list.get(i).ifcount=0;
			}
			//Iptable1 length
			try{
				int counter =0;
				ArrayList<String> result;
				do{
					ArrayList<String> variables = new ArrayList<String>();
					variables.add("ipAdEntNetMask");
					result = getSNMP(list.get(i),variables,"."+(counter+1));
					counter = counter +1;
				}while(result!=null);
				list.get(i).iptab1count=counter -1;
			}catch(Exception e){
				System.out.println(""+e.toString());
				list.get(i).iptab1count=0;
			}
			//Iptable2 length
			try{
				int counter =0;
				ArrayList<String> result;
				do{
					ArrayList<String> variables = new ArrayList<String>();
					variables.add("ipRouteNextHop");
					result = getSNMP(list.get(i),variables,"."+(counter+1));
					counter = counter +1;
				}while(result!=null);
				list.get(i).iptab2count=counter -1;
			}catch(Exception e){
				System.out.println(""+e.toString());
				list.get(i).iptab2count=0;
			}
			//Iptable3 length
			try{
				int counter =0;
				ArrayList<String> result;
				do{
					ArrayList<String> variables = new ArrayList<String>();
					variables.add("ipNetToMediaType");
					result = getSNMP(list.get(i),variables,"."+(counter+1));
					counter = counter +1;
				}while(result!=null);
				list.get(i).iptab3count=counter -1;
			}catch(Exception e){
				System.out.println(""+e.toString());
				list.get(i).iptab3count=0;
			}
			*/
			//System.out.println(i+"st entry: IF: "+list.get(i).ifindexes.size()+" - IP: "+list.get(i).ipindexes1.size());
		}
		iftablechecked=true;
		
	}
	public boolean[] checkConnectivity(){ //elegxei an oi eggrafes mas einai energes kai epistrefei to vector
		if(list.size()==0){
			JOptionPane.showMessageDialog(null, "No Machines Have been Added!");
			return null;
		}
		else{
			boolean[] temp = new boolean[list.size()];
			for(int i=0;i<list.size();i++){
				ArrayList<String> oidlist = new ArrayList<String>();
				oidlist.add("sysDescr");
				ArrayList<String> results =getSNMP(list.get(i),oidlist,".0");
				if(results==null){
					temp[i]=false;
					continue;
				}
				temp[i]=true;
			}
			return temp;
		}
	}
	public int intToSNMPVersion(int entryint){ //Metafrazei ena int se ena SnmpConstant
		if(entryint ==0){
			return SnmpConstants.version1;
		}
		else if(entryint==1){
			return SnmpConstants.version2c;
		}
		else {
			return SnmpConstants.version3;
		}
	}
	public ArrayList<String> getSNMP(SNMPEntry ent,ArrayList<String> variables,String end){ //Aitima SNMP get 
		    // Create TransportMapping and Listen
			System.out.println(ent.toString()+" - "+variables.toString()+" - "+end);
			TransportMapping transport;    
			try{
				transport = new DefaultUdpTransportMapping();
				transport.listen();
		    }catch(Exception e){
		    	return null;
		    }

		    // Create Target Address object
		    CommunityTarget comtarget = new CommunityTarget();
		    comtarget.setCommunity(new OctetString(ent.community));
		    comtarget.setVersion(intToSNMPVersion(ent.snmptype));
		    comtarget.setAddress(new UdpAddress(ent.hostname + "/" + ent.port));
		    comtarget.setRetries(2);
		    comtarget.setTimeout(100);

		    // Create the PDU object
		    PDU pdu = new PDU();
		    for(int i=0;i<variables.size();i++){
		    	String var2 = getOIDfromName(variables.get(i));
		    	String temp = var2+end;
		    	pdu.add(new VariableBinding(new OID(temp)));
		    }
		    pdu.setType(PDU.GET);
		    pdu.setRequestID(new Integer32(1));

		    // Create Snmp object for sending data to Agent
		    Snmp snmp = new Snmp(transport);

		    results.append("\nSending Request to Agent...");//System.out.println("Sending Request to Agent...");
		    ResponseEvent response;
		    try{
		    	response = snmp.get(pdu, comtarget);
		    }catch(Exception e){
		    	return null;
		    }

		    // Process Agent Response
		    if (response != null)
		    {
		      results.append("\nGot Response from Agent");//System.out.println("Got Response from Agent");
		      PDU responsePDU = response.getResponse();

		      if (responsePDU != null)
		      {
		        int errorStatus = responsePDU.getErrorStatus();
		        int errorIndex = responsePDU.getErrorIndex();
		        String errorStatusText = responsePDU.getErrorStatusText();

		        if (errorStatus == PDU.noError)
		        {

		          @SuppressWarnings("unchecked")
				  Vector<VariableBinding> v =responsePDU.getVariableBindings();
		          lab.setText(v.get(0).toValueString()+"");
		          results.append("\nRequest OK");
		          ArrayList<String> returnval = new ArrayList<String>();
		          for(int i=0;i<v.size();i++){
		        	  returnval.add(v.get(i).toValueString());
		          }
		          results.append(returnval.toString());
		          return returnval;
		          
		        }
		        else{
		          lab.setText("Error with get. Check Log");
		          results.append("\nError: Request Failed"+"\nError Status = " + errorStatus+"\nError Index = "+errorIndex+"\nError Status Text = "+errorStatusText);
		        }
		      }
		      else
		      {
		    	  lab.setText("Error with get. Check Log");
		    	  results.append("\nError: Response PDU is null");
		      }
		    }
		    else
		    {
		      results.append("Error: Agent Timeout... ");
		    }
		    try{
		    	snmp.close();
		    }catch(Exception e){
		    	return null;
		    }
		    return null;
	}
	@SuppressWarnings("rawtypes")
	public List getSNMPTable(SNMPEntry ent,String tabname){ 
		Snmp snmp  = null;
		int maxRepetitions=300;
		TransportMapping transport;    
		try{
			transport = new DefaultUdpTransportMapping();
			transport.listen();
	    }catch(Exception e){
	    	return null;
	    }

	    // Create Target Address object
	    CommunityTarget comtarget = new CommunityTarget();
	    comtarget.setCommunity(new OctetString(ent.community));
	    comtarget.setVersion(intToSNMPVersion(ent.snmptype));
	    comtarget.setAddress(new UdpAddress(ent.hostname + "/" + ent.port));
	    comtarget.setRetries(10);
	    comtarget.setTimeout(1000);
	    snmp = new Snmp(transport);
        PDUFactory pF = new DefaultPDUFactory (PDU.GETNEXT);
        TableUtils tableUtils = new TableUtils(snmp, pF);
        tableUtils.setMaxNumRowsPerPDU(maxRepetitions);
        OID[] columns = new OID[1];
        columns[0] = new VariableBinding (new OID(getOIDfromName(tabname))).getOid();
        //OID lowerBoundIndex =  new OID("1.3.6.1.2.1.2.1" ) ;
        //OID upperBoundIndex =  new OID( "1.3.6.1.2.1.2.10") ;
        List snmpList =  tableUtils.getTable(comtarget, columns,null,null);//lowerBoundIndex, upperBoundIndex);
        System.out.println("snmpList size : " + snmpList.size());
    	
        try{
            snmp.close();
            return snmpList;
        } catch ( IOException ioe ) {
            System.out.println( "Can not close the snmp connection  " + ioe );
            return null;
        }
	}
	public void createIfTableWindow(@SuppressWarnings("rawtypes") List snmpList){
		int counter2= 0;
        for ( int j = 0; j < snmpList.size();j++){
        	TableEvent temp =(TableEvent)snmpList.get(j);
        	VariableBinding[] temp2 =temp.getColumns();
        	if(temp2[0].toString().indexOf("1.3.6.1.2.1.2.2.1.1")!=-1){
        		counter2=counter2+1;
        	}
        	else{
        		break;
        	}
        }
        System.out.println(counter2+" rows");
        Object[][] finalob = new Object[counter2][ifnames.length];
        for ( int j = 0; j < snmpList.size();j++){
        	TableEvent temp =(TableEvent)snmpList.get(j);
        	VariableBinding[] temp2 =temp.getColumns();
        	finalob[j%counter2][j/counter2] = temp2[0].toValueString();
        }
        
        tabbb = new JTable(finalob,ifnames);
        tabbb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i=0;i<ifnames.length;i++){
        	String name = tabbb.getColumnName(i);
        	Font f = tabbb.getFont();
        	FontMetrics fm = tabbb.getFontMetrics(f);
        	int width = fm.stringWidth(name)+10;
        	tabbb.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
        JScrollPane scrollpan = new JScrollPane(tabbb);
        JFrame framed = new JFrame();
        
        scrollpan.setPreferredSize(new Dimension(750,400));
        framed.setLayout(new FlowLayout());
        framed.add(scrollpan);
        JButton exporttoxl = new JButton ("Export");
        exporttoxl.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = JOptionPane.showInputDialog("Enter file name :");
				
				if(s==null){
					return;
				}
				File f = new File(s+".xls");
				try {

		            WritableWorkbook workbook1 = Workbook.createWorkbook(f);
		            WritableSheet sheet1 = workbook1.createSheet("First Sheet", 0); 
		            TableModel model = tabbb.getModel();

		            for (int i = 0; i < model.getColumnCount(); i++) {
		                Label column = new Label(i, 0, model.getColumnName(i));
		                sheet1.addCell(column);
		            }
		            int j = 0;
		            for (int i = 0; i < model.getRowCount(); i++) {
		                for (j = 0; j < model.getColumnCount(); j++) {
		                    Label row = new Label(j, i + 1, model.getValueAt(i, j)!=null?model.getValueAt(i, j).toString():" - ");
		                    sheet1.addCell(row);
		                }
		            }
		            workbook1.write();
		            workbook1.close();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }

	
			}
        	
        });
        framed.add(exporttoxl);
        framed.setSize(800,500);
        framed.setLocationRelativeTo(fram);
        framed.setVisible(true);
	}
	public static void main(String[] args){
		@SuppressWarnings("unused")
		GUISNMP test = new GUISNMP();
	}
}
