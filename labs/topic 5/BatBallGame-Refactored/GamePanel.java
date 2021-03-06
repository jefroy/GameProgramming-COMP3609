import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements 
					Runnable, 
					KeyListener {

	private Bat bat = null;
	private Ball ball = null;

	AudioClip playSound = null;

	private Thread gameThread;
	boolean isRunning;

	public GamePanel () {

		setBackground(Color.CYAN);
		addKeyListener(this);			// respond to key events
		setFocusable(true);
    		requestFocus();    			// the GamePanel now has focus, so receives key events

		loadClips ();

		gameThread = null;
		isRunning = false;

	}

	// implementation of Runnable interface

	public void run () {
		try {
			isRunning = true;
			while (isRunning) {
				gameUpdate();
				gameRender();
				Thread.sleep (200);	// increase value of sleep time to slow down ball
			}
		}
		catch(InterruptedException e) {}
	}

	// implementation of KeyListener interface

	public void keyPressed (KeyEvent e) {

		if (bat == null)
			return;

		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			bat.moveLeft();
		}
		else
		if (keyCode == KeyEvent.VK_UP) {
			bat.moveRight();
		}
	}

	public void keyReleased (KeyEvent e) {

	}

	public void keyTyped (KeyEvent e) {

	}

	public void loadClips() {

		try {
			playSound = Applet.newAudioClip (
					getClass().getResource("Background.wav"));

		}
		catch (Exception e) {
			System.out.println ("Error loading sound file: " + e);
		}

	}

	public void playClip (int index) {

		if (index == 1 && playSound != null)
			playSound.play();

	}

	public void gameUpdate () {
		ball.move();
	}

	public void gameRender () {				// draw the game objects

		ball.draw();
		bat.draw();
	}	

	public void startGame() {				// initialise and start the game thread 

		if (gameThread == null) {
			isRunning = true;
			bat = new Bat (this);
			ball = new Ball (this, bat);
			gameThread = new Thread(this);
			gameThread.start();
			playSound.loop();
		}
	}

	public void endGame() {					// end the game thread

		if (isRunning) {
			isRunning = false;
			playSound.stop();
		}
	}
}