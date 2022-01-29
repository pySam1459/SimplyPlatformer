package map;

import java.awt.Graphics2D;
import java.util.HashMap;

import util.C;
import util.utils.ImageUtils;
import util.utils.TileUtils;

public class Tile {
	
	public String[] tiles;
	public int[] cRect;
	public boolean placed = false;
	public int textIndex = -1;
	
	public HashMap<String, Boolean> variables;
	
	public Tile(String type) {
		this.tiles = new String[] {type};
		
		loadFromConfig(tiles);
		if(cRect == null) {
			this.cRect = new int[] {0, 0, C.TILE_WIDTH, C.TILE_HEIGHT};
		}
	}
	
	public Tile(String[] tiles) {
		this.tiles = tiles;
		
		loadFromConfig(tiles);
		if(cRect == null) {
			this.cRect = new int[] {0, 0, C.TILE_WIDTH, C.TILE_HEIGHT};
		}
	}
	
	public void render(Graphics2D g, int i, int j, int xoff) {
		for(String type: tiles) {
			g.drawImage(ImageUtils.get("tiles", type), i*C.TILE_WIDTH - xoff, j*C.TILE_HEIGHT, C.TILE_WIDTH, C.TILE_HEIGHT, null);
		
		}
	}
	
	private void loadFromConfig(String[] tiles) {
		this.variables = new HashMap<>();
		Object x;
		for(String var: C.TILE_VARIABLES) {
			boolean results = false;
			for(String type: tiles) {
				x = TileUtils.getData(type, var);
				if(x != null) {
					if((Boolean)x) {
						results = true;
					}
				}
			}
			variables.put(var, results);
			
		} for(String type: tiles) {
			x = TileUtils.getData(type, "collisionrect");
			if(x != null) {
				if(cRect == null) {
					cRect = new int[] {-1, -1, -1, -1};
				}
				for(int i=0; i<4; i++) {
					cRect[i] = Math.max(cRect[i], ((int[]) x)[i]);
					
				}
			}
		}
	}

}
