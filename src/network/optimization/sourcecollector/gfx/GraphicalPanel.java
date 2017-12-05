package network.optimization.sourcecollector.gfx;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import network.optimization.sourcecollector.SourcesCollectorsProblemModel;


public class GraphicalPanel extends JPanel {
    
	private static final long serialVersionUID = 1L;
    //private int limit;   
    private final int PAD = 20;

    private int howmanyinpanel;
    private int paneloffset=0;
    private int max;
    private int[] data;
    private int[] xdata;
    private String xaxis,yaxis;
    private SourcesCollectorsProblemModel model;
    
    public GraphicalPanel(int[] dataset,int[] thexdata,String thexaxis,String theyaxis,int howmany){//int[] databoard,int length,LteSimulator sim){ //constructoras
    	if(thexdata==null){
    		xdata = new int[dataset.length];
    		for(int i=0;i<dataset.length;i++){
    			xdata[i]=i;
    		}
    	}
    	data = dataset;
    	howmanyinpanel=howmany;
    	max = getMax();
    	xaxis=thexaxis;
    	yaxis=theyaxis;
    }
    public GraphicalPanel(SourcesCollectorsProblemModel mod ,int[] thexdata,String thexaxis,String theyaxis){
    	if(thexdata==null){
    		xdata = new int[mod.getType2Gene().length];
    		for(int i=0;i<mod.getType2Gene().length;i++){
    			xdata[i]=i;
    		}
    	}
    	model=mod;
    	data=model.getType2Gene();
    	howmanyinpanel=data.length;
    	xaxis=thexaxis;
    	yaxis=theyaxis;
    }
    public void setPanelOffsetAndLimit(int start,int stop){
    	paneloffset=start;
    	howmanyinpanel=stop;
    	repaint();
    }
    public void resetPanelOffsetAndLimit(){
    	paneloffset=0;
    	howmanyinpanel=-1;
    }
    public int getOffset(){
    	return paneloffset;
    }
    public int getCount(){
    	return howmanyinpanel;
    }
    public int getMaxCount(){
    	return data.length;
    }
	protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     if(model!=null){
    	 data=model.getType2Gene();
    	// resetPanelOffsetAndLimit();
    	 if(howmanyinpanel==-1){
    		 howmanyinpanel=data.length;
    	 }
    	 max=getMax();
    	 xdata = new int[model.getType2Gene().length];
 		for(int i=0;i<model.getType2Gene().length;i++){
 			xdata[i]=i;
 		}
     }
     
     Graphics2D g2 = (Graphics2D)g;
     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
     int w = getWidth();
     int h = getHeight();
     g2.setColor(Color.white);
     g2.fillRect(0, 0, w, h);
     // Draw ordinate.
  
     g2.setColor(Color.black);
     g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        
     // Draw x,y axis.
     g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
     g2.setColor(Color.orange);
     g2.draw(new Line2D.Double(PAD,PAD,w-PAD,PAD));
     g2.drawString("Max("+max+")", PAD, PAD);
     g2.setColor(Color.black);  
     final  float dash1[] = {10.0f};
     final  BasicStroke dashed =
    	        new BasicStroke(2.0f,
    	                        BasicStroke.CAP_BUTT,
    	                        BasicStroke.JOIN_MITER,
    	                        10.0f, dash1, 0.0f);
    	g2.setStroke(dashed);
     Font font = g2.getFont();
     FontRenderContext frc =g2.getFontRenderContext();
     LineMetrics lm = font.getLineMetrics("0", frc);
     float sh = lm.getAscent() + lm.getDescent();

     // y axis label
     String s=xaxis;
     float sy = PAD+((h-2*PAD) - s.length()*sh)/2+ lm.getAscent();
     for(int i=0;i<s.length();i++){
        String letter = String.valueOf(s.charAt(i));
        float sw=(float)font.getStringBounds(letter, frc).getWidth();
        float sx =(PAD-sw)/2;
        g2.drawString(letter,sx,sy);
        sy +=sh;
     }
        
     // x axis label
     //s="Time";
     sy = h-PAD+(PAD-sh)/2 +lm.getAscent();
     float sw = (float)font.getStringBounds(yaxis, frc).getWidth();
     float sx = (w-sw)/2;
     g2.drawString(yaxis, sx, sy);

     // zwgrafizei tis grammes apo ta simeia
     double xInc = (double)(w - 1*PAD)/(howmanyinpanel);
     double scale = (double)(h - 2*PAD)/getMax();
     g2.setPaint(Color.green);
     for(int i = paneloffset; i < paneloffset+howmanyinpanel-1; i++) {          // 
        double x1 = PAD + (i-paneloffset)*xInc;
        double y1 = h - PAD - scale*data[(i)];
        double x2 = PAD+((i-paneloffset)+1)*xInc;
        double y2 = h - PAD -scale*data[(i)+1];//
        g2.draw(new Line2D.Double(x1,y1,x2,y2));
      }
 
     //zwgrafizei ta simeia kai ta labels toys
     g2.setPaint(Color.red);
     for(int i=paneloffset;i<paneloffset+howmanyinpanel;i++){
         double x = PAD + (i-paneloffset)*xInc;
         double y = h - PAD - scale*data[(i)];
         g2.drawString(Integer.toString(data[(i)]), (int)x,(int) y);
         g2.drawString(""+xdata[(i)],(int) x, h-PAD+11); //Integer.toString(i)
         g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
    }
 	public int getMax() {  //megisto toy axona
      int max = -Integer.MAX_VALUE;
      for(int i = paneloffset; i < paneloffset+howmanyinpanel; i++) {
          if(data[(i-paneloffset)] > max){
        	  max = data[(i-paneloffset)];
          }
      }
      if(model!=null){
    	  return model.getX()-1;
      }
      else{
    	  return max;
      }
    }
}
