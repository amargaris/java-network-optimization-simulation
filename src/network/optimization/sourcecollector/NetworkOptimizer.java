package network.optimization.sourcecollector;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import network.optimization.sourcecollector.gfx.GraphicalPanel;
import network.optimization.sourcecollector.gfx.NetworkGraphicsPanel;
import network.optimization.sourcecollector.ocl.GPUOperator;


public class NetworkOptimizer {
	private JFrame fram,results,panfram;
	private JScrollPane resultscroll;
	private JTable tab;
	private Container rescon;
	private JLabel resultlab;
	private NetworkGraphicsPanel netpan;
	private Container cont;
	private JPanel pan2;
	private JButton next,previous,refilter;//Koybia toy ResultsFrame
	private JButton reset,butt,auto,gene,hash,check,gpubutt,graphical,check2; //Koybia toy MainFrame
	private JButton zoomin,zoomout,moveright,moveleft; //Koybia toy Plot Frame
	private ImageIcon left = new ImageIcon(getClass().getResource("images/left.png"));
	private ImageIcon right = new ImageIcon(getClass().getResource("images/right.png"));
	private ImageIcon zoominic = new ImageIcon(getClass().getResource("images/zoomin.png"));
	private ImageIcon zoomoutic = new ImageIcon(getClass().getResource("images/zoomout.png"));

	private int resultsize,pages;
	private final int MAX_PER_PAGE=512;
	
	private GPUOperator gpu;//Xrhsh kartas grafikwn
	private GraphicalPanel pan;
	private SourcesCollectorsProblemModel mod;
	
	
	public NetworkOptimizer() { 
		//Synthetica
		try {
			SyntheticaBlackEyeLookAndFeel syn = new SyntheticaBlackEyeLookAndFeel();
		     UIManager.setLookAndFeel(syn);
		} catch (Exception e){
		    e.printStackTrace();
		}
		loadTheDll();
		//~Synthetica
		
		fram = new JFrame(){
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g){
				super.paint(g);
				g.clearRect(0,getHeight()-16, getWidth(), getHeight() );
			}

		};
		
		fram.setTitle("Simulating the Source - Collector problem");
		cont = fram.getContentPane();
		cont.setLayout(new FlowLayout());
		cont.setBackground(Color.white);
		netpan = new NetworkGraphicsPanel(this);
		fram.setIconImage(netpan.genetic.getImage());
		pan2 = new JPanel(new FlowLayout());
		pan2.setPreferredSize(new Dimension (100,330));
		pan2.setOpaque(false);
		reset=new JButton("Reset All");
		reset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				netpan.reinitialize();
				netpan.repaint();
				mod=(SourcesCollectorsProblemModel)netpan.getProblemModel();
				if(pan!=null){
					pan.resetPanelOffsetAndLimit();
					pan.repaint();
				}
			}
			
		});
		butt = new JButton("Next Random ");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				netpan.recalculateLink();
				if(pan!=null){
					pan.repaint();
				}
			}
			
		});
		auto = new JButton("Random Tries");
		auto.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mod.runNTimes(1000);
				netpan.refresh();
				if(pan!=null){
					pan.repaint();
				}
			}
			
		});
		gene = new JButton("To Gene");
		gene.addActionListener(new ActionListener(){
			 @Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,mod.linearToString(mod.matrixToLinear(mod.getGraph())));//.publishGene();
				JOptionPane.showMessageDialog(null,java.util.Arrays.toString(mod.linearToATGC(mod.matrixToLinear(mod.getGraph()))));
			 }
		});
		hash = new JButton("To Hash");
		hash.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,mod.LinearToHash(mod.matrixToLinear(mod.getGraph())));
			}
			
		});
		check = new JButton("Analysis");
		check.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createResultsFrame(mod.getExhaustiveSearchResults(0));
				fram.repaint();
			}
		});
		check2 = new JButton("Gene");
		check2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createGeneFrame(mod.getTableWithGenes(mod.getNGenes(10)));//getExhaustiveSearchResults(0));
				fram.repaint();
			}
			
		});
		gpubutt = new JButton("GPU");
		gpubutt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gpu = new GPUOperator();
				gpu.execute2(netpan.getProblemModel().matrixToLinear(mod.getGraph()));
			}
			
		});
		graphical = new JButton("Plot");
		graphical.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createPlotFrame();
			}
		});
		pan2.add(reset);
		pan2.add(butt);
		pan2.add(auto);
		pan2.add(gene);
		pan2.add(hash);
		pan2.add(check);
		pan2.add(check2);
		pan2.add(gpubutt);
		pan2.add(graphical);
		cont.add(netpan);
		cont.add(pan2);
		Component[] temp =pan2.getComponents();
		for(int i=0;i<temp.length;i++){
			temp[i].setPreferredSize(new Dimension(90,30));
		}
		fram.pack();
		fram.setLocationRelativeTo(null);
		fram.setVisible(true);
		mod=(SourcesCollectorsProblemModel)netpan.getProblemModel();
		fram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void createPlotFrame(){

		pan = new GraphicalPanel(mod,null,"Collector","Source");
		pan.setPreferredSize(new Dimension(400,400));
		panfram = new JFrame("Ploting"){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				super.paint(g);
				g.clearRect(0,getHeight()-16, getWidth(), getHeight() );
			}
			public void update(Graphics g){
				super.update(g);
				g.clearRect(0,getHeight()-16, getWidth(), getHeight() );
			}
		};
		Container con = panfram.getContentPane();
		con.setLayout(new FlowLayout());
		con.add(pan);
		panfram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panfram.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
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
		panfram.setSize(410,520);
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
	public void createResultsFrame(JTable thetab){
		tab=thetab;
		results = new JFrame("Results:");
		results.setSize(525,550);
		results.setResizable(false);
		results.setName("11");
		results.setLocationRelativeTo(null);
		rescon = results.getContentPane();
		resultsize = (int)Double.parseDouble(tab.getName());
		//double resultsize = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getSpaceOrder();
		if(resultsize<=MAX_PER_PAGE){
			resultscroll = new JScrollPane(tab);
			resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
			rescon.setLayout(new FlowLayout());
			rescon.add(resultscroll);
			refilter = new JButton("Refilter");
			refilter.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					rescon.invalidate();
					rescon.removeAll();
					tab = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getExhaustiveSuccessfulSearchResults(0);
					resultscroll = new JScrollPane(tab);
					resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
					rescon.add(resultscroll);
					rescon.validate();
					rescon.repaint();
				}
				
			});
			rescon.add(refilter);
		}
		else {
			pages = (resultsize/MAX_PER_PAGE)-1;
			resultscroll = new JScrollPane(tab);
			resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
			rescon.setLayout(new FlowLayout());
			previous = new JButton("previous");
			previous.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String[] temp = resultlab.getText().split(" ");
					int s = Integer.parseInt(temp[0]);
					s= s-1;
					if(results.getName().indexOf("1")!=-1){
						tab = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getExhaustiveSearchResults(s);
					}
					else{
						tab = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getExhaustiveSuccessfulSearchResults(s);
					}
					rescon.invalidate();
					rescon.removeAll();
					resultscroll = new JScrollPane(tab);
					resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
					resultlab.setText(""+(s)+" of "+pages);
					previous.setEnabled(true);
					if(s==0){
						previous.setEnabled(false);
					}
					next.setEnabled(true);
					rescon.add(resultscroll);
					rescon.add(previous);
					rescon.add(resultlab);
					rescon.add(next);
					if((results.getName().indexOf("1")!=-1)){
						rescon.add(refilter);
					}
					rescon.validate();
					results.repaint();
				}
				
			});
			previous.setEnabled(false);
			resultlab = new JLabel("0 of "+pages);
			next = new JButton("Next");
			next.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String[] temp = resultlab.getText().split(" ");
					int s = Integer.parseInt(temp[0]);
					s=s+1;
					rescon.invalidate();
					rescon.removeAll();
					if(results.getName().indexOf("1")!=-1){
						tab = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getExhaustiveSearchResults(s);
					}
					else{
						tab = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getExhaustiveSuccessfulSearchResults(s);
					}
					resultscroll = new JScrollPane(tab);
					resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
					resultlab.setText(""+s+" of "+pages);
					previous.setEnabled(true);
					if(s>=pages){
						next.setEnabled(false);
					}
					rescon.add(resultscroll);
					rescon.add(previous);
					rescon.add(resultlab);
					rescon.add(next);
					if(results.getName().indexOf("1")!=-1){
						rescon.add(refilter);
					}
					rescon.validate();
					results.repaint();
				}
				
			});
			refilter = new JButton("Refilter");
			refilter.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					rescon.invalidate();
					rescon.removeAll();
					tab = ((SourcesCollectorsProblemModel)netpan.getProblemModel()).getExhaustiveSuccessfulSearchResults(0);
					resultsize = (int)Double.parseDouble(tab.getName());
					if(resultsize>MAX_PER_PAGE){
						pages = (resultsize/MAX_PER_PAGE) -1;
					}
					else{
						pages=0;
						next.setEnabled(false);
						previous.setEnabled(false);
					}
					resultlab.setText(""+0+" of "+pages);
					resultscroll = new JScrollPane(tab);
					resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
					rescon.add(resultscroll);
					rescon.add(previous);
					rescon.add(resultlab);
					rescon.add(next);
					rescon.validate();
					results.setName("22");
					rescon.repaint();
				}
				
			});
			rescon.add(resultscroll);
			rescon.add(previous);
			rescon.add(resultlab);
			rescon.add(next);
			rescon.add(refilter);
		}
		results.setVisible(true);
		results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	public void createGeneFrame(JTable thetab){ //Gia ton genetiko algorithmo
		tab=thetab;
		//int howmany=10;
		results = new JFrame("Generation: "+1);
		results.setSize(525,550);
		results.setResizable(false);
		results.setName("11");
		results.setLocationRelativeTo(null);
		rescon = results.getContentPane();
		resultscroll = new JScrollPane(tab);
		resultscroll.setPreferredSize(new Dimension(results.getWidth()-20,results.getHeight()-100));
		rescon.setLayout(new FlowLayout());
		rescon.add(resultscroll);
		JButton action = new JButton("Select");
		action.addActionListener(new ActionListener(){
			int state =0;
			int generation=0;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(state==0){
					//select
					generation = generation+1;
					results.setTitle("Generation: "+generation);
					((JButton)arg0.getSource()).setText("reproduce");
					state=1;
				}
				else if(state==1){
					//reproduce
					((JButton)arg0.getSource()).setText("mutate");
					state=2;
				}
				else if(state==2){
					//mutate
					((JButton)arg0.getSource()).setText("Select");
					state=0;
				}
			}
			
		});
		//resultsize = (int)Double.parseDouble(tab.getName());
		rescon.add(action);
		results.setVisible(true);
		results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void loadTheDll(){ //Methodos poy epixeirei na fortwsei to dll apo to jar eksw apo to jar kai meta mesa sto systhma ( de leitoyrgei akoma)
		
		try {
			InputStream fin = getClass().getResourceAsStream("dll/JOCL-windows-x86_64.dll");
			byte[] by = new byte[264192];
			fin.read(by);
			
			File temp =new File("JOCL-windows-x86_64.dll");
			//JOptionPane.showMessageDialog(null,temp.getAbsolutePath());
			FileOutputStream fs = new FileOutputStream(temp);
			fs.write(by);
			fin.close();
			fs.close();
			System.out.println(temp.getAbsolutePath());
			System.load(temp.getAbsolutePath());
			temp.delete();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,e.toString());
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		@SuppressWarnings("unused")
		NetworkOptimizer opt = new NetworkOptimizer();
	}
}
