import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main extends JPanel implements KeyListener {
	// Anfang Attribute
	Player player = new Player();
//	Enemy enemy1 = new Enemy(250, 200);
	Enemy[] enemy = new Enemy[9];
	boolean rechts = false;
	boolean links = false;

	public static final int FRAME_HEIGHT = 800;
	public static final int FRAME_WIDTH = 800;
	private Loop loop = new Loop();
	private Thread t = new Thread(loop);
	private JFrame fenster;

	private Random rnd = new Random();
	
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
		fenster.setTitle("Brick Breaker");
		fenster.setResizable(false);
		// Anfang Komponenten
		fenster.addKeyListener(this);
		fenster.setVisible(true);
		fenster.add(this);
		t.start();
		setBackground(Color.white);
		
		//Enemy erstellung
		for(int i = 0; i < enemy.length; i++) {
			int xPos = rnd.nextInt(FRAME_WIDTH-Enemy.ENEMY_WIDTH);
			int yPos = rnd.nextInt(FRAME_HEIGHT/4-Enemy.ENEMY_HEIGHT)+Enemy.ENEMY_HEIGHT;
			
			enemy[i] = new Enemy(xPos, yPos);
		}
		
	} // end of public Main

	// Anfang Methoden

	public static void main(String[] args) {
		new Main();
	} // end of main

	public void update() {
		if (rechts)
			player.move("Rechts");
		else if (links) {
			player.move("Links");
		}
		for(int i = 0; i < enemy.length; i++) enemy[i].moveEnemy();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		player.renderChar(g2d);
		for(int i = 0; i < enemy.length; i++) enemy[i].renderChar(g2d);
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			links = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			rechts = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
} // end of class Main
