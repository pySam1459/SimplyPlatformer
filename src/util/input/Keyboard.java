package util.input;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard extends ArrayList<Boolean> implements KeyListener {
	
	private static final long serialVersionUID = -1883851031767861693L;

	public Keyboard(Canvas canvas) {
		canvas.addKeyListener(this);
		
		for(int i=0; i<256; i++) {
			add(false);
		}
	}

	@Override
	public void keyPressed(KeyEvent k) {
		set(k.getKeyCode(), true);

	}

	@Override
	public void keyReleased(KeyEvent k) {
		set(k.getKeyCode(), false);

	}


	public void keyTyped(KeyEvent arg0) {}

}
