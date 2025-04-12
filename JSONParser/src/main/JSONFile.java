package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONFile {
	static private StringBuilder globalListValue;
	static private StringBuilder globalJsonValue;
	
	public static Map<String, Object> parseJson(String json, String onlykey) {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> pattern = new ArrayList<Object>();
		
		if (onlykey == null) return parseAllJsonFile(json, map, pattern);
		else return parseAllJsonFile(json, map, pattern);
		
	}
	
	private static Map<String, Object> parseAllJsonFile(String json, Map<String, Object> map, ArrayList<Object> pattern) {
		String[] key_valueKey = prepareJsonToParse(json);
		
		for (short i = 0; i < key_valueKey.length; i++) {
			String key_valueKey_index = key_valueKey[i].trim();
			
			if (i == 0 || i == key_valueKey.length - 1) pattern.add(key_valueKey_index.replace("\"", "")); // add the first key and the last value
			else if (key_valueKey_index.startsWith("\"")) splitStringAndKey(key_valueKey_index, pattern); // a string value
			else if (key_valueKey_index.startsWith("[")) splitListAndKey(key_valueKey_index, pattern); // a list value
			else if (key_valueKey_index.startsWith("{")) i += splitJsonAndKey(pattern);
			else splitStringAndKey(key_valueKey_index, pattern); // an integer value
		}

		return addToMapThePattern(map, pattern);
	}
	
	private static int splitJsonAndKey(ArrayList<Object> pattern) { // {"Name"
		String[] items = newJsonFound();
	
		pattern.add(parseJson(items[0], null));
		pattern.add(items[1].replace("\"", "").trim());
		
		return items[0].split(":").length - 1; // - 1 is for i++ 
	}
	
	private static Map<String, Object> addToMapThePattern(Map<String, Object> map, ArrayList<Object> pattern) {
		for (short i = 0; i < pattern.size(); i += 2) {
			map.put(pattern.get(i).toString(), pattern.get(i + 1)); // even numbers are keys, odd numbers are values
		}
		return map;
	}
	
	private static void splitStringAndKey(String item, ArrayList<Object> pattern) {
		String[] value_key = item.replace("\"", "").split(",");
		
		pattern.add(value_key[0].trim());
		pattern.add(value_key[1].trim());
	}
	
	private static void splitListAndKey(String item, ArrayList<Object> pattern) {
		int indexOfLastList = item.lastIndexOf("]"); // find the last ] in order to which commas should not be splitted
		int indexOfMiddleComma = item.indexOf(",", indexOfLastList); //find the first , in order to split as key - value
		
		String value = item.substring(0, indexOfMiddleComma); // the part before comma, comma not included (list)
		String key = item.substring(indexOfMiddleComma + 1); // the part after comma, comma not included by + 1 (key)
		
		globalListValue = new StringBuilder(delFirstAndLastChar(value));
		
		pattern.add(convertStringToArrayList(value));
		pattern.add(key.replace("\"", "").trim());
	}
	
	private static String[] prepareJsonToParse(String json) {
		int firstindex = json.indexOf("{");
		int lastindex = json.lastIndexOf("}");
		
		String preparedJson = json.substring(firstindex + 1, lastindex); // get rid of { } symbols and get the pattern of [key, value_key, value_key ... value]
		
		globalJsonValue = new StringBuilder(preparedJson);
		
		return preparedJson.split(":"); 
	}
	
	private static ArrayList<Object> convertStringToArrayList(String list) {
		ArrayList<Object> listValue = new ArrayList<Object>();

		String[] items = prepareStringToParse(list);
		
		for (short i = 0; i < items.length; i++) {
			String item = items[i].trim();
			
			switch(item.charAt(0)) {
				case '[': i += newListFounded(listValue); break;
				case '"': listValue.add(item.replace("\"", "")); break;
				default: listValue.add(item);
			}
		}

		return listValue;
	}
	
	private static int newListFounded(ArrayList<Object> listValue) {
		ArrayList<Object> newList = convertStringToArrayList(newStringArray());
		
		listValue.add(newList);
		
		return countEveryItem(newList, 0) - 1; // skip the list, - 1 is for i++ at last
	}
	
	private static String newStringArray() {
		int firstIndex = globalListValue.indexOf("[");
		int endIndex = firstIndex;
		
		int listnumber = 1;   // 12, 34, [56, 128, 45]
		
		while (listnumber != 0) {
			endIndex++;
			char character = globalListValue.charAt(endIndex);
			
			switch(character) {
				case '[': listnumber++; break;
				case ']': listnumber--; break;
			}
		}
		
		String newListString = globalListValue.substring(firstIndex, endIndex + 1); // + 1 is for include ]
		globalListValue = globalListValue.deleteCharAt(firstIndex);
		
		return newListString; 
	}
	
	private static String delFirstAndLastChar(String str) {
		return str.trim().substring(1, str.length() - 1);
	}
	
	private static int countEveryItem(Object arrayList, int count) {
		for (Object item : (ArrayList<?>)arrayList) {
			if (item instanceof ArrayList<?>) count += countEveryItem(item, 0);
			else count++;
		}
		return count;
	}
	
	private static String[] prepareStringToParse(String list) {
		list = delFirstAndLastChar(list);
		return list.split(",");
	}
	
	private static String[] newJsonFound() {
		int firstIndex = globalJsonValue.indexOf("{");
		int endIndex = firstIndex;
		
		int jsonNumber = 1;
		
		while(jsonNumber != 0) {
			endIndex++;
			char character = globalJsonValue.charAt(endIndex);
			
			switch(character) {
				case '{': jsonNumber++; break;
				case '}': jsonNumber--; break;
			}
		}
		
		String newJsonString = globalJsonValue.substring(firstIndex, endIndex + 1); // + 1 is for include }
		String key = globalJsonValue.substring(globalJsonValue.indexOf("\"", endIndex), globalJsonValue.indexOf(":", endIndex));
		
		
		globalJsonValue = globalJsonValue.deleteCharAt(firstIndex);
		
		String[] returns = {newJsonString, key};
		
		return returns;
	}
}
