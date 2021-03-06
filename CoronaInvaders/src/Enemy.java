import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Enemy {

	private Random rnd = new Random();
	
	private Image img;
	private int xPos, yPos;
	public static final int ENEMY_WIDTH = 50;
	public static final int ENEMY_HEIGHT = 50;
	private int speed = 1;
	private Point[] hitbox = new Point[8];
	
	private int health;
	private int damage;
	
	public Enemy(int pXPos, int pYPos) {
		try {
			img = ImageIO.read(new File("res/virus.png"));
			img = img.getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		xPos = pXPos;
		yPos = pYPos;
	}
	
	public void renderChar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, xPos, yPos, null);
	}
	
	public void moveEnemy() {
		yPos += speed;
		if(rnd.nextInt(2) == 0) {
			if (xPos + ENEMY_WIDTH - 25 < Main.FRAME_WIDTH)	
				xPos += speed*4;
		}else {
			if (xPos > 0)
				xPos -= speed*4;
		}
	}
}
