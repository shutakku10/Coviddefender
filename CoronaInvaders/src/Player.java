import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player {
	private Image img;
	private int xPos = 400, yPos = 700, width = 100, height = 100, speed = 7;
	private int maxHealth = 100, health = maxHealth;
	// nicht kleiner als 100
	private int fireCooldownMillis = 250;
	private long lastShotTimestamp = System.currentTimeMillis();
	private ArrayList<Schuss> schussList = new ArrayList<Schuss>();

	public Player() {
		try {
			img = ImageIO.read(new File("res/doc.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getSpeed() {
		return speed;
	}

	public Point getLocation() {
		return new Point(xPos, yPos);
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

	public void update() {
		for (int i = 0; i < schussList.size(); i++) {
			if (schussList.get(i).getYpos() <= -100)
				schussList.remove(i);
		}
	}

	public void renderChar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// render Character
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, xPos, yPos, null);

		// render life point bar
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

		for (Schuss schuss : schussList) {
			schuss.move();
			schuss.renderChar(g2d);
		}
	}

	public void move(String pRichtung) {
		if (pRichtung.equals("Rechts")) {
			if (xPos + width - 25 < Main.FRAME_WIDTH)
				xPos += speed;
		} else if (pRichtung.equals("Links")) {
			if (xPos > 0)
				xPos -= speed;
		}
	}

	public void die() {
		Main.gameOver = true;
	}

	public void getDamage(int pAmount) {
		health -= pAmount;
		if (health <= 0)
			die();
	}

	public void fire() {
		if (System.currentTimeMillis() - lastShotTimestamp > fireCooldownMillis) {
			schussList.add(new Schuss(xPos));
			lastShotTimestamp = System.currentTimeMillis();
		}
	}
}
