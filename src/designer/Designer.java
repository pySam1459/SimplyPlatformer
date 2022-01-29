package designer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import designer.save.SaveEnemy;
import designer.save.SaveFile;
import designer.save.SaveTile;
import main.Game;
import map.Map;
import map.Tile;
import objects.Enemy;
import util.C;
import util.utils.LoaderUtils;

public class Designer {
	
	private Point p, p2, p3;
	private int[] highlightRect;
	public boolean up = true, prevleft=false, prevright=false;
	private int maxOff = 0, index;
	
	private Game game;
	private Map map;
	
	public TileMenu menu;
	private ObjectDrawer od;
	
	public Designer(Game game) {
		this.game = game;
		this.map = game.map;
		this.maxOff = map.width*C.TILE_WIDTH - C.WIDTH;

		this.od = new ObjectDrawer(game, this);
		this.menu = new TileMenu(od);
		
	}
	
	public void tick() {
		place();
		placeKey();
		doKeys();
		menu.tick();
		
		save();
		load();
	}
	
	private void place() {
		if(menu.state == 0) {
			placeTile();
			
		} else if(menu.state == 1) {
			od.draw();
			
		}
	}
	
	private void placeTile() {
		p = getIJ();
		if(p.x >= 0 && p.x < map.width && p.y >= 0 && p.y < map.height) {
			if(game.mouse.left) {
				up = false;
				if(game.keyboard.get(KeyEvent.VK_SHIFT)) {
					if(!map.array[p.y][p.x].tiles[map.array[p.y][p.x].tiles.length-1].equals(menu.currentTile)) {
						String[] parts = new String[map.array[p.y][p.x].tiles.length + 1];
						for(int i=0; i<parts.length-1; i++) {
							parts[i] = map.array[p.y][p.x].tiles[i];
						}
						parts[parts.length-1] = menu.currentTile;
						map.array[p.y][p.x] = new Tile(parts);
					}
				} else {
					map.array[p.y][p.x] = new Tile(menu.currentTile);
				}
				try {
					map.array[p.y][p.x].placed = true;
				} catch(ArrayIndexOutOfBoundsException e) {
					System.out.println(p.x + " " + p.y + " " + map.xoff);
					e.printStackTrace();
				}
			} else if (!up) {
				up = true;
				for(int j=0; j<map.height; j++) {
					for(int i=0; i<map.width; i++) {
						map.array[j][i].placed = false;
					}
				}
			}
			if(game.mouse.right) {
				if(highlightRect == null) {
					highlightRect = new int[] {p.x, p.y, 1, 1};
					
				} else {
					if(p.x < highlightRect[0]) {
						highlightRect[2] += highlightRect[0] - p.x;
						highlightRect[0] = p.x;
					} else if(p.x > highlightRect[0]) {
						highlightRect[2] = p.x - highlightRect[0] + 1;
					} if(p.y < highlightRect[1]) {
						highlightRect[3] += highlightRect[1] - p.y;
						highlightRect[1] = p.y;
					} else if(p.y > highlightRect[1]) {
						highlightRect[3] = p.y - highlightRect[1] + 1;
					}
				}
			} else if (highlightRect != null) {
				for(int j=highlightRect[1]; j<highlightRect[1]+highlightRect[3]; j++) {
					for(int i=highlightRect[0]; i<highlightRect[0]+highlightRect[2]; i++) {
						if(game.keyboard.get(KeyEvent.VK_SHIFT)) {
							if(!map.array[j][i].tiles[map.array[j][i].tiles.length-1].equals(menu.currentTile)) { 
								String[] parts = new String[map.array[j][i].tiles.length + 1];
								for(int k=0; k<parts.length-1; k++) {
									parts[k] = map.array[j][i].tiles[k];
								} 
								parts[parts.length-1] = menu.currentTile;
								map.array[j][i] = new Tile(parts);
							}
						} else {
							map.array[j][i] = new Tile(menu.currentTile);
							
						}
						
					}
				}
				
				highlightRect = null;
			}
		}
	}
	
	private void placeKey() {
		p = getIJ();
		if(p.x >= 0 && p.y < map.width && p.y >= 0 && p.y < map.height) {
			if(game.keyboard.get(KeyEvent.VK_1)) { // spawn Point
				map.spawn = new Point(p.x*C.TILE_WIDTH + C.TILE_WIDTH/2, p.y*C.TILE_HEIGHT + C.TILE_HEIGHT/2);
				
			}
		}
	}
	
	private void doKeys() {
		if(game.keyboard.get(KeyEvent.VK_DELETE)) {
			if(game.keyboard.get(KeyEvent.VK_RIGHT)) {
				if(!prevright) {
					if(map.width > C.WIDTH/C.TILE_WIDTH) {
						map.loading = true;
						map.width--;
						map.xoff = map.width*C.TILE_WIDTH - C.WIDTH;
						maxOff = map.xoff;
						for(int j=0; j<map.height; j++) {
							Tile[] newRow = new Tile[map.width];
							for(int i=0; i<map.width; i++) {
								newRow[i] = map.array[j][i];
							}
							map.array[j] = newRow;
						}
						
						map.loading = false;
					}
					prevright = true;
				}
			} else {
				prevright = false;
			}
		} else {
			if(game.keyboard.get(KeyEvent.VK_RIGHT)) {
				if(game.keyboard.get(KeyEvent.VK_CONTROL)) {
					map.xoff = map.width*C.TILE_WIDTH - C.WIDTH;
					
				} else {
					if(!prevright) {
						map.xoff += C.TILE_WIDTH;
						if(map.xoff > maxOff) {
							map.loading = true;
							maxOff = map.xoff;
							for(int j=0; j<map.height; j++) {
								Tile[] newRow = new Tile[map.width+1];
								for(int i=0; i<map.width; i++) {
									newRow[i] = map.array[j][i];
								}
								newRow[map.width] = new Tile("null");
								map.array[j] = newRow;
							}
							map.width++;
							prevright = true;
							map.loading = false;
						}
					}
				}
			} else {
				prevright = false;
			} 
			if(game.keyboard.get(KeyEvent.VK_LEFT)) {
				if(game.keyboard.get(KeyEvent.VK_CONTROL)) {
					map.xoff = 0;
				} else if(map.xoff >= C.TILE_WIDTH/4){
					map.loading = true;
					map.xoff -= C.TILE_WIDTH/4;
					if(map.xoff < 0) {
						map.xoff = 0;
					}
					map.loading = false;
				}
			}
		}
	}
	
	private void save() {
		if(game.keyboard.get(KeyEvent.VK_S)) {
			System.out.print("Map Name >> ");
			String name = C.input.next();
			File dir = new File("maps/" + name);
			if(!dir.exists()) {
				dir.mkdir();
			}

			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos);
				out.writeObject(createSaveFile());
				
				FileOutputStream fos = new FileOutputStream(String.format("maps/%s/%s.map", name, name));
				fos.write(bos.toByteArray());
				fos.close();
				
				createTextFile(name);
			
			} catch(IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private SaveFile createSaveFile() {
		SaveTile[][] stArray = new SaveTile[map.height][map.width];
		for(int j=0; j<map.height; j++) {
			for(int i=0; i<map.width; i++) {
				stArray[j][i] = new SaveTile(map.array[j][i].tiles);
				
			}
		}
		transferEnemies();
		SaveEnemy[] seArray = new SaveEnemy[map.enemies.length];
		for(int i=0; i<seArray.length; i++) {
			seArray[i] = new SaveEnemy(map.enemies[i].path.start, map.enemies[i].path.end);
		}
		
		SaveFile sf = new SaveFile(stArray, map.width, map.height);
		sf.spawn = map.spawn;
		sf.enemies = seArray;
		
		return sf;
	}
	
	private void createTextFile(String dir) {
		int count = 0;
		for(int j=0; j<map.height; j++) {
			for(int i=0; i<map.width; i++) {
				if(map.array[j][i].variables.get("hastext")) {
					count++;
	
				}
			}
		} String text = "";
		for(int i=0; i<count; i++) {
			text += String.format("%d{}\n", i);
		}
		
		try {
			FileWriter fw = new FileWriter(String.format("maps/%s/signText.txt", dir));
			fw.write(text);
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void transferEnemies() {
		map.enemies = new Enemy[od.enemies.size()];
		index=0;
		od.enemies.forEach((Integer xy, Enemy e) -> {
			map.enemies[index] = e;
			index++;
		});
	}
	
	private void load() {
		if(game.keyboard.get(KeyEvent.VK_L)) {
			System.out.print("Map Name >> ");
			String name = C.input.next();
			
			LoaderUtils.load(map, name);
		}
	}
	
	private Point getIJ() {
		p2 = game.mouse.getXY();
		return new Point((p2.x+map.xoff)/C.TILE_WIDTH, p2.y/C.TILE_HEIGHT);
	}
	
	public void render(Graphics2D g) {
		if(highlightRect != null) {
			g.setColor(Color.BLUE);
			g.setStroke(C.designerRightHighlightStroke);
			g.drawRect(highlightRect[0]*C.TILE_WIDTH-map.xoff, highlightRect[1]*C.TILE_HEIGHT, highlightRect[2]*C.TILE_WIDTH, highlightRect[3]*C.TILE_HEIGHT);
		
		} if(map.spawn != null) {
			p3 = map.spawn;
			g.setColor(Color.RED);
			g.fillOval(p3.x-4-map.xoff, p3.y-4, 8, 8);
		}
		
		od.render(g);
	}

}
