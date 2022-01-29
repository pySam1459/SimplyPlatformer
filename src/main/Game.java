package main;

import java.awt.Graphics2D;

import designer.Designer;
import map.Map;
import util.C;
import util.input.Keyboard;
import util.input.Mouse;

public class Game implements Runnable {
	
	private volatile boolean running = false;
	private Thread mainThread;
	
	public Menu menu;
	public Map map;
	private Designer designer;
	
	public Screen screen;
	public Mouse mouse;
	public Keyboard keyboard;
	
	public Game() {
		this.screen = new Screen(this);
		this.mouse = new Mouse(screen);
		this.keyboard = new Keyboard(screen);
		
		if(C.designing) {
			this.map = new Map(this, C.mapName);
			this.designer = new Designer(this);
			
		} else {
			this.menu = new Menu(this);
			this.map = new Map(this, C.mapName);
		}
		start();
	}
	
	public void tick() {
		if(!C.designing) {
			if(menu.inMenu) {
				menu.tick();
			} else {
				map.tick();
			}
		} else {
			designer.tick();
		}
	}
	
	public void render(Graphics2D g) {
		if(C.designing) {
			map.render(g);
			designer.render(g);
			
		} else if(menu.inMenu) {
			menu.render(g);
			
		} else {
			map.render(g);
			
		}
	}

	@Override
	public void run() {
		double ns = 1000000000 / C.FPS;
		double delta = 0.0;
		long now, lastTime = System.nanoTime();
		while(running) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				
				screen.render();
				delta--;
			}
		}

	}
	
	public synchronized void start() {
		running = true;
		mainThread = new Thread(this, "Main Game Loop");
		mainThread.start();
	}
	
	public synchronized void close() {
		running = false;
		C.input.close();
		mainThread.interrupt();
	}

	public static void main(String[] args) {
		new Game();

	}

}
