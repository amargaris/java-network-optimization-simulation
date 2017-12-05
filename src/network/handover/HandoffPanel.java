package network.handover;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class HandoffPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	public int xpos,ypos;
	public int dx,dy;
	public int speed=1;
	public int active;
	public int sb1x,sb2x,sb3x,sb1y,sb2y,sb3y;
	public double[] Rp={0,0,0};
	public ImageIcon phone = new ImageIcon(getClass().getResource("images/phone.png"));
	public ImageIcon bss = new ImageIcon(getClass().getResource("images/bss.png"));
	public ImageIcon activebss = new ImageIcon(getClass().getResource("images/active.png"));
	public JSlider prog;
	public JButton function2butt,functionbutt,function3butt,start;
	public HandoffThread thf;
	public JTabbedPane tab;
	public JScrollPane scroll1,scroll2;
	public JTextArea text1,text2;
	public JLabel speedlab;
	
	public static DateFormat form =new SimpleDateFormat("(hh:mm:ss)");
	
	public HandoffPanel(){
		
		setPreferredSize(new Dimension(385,540));
		setLayout(null);
		setOpaque(false);
		functionbutt = new JButton("To SB2");
		functionbutt.setPreferredSize(new Dimension(80,50));
		functionbutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startMovement();
			}
			
		});
		function2butt = new JButton("To SB3");
		function2butt.setPreferredSize(new Dimension(80,50));
		function2butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startMovement2();
			}
			
		});
		function3butt = new JButton("To SB1");
		function3butt.setEnabled(false);
		function3butt.setPreferredSize(new Dimension(80,50));
		function3butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startMovement3();
			}
			
		});
		prog = new JSlider();
		prog.setPaintLabels(true);
		prog.setMaximum(10);
		prog.setMinimum(1);
		prog.setValue(1);
		prog.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				speed = prog.getValue();
				speedlab.setText("Speed : "+speed);
			}
			
		});
		speedlab = new JLabel("Speed : "+speed);
		JPanel pan = new JPanel(new FlowLayout());
		pan.setOpaque(false);
		pan.add(function3butt);
		pan.add(functionbutt);
		pan.add(function2butt);
		pan.add(speedlab);
		pan.add(prog);
		pan.setBounds(50,250,300,130);	
		add(pan);
		tab = new JTabbedPane();
		text1 = new JTextArea();
		text1.setForeground(Color.green);
		text1.setText("System Log");
		text2 = new JTextArea();
		text2.setForeground(Color.blue);
		text2.setText("Power Log");
		scroll1 = new JScrollPane(text1);
		scroll1.setPreferredSize(new Dimension(200,100));
		scroll2 = new JScrollPane(text2);
		scroll2.setPreferredSize(new Dimension(200,100));
		tab.addTab("Handoffs", scroll1);
		tab.addTab("Power", scroll2);
		tab.setBounds(0, 340, 380, 110);
		start = new JButton("Stop");
		start.setBounds(0,260,70,30);
		start.setEnabled(false);
		start.addActionListener(new ActionListener(){
			boolean flag=true;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(flag){
					thf.pause(false);
					flag=false;
					start.setText("Start");
				}
				else{
					synchronized(thf){
						thf.pause(true);
					}
					
					flag=true;
					start.setText("Stop");
				}
			}
			
		});
		
		
		add(start);
		add(tab);
		
		initializeSimulator();
	}
	public void initializeSimulator(){
		active=1;
		xpos= 20;
		ypos= 100;
		dx=1;
		dy=0;
		sb1x= 20;
		sb1y= 100;
		sb2x= 20+300;
		sb2y= 100;
		sb3x= 20+150;
		sb3y= 0;
	}
	public void paint(Graphics g2){
		Graphics2D g = (Graphics2D)g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		super.paint(g);
		g.setColor(Color.black);
		g.drawRect(0, 0, 380, 230);
		g.setColor(Color.gray);
		g.fillRect(0, 0, 150, 80);
		g.fillRect(150+70, 0, 160, 80);
		g.fillRect(0, 150, 150, 80);
		g.fillRect(150+70,150 , 160, 80);
		g.setColor(Color.black);

		if(active!=1){
			g.drawImage(bss.getImage(),sb1x,sb1y,null);
		}
		else {
			g.drawImage(activebss.getImage(),sb1x,sb1y,null);
		}
		g.drawString("SB1",sb1x+10 , sb1y);

		if(active!=2){
			g.drawImage(bss.getImage(),sb2x,sb2y,null);
		}
		else {
			g.drawImage(activebss.getImage(),sb2x,sb2y,null);
		}
		g.drawString("SB2",sb2x+10 , sb2y);
		if(active!=3){
			g.drawImage(bss.getImage(),sb3x,sb3y,null);
		}
		else{
			g.drawImage(activebss.getImage(),sb3x,sb3y,null);
		}
		g.drawString("SB3",sb3x-20 , sb3y+10);
		
		g.drawLine(sb1x+15, sb1y+20, sb2x+15, sb2y+20);
		g.drawLine(sb3x+17, sb2y+20, sb3x+17, sb3y+40);
		g.setColor(Color.red);
		g.fillOval(xpos+13, ypos+16, 8, 8);
		g.drawImage(phone.getImage(),xpos,ypos+10,null);
	}
	public void startMovement(){
		function3butt.setEnabled(true);
		if(thf==null){
			thf = new HandoffThread(this,HandoffThread.MOVE_1);
			thf.start();
		}
		else{
			thf.setMode(HandoffThread.MOVE_1);
		}
	}
	public void startMovement2(){
		functionbutt.setEnabled(true);
		if(thf==null){
			thf = new HandoffThread(this,HandoffThread.MOVE_2);
			thf.start();
		}
		else{
			thf.setMode(HandoffThread.MOVE_2);
		}
	}
	public void startMovement3(){
		function2butt.setEnabled(true);
		if(thf==null){
			thf = new HandoffThread(this,HandoffThread.MOVE_3);
			thf.start();
		}
		else{
			thf.setMode(HandoffThread.MOVE_3);
		}
	}
	public void getPowerFromAll(){
		double d1 = Math.sqrt(Math.pow(xpos-sb1x, 2)+Math.pow(ypos-sb1y,2));
		d1 = d1*1000;
		int d1rr=(int)d1;
		double d1ff = ((double)d1rr)/1000.0;
		d1=d1ff;
		double d1temp = -10 -40*Math.log10(d1/1.5);
		d1temp = d1temp*1000;
		int d1tempint = (int)d1temp;
		double d1final = ((double)d1tempint)/1000.0;
		double d2 = Math.sqrt(Math.pow(xpos-sb2x, 2)+Math.pow(ypos-sb2y,2));
		d2 = d2*1000;
		int d2rr=(int)d2;
		double d2ff = ((double)d2rr)/1000.0;
		d2=d2ff;
		double d2temp = -10 -40*Math.log10(d2/1.5);
		d2temp = d2temp*1000;
		int d2tempint = (int)d2temp;
		double d2final = ((double)d2tempint)/1000.0;
		double d3 = Math.sqrt(Math.pow(xpos-sb3x, 2)+Math.pow(ypos-sb3y,2));
		d3 = d3*1000;
		int d3rr=(int)d3;
		double d3ff = ((double)d3rr)/1000.0;
		d3=d3ff;
		double d3temp = -10 -40*Math.log10(d3/1.5);
		d3temp = d3temp*1000;
		int d3tempint = (int)d3temp;
		double d3final = ((double)d3tempint)/1000.0;
		if(d1>1.5){
			Rp[0] = d1final;//-10 -40*Math.log10(d1/1.5);//d1final;//
		}
		else{
			Rp[0]=-10;
		}
		if(d2>1.5){
			Rp[1] = d2final;//-10 -40*Math.log10(d2/1.5);
		}
		else{
			Rp[1]=-10;
		}
		if(d3>1.5){
			Rp[2] = d3final;//-10 -40*Math.log10(d3/1.5);
		}
		else{
			Rp[2]=-10;
		}
		Date date = new Date();
		String s = form.format(date);
		text2.append("\n"+s+"Distance From BS1 :"+d1+" and Power"+Rp[0]+"dBm");
		text2.append("\n"+s+"Distance From BS2 :"+d2+" and Power"+Rp[1]+"dBm");
		text2.append("\n"+s+"Distance From BS3 :"+d3+" and Power"+Rp[2]+"dBm");
	}
	public void pause(boolean flag){
		
	}
	public void calculateServing(){
		getPowerFromAll();

		double T1 = -90;
		double T2 = -82.5;
		double T3 = -95;
		
		if(Rp[active-1]<=T1){
			for(int i=0;i<3;i++){
				if(i+1==active){
					continue;
				}
				if(Rp[i]>=T2){
					active= i+1;
					Date date = new Date();
					String s = form.format(date);
					text1.append("\n"+s+" Handoff to SB"+active);
					break;
				}
			}
			return;
		}
		if(Rp[active-1]<=T3){
			int temptop=active;
			for(int i=0;i<3;i++){
				if(i+1==active){
					continue;
				}
				if(Rp[i]>Rp[temptop-1]){
					temptop=i+1;
				}
			}
			if(temptop==active){
				return;
			}
			else{
				active=temptop;
				Date date = new Date();
				String s = form.format(date);
				text1.append("\n"+s+" Handoff to SB"+active);
			}
		}
	}
	public static void main(String[] args){
		final HandoffPanel  pan = new HandoffPanel();
		JFrame fram = new JFrame("Handover Simulation");
		fram.setSize(410,510);
		fram.setLocationRelativeTo(null);
		fram.setLayout(new FlowLayout());
		Container con = fram.getContentPane();
		con.setBackground(Color.white);
		con.add(pan);
		fram.setVisible(true);
		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
