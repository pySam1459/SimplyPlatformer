package util.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	private static HashMap<String, BufferedImage> images = new HashMap<>();
	private static String key;
	
	private static AffineTransform tx;
	private static AffineTransformOp op;

	public static BufferedImage get(String dir, String image) {
		key = "res/" + dir + "/" + image + ".png";
		if(images.containsKey(key)) {
			return images.get(key);
			
		} else {
			try {
				images.put(key, ImageIO.read(new File(key)));
				return images.get(key);
				
			} catch (IOException e) {
				System.out.println(key);
				e.printStackTrace();
				return null;
			}			
		}
	}

	public static BufferedImage flip(BufferedImage image) {
		tx = AffineTransform.getScaleInstance(-1,  1);
		tx.translate(-image.getWidth(), 0);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}
	
	public static void cleanCache() {
		images.clear();
		
	}

}
