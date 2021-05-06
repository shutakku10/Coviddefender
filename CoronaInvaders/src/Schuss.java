import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Schuss {
	private Image img;
	private int xPos, yPos = 650, width = 10, height = 40;
	private int speed = 7;
	private int damage = 5;

	public Schuss(int pXpos) {
		try {
			img = ImageIO.read(new File("res/syringe.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		xPos = pXpos;
	}

	public Enemy checkForHits(ArrayList<Enemy> pGegnerList) {
		Enemy hitEnemy = null;
		for (Enemy enemy : pGegnerList) {
			if (xPos < enemy.getXpos() + Enemy.ENEMY_WIDTH && xPos + width > enemy.getXpos()) {
				if (yPos < enemy.getYpos() + Enemy.ENEMY_HEIGHT && yPos + height > enemy.getYpos()) {
					System.out.println("Hit");
					enemy.getHit(damage);
					damage = 0;
				}
			}
		}

		return hitEnemy;
	}

	public int getSpeed() {
		return speed;
	}

	public Point getLocation() {
		return new Point(xPos, yPos);
	}

	public int getXpos() {
		return xPos;
	}

	public int getDamage() {
		return damage;
	}

	public int getYpos() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setLocation(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void renderChar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, xPos, yPos, null);
	}

	public void move() {
		yPos -= speed;
	}
}
