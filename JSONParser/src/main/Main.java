package main;

import java.util.ArrayList;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		String json = "[{\"Position\": \"1\", \"Team Team\": \"Wycombe Wanderers\", \"Pl Played\": {\"Name\": \"Anderson\", \"Age\": 23, \"Job\": \"Footballer\"}, \"W Won\": [12, 34, [56, [100, \"Hello\"], 128, 45], 567, [23, 14]], \"D Drawn\": \"4\", \"L Lost\": 2, \"F Goals For\": \"43\", \"A Goals Against\": [\"Michael\", \"Frank\", \"Mahmut\"], \"GD Goal Difference\": \"+21\", \"Pts Points\": {\"Name\": \"Wowowow\", \"The List\": [\"23\", 45, [56]]}, \"MyJob\": \"Teacher\"}]";
		
		Map<String, Object> map = JSONFile.parseJson(json, null);
		
		// example of getting a single string value
		
		System.out.println(map.get("Team Team"));
		System.out.println(map.get("MyJob"));
		
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
		
		Map<?, ?> map2 = (Map<?, ?>)map.get("Pts Points");
		System.out.println(map2.get("Name"));
		
		ArrayList<?> list2 = (ArrayList<?>)map2.get("The List");
		ArrayList<?> element56list = (ArrayList<?>)list2.get(2); 
		System.out.println(Integer.parseInt((String)element56list.get(0)) + Integer.parseInt((String)list2.get(1))); // 45 + 56
		
	}
}
