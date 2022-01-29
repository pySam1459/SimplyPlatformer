package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.Game;
import map.Map;
import map.Tile;
import util.C;
import util.utils.ImageUtils;

public class Player {
	
	public double w=40.0, h=44.0;
	public double[] cr = new double[] {0, 0, w, h};
	public double vx=0.0, vy=0.0, speed=0.5, maxspeed=6.5;
	private double prevVY = 0.0;
	
	public boolean standing = false, updatePrevVY = false, allowfall = true, prevJump = false, lookleft = false;
	
	private Animation playerMovementAnim;
	private BufferedImage image;
	
	private Point ij;
	private Tile t;
	
	private Game game;
	private Map map;
	
	public Player(Game game, Map map) {
		this.game = game;
		this.map = map;
		if(map.spawn == null) {
			map.spawn = new Point((int)cr[2]/2, C.HEIGHT/2);
		}
		this.cr[0] = map.spawn.x - cr[2]/2;
		this.cr[1] = map.spawn.y - cr[3]/2;
		
		this.playerMovementAnim = new Animation(new String[] {"char0", "char1", "char2", "char3"}, 100);
		
	}
	
	public void tick() {
		move();
		applyVelocity();
		collision();
		checkDeath();
		
		animations();
		
	}
	
	private void move() {
		boolean moving = false;
		if(game.keyboard.get(KeyEvent.VK_A)) {
			playerMovementAnim.unpause();
			this.vx -= speed * (standing ? 1 : 0.25);
			moving = true;
			lookleft = true;
			
		} if(game.keyboard.get(KeyEvent.VK_D)) {
			playerMovementAnim.unpause();
			this.vx += speed * (standing ? 1 : 0.25);
			moving = true;
			lookleft = false;
			
		} if(game.keyboard.get(KeyEvent.VK_S)) {
			if(!checkFeetTiles("solid") && checkFeetTiles("walkable")) {
				this.vy = 3;
				this.prevVY = -1;
			}
		} if(!moving) {
			playerMovementAnim.pause();
			this.vx *= 0.75;
			if(Math.abs(this.vx) < 0.001) {
				this.vx = 0.0;
			}
		} else {
			if(!checkFeetTiles("solid") && !checkFeetTiles("walkable")) {
				standing = false;
			}
		}
		if(game.keyboard.get(KeyEvent.VK_SPACE)) {
			if(standing && !prevJump) {
				this.vy -= C.JUMPFORCE;
				standing = false;
				prevJump = true;
			}
		} else {
			prevJump = false;
		}
		
		vy += C.GRAVITY * (vy < 0 ? 1 : 1.4);
	}
	
	private void applyVelocity() {
		this.cr[0] += vx;
		if(this.cr[0] < 0) {
			this.cr[0] = 0.0;
			this.vx = 0.0;
		} else if(this.cr[0] + this.cr[2] > map.width * C.TILE_WIDTH) {
			this.cr[0] = map.width * C.TILE_WIDTH - cr[2];
			this.vx = 0.0;
		} if(Math.abs(this.vx) > maxspeed) {
			this.vx = maxspeed * Math.signum(this.vx);
		}
		
		this.cr[1] += vy;
	}
	
	private void collision() {
		ij = getIJ();
		
		boolean walkable = false;
		updatePrevVY = true;
		for(int j=ij.y; j<ij.y+3; j++) {
			for(int i=ij.x; i<ij.x+3; i++) {
				if(i >= 0 && i < game.map.width && j >= 0 && j < game.map.height) {
					t = game.map.array[j][i];
			
					double w = 0.5 * (this.cr[2] + t.cRect[2]);
					double h = 0.5 * (this.cr[3] + t.cRect[3]);
					double dx = (this.cr[0] + this.cr[2]/2) - (i*C.TILE_WIDTH+t.cRect[0] + t.cRect[2]/2);
					double dy = (this.cr[1] + this.cr[3]/2) - (j*C.TILE_HEIGHT+t.cRect[1] + t.cRect[3]/2);
					if(Math.abs(dx) <= w && Math.abs(dy) <= h) {
						double wy = w * dy;
						double hx = h * dx;
						tileCollisionEffects(t);
						
						walkable = t.variables.get("walkable");
						if(t.variables.get("solid") || walkable) {
							if(wy > hx) {
								if(!walkable) {
									if(wy > -hx) {
										if(this.vy < 0) {
											this.cr[1] = j*C.TILE_HEIGHT + t.cRect[1] + t.cRect[3];
											this.vy = 0.0;
										}
									} else {
										this.cr[0] = i*C.TILE_WIDTH + t.cRect[0] - this.cr[2];
										this.vx = 0.0;
									}
								}
							} else {
								if(wy > -hx) {
									if(!walkable) {
										this.cr[0] = i*C.TILE_WIDTH + t.cRect[0] + t.cRect[2];
										this.vx = 0.0;
									}
								} else if(this.vy > 0 && allowfall) {
									if(this.prevVY >= 0.0) {
										this.cr[1] = j*C.TILE_HEIGHT + t.cRect[1] - this.cr[3];
										this.vy = 0.0;
										standing = true;
									} else {
										updatePrevVY = false;
									}
								}
							}
						}
					}
				}
			}
		}
		if(updatePrevVY) {
			this.prevVY = vy;
		}
	}
	
	private void tileCollisionEffects(Tile t) {
		if(t.variables.get("finish")) {
			shrink();
			map.finish();
			
		} if(t.variables.get("dealdamage")) {
			death();
		}
	}
	
	private void checkDeath() {
		if(this.cr[1] + this.cr[3] > C.HEIGHT) {
			death();
			
		}
	}
	
	private void animations() {
		playerMovementAnim.tick();
		
	}
	
	public void render(Graphics2D g) {
		image = ImageUtils.get("objects", playerMovementAnim.poll());
		if(!lookleft) {
			image = ImageUtils.flip(image);
		}
		
		g.drawImage(image, (int)cr[0] - map.renderxoff, (int)cr[1], (int)cr[2], (int)cr[3], null);
		renderStats(g);
	}
	
	private void renderStats(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(C.PLAYER_STATS_FONT);
		g.drawString("Standing >> " + Boolean.toString(standing), 10, 16);
		g.drawString(String.format("prevVY >> %.2f", prevVY), 10, 30);
	}
	
	public void death() {
		shrink();
		this.cr = new double[] {map.spawn.x - w/2, map.spawn.y - h/2, w, h};
		this.vx = 0;
		this.vy = 0;
		this.prevVY = 0.0;
		this.playerMovementAnim.reset();
		standing = false;
		updatePrevVY = false; 
		allowfall = true; 
		prevJump = false;
		lookleft = false;
		
		map.reset();

	}
	
	private void shrink() {
		game.screen.start();
		double wid = cr[2]/C.DEATH_ITERATIONS, hght = cr[3]/C.DEATH_ITERATIONS;
		while(this.cr[2] > 0) {
			cr = new double[] {cr[0]+wid/2, cr[1]+hght/2, cr[2]-wid, cr[3]-hght};
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		game.screen.stop();
	}
	
	public Point getIJ() {
		return new Point((int)(cr[0]/C.TILE_WIDTH), (int)(cr[1]/C.TILE_HEIGHT));
		
	}
	
	private boolean checkFeetTiles(String var) {
		ij = getIJ();
		for(int i=ij.x; i<ij.x+2; i++) {
			if(i < map.width && ij.y+1 < map.height) {
				if(map.array[ij.y+1][i].variables.get(var)) {
					return true;
				}
			}
		}
		return false;
	}

}
