package designer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import util.C;
import util.input.Keyboard;
import util.input.Mouse;
import util.utils.ImageUtils;
import util.utils.TileUtils;

public class TileMenu {
		
	public final int width=512, height=512;
	
	public int state; // tile, object
	private String[] tiles, objects;
	public String currentTile, currentObject;
	private Point p1, p2, cpt, cpo;
	
	private boolean justSwitched = false;
	
	private ObjectDrawer od;
	public MenuScreen mScreen;
	private Mouse mouse;
	private Keyboard keyboard;
	
	public TileMenu(ObjectDrawer od) {
		this.od = od;
		this.mScreen = new MenuScreen(this);
		this.mouse = new Mouse(mScreen);
		this.keyboard = new Keyboard(mScreen);
		
		this.tiles = TileUtils.getTileNames();
		this.objects = new String[] {"enemy", "path"};
		setDefaultCurrent();
		
	}
	
	public void tick() {
		pick();
		switchState();

		mScreen.render();
	}
	
	private void pick() {
		if(mouse.left) {
			p2 = getIJ();
			if(p2.y*(512/32) + p2.x < tiles.length && state == 0) {
				this.cpt = p2;
				this.currentTile = tiles[p2.y*(512/32) + p2.x];
				
			} else if(p2.y*(512/32) + p2.x < objects.length && state == 1) {
				this.cpo = p2;
				this.currentObject = objects[p2.y*(512/32) + p2.x];
			}
		}
	}
	
	private void switchState() {
		if(keyboard.get(KeyEvent.VK_2)) {
			if(!justSwitched) {
				//this.state = state*-1 + 2;
				this.state = (state + 1) % 2;
				justSwitched = true;
			}
		} else {
			justSwitched = false;
		}
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		if(state == 0) {
			renderList(g, "tiles", tiles);
		} else if(state == 1) {
			renderList(g, "objects", objects);
		}
		
		g.setColor(Color.BLUE);
		g.setStroke(C.designerMenuHighlightStroke);
		if(state == 0) {
			g.drawRect(cpt.x*32, cpt.y*32, 32, 32);
		} else if(state == 1) {
			g.drawRect(cpo.x*32, cpo.y*32, 32, 32);
		}
	}
	
	private void renderList(Graphics2D g, String dir, String[] list) {
		g.setColor(Color.BLACK);
		g.setStroke(C.designerMenuTileStroke);
		int i=0, j=0;
		for(String value: list) {
			if("objects".equals(dir)) {
				value = od.sanatize(value);
			}
			
			g.drawImage(ImageUtils.get(dir, value), i*32, j*32, 32, 32, null);

			g.drawRect(i*32, j*32, 32, 32);
			i++;
			if(i*32 >= 512) {
				i = 0;
				j++;
				
			}
		}
	}
	
	private void setDefaultCurrent() {
		this.currentTile = "null";
		this.currentObject = "enemy";
		this.state = 0;
		cpt = getCP(tiles, "null");
		cpo = getCP(objects, "enemy");
		
	}
	
	private Point getCP(String[] list, String name) {
		int i=0, j=0;
		for(String x: list) {
			if(name.equals(x)) {
				return new Point(i, j);
			} else {
				i++;
				if(i*32 >= width) {
					i = 0;
					j++;
				}
			}
		}
		return null;
	}
	
	private Point getIJ() {
		p1 = mouse.getXY();
		return new Point(p1.x/32, p1.y/32);
	}

}
