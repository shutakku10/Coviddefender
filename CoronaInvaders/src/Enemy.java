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
	private int nextDirChange = 500;
	private long lastDirChange = 500;
	private int speedX = 1;
	private int speedY = 1;
	private int health = 1;
	private int damage = 10;

	public Enemy(int pXPos, int pYPos) {
		try {
			img = ImageIO.read(new File("res/virus.png"));
			img = img.getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		xPos = pXPos;
		yPos = pYPos;

		if (rnd.nextBoolean())
			speedX *= -1;
	}

	public Point getLocation() {
		return new Point(xPos, yPos);
	}

	public boolean untenAngekommen() {
		if (yPos + ENEMY_HEIGHT >= Main.FRAME_HEIGHT) {
			health = 0;
			return true;
		} else
			return false;
	}

	public int getHealth() {
		return health;
	}

	public int getXpos() {
		return xPos;
	}

	public int getYpos() {
		return yPos;
	}

	public int getDamage() {
		return damage;
	}

	public void getHit(int pDamage) {
		health -= pDamage;
	}

	public void renderChar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, xPos, yPos, null);
	}

	public void moveEnemy() {
		if (System.currentTimeMillis() - lastDirChange > nextDirChange) {
			speedX *= -1;
			lastDirChange = System.currentTimeMillis();
			nextDirChange = rnd.nextInt(3000) + 500;
		}

		if (xPos + ENEMY_WIDTH - 25 > Main.FRAME_WIDTH) {
			xPos = Main.FRAME_WIDTH - ENEMY_WIDTH;
			speedX *= -1;
		} else if (xPos < 0) {
			xPos = 0;
			speedX *= -1;
		}

		xPos += speedX;
		yPos += speedY;
	}

	public void moveEnemyErik() {
		yPos += speedY;
		if (rnd.nextInt(2) == 0) {
			if (xPos + ENEMY_WIDTH - 25 < Main.FRAME_WIDTH)
				xPos += speedX * 4;
		} else {
			if (xPos > 0)
				xPos -= speedX * 4;
		}
	}
}
