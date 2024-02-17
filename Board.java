import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
	
	private final int B_WIDTH = 320;
	private final int B_HEIGHT = 310;
	private final int DOT_SIZE = 10;
	private final int ALL_DOTS = 300;
	private final int RAND_POS = 29;
	private final int DELAY = 140;
	
	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];
	
	private int dots;
	private int apple_x;
	private int apple_y;
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	
	private Timer timer;
	private Image ball;
	private Image apple;
	private Image head;
	
	public static JPanel contentPane;
	public JButton b = new JButton("Play Again?");
	
	public int score = 0;
	public Color myColor = new Color(0, 22, 43);
	
	public Board(JPanel c) {
		setSize(B_WIDTH, B_HEIGHT);
		setBackground(myColor);
		setFocusable(true);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		initBoard();
		c.add(b);
		b.setVerticalTextPosition(AbstractButton.CENTER);
		b.setSize(100, 50);
		b.setLocation((B_WIDTH/2-45), B_HEIGHT / 2+50);
		b.setSelected(true);
		b.setEnabled(false);
		b.setVisible(false);
	}
	
	private void initBoard() {
		addKeyListener(new KeyHandler());
		loadImages();
		initGame();
	}
	
	private void loadImages() {
		ImageIcon dot = new ImageIcon("/Users/ayushisampath/Downloads/Ayushi/Java_Class/Workplace/SnakeGame/src/dot.png");
		ball = dot.getImage();
		
		ImageIcon appleImg = new ImageIcon("/Users/ayushisampath/Downloads/Ayushi/Java_Class/Workplace/SnakeGame/src/apple.png");
		apple = appleImg.getImage();
		
		ImageIcon headImg = new ImageIcon("/Users/ayushisampath/Downloads/Ayushi/Java_Class/Workplace/SnakeGame/src/head.png");
		head = headImg.getImage();
	}
	
	private void initGame() {
		dots = 3;
		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}
		locateApple();
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(10,  10, B_WIDTH, B_HEIGHT);
		super.paintComponent(g);
		if (inGame) {
			g.drawImage(apple, apple_x, apple_y, this);
			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(head, x[z], y[z], this);
				} else {
					g.drawImage(ball, x[z], y[z], this);
				}
			}
			// Toolkit.getDefaultToolkit().sync();
		} else {
			gameOver(g);
		}
	}
	
	private void gameOver(Graphics g) {
		String msg = "Game Over. Score: "+(dots-3);
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (B_WIDTH - metr.stringWidth(msg))
		/ 2, B_HEIGHT / 2);
		b.setEnabled(true);
		b.setVisible(true);
		b.addActionListener(new ActionListener() {
		//boolean click = true;
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(b.isEnabled()) {
				inGame = true;
				initGame();
				paintComponent(g);
				b.setEnabled(false);
				b.setVisible(false);
			}
		}
		
		});
	}
	private void checkApple() {
		if ((x[0] == apple_x) && (y[0] == apple_y)) {
			dots++;
			locateApple();
		}
	}
	
	private void move() {
		for (int z = dots; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}
		if (leftDirection) {
			x[0] -= DOT_SIZE;
		}
		if (rightDirection) {
			x[0] += DOT_SIZE;
		}
		if (upDirection) {
			y[0] -= DOT_SIZE;
		}
		if (downDirection) {
			y[0] += DOT_SIZE;
		}
	}
	
	private void checkCollision() {
		for (int z = dots; z > 0; z--) {
			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				inGame = false;
			}
		}
		if (y[0] >= B_HEIGHT) {
			inGame = false;
		}
		if (y[0] < 0) {
			inGame = false;
		}
		if (x[0] >= B_WIDTH) {
			inGame = false;
		}
		if (x[0] < 0) {
			inGame = false;
		}
		if (!inGame) {
			timer.stop();
		}
	}
	
	private void locateApple() {
		int r = (int) (Math.random() * RAND_POS);
		apple_x = ((r * DOT_SIZE));
		r = (int) (Math.random() * RAND_POS);
		apple_y = ((r * DOT_SIZE));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			checkApple();
			checkCollision();
			move();
		}
		repaint();
	}
	
	private class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}
			if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
			}
			if ((key == KeyEvent.VK_UP) && (!downDirection)) {
				upDirection = true;
				rightDirection = false;
				leftDirection = false;
			}
			
			if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
				downDirection = true;
				rightDirection = false;
				leftDirection = false;
			}
		}
	}
}