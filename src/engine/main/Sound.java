package engine.main;
import java.io.*;
import javax.sound.sampled.*;
public class Sound implements Runnable{
    private File soundFile = null;
    volatile private Clip clip = null;
    private boolean loaded = false;
    
    
    
    public Sound(String file){
    	Thread t = new Thread(this);
    	t.start();
        try{
            soundFile = new File("Resources/"+file);
        }catch(Exception e){
            System.err.println(e);
            System.exit(0);
        }
    }
    public void play(){
         req++;
    }
    public void setVolume(float f) {
    	FloatControl volume= (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    	volume.setValue(f);
    }
    public void stop(){
        if(!loaded)
            return;
        clip.stop();
    }
    public boolean isPlaying(){
        if(loaded){
            return clip.isActive()||req>0;
        }else{
            return false;
        }
    }
    public void load(){
        try{
        	if(clip!=null) {
        		clip.stop();
        		clip.drain();
        	}
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }catch(Exception e){
            System.err.println(e);
            return;
        }
        loaded = true;
    }
    volatile int req = 0;
	@Override
	public void run() {
		while(true) {
			if(req>2) req = 2;
			if(req>0) {
				if(!loaded)
		            return;
		         clip.stop();
		         try {
					Thread.sleep(10);
		         } catch (InterruptedException e) {}
		         clip.setMicrosecondPosition(0);
		         try {
					Thread.sleep(10);
			     } catch (InterruptedException e) {}
		         clip.start(); 
		         req--;
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {}
		}
		
	}
}