package objects;

import java.awt.Point;

import util.C;

public class EnemyPath {
	
	private final double enemySpeed = 1.5;
	
	public boolean stationary = false;
	public Point start, end;
	private boolean dirIsRightwards;
	
	private boolean movingRight;
	
	public EnemyPath(Point s, Point e) {
		this.start = s;
		this.end = e;
		
		if(e == null || (s.x == e.x && s.y == e.y)) {
			stationary = true;
		} else {
			dirIsRightwards = s.x < e.x;
			movingRight = dirIsRightwards;
		}
	}
	
	public void move(Enemy e) {
		if(!stationary) {
			if(movingRight) {
				e.rect[0] += enemySpeed;
				if(dirIsRightwards && e.rect[0] + e.rect[2] > end.x*C.TILE_WIDTH) {
					movingRight = false;
					e.rect[0] = end.x*C.TILE_WIDTH - e.rect[2];
				} else if(!dirIsRightwards && e.rect[0] + e.rect[2] > start.x*C.TILE_WIDTH) {
					movingRight = false;
					e.rect[0] = start.x*C.TILE_WIDTH - e.rect[2];
				}
				e.lookingright = true;
			} else {
				e.rect[0] -= enemySpeed;
				if(!dirIsRightwards && e.rect[0] < end.x*C.TILE_WIDTH) {
					movingRight = true;
					e.rect[0] = end.x*C.TILE_WIDTH;
				} else if(dirIsRightwards && e.rect[0] < start.x*C.TILE_WIDTH) {
					movingRight = true;
					e.rect[0] = start.x*C.TILE_WIDTH;
				}
				e.lookingright = false;
			}
		}
	}

}
