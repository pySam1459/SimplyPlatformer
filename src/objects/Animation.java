package objects;

public class Animation {
	
	private String[] images;
	private int index = 0, length;
	private long period;
	
	private long phase, now, lastTime;
	private boolean paused = false;
	
	public Animation(String[] images, long period) {
		this.images = images;
		this.period = period * 1000000;
		this.lastTime = System.nanoTime();
		
		length = images.length;
		
	}
	public void tick() {
		if(!paused) {
			updatePhase();
			updateIndex();
		}
	}
	
	private void updatePhase() {
		now = System.nanoTime();
		phase += (now - lastTime);
		lastTime = now;
		
	}
	
	private void updateIndex() {
		if(phase >= period && period > 0) {
			index = (index + 1) % length;
			
			phase = 0;
		}
	}
	
	public String poll() {
		return images[index];
	}
	
	public void pause() {
		this.paused = true;
	} public void unpause() {
		this.paused = false;
	}
	
	public void reset() {
		phase = 0;
		index = 0;
		
	}

}
