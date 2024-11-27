package omok;

import lombok.Data;

@Data
public class UserVO {
	
	private int useridx;
	private String userId;
	private String userPw;
	
	private int win;
	private int lose;
}
