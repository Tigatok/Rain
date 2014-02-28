package com.tylermarshall.rain;
import com.tylermarshall.rain.graphics.Screen;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

/**
 * 
 * @author Tyler Marshall
 *
 */

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static int width = 300; //Sets window width
	public static int height = width / 16 * 8; // 16:9 aspect ratio
	public static int scale = 3; // how much the game will be scaled up to(pixels).
	private Thread thread; // process within a process. Multiple things simultaenously
	private boolean running = false;
	private Screen screen;
	private JFrame frame;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale); // Sets
		setPreferredSize(size);
		screen = new Screen(width, height); //sets the screen to the width and the height.
		frame = new JFrame(); //Creates a new frame for the window.
	}

	public synchronized void start() { // Preventing thread interferences
		running = true; //Sets the running of the game to true.
		thread = new Thread(this, "Display"); // visible to multiple threads. First runs program enables no overlap of threads.
		thread.start(); //Starts the thread.
	}
	public synchronized void stop() { // stops the thread.
		running = false; //Sets the game running boolean to false.
		try {
			thread.join(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (running) {
			tick();    // or update 60 times per second
			render();  // Unlimited
		}
	}
	public void tick() {

	}
	
	/**
	 * render is going to render the images. 
	 */
	public void render() {
		// Buffer = temporary storage place.
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);// speed improvements. Load more buffers.
			return;
		}//End if 
		screen.clear();
		screen.render();
		
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		Graphics g = bs.getDrawGraphics();// creates a link between graphics, and buffer
		//g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image,  0,  0,  getWidth(),  getHeight(),  null);
		g.dispose();
		bs.show();
	} //Ending render

	/**
	 * 
	 * @param args 
	 * Start the main method.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Rain");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.start();
	}//Ending main
}