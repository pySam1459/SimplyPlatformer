package designer.save;

import java.awt.Point;
import java.io.Serializable;

public class SaveEnemy implements Serializable {

	private static final long serialVersionUID = 8593682854314611470L;
	
	public Point start, end;
	
	public SaveEnemy(Point s, Point e) {
		this.start = s;
		this.end = e;
		
	}

}
