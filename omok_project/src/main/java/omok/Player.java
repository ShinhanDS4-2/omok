package omok;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Player {
	   
	String idx;
    String name;
    String stone;
    
    String sessionNumber;
    
    public Player(String idx, String name, String stone, String sessionNumber) {
        this.idx = idx;
        this.name = name;
        this.stone = stone;
        this.sessionNumber = sessionNumber;
    }
    
    public Map<String, Object> getPlayer() {
    	Map<String, Object> map = new HashMap<>();
    	
    	map.put("userIdx", idx);
    	map.put("userId", name);
    	map.put("stone", stone);
    	map.put("sessionNumber", sessionNumber);
    	
    	return map;
    }
}