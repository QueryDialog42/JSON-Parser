package main;

import java.util.ArrayList;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		String json = "{\"Driver\": {\"Name\": \"Anderson\", \"Age\": 23, \"Job\": \"Teacher\"}, \"Car\": {\"Model\": \"Audi\", \"Years\": [2020, 2021, 2022]}, \"Class Members ID\": [11, 5, 34, 367, [45, 13, [67, 46, 44]], 128, [13, 14, 15, 16]], \"Manager\": \"Maria\", \"Education Season\": \"Summer\", \"Education Time\": \"6\", \"Countries\": [\"Istanbul\", \"Ankara\"], \"Times\": [\"12.00\", \"18.00\"]}";

		Map<String, Object> map = JSONFile.parseJson(json);
		
		// example of getting map
		
		Map<?, ?> map1 = (Map<?, ?>)map.get("Driver");
		System.out.println(map1.get("Age"));
		
		Map<?, ?> map2 = (Map<?, ?>)map.get("Car");
		ArrayList<?> caryears = (ArrayList<?>)map2.get("Years");
		System.out.println(caryears.get(2));
		
		// all class member ID
		
		ArrayList<?> IDs = (ArrayList<?>)map.get("Class Members ID");
		printAllItems(IDs);
		System.out.println();
		
		// example of getting only a special key's value, others will not be processed
		
		Map<String, Object> map3 = JSONFile.parseJson(json, "Manager");
		System.out.println(map3.get("Manager"));
		
		Map<String, Object> map4 = JSONFile.parseJson(json, "Car");
		Map<?, ?> map5 = (Map<?, ?>)map4.get("Car");
		System.out.println(map5.get("Model"));
		
		Map<String, Object> map6 = JSONFile.parseJson(json, "Education Time");
		System.out.println(map6.get("Education Time"));
		
		int number1 = Integer.parseInt((String)IDs.get(2));
		int number2 = Integer.parseInt((String)IDs.get(3));
		System.out.println(number1 + number2);
		
		ArrayList<?> list = (ArrayList<?>)map.get("Countries");
		System.out.println(list.get(1));
		
		ArrayList<?> list1 = (ArrayList<?>)map.get("Times");
		System.out.println(list1.get(0));
	}
	
	public static void printAllItems(ArrayList<?> list) {
		for (Object i : list) {
			if (i instanceof ArrayList<?>) {
				printAllItems((ArrayList<?>)i);
			}
			else System.out.print(i + " ");
		}
	}
}
