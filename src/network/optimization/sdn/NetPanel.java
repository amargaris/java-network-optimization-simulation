package network.optimization.sdn;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class NetPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Network> netlist=new ArrayList<Network>();
	private Timer time;
	private ActionListener refresher;
	BufferedImage buffImg,apimage,messageimage;
	public int selectednetwork=0;
	public int refreshrate=200;
	private Point[] points,infopoints;
	public boolean infopainted=false;
	
	//KSENO KOMMATI GIA EIDIKA EFE STO JPANEL credits @ http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
		/** Stroke size. it is recommended to set it to 1 for better view */ 
	    protected final int strokeSize = 1; 
	    /** Color of shadow */
	    protected final Color shadowColor = Color.black;
	    /** Sets if it drops shadow */
	    protected final boolean shady = true;
	    /** Sets if it has an High Quality view */
	    protected final boolean highQuality = true;
	    /** Double values for Horizontal and Vertical radius of corner arcs */
	    protected final Dimension arcs = new Dimension(20, 20);
	    /** Distance between shadow border and opaque panel border */
	    protected final int shadowGap = 5;
	    /** The offset of shadow.  */
	    protected final int shadowOffset = 4;
	    /** The transparency value of shadow. ( 0 - 255) */
	    protected final int shadowAlpha = 150;
	    /**Megethos toy belous enwshs */
	    private final int ARR_SIZE = 10;
	    //Telos apo kseno kommati
	
	public NetPanel(Network thenet){
		netlist.add(thenet);
		
		refresher= new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					repaint();
					//System.gc();
				}
				catch(Exception exvc){
						
				}
			}
			
		};
		time = new Timer(refreshrate,refresher);
		//setBackground(Color.white);
		setOpaque(false);
		setBackground(new Color(238,238,224));
		setPreferredSize(new Dimension(700,600));
		//System.out.println("here");
		points = netlist.get(0).randomGraph(new Point(getPreferredSize().width/2,getPreferredSize().height/2), 200, netlist.get(0).getNodes().size());
		infopoints = netlist.get(0).randomGraph(new Point(getPreferredSize().width/2,getPreferredSize().height/2), 250, netlist.get(0).getNodes().size());
		ImageIcon image = new ImageIcon(getClass().getResource("images/access_point.png"));
		apimage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics g = apimage.getGraphics();
		image.paintIcon(null, g, 0, 0);
		g.dispose();
		image=null;
		image = new ImageIcon(getClass().getResource("images/message.png"));
		messageimage = new BufferedImage(image.getIconWidth(),image.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		g=messageimage.getGraphics();
		image.paintIcon(null, g, 0, 0);
		g.dispose();
		image=null;
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int width = getWidth();
        int height = getHeight();
        int shadowGap = this.shadowGap;
        Color shadowColorA = new Color(shadowColor.getRed(),shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;
        
        //Sets antialiasing if HQ.
        if (highQuality) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			RenderingHints.VALUE_ANTIALIAS_ON);
        }

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    width - strokeSize - shadowOffset, // width
                    height - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        //Draws the rounded opaque panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - shadowGap, height - shadowGap, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(0, 0, width - shadowGap, 
		height - shadowGap, arcs.width, arcs.height);

        //Sets strokes to default, is better.
        graphics.setStroke(new BasicStroke());
        
    	//Graphics2D g2 = (Graphics2D)g;
		graphics.setColor(Color.white);
		//g2.clearRect(0, 0, 400, 300);
		graphics.setColor(Color.black);

		Font font = new Font(Font.SERIF, Font.BOLD, 40);
		Font temp = graphics.getFont();
		graphics.setFont(font);
		if(selectednetwork==0){
	        graphics.drawString("Physical Network", 10, 33);
	    }
	    else{
	        graphics.drawString("Virtual Network "+selectednetwork, 10, 33);
	     }
		graphics.setFont(new Font("Book Antiqua", Font.ITALIC, 15));
		graphics.drawString("2013 Aristotelis Margaris \u00a9",500, 580);
		graphics.setFont(temp);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		if(netlist.get(0)==null){
			
		}
		else{
			//draw kathe node
			if(netlist.get(selectednetwork)==null){
				graphics.drawString("Empty Network", 200, 200);
			}
			else{
				int[] array=null;
				if(selectednetwork!=0){
					array = new int[points.length];
					for(int i=0;i<array.length;i++){
						array[i]=0;
					}
					ArrayList<Node> nodes =netlist.get(selectednetwork).getNodes();
					for(Node n:nodes){
						array[n.getId()-1]=1;
						//System.out.println("Adding "+(n.getId()-1));
					}		
				}
				for(int i=0;i<points.length;i++){
					Node b=null;
					if(selectednetwork!=0){
						if(array[i]==0){
							continue;
						}
						
					}
					b = netlist.get(0).getNodes().get(i);
					int x=points[i].x;
					int y=points[i].y;
					//Node 
					graphics.drawImage(apimage,x-25,y-25,null);
					graphics.drawString("192.168.2."+b.getId(), x-40, y-25);
					if(infopainted){
						int xinfo =infopoints[i].x;
						int yinfo = infopoints[i].y;
						graphics.setColor(new Color(185,211,238));
						graphics.fillRect(xinfo-35, yinfo-35, 70, 70);
						graphics.setColor(Color.black);
						graphics.drawRect(xinfo-35, yinfo-35 , 70, 70);
						graphics.drawString("C="+b.getCores()+" cores", xinfo-30,yinfo+15-35 );
						graphics.drawString("F="+b.getFrequency()+" GHz", xinfo-30, yinfo+35-35);
						graphics.drawString("M="+b.getRam()+"GB", xinfo-30, yinfo+55-35);
					}
					if(b.hasMessage()){
						graphics.drawImage(messageimage, x-30,y-30,null);
					}
				}
				//draw kathe link
				for(Link l:netlist.get(selectednetwork).getLinks()){
					Node a =l.getNodes().get(0);
					Node b =l.getNodes().get(1);
					int x1 = points[a.getId()-1].x;
					int y1 = points[a.getId()-1].y;
					int x2 = points[b.getId()-1].x;
					int y2 = points[b.getId()-1].y;
					drawArrow(graphics,x1,y1,x2,y2,l.getSpeed(),0);
				}
			}
		}
		
       
	}
	public void addNetwork(Network n){
		netlist.add(n);
	}
	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2,int cost,int which) {//Methodos gia tin dimioyrgia toy belous syndeshs
		
		Graphics2D g = (Graphics2D) g1.create();
        g.setColor(Color.black);
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
     
        g.transform(at);
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        g.fillPolygon(new int[]{0,ARR_SIZE,ARR_SIZE},new int[]{0,-ARR_SIZE,ARR_SIZE},3);
        try{
        	at.invert();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        g.transform(at);
        g.drawString(""+cost+"Mbps", (x1+x2)/2, (y1+y2)/2); 
    }
	public void startTimer(){
		time.start();
	}
	public void stopTimer(){
		time.stop();
	}
}
