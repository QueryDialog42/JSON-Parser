package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONFile {
	static private StringBuilder globalJsonValue;
	
	public static Map<String, Object> parseJson(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> pattern = new ArrayList<Object>();
		
		return parseAllJsonFile(json, map, pattern);
		
	}
	
	public static Map<String, Object> parseJson(String json, String onlykey) {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> pattern = new ArrayList<Object>();
		
		return parseAllJsonFile(limitJsonString(json, onlykey), map, pattern);
	}
	
	private static Map<String, Object> parseAllJsonFile(String json, Map<String, Object> map, ArrayList<Object> pattern) {
		String[] key_valueKey = prepareJsonToParse(json);
		
		pattern.add(key_valueKey[0].trim().replace("\"", ""));
		
		for (short i = 1; i < key_valueKey.length; i++) { // first index is added before
			String key_valueKey_index = key_valueKey[i].trim();
			
			if (i == key_valueKey.length - 1) {
				addLastItem(key_valueKey_index, pattern); // add the first key and the last value
				break;
			}
			
			switch(key_valueKey_index.charAt(0)) {
				case '"': splitStringAndKey(key_valueKey_index, pattern); break;
				case '[': splitListAndKey(key_valueKey_index, pattern); break;
				case '{': i += splitJsonAndKey(pattern); break;
				default: splitStringAndKey(key_valueKey_index, pattern);
			}
		}

		return addToMapThePattern(map, pattern);
	}
	
	private static int splitJsonAndKey(ArrayList<Object> pattern) { // {"Name"
		String[] items = newJsonFound();
	
		pattern.add(parseJson(items[0])); 
		if (items[1] != null) pattern.add(items[1].replace("\"", "").trim());
		
		return items[0].split(":").length - 1; // - 1 is for i++ 
	}
	
	private static Map<String, Object> addToMapThePattern(Map<String, Object> map, ArrayList<Object> pattern) {
		if (pattern.size() % 2 != 0) pattern.removeLast(); // sometimes the last key is added the wrong pattern
		
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
		int indexOfLastList;
		String value;
		String key;
		
		if ((indexOfLastList = item.lastIndexOf("]")) != -1) { // find the last ] in order to which commas should not be splitted
			int indexOfMiddleComma = item.indexOf(",", indexOfLastList); //find the first , in order to split as key - value
		
			value = item.substring(0, indexOfMiddleComma); // the part before comma, comma not included (list)
			key = item.substring(indexOfMiddleComma + 1); // the part after comma, comma not included by + 1 (key)
		}
		else { // list includes a json. item now do not has last index of ]
			value = newStringArray();
			
			int indexOfMiddleComma = globalJsonValue.indexOf(",", globalJsonValue.lastIndexOf(value)); // find , end of the list
			int firstIndex = globalJsonValue.indexOf("\"", indexOfMiddleComma); // find the first " for key
			int lastIndex = globalJsonValue.indexOf("\"", firstIndex);
			key = globalJsonValue.substring(firstIndex, lastIndex);
		}
		
		
		pattern.add(convertStringToArrayList(value));
		if (key.isEmpty() == false) pattern.add(key.replace("\"", "").trim());
	}
	
	private static String[] prepareJsonToParse(String json) {
		int firstindex = json.indexOf("{");
		int lastindex = json.lastIndexOf("}");
		
		String preparedJson = json.substring(firstindex + 1, lastindex); // get rid of { } symbols and get the pattern of [key, value_key, value_key ... value]
		
		setJsonGlobalValue(preparedJson);
		
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
				case '{': i += doJsonLogics(listValue); break; // not works correctly
				default: listValue.add(item);
			}
		}

		return listValue;
	}
	
	private static int doJsonLogics(ArrayList<Object> listValue) {
		String[] items = newJsonFound();
		
		listValue.add(parseJson(items[0]));
		
		if (items[1] != null) listValue.add(items[1]);
		return items[0].split(":").length - 1;
	}
	
	private static int newListFounded(ArrayList<Object> listValue) {
		ArrayList<Object> newList = convertStringToArrayList(newStringArray());
		
		listValue.add(newList);
		
		return countEveryItem(newList, 0) - 1; // skip the list, - 1 is for i++ at last
	}
	
	private static String newStringArray() {
		int firstIndex = globalJsonValue.indexOf("[");
		int endIndex = firstIndex;
		
		int listnumber = 1;   // one list is already found above
		
		while (listnumber != 0) {
			endIndex++;
			char character = globalJsonValue.charAt(endIndex);
			
			switch(character) {
				case '[': listnumber++; break;
				case ']': listnumber--; break;
			}
		}
		
		String newListString = globalJsonValue.substring(firstIndex, endIndex + 1); // + 1 is for include ]
		
		
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
		globalJsonValue = globalJsonValue.deleteCharAt(globalJsonValue.indexOf(list));
		list = delFirstAndLastChar(list);
		return list.split(",");
	}
	
	private static String[] newJsonFound() {
		String[] returns = new String[2];
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
		returns[0] = newJsonString;
		
		try {
			String key = globalJsonValue.substring(globalJsonValue.indexOf("\"", endIndex), globalJsonValue.indexOf(":", endIndex));
			returns[1] = key;
		} catch(StringIndexOutOfBoundsException ex) {
			// do nothing at this exception
		}
		
		globalJsonValue = globalJsonValue.deleteCharAt(firstIndex);
		
		return returns;
	}
	
	private static void setJsonGlobalValue(String json) {
		if (globalJsonValue == null) globalJsonValue = new StringBuilder(json);
	}
	
	private static void addLastItem(String item, ArrayList<Object> pattern) {
		switch(item.charAt(0)) {
			case '"': pattern.add(item.replace("\"", "")); break;
			case '[': pattern.add(convertStringToArrayList(item)); break;
			case '{': pattern.add(parseJson(newJsonFound()[0]));
		}
	}
	
	private static String limitJsonString(String json, String onlykey) {
		int onlyKeyIndex = json.indexOf(onlykey);
		
		String extractedJson = json.substring(onlyKeyIndex - 1);
		char character = extractedJson.split(":")[1].trim().charAt(0);
		switch(character) {
			case '"': return extractJsonString(extractedJson, 1);
			case '[': return extractJsonList(extractedJson);
			case '{': return extractJsonJson(extractedJson);
			default: return extractJsonString(extractedJson, 1);
		}
	}
	
	private static String extractJsonString(String extractedJson, int fromIndex) {
		for (short i = 0; i < 3; i++) {
			fromIndex = extractedJson.indexOf("\"", fromIndex + 1);
		}
		return "{" + extractedJson.substring(0, fromIndex + 1) + "}";
	}
	
	private static String extractJsonList(String extractedJson) {
		String list = newStringArray();
		return "{" +  extractedJson.substring(0, extractedJson.indexOf(":") + 1) + list + "}";
	}
	
	private static String extractJsonJson(String extractedJson) {
		globalJsonValue = new StringBuilder(extractedJson);
		String json =  "{" + extractedJson.substring(0, extractedJson.indexOf(":") + 1) + newJsonFound()[0] + "}";
		globalJsonValue = null;
		return json;	
	}
}
