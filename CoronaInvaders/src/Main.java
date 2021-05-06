import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Main extends JPanel implements KeyListener {
	// Anfang Attribute
	private Player player = new Player(this);
	private ArrayList<Enemy> gegner = new ArrayList<Enemy>();

	private Long startTime = System.currentTimeMillis();
	private Long elapsedTime;
	private int killScore = 0;
	private JLabel scoreLabel = new JLabel();

	boolean rechts = false;
	boolean links = false;
	boolean fire = false;

	public static final int FRAME_HEIGHT = 800;
	public static final int FRAME_WIDTH = 800;
	private Loop loop = new Loop();
	private Thread t = new Thread(loop);
	private JFrame fenster;
	private JFrame gameOverScreen;

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
		scoreLabel.setText(String.valueOf(killScore));
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 35));
		scoreLabel.setForeground(Color.RED);
		fenster.addKeyListener(this);
		fenster.setVisible(true);
		this.add(scoreLabel);
		fenster.add(this);
		setBackground(Color.LIGHT_GRAY);

		// Enemy erstellung
		createEnemies(10);
		t.start();
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

		for (Enemy enemy : gegner) {
			enemy.moveEnemy();
			if (enemy.untenAngekommen()) {
				player.getDamaged(enemy.getDamage());
				killScore--;
			}
		}

		// Time since last enemy creation
		elapsedTime = System.currentTimeMillis() - startTime;

		if (gegner.size() == 0)
			createEnemies(10);

		if (elapsedTime > 10 * 1000) {
			createEnemies(10);
		}

		player.update();
		for (int i = 0; i < gegner.size(); i++) {
			if (gegner.get(i).isDead()) {
				killScore++;
				System.out.println("Score: " + killScore);
				scoreLabel.setText(String.valueOf(killScore));
				gegner.remove(i);
			}
		}

		if (killScore % 25 == 0) {
			createBoss(killScore / 25);
			killScore++;
		}

		if (gameOver) {
			gameOver = false;
			gegner = new ArrayList<Enemy>();
			startTime = System.currentTimeMillis();
			killScore = 0;
			scoreLabel = new JLabel();
			rechts = false;
			links = false;
			fire = false;
			loop = new Loop();
			t = new Thread(loop);
			rnd = new Random();
			player = new Player(this);
			gameOverScreen = new GameOverScreen();
			fenster.dispose();
		}
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		player.renderChar(g2d);
		for (int i = 0; i < gegner.size(); i++) {
			if (gegner.get(i) != null)
				gegner.get(i).renderChar(g2d);
		}

	}

	public void createEnemies(int numberOfEnemies) {
		for (int i = 0; i < numberOfEnemies; i++) {
			int xPos = rnd.nextInt(FRAME_WIDTH - Enemy.ENEMY_WIDTH);
			int yPos = rnd.nextInt(FRAME_HEIGHT / 4 - Enemy.ENEMY_HEIGHT) + Enemy.ENEMY_HEIGHT;

			gegner.add(new Enemy(xPos, yPos, false));
		}
		// Time when last enemies created
		startTime = System.currentTimeMillis();
	}

	public void createBoss(int numberOfBosses) {
		for (int i = 0; i < numberOfBosses; i++) {
			int xPos = rnd.nextInt(FRAME_WIDTH - Enemy.ENEMY_WIDTH);
			int yPos = 100;

			gegner.add(new Enemy(xPos, yPos, true));
		}
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

	public class GameOverScreen extends JFrame {
		public GameOverScreen() {
			super();

			setSize(400, 400);
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (d.width - getSize().width) / 2;
			int y = (d.height - getSize().height) / 2;
			setLocation(x, y);
			setResizable(false);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			getContentPane().setLayout(new BorderLayout(0, 0));

			JPanel panel = new JPanel();
			panel.setBorder(UIManager.getBorder("InternalFrame.border"));
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setLayout(new GridLayout(0, 2, 0, 0));

			JButton btnNewButton = new JButton("Main Menu");
			btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btnNewButton.setBackground(Color.BLACK);
			btnNewButton.setForeground(Color.BLACK);
			btnNewButton.setSize(100, 100);
			panel.add(btnNewButton);

			JButton btRetry = new JButton("Retry");
			btRetry.setFont(new Font("Tahoma", Font.PLAIN, 20));
			btRetry.setBackground(Color.BLACK);
			btRetry.setForeground(Color.WHITE);
			panel.add(btRetry);
			btRetry.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new Main();
					dispose();
				}
			});

			JLabel lblNewLabel = new JLabel("Score");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblNewLabel.setForeground(Color.BLACK);
			lblNewLabel.setBackground(Color.BLACK);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			getContentPane().add(lblNewLabel, BorderLayout.NORTH);

			JPanel panel_1 = new JPanel();
			panel_1.setBackground(Color.LIGHT_GRAY);
			panel_1.setForeground(Color.RED);
			getContentPane().add(panel_1, BorderLayout.CENTER);
			panel_1.setLayout(new CardLayout(0, 0));

			JLabel lblNewLabel_1 = new JLabel("You died!");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 50));
			lblNewLabel_1.setForeground(Color.RED);
			lblNewLabel_1.setBackground(Color.LIGHT_GRAY);
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			panel_1.add(lblNewLabel_1, "name_21169377146200");
			setVisible(true);
		}

	}

} // end of class Main
