import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
	private Image img;
	private int xPos = 400, yPos = 700, width = 100, height = 100;
	private int speed = 7;

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

	public void setLocation(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void renderChar(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, xPos, yPos, null);
	}

	public void move(String pRichtung) {
		if (pRichtung.equals("Rechts")) {
			if (xPos + width - 25 < Main.FRAME_WIDTH)
				xPos += speed;
		} else if (pRichtung.equals("Links")) {
			if (xPos > 0)
				xPos -= speed;
		}
		System.out.println("move");
	}
}
