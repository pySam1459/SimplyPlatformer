package designer;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MenuScreen extends Canvas implements Runnable {

	private static final long serialVersionUID = 93528612213874485L;
	
	private volatile boolean rendering = false;
	private Thread renderThread;
	
	private TileMenu menu;
	private JFrame frame;
	private BufferStrategy bs;
	
	public MenuScreen(TileMenu menu) {
		this.menu = menu;
		this.frame = new JFrame("Menu");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(menu.width, menu.height);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.add(this);
		
	}

	@Override
	public void run() {
		while(rendering) {
			render();
			
		}
	}
	
	public void render() {
		bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		
		menu.render(g);
		
		bs.show();
		g.dispose();
	}
	
	public synchronized void start() {
		rendering = true;
		renderThread = new Thread(this, "Menu Rendering Thread");
		renderThread.start();
	}
	
	public synchronized void close() {
		rendering = false;
		renderThread.interrupt();
	}

}
