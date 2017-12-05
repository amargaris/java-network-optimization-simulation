package network.optimization.sourcecollector.gfx;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import network.optimization.sourcecollector.NetworkOptimizer;
import network.optimization.sourcecollector.SourcesCollectorsProblemModel;
import network.optimization.sourcecollector.iface.ProblemModel;


public class NetworkGraphicsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private int collectors,sources;
	private int[][] graph;
	private int[][] cordscollector,cordssources;
	private int[][] cost;
	//private int[] collectorlimit;
	private JLabel[] collectorslab,sourceslab;
	private JLabel score;
	private Random rand;
	private Color[] arrowcolors;
	@SuppressWarnings("unused")
	private NetworkOptimizer parent;
	
	
	//Arxikopoiiseis
	private final int MAX_COLLECTOR=5;
	private final int MAX_SOURCES=10;
	private final int MAX_COST=100;
	private final int MAX_LIMIT=500;
	private final int SIZE_X = 600;
	private final int SIZE_Y = 400;
	private final int ARR_SIZE = 10;/**Megethos toy belous enwshs */
	
	
	//Eikones
	private ImageIcon collector = new ImageIcon(NetworkOptimizer.class.getResource("images/router.gif"));
	private ImageIcon source = new ImageIcon(NetworkOptimizer.class.getResource("images/access_point.png"));
	public ImageIcon genetic = new ImageIcon(NetworkOptimizer.class.getResource("images/genetics.png"));

	
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
    //Telos apo kseno kommati
    
    private SourcesCollectorsProblemModel mod;
    
	public NetworkGraphicsPanel(NetworkOptimizer theparent){ //constructoras me tyxaio deigma
		super();
		rand = new Random();
		parent=theparent;
		setPreferredSize(new Dimension(SIZE_X,SIZE_Y));
		setOpaque(false);
		setLayout(null);
		setBackground(Color.white);
		
		mod = new SourcesCollectorsProblemModel(MAX_COLLECTOR,MAX_SOURCES,MAX_COST,MAX_LIMIT);
		reinitialize();
		
	}
	public NetworkGraphicsPanel(NetworkOptimizer theparent,SourcesCollectorsProblemModel themod){
		super();
		rand = new Random();
		parent=theparent;
		setPreferredSize(new Dimension(SIZE_X,SIZE_Y));
		setOpaque(false);
		setLayout(null);
		setBackground(Color.white);
		mod=themod;
		reinitialize();
	}
	public void reinitialize(){
		mod.initialize();
		graph = mod.getGraph();
		//Arxikopoiiseis
		collectors = mod.getX();//rand.nextInt(MAX_COLLECTOR)+1;
		sources = mod.getY();//rand.nextInt(MAX_SOURCES)+1;
		collectorslab= new JLabel[collectors];
		sourceslab = new JLabel[sources];
		//graph = new boolean[collectors][sources];
		cost = mod.getCostGraph();//new int[collectors][sources];
		//collectorlimit = new int[collectors];
		arrowcolors = new Color[sources];
		
		//Dhmioyrgia mhtrwn / syntetagmenwn ws custom LayoutManager
		cordscollector = new int[collectors][2]; //0 gia x 1 gia Y
		cordssources = new int[sources][2];
		for(int i=0;i<collectors;i++){
			cordscollector[i][1]=100;
			cordscollector[i][0]=((i+1)*(SIZE_X/(collectors+1)))-40;
		}
		for(int j=0;j<sources;j++){
			cordssources[j][1]=SIZE_Y-100;
			cordssources[j][0]=((j+1)*(SIZE_X/(sources+1)))-35;
			arrowcolors[j] = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
		}
		removeAll();
		revalidate();
		//Dhmioyrgia twn labels sto JPanel
		
		//Dhmioyrgia twn labels gia ta collectors
		for(int i=0;i<collectors;i++){
			collectorslab[i]= new JLabel(collector);
			collectorslab[i].setText(""+i);
			collectorslab[i].setName(""+i);
			collectorslab[i].setHorizontalTextPosition(JLabel.CENTER);
			collectorslab[i].setVerticalTextPosition(JLabel.TOP);
			collectorslab[i].setForeground(Color.black);
			collectorslab[i].setBounds(cordscollector[i][0],cordscollector[i][1],82,50);
			collectorslab[i].addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {
					JPopupMenu menu = new JPopupMenu();
					menu.setName(((JLabel)arg0.getSource()).getName());
					JMenuItem item1 = new JMenuItem("Stats");
					item1.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent arg0) {
							JOptionPane.showMessageDialog(null, "Cost : "+mod.calculateCostAt(Integer.parseInt(((JPopupMenu)((JMenuItem)arg0.getSource()).getParent()).getName())));//"BlaBlaBla");
						}
						
					});
					JMenuItem item2 = new JMenuItem("MaxCap");
					item2.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent arg0) {
							JOptionPane.showMessageDialog(null, "MAX : "+mod.getLimitAt(Integer.parseInt(((JPopupMenu)((JMenuItem)arg0.getSource()).getParent()).getName())));//"BlaBlaBla");
						}
						
					});
					JMenuItem item3 = new JMenuItem("Exit");
					menu.add(item1);
					menu.add(item2);
					menu.add(item3);
					menu.show((JLabel)arg0.getSource(),arg0.getX(),arg0.getY());
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
			add(collectorslab[i]);
		}
		//Dhmioyrgia twn labels gia ta sources
		for(int i=0;i<sources;i++){
			sourceslab[i] = new JLabel(source);
			sourceslab[i].setText(""+i);
			sourceslab[i].setName(""+i);
			sourceslab[i].setForeground(Color.black);
			sourceslab[i].setHorizontalTextPosition(JLabel.CENTER);
			sourceslab[i].setVerticalTextPosition(JLabel.BOTTOM);
			sourceslab[i].setBounds(cordssources[i][0],cordssources[i][1],70,65);
			sourceslab[i].addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {
					JPopupMenu menu = new JPopupMenu();
					JMenuItem item1 = new JMenuItem("Stats");
					item1.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent arg0) {
							JOptionPane.showMessageDialog(null, "BlaBlaBla");
						}
						
					});
					JMenuItem item2 = new JMenuItem("Exit");
					menu.add(item1);
					menu.add(item2);
					menu.show((JLabel)arg0.getSource(),arg0.getX(),arg0.getY());
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
			add(sourceslab[i]);
		}
		score = new JLabel("Score is: "+mod.calculateCost());
		score.setForeground(Color.black);
		score.setBounds(SIZE_X/2-50, 10, 100, 20);
		add(score);
	}
	public ProblemModel getProblemModel(){
		return mod;
	}
	public void paint(Graphics g){ //Override tis paint gia dimioyrgia twn velwn
		super.paint(g);
		paintComponents(g);
		for(int i=0;i<collectors;i++){
			for(int j=0;j<sources;j++){
				if(graph[i][j]==1){
					drawArrow(g,cordssources[j][0]+25,cordssources[j][1],cordscollector[i][0]+35,cordscollector[i][1]+35,cost[i][j],j);		
				}
			}
		}
		
	}
	protected void paintComponent(Graphics g) { //Kseno Kommati Override tis paintComponent apo to idio site http://www.codeproject.com/Articles/114959/Rounded-Border-JPanel-JPanel-graphics-improvements
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
        graphics.fillRoundRect(0, 0, width - shadowGap, 
		height - shadowGap, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(0, 0, width - shadowGap, 
		height - shadowGap, arcs.width, arcs.height);

        //Sets strokes to default, is better.
        graphics.setStroke(new BasicStroke());
    }
	public void recalculateLink(){ //Methodos poy ypologizei mia nea tyxaia syndesmologia kai to neo tis kostos
		mod.randomizeGraph();
		refresh();
		//score.setText("Score is: "+mod.calculateCost());
		//graph = mod.getGraph();
		//repaint();
	}
	public void refresh(){
		score.setText("Score is: "+mod.calculateCost());
		graph = mod.getGraph();
		repaint();
	}
	
/*
	public static boolean getGraphValidity2(boolean[][] thegraph,int collectorlimit[],int[] costs){
		for(int i=0;i<thegraph.length;i++){
			int counter = collectorlimit[i];
			for(int j=0;j<thegraph[0].length;j++){
				if(thegraph[i][j]){
					counter = counter - costs[j];
				}
				if(counter<0){
					return false;
				}
			}
		}
		return true;
	}

*/
	void drawArrow(Graphics g1, int x1, int y1, int x2, int y2,int cost,int which) {//Methodos gia tin dimioyrgia toy belous syndeshs
		
		Graphics2D g = (Graphics2D) g1.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(arrowcolors[which]);
        
        
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
     
        g.transform(at);
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        try{
        	at.invert();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        g.transform(at);
        g.drawString(""+cost, (x1+x2)/2, (y1+y2)/2); 
    }
}
