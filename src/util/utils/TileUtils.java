package util.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import util.C;

public class TileUtils {
	
	private static HashMap<String, HashMap<String, Object>> config = new HashMap<>();
	
	private static HashMap<String, Object> hm;
	private static BufferedReader br;
	private static String line;
	private static String[] keyValue, parts, nums;
	private static double[] scale = new double[] {C.TILE_WIDTH/32, C.TILE_HEIGHT/32};

	public static Object getData(String type, String data) {
		if(config.containsKey(type)) {
			return config.get(type).get(data);
		} else {
			try {
				hm = createNewTileConfig(type);
				if(hm != null) {
					config.put(type, hm);
					return hm.get(data);
				} else {
					return null;
				}	
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private static HashMap<String, Object> createNewTileConfig(String type) throws IOException {
		br = new BufferedReader(new FileReader("res/configs/tiles.cfg"));
		while((line = br.readLine()) != null) {
			line = line.replace(" ", "");
			keyValue = line.split("=");
			if(Pattern.matches(keyValue[0], type)) {
				br.close();
				return createHashMap(keyValue[1]);
				
			}
		}
		
		br.close();
		return null;
	}
	
	private static HashMap<String, Object> createHashMap(String line) {
		HashMap<String, Object> hm = new HashMap<>();
		for(String cat: line.split(",")) {
			parts = cat.split(":");
			switch(parts[1]) {
			case "true":
				hm.put(parts[0], true);
				break;
			case "false":
				hm.put(parts[0], false);
				break;
			case "null":
				hm.put(parts[0], null);
			default:
				if(Pattern.matches("[0-9]+", parts[1])) {
					hm.put(parts[0], Integer.parseInt(parts[1]));
					
				} else if(Pattern.matches("\\[[0-9]+'[0-9]+'[0-9]+'[0-9]+\\]", parts[1])) { 
					parts[1] = parts[1].substring(1, parts[1].length()-2);
					nums = parts[1].split("'");
					hm.put(parts[0], new int[] {(int)(Integer.parseInt(nums[0])*scale[0]), 
							(int)(Integer.parseInt(nums[1])*scale[1]), 
							(int)(Integer.parseInt(nums[2])*scale[0]), (int)(Integer.parseInt(nums[3])*scale[1])});
				} else {
					hm.put(parts[0], parts[1]);
				}
				break;
			}
		}
		return hm;
	}
	
	public static String[] getTileNames() {
		File[] files = new File("res/tiles").listFiles();
		List<String> names = new ArrayList<>();
		String[] parts;
		for(int i=0; i<files.length; i++) {
			if(files[i].isFile()) {
				parts = files[i].getName().split("\\.");
				if("png".equals(parts[1])) {
					names.add(parts[0]);
				}
			}
		}
		return names.toArray(new String[0]);
	}

}
