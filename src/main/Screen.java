package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import util.C;

public class Screen extends Canvas implements Runnable {

	private static final long serialVersionUID = 527747883480128001L;
	
	private volatile boolean rendering = false;
	private Thread renderThread;
	private BufferStrategy bs;
	
	private JFrame frame;
	private Game game;
	
	public Screen(Game g) {
		this.game = g;
		this.frame = new JFrame(C.TITLE);
		frame.setSize(C.WIDTH, C.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(this);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				game.close();
			}
		});
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
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, C.WIDTH, C.HEIGHT);
		
		game.render(g);
		
		bs.show();
		g.dispose();
	}
	
	public synchronized void start() {
		rendering = true;
		renderThread = new Thread(this, "Rendering Thread");
		renderThread.start();
		
	}
	
	public synchronized void stop() {
		rendering = false;
		renderThread.interrupt();
	}

}
