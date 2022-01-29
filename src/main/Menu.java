package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import util.C;

public class Menu {
	
	public boolean inMenu = false;
	
	private Game game;
	
	public Menu(Game game) {
		this.game = game;
		
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("comicsansms", Font.BOLD, 56));
		g.drawString("Menu", C.WIDTH/2, C.HEIGHT/2);
	}

}
