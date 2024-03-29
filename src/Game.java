import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas{
	
	private BufferStrategy strategy;
	private boolean gameRunning = true;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList <Entity>toRemove = new ArrayList<Entity>();
	private Entity ship;
	private double moveSpeed = 300;
	private long lastFire = 0;
	private long firingInterval = 500;
	private int alienCount;
	
	private String message = "";
	private boolean waitingForKeyPress = true;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean firePressed = false;
	private boolean logicRequiredThisLoop = false;

	public Game() {
		JFrame window = new JFrame ("It's a Game Boiii");

		JPanel panel = (JPanel) window.getContentPane();
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);

		setBounds(0,0,800,600);
		panel.add(this);

		setIgnoreRepaint(true);
		
		window.pack();	
		window.setResizable(false);
		window.setVisible(true);
		
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				System.exit(0);
			}
		});
		
		addKeyListener(new KeyInputHandler());
		
		requestFocus();
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		initEntities();

	}

	private void startGame() {
		entities.clear();
		initEntities();
		
		leftPressed=false;
		rightPressed=false;
		firePressed=false;
	}
	
	private void initEntities() {
		ship = new ShipEntity(this, "ship.jpg", 370, 550);
		entities.add(ship);
		
		alienCount=0;
		for(int row=0;row<5;row++) {
			for (int col=0;col<12;col++) {
				Entity alien = new AlienEntity(this, "alien.jpg", 100+(col*50),100+(row*50));
				entities.add(alien);
				alienCount++;
			}
		}
	}
	
	public void removeEntity(Entity e) {
		toRemove.add(e);
	}
	
	public void gameloop() {
		long lastLoopTime = System.currentTimeMillis();
		while(gameRunning) {
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			Graphics2D gr = (Graphics2D) strategy.getDrawGraphics();
			gr.setColor(Color.black);
			gr.fillRect(0, 0, 800, 600);
			
			for (int i=0;i<entities.size();i++) {
				Entity e = entities.get(i);
				e.move(delta);
			}
			
			for (int i=0;i<entities.size();i++) {
				Entity e = entities.get(i);
				e.draw(gr);
			}
			
			gr.dispose();
			strategy.show();
			
			try {
				Thread.sleep(10);
			} catch (Exception e) {}
		}
	}
	
	private class KeyInputHandler extends KeyAdapter{
		public void keyPressed(KeyEvent k) {
			if(k.getKeyCode()==KeyEvent.VK_LEFT) {
				leftPressed=true;
			}
			if(k.getKeyCode()==KeyEvent.VK_RIGHT) {
				rightPressed=true;
			}
			if(k.getKeyCode()==KeyEvent.VK_SPACE) {
				firePressed=true;
			}
		}
		
		public void keyReleased(KeyEvent k) {
			if(k.getKeyCode()==KeyEvent.VK_LEFT) {
				leftPressed=false;
			}
			if(k.getKeyCode()==KeyEvent.VK_RIGHT) {
				rightPressed=false;
			}
			if(k.getKeyCode()==KeyEvent.VK_SPACE) {
				firePressed=false;
			}
		}
	}


	public static void main(String[] args) {
		Game g = new Game();
		g.gameloop();
	}

	public void updateGame() {
		// TODO Auto-generated method stub
		
	}

}
