package util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class C {
	
	// Misc
	public static final boolean designing = false;
	public static final String mapName = "map3";
	public static final Scanner input = new Scanner(System.in);
	
	// Game
	public static final String TITLE = "Platformer";
	public static final double FPS = 60.0;
	public static final int WIDTH=1920, HEIGHT=1080;
	
	// Tiles
	public static final int TILE_WIDTH = 64, TILE_HEIGHT = 64;
	public static final String[] TILE_VARIABLES = new String[] {"solid", "clickable", "hastext", "walkable", "dealdamage", "finish"};
	
	// Player
	public static final double GRAVITY = 0.3;
	public static final double GROUND_FRICTION = 0.0001;
	public static final double JUMPFORCE = 11;
	
	public static final int DEATH_ITERATIONS = 75;
	public static final int SIGN_RENDER_DISTANCE = 300;
	
	public static final Font PLAYER_STATS_FONT = new Font("Courier New Bold", Font.BOLD, 12);
	public static final int TEXT_FONT_SPEED = 25;
	public static final int TEXT_FONT_SIZE = 32;
	public static final Font TEXT_FONT = LOAD_ORANGEKID_REGULAR_FONT();
	
	//Enemy
	public static final int DEATH_SPEED = 4;
	
	
	// Designer
	public static final BasicStroke designerRightHighlightStroke = new BasicStroke(4);
	public static final BasicStroke designerMenuHighlightStroke = new BasicStroke(3);
	public static final BasicStroke designerMenuTileStroke = new BasicStroke(1);
	
	
	
	// Functions
	public static Font LOAD_ORANGEKID_REGULAR_FONT() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/misc/orange kid.ttf")));
			return new Font("orange kid", Font.PLAIN, TEXT_FONT_SIZE);
			
		} catch(IOException | FontFormatException e) {
			e.printStackTrace();
			return new Font("comicsansms", Font.PLAIN, TEXT_FONT_SIZE);
		}
	}
	
}
