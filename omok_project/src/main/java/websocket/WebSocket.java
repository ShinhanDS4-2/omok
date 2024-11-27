package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import omok.Board;
import omok.OmokDAO;
import omok.Player;
import omok.UserVO;

// url 매핑
@ServerEndpoint(value = "/socket", configurator = WebSocketConfigurator.class)
public class WebSocket {
	// 세션 리스트
	private static final List<Session> sessionList = new ArrayList<>();
	
	// 세션에 접속한 유저 리스트
    private static final List<String> userNameList = new ArrayList<>();
    // Player 객체 리스트
    private static List<Player> playerList = new ArrayList<>();
    // 오목판 객체
    public static Board board = new Board(19);
    
    // DAO
    public static OmokDAO dao = new OmokDAO();
	
    // 세션열리면 실행
	@OnOpen
    public void handleOpen(Session sess, EndpointConfig config) {
		// WebSocketConfigurator에서 HttpSession을 가져옴
        HttpSession session = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        String userId = null;
        String userIdx = null;
        
        // 세션에 관련된 로직 수행
        if (session != null) {
            userId = (String) session.getAttribute("userID");
            userIdx = String.valueOf(session.getAttribute("userIDX"));
            
            System.out.println("Login User ID: " + userId);  
            System.out.println("Login User IDX: " + userIdx);  
        } else {
            System.out.println("No HTTP session found.");
        }

        synchronized (sessionList) {
            // 접속 인원 제한: 최대 2명
            if (sessionList.size() < 2) {
            	
                sessionList.add(sess); // 세션 추가
                
                String sessionNumber = "user" + (sessionList.size()); // 접속 순서에 따라 user1, user2 부여
                userNameList.add(sessionNumber);
                
                String stone = sessionList.size() == 1 ? "O" : "X";
                
                System.out.println(sessionNumber + " has connected...");
                
                Player player = new Player(userIdx, userId, stone, sessionNumber);
                playerList.add(player);
                
                JSONObject jsonData = new JSONObject();
                jsonData.put("player", player.getPlayer());
                
                // 플레이어 리스트 추가
                jsonData.put("playerList", playerList);

                try {
                	for (Session s : sessionList) {
                        if (!s.equals(sess)) {
                        	// user2 세션이 들어왔을 때 user1에게 user2의 정보를 전송하기 위한 send
                        	JSONObject jsonData2 = new JSONObject();
                        	jsonData2.put("playerList", playerList);
                        	
                        	// user1에게 첫번째 턴 부여
                            jsonData2.put("turn", true);
                        	s.getBasicRemote().sendText(jsonData2.toString());
                        } else {
                        	// user2에게 2번째 턴 부여
                        	jsonData.put("turn", false);
                        	s.getBasicRemote().sendText(jsonData.toString());
                        }
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
                
                System.out.println("플레이어 수 : " + playerList.size());
            } else {
                // 접속 인원이 초과했을 때 세션 차단 및 클라이언트 연결 종료
                try {
                    sess.getBasicRemote().sendText("Connection refused: Maximum players reached.");
                    sess.close(); // 연결 종료
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	@OnMessage
    public void handleMessage(Session sess, String message) {
		
        synchronized (sessionList) {
            try {
                // 메시지와 사용자 이름 결합
                int userIndex = sessionList.indexOf(sess);
                String userName = userNameList.get(userIndex);
                
                // 메시지 JSON 업데이트
                JSONObject jsonData = new JSONObject(message);
                jsonData.put("userName", userName);
                jsonData.put("playerList", playerList);
               
                parsing(jsonData);
               
                // 다른 사용자에게 메시지 전송
                for (Session s : sessionList) {
                    if (!s.equals(sess)) {
                        s.getBasicRemote().sendText(jsonData.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * 전송 받은 데이터를 파싱 후 좌표에 맞게 업데이트
	 * @param jsonObject
	 */
	private void parsing(JSONObject jsonObject) {
	
		// JSON 데이터에서 필요한 값 추출
	    String point = jsonObject.getString("point"); 
	    String name = jsonObject.getString("userName");  
	
	    // 콘솔에 출력 (디버깅 용도)
	    System.out.println("Point: " + point);
	    System.out.println("Name: " + name);
	    
	    String letter = point.substring(0, 1); // 행 좌표
	    int number = Integer.parseInt(point.substring(1)); // 열 좌표
	    
	    System.out.println(letter + " " + number);
	    System.out.println();
	    
	    char aa = Character.toUpperCase(letter.charAt(0));
        int row = (int) aa - 65;
        int col = number;
        
	    start(name, row, col);
	}
	
	
	/**
	 * board 업데이트
	 * @param name
	 * @param row
	 * @param col
	 */
	private void start(String name, int row, int col) {
		
		if("user1".equals(name) && board.possible(row, col)) {
			board.put(playerList.get(0), row, col);
			
			board.print();
			System.out.println();
			
			if(board.check(row, col)) {
				System.out.println("user1의 승리입니다.");
				
				// 승리
				String winUserName = playerList.get(0).getName();
				String winUserIdx = playerList.get(0).getIdx();
				System.out.println("승리한 유저 : " + winUserIdx);
				
				try {
		            for (Session s : sessionList) { // 모든 클라이언트에게 전송
		            	JSONObject jsonData = new JSONObject();
		            	jsonData.put("type", "end");
		            	jsonData.put("winner", winUserName);
		                s.getBasicRemote().sendText(jsonData.toString());
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				
				UserVO winVO = new UserVO();
				winVO.setUseridx(Integer.parseInt(winUserIdx));
				winVO.setWin(1);

				int result = dao.getUserRanking(winVO);
				if(result <= 0) {
					dao.addResult(winVO);
				}
				dao.updateResult(winVO);

				// 패배
				String loseUserIdx = playerList.get(1).getIdx();
				System.out.println("패배한 유저 : " + loseUserIdx);
				
				UserVO loseVO = new UserVO();
				loseVO.setUseridx(Integer.parseInt(loseUserIdx));
				loseVO.setLose(1);
				
				result = dao.getUserRanking(loseVO);
				if(result <= 0) {
					dao.addResult(loseVO);
				}
				dao.updateResult(loseVO);
			}
		} else if("user2".equals(name) && board.possible(row, col)) {
			board.put(playerList.get(1), row, col);
			
			board.print();
			System.out.println();
			
			if(board.check(row, col)) {
				System.out.println("user2의 승리입니다.");
				
				// 승리
				String winUserName = playerList.get(1).getName();
				String winUserIdx = playerList.get(1).getIdx();
				System.out.println("승리한 유저 : " + winUserIdx);
				
				try {
		            for (Session s : sessionList) { // 모든 클라이언트에게 전송
		            	JSONObject jsonData = new JSONObject();
		            	jsonData.put("type", "end");
		            	jsonData.put("winner", winUserName);
		                s.getBasicRemote().sendText(jsonData.toString());
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				
				UserVO winVO = new UserVO();
				winVO.setUseridx(Integer.parseInt(winUserIdx));
				winVO.setWin(1);
				
				int result = dao.getUserRanking(winVO);
				if(result <= 0) {
					dao.addResult(winVO);
				}
				dao.updateResult(winVO);

				// 패배
				String loseUserIdx = playerList.get(0).getIdx();
				System.out.println("패배한 유저 : " + loseUserIdx);
				
				UserVO loseVO = new UserVO();
				loseVO.setUseridx(Integer.parseInt(loseUserIdx));
				loseVO.setLose(1);
				
				result = dao.getUserRanking(loseVO);
				if(result <= 0) {
					dao.addResult(loseVO);
				}
				dao.updateResult(loseVO);
			}
		}
	}

	// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose() {
		System.out.println("client is now disconnected...");
		
	}

	// WebSocket과 브라우저 간에 통신 에러가 발생하면 요청되는 함수.
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
}