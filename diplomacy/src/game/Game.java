package game;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.input.Mouse.isButtonDown;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Random;

import main.Draw;
import main.Main;
import main.Setup;
import main.Settings;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

public class Game {

	public static Random random = new Random();
	public static int[] settings;
	public static Game mthis;
	public static int WIDTH, HEIGHT, RWIDTH, RHEIGHT, iHEIGHT, iWIDTH, player;
	public static int twidth, theight, go, stage;
	public static UnicodeFont FONT, FONT2;
	public static boolean run;
	public static UnicodeFont[] FONTS;
	
	public Texture[] images;
	
	public String message = "";
	public int[] selected = new int[]{-1, 0};
	public Button[] buttons;
	public Player[] players;
	public Country[] continents;
	public int mousex, mousey, translate_x = 0, translate_y = 0, mtime = 0;
	
	public Game(int country, int number){
		mthis = this;
		player = country;
		run = true;
	}
	
	public void init(){
		
		FONT = Main.FONT;
		FONT2 = Main.FONT2;
		
		WIDTH = Main.WIDTH;
		HEIGHT = Main.HEIGHT;
		
		RWIDTH = Main.RWIDTH;
		RHEIGHT = Main.RHEIGHT;
		
		iWIDTH = WIDTH * 2;
		iHEIGHT = HEIGHT * 2;
		
		for(Territory country: Country.overall){
			country.position();
		}
		
		go = 0;
		stage = 0;
		players[player].update();
		
	}
	
	public boolean run(){
		mthis = this;
		init();
		
		while(run){
			
			// Retrieve the "true" coordinates of the mouse.
			mousex = (int) (Mouse.getX() * ((double)(WIDTH) / RWIDTH) - -translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((double)(HEIGHT) / RHEIGHT) - 1 - -translate_y);
			
			if(Display.isCloseRequested()){
				run = false;
				Setup.run = false;
				Main.run = false;
			}
			if(isKeyDown(KEY_ESCAPE)){
				run = false;
			}
			
			logic();
			draw();
			update();
			
			Display.sync(60);
			
		}
		
		Setup.RWIDTH = RWIDTH;
		Setup.RHEIGHT = RHEIGHT;
		
		return true;
	}
	
	// Top level logic
	public void logic(){
		
		Mouse.poll();
		while(Mouse.next()){
			if(player != go){
				break;
			}
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					System.out.println("<territory name=\"\" pos=\""+(((float)mousex)/iWIDTH)+" "+(((float)mousey)/iHEIGHT)+"\"/>");
					for(Button button: buttons){
						if(mousex > button.x && mousex < button.x + button.width && mousey > button.y && mousey < button.y + button.height){
							switch(button.id){
							case 0:
								(new Settings()).run(false);
								break;
							case 1:
								// Go changing
								break;
							}
						}
					}
					switch(stage){
					case 0:
						for(Territory country: players[player].countries){
							if(country.owner!=player){continue;}
							int width = FONTS[2].getWidth(country.name+": "+country.army);
							int height = FONTS[2].getHeight(country.name+": "+country.army);
							boolean trip = false;
							if(Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							}
							if(trip){
								// Drafting troops
							}
						}
						break;
					case 1:
						boolean trp = true;
						for(Territory country: Country.overall){
							int width = FONTS[2].getWidth(country.name+": "+country.army);
							int height = FONTS[2].getHeight(country.name+": "+country.army);
							boolean trip = false;
							if(Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
								trip = true;
							}
							if(trip){
								// Selecting countries
								trp = false;
								break;
							}
						}
						if(trp){
							selected[0] = -1;
						}
						break;
					}
					break;
				case 1:
					System.out.println("BREAK");
					break;
				default:
					break;
				}
			}
		}
		
		Keyboard.poll();
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				int eventKey = Keyboard.getEventKey();
				switch(eventKey){
				default:
					break;
				}
			}
		}
		
	}
	
	public void draw(){
		
		glClear(GL_COLOR_BUFFER_BIT);
		glPushMatrix();
		glTranslatef(-translate_x, -translate_y, 0);
		if (Display.wasResized()) {
            RWIDTH = Display.getWidth();
            RHEIGHT = Display.getHeight();
            GL11.glViewport(0, 0, RWIDTH, RHEIGHT);
            GL11.glLoadIdentity();
		}
		
		Draw.renderthistex(new Rectangle(0,0,iWIDTH,iHEIGHT), images[0]);
		
		boolean trip = false;
		/*for(Country cont: continents){
			for(Territory country: cont.territories){
				float[] colour = Territory.colours[country.owner];
				String string = country.name+": "+country.army;
				int width = FONTS[2].getWidth(string);
				int height = FONTS[2].getHeight(string);
				if(!trip && Math.abs(mousex - country.rpos[0]) < width / 2 && Math.abs(mousey - country.rpos[1]) < height / 2){
					trip = true;
					FONTS[2].drawString(country.rpos[0] - width / 2, country.rpos[1] - height / 2, string, new Color(255, 255, 255));
				} else {
					FONTS[2].drawString(country.rpos[0] - width / 2, country.rpos[1] - height / 2, string, new Color(colour[0], colour[1], colour[2]));
				}
			}
		}*/
		
		Init.drawTop();
		
		for(Button button: buttons){
			button.render();
		}
		
		if(mtime != 0){
			int trans = 255;
			if(mtime < 255){
				trans = mtime;
			}
			FONTS[2].drawString(translate_x, translate_y + HEIGHT - FONTS[2].getHeight("I"), message, new Color(255, 255, 255, trans));
			mtime--;
		}
		
		glPopMatrix();
		Display.update();
		
	}
	
	public void setMessage(String string){
		message = string;
		mtime = 500;
	}
	
	// Back end logic
	public void update(){
		
		// SCREEN
		
		if(isKeyDown(KEY_LEFT) || mousex - translate_x < WIDTH / 10 && mousey - translate_y > 100){
			translate_x -= 3;
		} else if(isKeyDown(KEY_RIGHT) || mousex - translate_x > WIDTH - WIDTH / 10 && mousey - translate_y > 100){
			translate_x += 3;
		}
		if(isKeyDown(KEY_DOWN) || mousey - translate_y - 100 > (HEIGHT - 100) - (HEIGHT - 100) / 10){
			translate_y += 3;
		} else if(isKeyDown(KEY_UP) || mousey - translate_y - 100 < (HEIGHT - 100) / 10 && mousey - translate_y > 100 && mousey - translate_y < 200){
			translate_y -= 3;
		}
		if(isKeyDown(KEY_SPACE)){
			translate_x = 0; translate_y = 0;
		}
		if(isKeyDown(KEY_Q)){
			iWIDTH*=1.01;
			iHEIGHT*=1.01;
			for(Territory country: Country.overall){
				country.position();
			}
		} else if(isKeyDown(KEY_A)){
			if(!(iWIDTH/1.01<WIDTH/1.2 || iHEIGHT/1.01<HEIGHT)){
				iWIDTH/=1.01;
				iHEIGHT/=1.01;
				for(Territory country: Country.overall){
					country.position();
				}
			}
		}
		if(translate_x < 0){
			translate_x = 0;
		} else if(translate_x + WIDTH > iWIDTH){
			translate_x = (int) (iWIDTH - WIDTH);
		}
		if(translate_y < 0){
			translate_y = 0;
		} else if(translate_y + HEIGHT > iHEIGHT){
			translate_y = iHEIGHT - HEIGHT;
		}
		
		// SCREEN
		
		// BUTTON
		
		for(Button button: buttons){
			button.update();
		}
		
		// BUTTON
		
		// PLAYERS
		
		if(go != player){
			players[go].update();
		}
		
		// PLAYERS
		
	}
	
}
