package com.Martin.Snake;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Snake extends JPanel implements ActionListener, KeyListener {
	
	/**
	 *@author: Daniel Martin
	 */
	private static final long serialVersionUID = 1L;
	// the scale of the rows and columns of the screen (i.e. scale x scale boxes)
	private static final int SCALE = 20;
	//Get screen size of client
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	//width of screen
	private final static int WIDTH = 500;
	//height of screen
	private final static int HEIGHT = 500;
	public static int ax = 0; //Initial acceleration of the snake in x
	public static int ay = SCALE; //initial acceleration of the snake in y
	public static int ref_rate = 155; // refresh rate
	public static JFrame f;
	Timer time = new Timer(ref_rate, this); //
	public  int apple_x = 0; // X axis for Apple
	public  int apple_y = 0; // Y Axis for apple
	public  int body = 3;  //initial length of body
	public  boolean inGame = true;
	public static int score = 0;
	public static int high_score = score;
	///Number of columns in the screen
	int cols = (int) Math.floor(WIDTH/SCALE);
	//Number of rows in the screen
	int rows = (int) Math.floor(HEIGHT/SCALE);
	private  int x[] = new int[rows];
	private  int y[] = new int[cols];
	public Snake() { // Setup of the game
		this.requestFocusInWindow();
		this.setFocusable(true);		
		y[0] = SCALE * body;
		spawnApple();
		repaint();
		addKeyListener(this);
		setBackground(Color.CYAN);
		time.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		DoDraw(g);
	}
	public void DoDraw(Graphics g) {
		if (inGame) {
			g.setColor(Color.red);
			g.fillRect(apple_x, apple_y,SCALE,SCALE);
			//Draw Snake Head and Body
			for (int i = 0; i < body; i++) {
				//draws head
				if (i == 0) { 
					g.setColor(Color.yellow);
					g.fillRect(x[i], y[i],SCALE,SCALE);
				//draws body
				} else if (i >= 1) { 
					//Move body in increasing order
					g.setColor(Color.black);
					g.fillRect(x[i], y[i],SCALE,SCALE);
				}
			}
		} else {
			time.stop();
			gameOver();
		}
	}
	public void actionPerformed(ActionEvent e) {
		moveSnake();
		//Checks if the player eats apple
		if ((x[0] == apple_x && y[0] == apple_y)){
			body = body + 1;
			//places apple randomly on the screen
			spawnApple();
			score += 500;
		}
		//If player goes off screen
			if ( x[0] < 0 ) 
				inGame = false;
			if (x[0] > WIDTH - SCALE) 
				inGame = false;
			if (y[0] < 0) 
				inGame = false;
			if (y[0] > HEIGHT + SCALE) 
				inGame = false;
		//If head bumps into body
		for (int i = body; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) 
				inGame = false;
		}
		//built-in call to the graphics part.
		repaint();
	}
	/**
	 * 
	 */
	public void moveSnake() {
		//Moves the body
		for (int i = body; i > 0; i--) {
				x[i] = x[(i - 1)];
				y[i] = y[(i - 1)];
		}
		//Moves the head
		x[0] += ax;
		y[0] += ay;
	}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	/**Generates new apple
	 * 
	 */
	public void spawnApple() {
		
		apple_x = (int)Math.round(Math.random()*cols) * SCALE;
		apple_y = (int)Math.floor(Math.random()*rows) * SCALE;
	}
	public static void main(String[] args) {
		f = new JFrame();
		f.setTitle("SNAKE");
		f.setSize(WIDTH,HEIGHT);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.setLocation(dim.width / 2 - f.getWidth() / 2, dim.height / 2 - f.getHeight() / 2);
		JLabel Title = new JLabel("SNAKE");
		Title.setFont(new Font("Algerian", Font.BOLD, 24));
		Title.setForeground(Color.GREEN);
		Title.setBounds(600, 370, 155, 155);
		f.add(Title);
		Snake p = new Snake();
		f.add(p);
		f.setUndecorated(true);
		f.setVisible(true);
	}
	/**Takes keyboard input from player
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int Key = e.getKeyCode(); //Tracks the keycode of the key the user presses 
		//Move head one 'box' to the left
		if (Key == KeyEvent.VK_LEFT) {
			ax = -SCALE;  //Sets the speed of the snake to move one 'box'
			ay = 0;
		}
		//Move head one 'box' to the right
		if (Key == KeyEvent.VK_RIGHT){
			ax = SCALE;
			ay = 0;
		}//Move head one 'box' down
		if (Key == KeyEvent.VK_DOWN){
			ay = SCALE;
			ax = 0;
		}//Move head one 'box' up
		if (Key == KeyEvent.VK_UP) {
			ay = -SCALE;
			ax = 0;
		}
	}
	public void gameOver() {
		if (score > high_score)
			high_score = score;
		ax = 0;
		ay = 0;
		Object opt[] = { "Try again", "No thanks" };
		Object OPTIONS = JOptionPane.showOptionDialog(f, "Loser!" + "\nScore: " + score + "\nApples eaten: " + score/500+
				"\nHigh score "+high_score
				,"Replay", JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, opt, opt[0]);
		if (OPTIONS.equals(0)) {
			reset();
		} else {
			f.dispose();
			System.exit(0);
		}
	}
	/**Resets the board
	 */
	public void reset() {
		x[0] = (int) Math.round((Math.random()*WIDTH))*SCALE;
		y[0] = 100;
		ax = SCALE;
		ay = 0;
		body = 3;
		ref_rate = 150;
		score = 0;
		inGame = true;
		f.removeAll();
		f.dispose();
		main(null);
		time.setDelay(ref_rate);
	}
}
