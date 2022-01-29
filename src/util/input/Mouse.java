package util.input;

import java.awt.Canvas;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	
	public boolean left = false;
	public boolean right = false;
	
	private Point p1, p2;
	private Canvas canvas;

	public Mouse(Canvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		
	}


	@Override
	public void mousePressed(MouseEvent m) {
		switch(m.getButton()) {
		case MouseEvent.BUTTON1:
			left = true;
			break;
		case MouseEvent.BUTTON3:
			right = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		switch(m.getButton()) {
		case MouseEvent.BUTTON1:
			left = false;
			break;
		case MouseEvent.BUTTON3:
			right = false;
			break;
		default:
			break;
		}
	}
	
	public Point getXY() {
		p1 = MouseInfo.getPointerInfo().getLocation();
		p2 = canvas.getLocationOnScreen();
		return new Point(p1.x-p2.x, p1.y-p2.y);
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

}
