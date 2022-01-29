package designer.save;

import java.awt.Point;
import java.io.Serializable;

public class SaveFile implements Serializable {
	
	private static final long serialVersionUID = -8167070793970066890L;
	public SaveTile[][] array;
	public int width, height;
	public Point spawn;
	
	public SaveEnemy[] enemies;
	
	public SaveFile(SaveTile[][] arr, int w, int h) {
		this.array = arr;
		this.width = w;
		this.height = h;
		
	}

}
