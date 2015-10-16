package game;

import java.util.ArrayList;

public class Player {
	
	public static int[] tactics;
	
	public String name;
	public boolean player = false;
	public int id, draft = 0, strategy;
	public ArrayList<Territory> countries = new ArrayList<Territory>();
	
	public Player(int id){
		this.id = id;
		strategy = id;
		name = Territory.names[id];
		strategy = tactics[id];
	}
	
	public Player(Player player, ArrayList<Territory> countries2){
		this.id = player.id;
		this.name = player.name;
		this.draft = player.draft;
		this.strategy = player.strategy;
		this.countries = new ArrayList<Territory>();
		for(int i = 0; i != player.countries.size(); i++){
			countries.add(countries2.get(player.countries.get(i).id));
		}
	}
	
	public Player clone(ArrayList<Territory> countries2){
		return new Player(this, countries2);
	}
	
	public void update(){
		
	}
	
	public void tactic0(){
		
	}
	
}
