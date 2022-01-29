package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

import main.Game;
import objects.Enemy;
import objects.Player;
import util.C;
import util.Maths;
import util.utils.LoaderUtils;

public class Map {
	
	public Tile[][] array;
	public int width=(int)(C.WIDTH/C.TILE_WIDTH), height=(int)(C.HEIGHT/C.TILE_HEIGHT) + 1;
	public Point spawn;
	public Enemy[] enemies;
	
	public int xoff = 0, renderxoff;
	public boolean loading = false;
	
	public HashMap<Integer, Text> textMap;
	private Text currentText;
	private Point ij, p1;
	
	private Game game;
	public Player player;
	
	public Map(Game game, String mapName) {
		load(mapName);
		
		this.game = game;
		if(!C.designing) {
			this.player = new Player(game, this);
		
		}
	}
	
	public void tick() {
		if(!C.designing) {
			player.tick();
			moveScreen();
			enemyTick();
			
			if(currentText != null) {
				currentText.tick();
			}
		}
	}
	
	private void moveScreen() {
		if(player.cr[0] + player.cr[2] >= C.WIDTH/2 && player.cr[0]+player.cr[2] <= width*C.TILE_WIDTH-C.WIDTH/2) {
			xoff = (int)(player.cr[0] + player.cr[2] - C.WIDTH/2);
		} else if(player.cr[0] + player.cr[2] < C.WIDTH/2) {
			xoff = 0;
		} else if(player.cr[0] + player.cr[2] > width*C.TILE_WIDTH-C.WIDTH/2) {
			xoff = width*C.TILE_WIDTH-C.WIDTH;
		}
		
	}
	
	private void enemyTick() {
		for(Enemy e: enemies) {
			e.tick();
		}
	}
	
	private void showText(Graphics2D g) {
		ij = getMouseIJ();
		boolean needToStop = true;
		if(ij.x >= 0 && ij.x < width && ij.y >= 0 && ij.y < height) {
			if(array[ij.y][ij.x].textIndex != -1) {
				if(Maths.getDistance(p1.x+xoff, p1.y, player.cr[0], player.cr[1]) < C.SIGN_RENDER_DISTANCE) {
					if(currentText == null) {
						currentText = textMap.get(array[ij.y][ij.x].textIndex);
					} else if(currentText != textMap.get(array[ij.y][ij.x].textIndex)) {
						currentText.reset();
						currentText = textMap.get(array[ij.y][ij.x].textIndex);
						
					} 
					
					currentText.render(g);
					needToStop = false;
				}
			}
		} if(needToStop && currentText != null) {
			currentText.reset();
			currentText = null;
		}
	}
	
	public void render(Graphics2D g) {
		if(!loading) {
			renderxoff = xoff;
			renderTiles(g);
			
			if(!C.designing) {
				player.render(g);
				renderEnemies(g);
				showText(g);
			}
		}
	}
	
	private void renderTiles(Graphics2D g) {
		g.setColor(Color.BLACK);
		for(int j=0; j<height; j++) {
			for(int i=0; i<width; i++) {
				if((i+1)*C.TILE_WIDTH > renderxoff || i*C.TILE_WIDTH < C.WIDTH + renderxoff) {
					array[j][i].render(g, i, j, renderxoff);
					if(C.designing) {
						g.drawRect(i*C.TILE_WIDTH-renderxoff, j*C.TILE_HEIGHT, C.TILE_WIDTH, C.TILE_HEIGHT);
						
					}
				}
			}
		}
	}
	
	private void renderEnemies(Graphics2D g) {
		for(Enemy e: enemies) {
			e.render(g);
			
		}
	}
	
	public void reset() {
		for(Enemy e: enemies) {
			e.reset();
			
		}
	}
	
	private Point getMouseIJ() {
		p1 = game.mouse.getXY();
		return new Point((p1.x+xoff)/C.TILE_WIDTH, p1.y/C.TILE_HEIGHT);
	}
	
	private void load(String mapName) {
		if(mapName == null) {
			this.array = new Tile[height][width];
			for(int j=0; j<height; j++) {
				for(int i=0; i<width; i++) {
					array[j][i] = new Tile("null");
				}
			}
		} else {
			LoaderUtils.load(this, mapName);
			
		}
	}
	
	public void finish() {
		game.menu.inMenu = true;
		
		
	}

}
