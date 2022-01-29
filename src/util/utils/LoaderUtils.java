package util.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.HashMap;

import designer.save.SaveFile;
import map.Map;
import map.Text;
import map.Tile;
import objects.Enemy;
import objects.EnemyPath;
import util.C;

public class LoaderUtils {
	
	public static void load(Map map, String mapName) {
		try {
			map.loading = true;
			byte[] barray = Files.readAllBytes(new File(String.format("maps/%s/%s.map", mapName, mapName)).toPath());
			ByteArrayInputStream bis = new ByteArrayInputStream(barray);
			ObjectInputStream in = new ObjectInputStream(bis);
			SaveFile sf = (SaveFile) in.readObject();
			
			map.width = sf.width;
			map.height = sf.height;
			map.array = new Tile[sf.height][sf.width];
			int textCount = 0;
			for(int j=0; j<map.height; j++) {
				for(int i=0; i<map.width; i++) {
					map.array[j][i] = new Tile(sf.array[j][i].tiles);
					
					if(map.array[j][i].variables.get("hastext")) {
						map.array[j][i].textIndex = textCount++;
					}
				}
			}
			map.spawn = sf.spawn;
			map.enemies = new Enemy[sf.enemies.length];
			for(int i=0; i<map.enemies.length; i++) {
				map.enemies[i] = new Enemy(map, new EnemyPath(sf.enemies[i].start, sf.enemies[i].end));

			}
			loadText(map, mapName);
			
			map.loading = false;
			
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
	}
	
	private static void loadText(Map map, String mapName) throws IOException {
		map.textMap = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader("maps/" + mapName + "/signText.txt"));
		String text = "";
		String line;
		while((line = br.readLine()) != null) {
			text += line;
			
			if('}' == text.charAt(text.length()-1)) {
				text = text.substring(0, text.length()-1);
				String index = "", message = "";
				boolean doingMessage = false;
				for(char c: text.toCharArray()) {
					if(c == '{') {
						doingMessage = true;
					} else if(doingMessage) {
						message += c;
					} else {
						index += c;
					}
				}

				map.textMap.put(Integer.parseInt(index), new Text(message, C.TEXT_FONT_SPEED));
				text = "";
			} else {
				text += "\n";
			}
		}
		
		System.out.println();
		br.close();
	}

}
