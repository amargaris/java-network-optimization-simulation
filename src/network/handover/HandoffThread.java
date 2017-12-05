package network.handover;

public class HandoffThread extends Thread{
	private HandoffPanel pan;
	private int mode;
	private boolean works=true;
	public static final int MOVE_1 = 1;
	public static final int MOVE_2 = 2;
	public static final int MOVE_3 = 3;
	
	public HandoffThread(HandoffPanel thepan,int themode){
		pan=thepan;
		mode=themode;
	}
	public void run(){
		pan.start.setEnabled(true);
		while(true){
			System.out.println();
			if(works){
				if (mode==MOVE_1){
					if(pan.ypos==pan.sb2y){
						pan.dx=1;
						pan.dy=0;
					}
					else{
						pan.dx=0;
						pan.dy=1;
					}
					pan.xpos = pan.xpos+ pan.dx;
					pan.ypos = pan.ypos+pan.dy;
					pan.calculateServing();
					pan.repaint();
					try{
						Thread.sleep(100/pan.speed);
					}catch(Exception e){
						
					}
					if(pan.xpos==pan.sb2x){
						pan.functionbutt.setEnabled(false);
						pan.function2butt.setEnabled(true);
						pan.function3butt.setEnabled(true);
						break;
					}
				}
				else if(mode==MOVE_2){
					if(pan.xpos<pan.sb3x){
						pan.dx=1;
						pan.dy=0;
					}
					else{
						pan.dy=0;
						pan.dx=-1;
					}
					if(pan.xpos==pan.sb3x){
						pan.dx=0;
						pan.dy=-1;
					}
					pan.xpos = pan.xpos+pan.dx;
					pan.ypos = pan.ypos+pan.dy;
					pan.calculateServing();
					pan.repaint();
					try{
						Thread.sleep(100/pan.speed);
					}catch(Exception e){
						//e.printStackTrace();
					}
					if(pan.ypos==pan.sb3y){
						pan.function2butt.setEnabled(false);
						pan.function3butt.setEnabled(true);
						pan.functionbutt.setEnabled(true);
						break;
					}
				}
				else if(mode==MOVE_3){
					if(pan.ypos<pan.sb1y){
						pan.dx=0;
						pan.dy=1;
					}
					else{
						pan.dx=-1;
						pan.dy=0;
					}
					pan.xpos =pan.xpos +pan.dx;
					pan.ypos = pan.ypos+pan.dy;
					pan.calculateServing();
					pan.repaint();
					try{
						Thread.sleep(100/pan.speed);
					}catch(Exception e){
						//e.printStackTrace();
					}
					if(pan.xpos==pan.sb1x){
						pan.function3butt.setEnabled(false);
						pan.function2butt.setEnabled(true);
						pan.functionbutt.setEnabled(true);
						break;
					}
				}
			}
		}
		pan.start.setEnabled(false);
		pan.thf=null;
	}
	public synchronized void pause(boolean flag){
		works=flag;
	}
	public synchronized void setMode(int themode){
		mode=themode;
	}
}
