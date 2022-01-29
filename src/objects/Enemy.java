package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import map.Map;
import util.C;
import util.utils.ImageUtils;

public class Enemy {

	public double w=60, h=64;
	public double[] rect;
	public boolean lookingright = false;
	
	public boolean stationary = false;
	public boolean dying = false, dead=false;
	
	private Map map;
	public EnemyPath path;
	private Player p;
	
	private Animation movementAnim;
	private BufferedImage image;
	
	public Enemy(Map map, EnemyPath ep) {
		this.map = map;
		
		this.rect = new double[] {ep.start.x*C.TILE_WIDTH + C.TILE_WIDTH/2 - w/2, ep.start.y*C.TILE_HEIGHT+C.TILE_HEIGHT-h, w, h};
		this.path = ep;
		
		if(ep.stationary) {
			this.lookingright = false;
			this.movementAnim = new Animation(new String[] {"enemyStat"}, -1);
			
		} else {
			this.lookingright = ep.start.x < ep.end.x;
			this.movementAnim = new Animation(new String[] {"enemy0", "enemy1", "enemy2", "enemy3", "enemy4"}, 100);
		}
	}
	
	public void tick() {
		if(!(C.designing || dead)) {
			if(!dying) {
				move();
				checkSquish();
				
			} else {
				deathProcess();
			} 
			movementAnim.tick();
		}
	}
	
	private void move() {
		path.move(this);
		
	}
	
	private void checkSquish() {
		p = map.player;
		double w = 0.5 * (rect[2] + p.cr[2]);
		double h = 0.5 * (rect[3] + p.cr[3]);
		double dx = (p.cr[0] + p.cr[2]/2) - (rect[0] + rect[2]/2);
		double dy = (p.cr[1] + p.cr[3]/2) - (rect[1] + rect[3]/2);
		
		if(Math.abs(dx) <= w && Math.abs(dy) <= h) {
			double wy = w * dy;
			double hx = h * dx;
			if(wy > hx) {
				p.death();
			} else {
				if(wy > -hx) {
					p.death();
				} else if(p.vy > 0) {
					dying = true;
					p.vy *= -0.8;
					
				}
			}
		}
	}
	
	private void deathProcess() {
		this.rect[3] -= C.DEATH_SPEED;
		this.rect[1] += C.DEATH_SPEED;
		if(rect[3] <= 0) {
			dead = true;
			dying = false;
		}
	}
	
	public void reset() {
		dead = false;
		movementAnim.reset();
		
		this.rect = new double[] {path.start.x*C.TILE_WIDTH + C.TILE_WIDTH/2 - w/2, path.start.y*C.TILE_HEIGHT+C.TILE_HEIGHT-h, w, h};
		this.path = new EnemyPath(path.start, path.end);
	}
	
	public void render(Graphics2D g) {
		if(!dead) {
			image = ImageUtils.get("objects", movementAnim.poll());
			if(lookingright) {
				image = ImageUtils.flip(image);
			}
			
			g.drawImage(image, (int)rect[0]-map.renderxoff, (int)rect[1], (int)rect[2], (int)rect[3], null);
		}
	}

}
