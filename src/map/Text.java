package map;

import java.awt.Graphics2D;

import util.C;

public class Text {

	private String text, subText;
	private String[] parts;
	private int index=0, length;
	private long period, lastTime, now, delta;
	
	
	public Text(String text, long period) {
		this.text = text;
		this.length = text.length();
		
		this.period = period * 1000000;
		this.lastTime = System.nanoTime();
	}
	
	public void tick() {
		updateIndex();
		
	}
	
	private void updateIndex() {
		if(index < length) {
			now = System.nanoTime();
			delta += now-lastTime;
			lastTime = now;
			if(delta >= period) {
				index++;
				delta = 0;
			}
		}
	}
	
	public void reset() {
		index = 0;
		delta = 0;
		lastTime = 0;
	}
	
	public void render(Graphics2D g) {
		g.setFont(C.TEXT_FONT);
		subText = text.substring(0, index);
		parts = subText.split("\n");
		for(int i=0; i<parts.length; i++) {
			g.drawString(parts[i], C.WIDTH/2 - g.getFontMetrics().stringWidth(parts[i])/2, C.TEXT_FONT_SIZE*(i+1));
			
		}		
	}

}
