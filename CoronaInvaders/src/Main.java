import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main extends JPanel implements KeyListener {
	// Anfang Attribute
	Player player = new Player(this);
	ArrayList<Enemy> gegner = new ArrayList<Enemy>();
	boolean rechts = false;
	boolean links = false;
	boolean fire = false;

	public static final int FRAME_HEIGHT = 800;
	public static final int FRAME_WIDTH = 800;
	private Loop loop = new Loop();
	private Thread t = new Thread(loop);
	private JFrame fenster;

	private Random rnd = new Random();
	public static boolean gameOver = false;

	// Ende Attribute
	public Main() {
		// Frame-Initialisierung
		super();
		fenster = new JFrame("Corona Invader");
		fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fenster.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - fenster.getSize().width) / 2;
		int y = (d.height - fenster.getSize().height) / 2;
		fenster.setLocation(x, y);
		fenster.setResizable(false);
		// Anfang Komponenten
		fenster.addKeyListener(this);
		fenster.setVisible(true);
		fenster.add(this);
		t.start();
		setBackground(Color.white);

		// Enemy erstellung
		for (int i = 0; i < 10; i++) {
			int xPos = rnd.nextInt(FRAME_WIDTH - Enemy.ENEMY_WIDTH);
			int yPos = rnd.nextInt(FRAME_HEIGHT / 4 - Enemy.ENEMY_HEIGHT) + Enemy.ENEMY_HEIGHT;

			gegner.add(new Enemy(xPos, yPos));
		}

	} // end of public Main

	// Anfang Methoden

	public static void main(String[] args) {
		new Main();
	} // end of main

	public ArrayList<Enemy> getGegnerList() {
		return gegner;
	}

	public void update() {
		if (rechts)
			player.move("Rechts");
		else if (links)
			player.move("Links");

		if (fire)
			player.fire();
		for (Enemy enemy : gegner)
			enemy.moveEnemy();

		player.update();
		repaint();

		if (gameOver) {
			// game over screen
			System.out.println("Game over!");
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		player.renderChar(g2d);
		for (Enemy enemy : gegner)
			enemy.renderChar(g2d);
	}

	class Loop implements Runnable {
		long timestamp = 0;
		long oldTimestamp = 0;

		@Override
		public void run() {
			while (true) {
				oldTimestamp = System.currentTimeMillis();
				update();
				timestamp = System.currentTimeMillis();
				if ((timestamp - oldTimestamp) > 1000 / 60) {
					System.out.println("Zu langsam");
					continue;
				}

				try {
					Thread.sleep(1000 / 60 - (timestamp - oldTimestamp));
				} catch (Exception e) {
					System.out.println("Loopfehler: " + 1000 / 60); // - (timestamp - oldTimestamp)));
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			links = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			rechts = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_J) {
			fire = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			links = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			rechts = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_J) {
			fire = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
} // end of class Main
