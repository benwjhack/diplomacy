package game;

import java.awt.Font;
import java.io.*;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.Draw;
import main.IOHandle;
import main.Setup;

public class Init extends Thread{
	
	public void run(){
		init();
	}
	
	public void init(){
		
		/*try {
			String[] args = new String[]{"cmd.exe","/C","call","scripts/run.bat"};
			Process p = Runtime.getRuntime().exec(args);
			StreamGobbler a = new StreamGobbler(p.getInputStream(), "input");
			StreamGobbler b = new StreamGobbler(p.getErrorStream(), "error");
			a.start();
			b.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int[][] level = IOHandle.loadLevel("world.txt");
		Game.level = level;*/
		
		Setup.STATE = 2;
		Setup.drawText = "Setting up game...";
		
	}
	
	@SuppressWarnings("unchecked")
	public static void init2(){
		
		Territory.players = Setup.players;
		Territory.names = new String[Territory.players];
		for(int i = 0; i != Territory.players; i++){
			Territory.names[i] = Setup.mthis.list[Setup.mthis.list[0].length-Setup.players][i];
		}
		float[][] colours = new float[][]{{1f, 1f, 0f}, {0f, 1f, 0f}, {0f, 0f, 1f}, {1f, 0f, 0f}, {0f, 0f, 0f}, {0.5f, 0f, 0f}, {0f, 0.5f, 0f}, {0f, 0f, 0.5f}, {0f, 1f, 1f}, {1f, 0f, 1f}, {0.5f, 0.5f, 0f}};
		Territory.colours = new float[Territory.players][];
		for(int i = 0; i != Territory.players; i++){
			Territory.colours[i] = colours[i];
		}
		
		Texture[] images = new Texture[1];
		
		try {
			images[0] = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/map.png")));
			//images[1] = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/city.png")));
			//images[2] = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/capital.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Game.mthis.images = images;
		
		Game.FONTS = new UnicodeFont[4];
		Game.FONTS[0] = Setup.FONT;
		Game.FONTS[1] = Setup.FONT2;
		
		Font font = new Font(main.Main.FONT_TYPE, Font.BOLD, 30);
	    UnicodeFont FONT = new UnicodeFont(font);
	    FONT.addAsciiGlyphs();
	    FONT.addGlyphs(400, 600);
	    FONT.getEffects().add(new ColorEffect(java.awt.Color.white));
	    try {
	        FONT.loadGlyphs();
	    } catch (SlickException e) {
		    e.printStackTrace();
		    Display.destroy();
	    }
	    Game.FONTS[2] = FONT;
	    FONT = new UnicodeFont(font);
	    FONT.addAsciiGlyphs();
	    FONT.addGlyphs(400, 600);
	    FONT.getEffects().add(new ColorEffect(java.awt.Color.blue));
	    try {
	        FONT.loadGlyphs();
	    } catch (SlickException e) {
		    e.printStackTrace();
		    Display.destroy();
	    }
	    Game.FONTS[3] = FONT;
	    
	    Game.mthis.players = new Player[Territory.players];
	    for(int i = 0; i != Territory.players; i++){
	    	Game.mthis.players[i] = new Player(i);
	    }
	    Game.mthis.players[Game.player].player = true;
	    
	    Document doc = IOHandle.readXML("info/setup/countries.xml");
	    Node origNode = doc.getDocumentElement();
	    Country[] countries = new Country[Integer.parseInt(origNode.getAttributes().item(0).getTextContent())];
	    
	    for(int i = 0; i != origNode.getChildNodes().getLength(); i++){
	    	ArrayList<Territory> terrs = new ArrayList<Territory>();
	    	for(int i2 = 0; i2 != origNode.getChildNodes().item(i).getChildNodes().getLength(); i2++){
	    		Node node = origNode.getChildNodes().item(i).getChildNodes().item(i2);
	    		String[] string = node.getAttributes().getNamedItem("pos").getTextContent().split(" ");
	    		float x = Float.parseFloat(string[0]), y = Float.parseFloat(string[1]);
	    		terrs.add(new Territory(node.getAttributes().item(0).getTextContent(), x, y, i));
	    	}
	    	Territory[] terrs2 = new Territory[terrs.size()];
	    	for(int i2 = 0; i2 != terrs.size(); i2++){
	    		terrs2[i2] = terrs.get(i2);
	    	}
	    	countries[i] = new Country(origNode.getChildNodes().item(i).getAttributes().getNamedItem("name").getTextContent(), i, terrs2);
	    	if(origNode.getChildNodes().item(i).getAttributes().getNamedItem("uncontrolled")!=null){
	    		countries[i].uncontrolled = true;
	    	}
	    	if(origNode.getChildNodes().item(i).getAttributes().getNamedItem("colour")!=null){
	    		String[] string = origNode.getChildNodes().item(i).getAttributes().getNamedItem("colour").getTextContent().split(" ");
	    		Territory.colours[i] = new float[]{Integer.parseInt(string[0]), Integer.parseInt(string[1]), Integer.parseInt(string[2])};
	    	}
	    }
	    
	    Game.mthis.countries = countries;
	    
	    /*Country[] continents = new Country[6];
	    Document doc = IOHandle.readXML("info/setup/countries.xml");
		NodeList nodes = doc.getDocumentElement().getChildNodes();
	    for(int i = 0; i != nodes.getLength(); i++){
	    	Node node = nodes.item(i);
	    	Territory[] countries = new Territory[node.getChildNodes().getLength()];
			for(int i2 = 0; i2 != node.getChildNodes().getLength(); i2++){
				Node cnode = node.getChildNodes().item(i2);
				String[] pos = cnode.getAttributes().getNamedItem("pos").getTextContent().split(" ");
				Territory country = new Territory(cnode.getAttributes().getNamedItem("name").getTextContent(), Float.parseFloat(pos[0]), Float.parseFloat(pos[1]), i);
				countries[i2] = country;
			}
			Country continent = new Country(node.getAttributes().getNamedItem("name").getTextContent(), Integer.parseInt(node.getAttributes().getNamedItem("bonus").getTextContent()), countries);
			continents[i] = continent;
		}
	    
	    doc = IOHandle.readXML("info/setup/connections.xml");
		nodes = doc.getDocumentElement().getChildNodes();
		ArrayList<Integer>[] nexts = new ArrayList[Country.overall.size()];
		for(int i = 0; i != nexts.length; i++){
			nexts[i] = new ArrayList<Integer>();
		}
	    for(int i = 0; i != nodes.getLength(); i++){
	    	Node node = nodes.item(i);
	    	int one = Integer.parseInt(node.getAttributes().getNamedItem("one").getTextContent()) - 1;
	    	int two = Integer.parseInt(node.getAttributes().getNamedItem("two").getTextContent()) - 1;
    		//System.out.println("Connecting "+one+" and "+two);
	    	nexts[one].add(two);
	    	nexts[two].add(one);
		}
	    
	    for(int i = 0; i != nexts.length; i++){
	    	ArrayList<Integer> array = nexts[i];
    		Country.overall.get(i).nextTo = new int[array.size()];
	    	for(int i2 = 0; i2 != array.size(); i2++){
	    		//System.out.println("Connecting "+i+" and "+array.get(i2));
	    		Country.overall.get(i).nextTo[i2] = array.get(i2);
	    	}
	    }
	    
	    doc = IOHandle.readXML("info/continents/setup.xml");
		nodes = doc.getDocumentElement().getChildNodes().item(Setup.players-3).getChildNodes();
	    for(int i = 0; i != nodes.getLength(); i++){
	    	NodeList nodes2 = nodes.item(i).getChildNodes();
	    	for(int i2 = 0; i2 != nodes2.getLength(); i2++){
	    		Node node = nodes2.item(i2);
	    		int id = Integer.parseInt(node.getAttributes().getNamedItem("id").getTextContent())-1;
	    		int num = Integer.parseInt(node.getAttributes().getNamedItem("num").getTextContent());
	    		boolean city = node.getAttributes().getNamedItem("city")!=null;
	    		boolean capital = node.getAttributes().getNamedItem("capital")!=null;
	    		Country.overall.get(id).army = num;
	    		Country.overall.get(id).city = city;
	    		Country.overall.get(id).owner = i;
	    		Country.overall.get(id).origOwner = i;
	    		Country.overall.get(id).capital = capital;
	    		Country.overall.get(id).done = true;
	    		Game.mthis.players[i].countries.add(Country.overall.get(id));
	    	}
		}
	    
	    Game.mthis.continents = continents;
	    */
	    int sb = 2;
	    Game.mthis.buttons = new Button[sb];
		Game.mthis.buttons[0] = new Button(Setup.WIDTH-Game.FONTS[2].getWidth("Menu"), 0, "Menu", 1, true);
		Game.mthis.buttons[1] = new Button(Setup.WIDTH-Game.FONTS[2].getWidth("Advance stage"), (int)(Game.FONTS[2].getHeight("I")*2), "Advance stage", 1, true);
	    
		Setup.STATE = 3;
		Setup.drawText = "Done!";
	}
	
	public static void drawTop(){
		
		Draw.renderthiso(new Rectangle(Game.mthis.translate_x,Game.mthis.translate_y,Game.WIDTH,100), 1f, 1f, 1f, 0.5f);
		Draw.drawSquare(Game.mthis.translate_x, Game.mthis.translate_y, Game.WIDTH, 100);
		
		int height = 0;
		int width = 0;
		String string = "Go : "+Territory.names[Game.go];
		Game.FONTS[2].drawString(Game.mthis.translate_x, Game.mthis.translate_y + height, string);
		height += Game.FONTS[2].getHeight(string);
		width += Game.FONTS[2].getWidth(string);
		string = "Attack dice : "+Game.settings[1];
		Game.FONTS[2].drawString(Game.mthis.translate_x, Game.mthis.translate_y + height, string);
		height += Game.FONTS[2].getHeight(string);
		if(Game.FONTS[2].getWidth(string) > width){
			width = Game.FONTS[2].getWidth(string);
		}
		string = "Defend dice : "+Game.settings[2];
		Game.FONTS[2].drawString(Game.mthis.translate_x, Game.mthis.translate_y + height, string);
		if(Game.FONTS[2].getWidth(string) > width){
			width = Game.FONTS[2].getWidth(string);
		}
		
		width += Game.FONTS[2].getWidth("    ");
		String[] adds = new String[]{"Draft", "Attack", "Manoveur"};
		string = "Stage : "+adds[Game.stage];
		Game.FONTS[2].drawString(Game.mthis.translate_x + width, Game.mthis.translate_y, string);
		
	}
	
}

class StreamGobbler extends Thread {
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }
    
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                System.out.println(type + ">" + line);
                if(line.equals("DONE")){
                	Setup.drawText = "Parsing level...";
                }
            }
        } catch (IOException ioe){
            ioe.printStackTrace();  
        }
    }
}