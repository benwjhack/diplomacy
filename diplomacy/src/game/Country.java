package game;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Country {
	
	public static ArrayList<Territory> overall = new ArrayList<Territory>();
	public static float cwidth = 0.04f, cheight = 0.04f;
	
	public String name;
	public Territory[] territories;
	
	public Country(String name, int bonus, Territory[] countries){
		this.name = name;
		this.territories = countries;
		for(Territory country: countries){
			overall.add(country);
		}
	}
	
	public Country(Country continent){
		this.name = continent.name;
		this.territories = new Territory[continent.territories.length];
		for(int i = 0; i != territories.length; i++){
			territories[i] = continent.territories[i].clone();
		}
	}
	
	public Country clone(){
		return new Country(this);
	}
	
	public static int[] sort(int[] array){
		int[] answer = new int[array.length];
		int[] used = new int[array.length];
		int count = 0;
		for(int i = 0; i != array.length; i++){used[i] = -1;}
		while(count != array.length){
			int biggest = -1;
			for(int i = 0; i != array.length; i++){
				if(used[i]==-1 && (biggest == -1 || array[i] > array[biggest])){
					biggest = i;
				}
			}
			used[biggest] = 1;
			answer[count] = array[biggest];
			count++;
		}
		return answer;
	}
	
	public static float[] sort(float[] array){
		float[] answer = new float[array.length];
		int[] used = new int[array.length];
		int count = 0;
		for(int i = 0; i != array.length; i++){used[i] = -1;}
		while(count != array.length){
			int biggest = -1;
			for(int i = 0; i != array.length; i++){
				if(used[i]==-1 && (biggest == -1 || array[i] > array[biggest])){
					biggest = i;
				}
			}
			used[biggest] = 1;
			answer[count] = array[biggest];
			count++;
		}
		return answer;
	}
	
	public static boolean joined(Territory country1, Territory country2){
		ArrayBlockingQueue<Territory> queue = new ArrayBlockingQueue<Territory>(1024);
		ArrayList<Territory> countries = new ArrayList<Territory>();
		queue.add(country1);
		while(!queue.isEmpty()){
			Territory country = queue.remove();
			countries.add(country);
			for(int i: country.nextTo){
				if(country2.id == i){
					return true;
				}
				if(!countries.contains(overall.get(i)) && overall.get(i).owner == country1.owner){
					queue.add(overall.get(i));
				}
			}
		}
		return false;
	}
	
}
