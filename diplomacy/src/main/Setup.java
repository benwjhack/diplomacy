package main;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import game.Game;
import game.Init;
import game.Player;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Setup {
	
	public static Setup mthis;
	public static Game game;
	public static int WIDTH = 1000;
	public static int HEIGHT = 1000;
	public static UnicodeFont FONT, FONT2, FONT3, FONT4;
	public static int RWIDTH, RHEIGHT, twidth, theight, STATE = 0;
	public static boolean run;
	
	public Texture black;
	public Texture purple;
	public Texture[] images;
	
	public static int chosen = -1, players;
	public static String drawText = "";
	public static Integer[][][] choices;
	public static String[] countries;// = new String[]{"England", "Germany", "Russia", "Turkey", "Austria", "Italy", "France"};
	public String[][] list;
	public boolean[] colours;
	public boolean[] colours2;
	public int[][] text, text2;
	public String[] texts, texts2;
	
	public int mousex, mousey, translate_x, translate_y;
	
	public Setup(){
		run = true;
	}
	
	@SuppressWarnings("unchecked")
	public void init_game(int[] settings){
		mthis = this;
		texts2 = new String[]{"Back", "Next", "Players:"};
		text2 = new int[][]{{0, HEIGHT - Main.FONT.getHeight("Back")}, {WIDTH - FONT.getWidth("Next"),  HEIGHT - Main.FONT.getHeight("Back")}, {0, 0}};
        colours2 = new boolean[text2.length];
		
		theight = HEIGHT / 10;
		twidth = theight;
		images = new Texture[1];
		
		try {
			black = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/black.png")));
			purple = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/purple.png")));
			images[0] = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/map.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Scanner scan = IOHandle.getText("info/setup/countries.txt");
		ArrayList<String> temp = new ArrayList<String>();
		while(scan.hasNext()){
			temp.add(scan.next());
		}
		countries = new String[temp.size()];
		for(int i = 0; i != temp.size(); i++){
			countries[i] = temp.get(i);
		}
		
		ArrayList<Integer[][]> choices = new ArrayList<Integer[][]>();
		
		Document doc = IOHandle.readXML("info/setup/setup.xml");
		NodeList nodes = doc.getDocumentElement().getChildNodes();
	    for(int i = 0; i != nodes.getLength(); i++){
	    	Node node = nodes.item(i);
	    	NodeList nodes2 = node.getChildNodes();
	    	ArrayList<Integer[]> choice = new ArrayList<Integer[]>();
	    	for(int i2 = 0; i2 != nodes2.getLength(); i2++){
	    		Node node2 = nodes2.item(i2);
	    		if(node2 == null){continue;}
	    		Node node3 = node2.getAttributes().getNamedItem("countries");
	    		String[] temp2 = node3.getTextContent().split(" ");
	    		choice.add(new Integer[temp2.length]);
	    		for(int i3 = 0; i3 != temp2.length; i3++){
	    			choice.get(i2)[i3] = Integer.parseInt(temp2[i3]);
	    		}
	    	}
	    	choices.add(new Integer[choice.size()][]);
	    	for(int i2 = 0; i2 != choice.size(); i2++){
	    		choices.get(i)[i2] = choice.get(i2);
	    	}
		}
	    
	    Setup.choices = new Integer[choices.size()][][];
	    for(int i = 0; i != choices.size(); i++){
	    	Setup.choices[i] = choices.get(i);
	    }
	    
	    list = new String[Setup.choices.length][];
	    for(int i = 0; i != Setup.choices.length; i++){
	    	list[i] = new String[Setup.choices[i].length];
	    	for(int i2 = 0; i2 != Setup.choices[i].length; i2++){
	    		list[i][i2] = "";
	    		for(int i3 = 0; i3 != Setup.choices[i][i2].length; i3++){
	    			list[i][i2] += countries[Setup.choices[i][i2][i3]]+(i3==Setup.choices[i][i2].length-1?"":"/");
	    		}
	    	}
	    }
		
        setupStrings(list[0].clone());
        players = list[0].length;
		
        int size = 30;
        
        Font font = new Font(Main.FONT_TYPE, Font.BOLD, size);
	    FONT3 = new UnicodeFont(font);
	    FONT3.addAsciiGlyphs();
	    FONT3.addGlyphs(400, 600);
	    FONT3.getEffects().add(new ColorEffect(java.awt.Color.white));
	    try {
	        FONT3.loadGlyphs();
	    } catch (SlickException e) {
		    System.out.println("something went wrong here!");
		    e.printStackTrace();
		    Display.destroy();
	    }
	    FONT4 = new UnicodeFont(font);
	    FONT4.addAsciiGlyphs();
	    FONT4.addGlyphs(400, 600);
	    FONT4.getEffects().add(new ColorEffect(java.awt.Color.blue));
	    try {
	        FONT4.loadGlyphs();
	    } catch (SlickException e) {
		    System.out.println("something went wrong here!");
		    e.printStackTrace();
		    Display.destroy();
	    }
        
	}
	
	public void setupStrings(String[] newa){
		
		texts = newa;
		if(chosen == -1){
			chosen = 0;
			texts[0]+=" -";
		}
        text = new int[texts.length][];
        colours = new boolean[text.length];
		
		for(int i = 0; i != text.length; i++){
        	text[i] = new int[]{(int) (WIDTH / 2.3 - Main.FONT.getWidth(texts[i]) / 2), HEIGHT / (text.length + 1) * (i + 1) - Main.FONT.getHeight(texts[i])};
        }
	}
	
	public void init(boolean initi){
		int[] settings = IOHandle.getSettings();
		if(initi){
			System.out.println("THIS SHOULD NEVER HAPPEN 123");
		} else {
			RWIDTH = Main.RWIDTH;
			RHEIGHT = Main.RHEIGHT;
		}
		init_game(settings);
	}
	
	public void run(boolean initi) {
		
		FONT = Main.FONT;
		FONT2 = Main.FONT2;
		
		WIDTH = Main.WIDTH;
		HEIGHT = Main.HEIGHT;
		
		init(initi);
		
		while (run) {
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			glPushMatrix();
			
			glTranslatef(translate_x, translate_y, 0);
			
			if (Display.wasResized()) {
	            RWIDTH = Display.getWidth();
	            RHEIGHT = Display.getHeight();

	            GL11.glViewport(0, 0, RWIDTH, RHEIGHT);
	            GL11.glLoadIdentity();
			}
			
			// Retrieve the "true" coordinates of the mouse.
			mousex = (int) (Mouse.getX() * ((float)(WIDTH) / RWIDTH) - translate_x);
			mousey = (int) (HEIGHT - Mouse.getY() * ((float)(HEIGHT) / RHEIGHT) - 1 - translate_y);
			Draw.renderthistex(new Rectangle(0,0,WIDTH,HEIGHT), purple);
			
			logic();
			if(!run){
				break;
			}
			render();
			update();
			
			if(Display.isCloseRequested()){
				run = false;
				Main.run = false;
				Game.run = false;
				//Main.mthis.end();
			}

			glPopMatrix();
			Display.update();
			Display.sync(60);
		}
		
		Main.RWIDTH = RWIDTH;
		Main.RHEIGHT = RHEIGHT;
		
	}
	
	public void logic(){
		
		if(STATE == 3){
			game.run();
		}
		
		Mouse.poll();
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				int eventKey = Mouse.getEventButton();
				switch(eventKey){
				case 0:
					outLoop: for(int i = 0; i != text.length; i++){
						if(mousex >= text[i][0] && mousex <= text[i][0] + Main.FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + Main.FONT.getHeight(texts[i])){
							if(Main.unlocked < 5-i){
								break outLoop;
							}
							chosen = i;
							String[] temp = new String[players];
							for(int i2=0;i2!=players;i2++){
								temp[i2] = list[list[0].length-players][i2]+(i2==chosen?" -":"");
							}
							setupStrings(temp);
						}
					}
					for(int i = 0; i != text2.length; i++){
						if(mousex >= text2[i][0] && mousex <= text2[i][0] + Main.FONT.getWidth(texts2[i]+(i==2?" "+players:"")) && mousey >= text2[i][1] && mousey <= text2[i][1] + Main.FONT.getHeight(texts2[i])){
							switch(i){
							case 0:
								run = false;
								break;
							case 1:
								game = new Game(chosen, 0);
								STATE = 1;
								drawText = "Generating level...";
								(new Init()).start();
								break;
							case 2:
								players--;
								if(players == 1){
									players = list[0].length;
								}
								if(chosen>=players){
									chosen = 0;
								}
								System.out.println(chosen+" "+players);
								String[] temp = new String[players];
								for(int i2=0;i2!=players;i2++){
									temp[i2] = list[list[0].length-players][i2]+(i2==chosen?" -":"");
								}
								setupStrings(temp);
							}
						}
					}
					break;
				case 1:
					for(int i = 0; i != text.length; i++){
						if(mousex >= text[i][0] && mousex <= text[i][0] + Main.FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + Main.FONT.getHeight(texts[i])){
							Player.tactics[i]++;
							Player.tactics[i]%=3;
						}
					}
					break;
				default:
					//System.out.println(eventKey);
					break;
				}
			}
		}
		
		Keyboard.poll();
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				int eventKey = Keyboard.getEventKey();
				switch(eventKey){
				case KEY_ESCAPE:
					run = false;
					break;
				default:
					break;
				}
			}
		}

		for(int i = 0; i != text.length; i++){
			if(mousex >= text[i][0] && mousex <= text[i][0] + FONT.getWidth(texts[i]) && mousey >= text[i][1] && mousey <= text[i][1] + FONT.getHeight(texts[i])){
				colours[i] = true;
			} else {
				colours[i] = false;
			}
		}
		

		for(int i = 0; i != text2.length; i++){
			if(mousex >= text2[i][0] && mousex <= text2[i][0] + FONT.getWidth(texts2[i]+(i==2?" "+players:"")) && mousey >= text2[i][1] && mousey <= text2[i][1] + FONT.getHeight(texts2[i])){
				colours2[i] = true;
			} else {
				colours2[i] = false;
			}
		}
	}
	
	public void render(){
		
		if(STATE != 0){
			FONT.drawString(WIDTH / 2 - FONT.getWidth(drawText) / 2, HEIGHT / 2 - FONT.getHeight("I") / 2, drawText);
			if(STATE == 2){
				Init.init2();
			}
			return;
		}
		
		for(int i = 0; i != text.length; i++){
			if(colours[i]){
				FONT2.drawString(text[i][0], text[i][1], texts[i]);
				continue;
			}
			FONT.drawString(text[i][0], text[i][1], texts[i]);
		}
		
		for(int i = 0; i != text2.length; i++){
			if(colours2[i]){
				if(i == 2){
					FONT2.drawString(text2[i][0], text2[i][1], texts2[i]+" "+players);
					continue;
				}
				FONT2.drawString(text2[i][0], text2[i][1], texts2[i]);
				continue;
			}
			if(i==2){
				FONT.drawString(text2[i][0], text2[i][1], texts2[i]+" "+players);
				continue;
			}
			FONT.drawString(text2[i][0], text2[i][1], texts2[i]);
		}
		
	}
	
	public void update(){
		
	}
	
	public void end(){
		
	}
	
}
