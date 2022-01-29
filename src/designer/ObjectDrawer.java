package designer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Game;
import objects.Enemy;
import objects.EnemyPath;
import util.C;
import util.utils.ImageUtils;

public class ObjectDrawer {
	
	public HashMap<Integer, Enemy> enemies;
	private HashMap<Integer, EnemyPath> paths;
	private Point p, p2;
	private int xy;
	
	private Point wStart, wCur;
	private boolean working = false;
	private boolean prevLeft = false;
	
	private Game game;
	private Designer designer;
	
	public ObjectDrawer(Game game, Designer designer) {
		this.game = game;
		this.designer = designer;
		
		createHashMaps();
		
	}
	
	private void createHashMaps() {
		this.enemies = new HashMap<>();
		this.paths = new HashMap<>();
		if(game.map.enemies != null) {
			for(Enemy e: game.map.enemies) {
				enemies.put(toInt(e.path.start), e);
				addPathToPaths(e.path);
				
			} 
		}
	}
	
	public void draw() {
		p = getIJ();
		if(p.x >= 0 && p.x < game.map.width && p.y >= 0 && p.y < game.map.width) {
			xy = toInt(p.x, p.y);
			if(game.mouse.left) {
				switch(designer.menu.currentObject) {
				case "enemy":
					if(!prevLeft) {
						if(paths.get(xy) == null) {
							paths.put(xy, new EnemyPath(p, p));
						}
						enemies.put(xy, new Enemy(game.map, paths.get(xy)));
					}
					break;
				case "path":
					if(!working) {
						wStart = p;
						working = true;
					}
					wCur = p;
					break;
				default:
					System.out.println("Cannot draw " + designer.menu.currentObject);
					break;
				}
				
				prevLeft = true;
			} else {
				if(working) {
					addPathToPaths(new EnemyPath(wStart, wCur));
					wStart = null;
					wCur = null;
					working = false;
				}
				
				prevLeft = false;
			} if(game.mouse.right) {
				if(enemies.get(xy) != null) {
					enemies.remove(xy);
				} if(paths.get(xy) != null) {
					removePath(xy);	
				}
			}
		}
	}
	
	private void addPathToPaths(EnemyPath ep) {
		if(ep.stationary) {
			paths.put(toInt(ep.start), ep); 
		} else if(ep.start.x < ep.end.x) {
			for(int i=ep.start.x; i<=ep.end.x; i++) {
				paths.put(toInt(i, ep.start.y), ep);
			}
		} else {
			for(int i=ep.end.x; i<=ep.start.x; i++) {
				paths.put(toInt(i, ep.start.y), ep);
			}
		}
	}
	
	private void removePath(int xy) {
		EnemyPath ep = paths.get(xy);
		List<Integer> arr = new ArrayList<>();
		arr.add(xy);
		paths.forEach((Integer x, EnemyPath p) -> {
			if(p.equals(ep)) {
				arr.add(x);
			}
		});
		for(int x: arr) {
			paths.remove(x);
			
		}
	}
	
	public void render(Graphics2D g) {
		enemies.forEach((Integer xy, Enemy e) -> {
			e.render(g);
			
			if(C.designing) {
				renderPath(g, e.path.start, e.path.end);
			}
		});
		paths.forEach((Integer xy, EnemyPath ep) -> {
			renderPath(g, ep.start, ep.end);
		});
		if(working) {
			renderPath(g, wStart, wCur);
		}
	}
	
	private void renderPath(Graphics2D g, Point p1, Point p2) {
		if(p1.x < p2.x) {
			for(int i=p1.x; i<=p2.x; i++) {
				g.drawImage(ImageUtils.get("objects", "path"), i*C.TILE_WIDTH-game.map.renderxoff, p1.y*C.TILE_HEIGHT, C.TILE_WIDTH, C.TILE_HEIGHT, null);
			}
		} else {
			for(int i=p2.x; i<=p1.x; i++) {
				g.drawImage(ImageUtils.get("objects", "path"), i*C.TILE_WIDTH-game.map.renderxoff, p1.y*C.TILE_HEIGHT, C.TILE_WIDTH, C.TILE_HEIGHT, null);
			}
		}
	}
	
	public String sanatize(String object) {
		String actualImage;
		switch(object) {
		case "enemy": 
			actualImage = "enemy0";
			break;
		default: 
			actualImage = object;
			break;
		}
		
		return actualImage;
	}
	
	private Point getIJ() {
		p2 = game.mouse.getXY();
		return new Point((p2.x+game.map.xoff)/C.TILE_WIDTH, p2.y/C.TILE_HEIGHT);
	}
	
	private int toInt(Point a) {
		return a.y*game.map.width + a.x;
	} private int toInt(int[] xy) {
		return xy[1]*game.map.width + xy[0];
	} private int toInt(int i, int j) {
		return j*game.map.width + i;
	}

}
