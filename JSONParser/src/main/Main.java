package main;

import java.util.ArrayList;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		String json = "[{\"Position\": \"1\", \"Team Team\": \"Wycombe Wanderers\", \"Pl Played\": {\"Name\": \"Anderson\", \"Age\": 23, \"Job\": \"Footballer\"}, \"W Won\": [12, 34, [56, [100, \"Hello\"], 128, 45], 567, [23, 14]], \"D Drawn\": \"4\", \"L Lost\": 2, \"F Goals For\": \"43\", \"A Goals Against\": [\"Michael\", \"Frank\", \"Mahmut\"], \"GD Goal Difference\": \"+21\", \"Pts Points\": \"43\"}]";
		
		Map<String, Object> map = JSONFile.parseJson(json, null);
		
		// example of getting a single string value
		
		System.out.println(map.get("Team Team"));
		
		// example of getting an Integer value
		
		int number1 = Integer.parseInt(map.get("Position").toString());
		int number2 = Integer.parseInt(map.get("L Lost").toString());
		
		System.out.println(number1 + number2); // 1 + 19
		
		// example of get ArrayList usage (for getting Hello element)
		
		ArrayList<?> list = (ArrayList<?>)map.get("W Won");
		
		ArrayList<?> secondlist = (ArrayList<?>)list.get(2);
		ArrayList<?> thirdlist = (ArrayList<?>)secondlist.get(1);
		
		System.out.println(thirdlist.get(1));
		
		// or
		
		System.out.println(((ArrayList<?>)((ArrayList<?>)list.get(2)).get(1)).get(1));
		
		
		// example of getting map
		
		Map<?, ?> map1 = (Map<?, ?>)map.get("Pl Played");
		System.out.println(map1.get("Job"));
	}
}
