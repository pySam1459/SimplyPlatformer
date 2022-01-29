package designer.save;

import java.io.Serializable;

public class SaveTile implements Serializable {

	private static final long serialVersionUID = -4792698981714081498L;
	
	public String[] tiles;
	
	public SaveTile(String[] tiles) {
		this.tiles = tiles;
		
	}

}
