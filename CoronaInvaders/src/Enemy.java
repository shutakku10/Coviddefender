import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Enemy {

	private Random rnd = new Random();
	private Image img;
	private Image dead1img;
	private Image dead2img;
	private int xPos, yPos;
	public static final int ENEMY_WIDTH = 50;
	public static final int ENEMY_HEIGHT = 50;
	private int nextDirChange = 500;
	private long lastDirChange = 500;
	private int speedX = 1;
	private int speedY = 1;
	private int maxHealth = 1, health = maxHealth;
	private int damage = 5;
	private long deadTimestamp = 0;
	private boolean dead = false;

	private boolean bossEnemy;

	public Enemy(int pXPos, int pYPos, boolean pBossEnemy) {
		this.bossEnemy = pBossEnemy;

		if (!bossEnemy) {
			try {
				img = ImageIO.read(getClass().getResource("images/virus.png"));
				img = img.getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, Image.SCALE_SMOOTH);

				dead1img = ImageIO.read(getClass().getResource("images/virusTod2.png"));
				dead1img = dead1img.getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, Image.SCALE_SMOOTH);

				dead2img = ImageIO.read(getClass().getResource("images/virusTod3.png"));
				dead2img = dead2img.getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			maxHealth = 30;
			damage = 35;
			speedX = 2;
			health = maxHealth;
			try {
				img = ImageIO.read(getClass().getResource("images/bossvirus.png"));
				img = img.getScaledInstance(ENEMY_WIDTH, ENEMY_HEIGHT, Image.SCALE_SMOOTH);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		xPos = pXPos;
		yPos = pYPos;

		if (rnd.nextBoolean())
			speedX *= -1;
	}

	public boolean isDead() {
		return dead;
	}

	public Point getLocation() {
		return new Point(xPos, yPos);
	}

	public boolean untenAngekommen() {
		if (yPos + ENEMY_HEIGHT >= Main.FRAME_HEIGHT) {
			dead = true;
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
		if (health <= 0)
			deadTimestamp = System.currentTimeMillis();
	}

	public void renderChar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (deadTimestamp > 0) {
			long deadTime = System.currentTimeMillis() - deadTimestamp;
			if (deadTime < 250) {
				img = dead1img;
			} else if (deadTime < 500) {
				img = dead2img;
			} else {
				dead = true;
			}
		}

		g2d.drawImage(img, xPos, yPos, null);
		if (bossEnemy) {

			BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
			g2d.setStroke(stroke);
			g2d.setColor(Color.black);
			int barSizeX = 50;
			g2d.drawRect(xPos + 8, yPos - 20, barSizeX, 10);
			double obereGrenze = maxHealth * 2 / 3;
			double unterGrenze = maxHealth * 1 / 3;
			if (health >= obereGrenze)
				g2d.setColor(Color.green);
			else if (health >= unterGrenze) // farbliche Einstufung der Lebensanzeige
				g2d.setColor(Color.yellow);
			else if (health <= unterGrenze)
				g2d.setColor(Color.red);
			double tempHealth = health;
			g2d.fillRect(xPos + 9, yPos - 19, (int) ((tempHealth / maxHealth) * barSizeX - 1), 9);
		}
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

		if (health > 0) {
			xPos += speedX;
			yPos += speedY;
		}
	}
}
