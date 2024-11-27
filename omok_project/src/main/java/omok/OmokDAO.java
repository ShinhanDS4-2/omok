package omok;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class OmokDAO {

	private PreparedStatement pstmt;
	private Connection con;
	private DataSource dataFactory;
	
	// DB 연결
	public OmokDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 사용자 로그인 (회원 데이터가 있을 경우 1 리턴)
	 * @return
	 */
	public UserVO userLogin(UserVO userVO) {
		UserVO vo = new UserVO();
		
		try {
			con = dataFactory.getConnection();
			String id = userVO.getUserId();
			String pw = userVO.getUserPw();

			String query = "SELECT idx, user_id FROM OMOK_USER WHERE user_id = ? AND user_pw = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				vo.setUseridx(rs.getInt("idx"));
				vo.setUserId(rs.getString("user_id"));
			}
			
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	/**
	 * 사용자 가입
	 * @return
	 */
	public void userSignUp(UserVO userVO) {
		try {
			con = dataFactory.getConnection();
			String id = userVO.getUserId();
			String pw = userVO.getUserPw();
			
			String query = "INSERT INTO OMOK_USER (idx, user_id, user_pw) VALUES (seq_omok_user.NEXTVAL, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 아이디 중복 확인
	 * @param userVO
	 * @return
	 */
	public int duplicationCheckId(UserVO userVO) {
		int result = 0;
		
		try {
			con = dataFactory.getConnection();
			String id = userVO.getUserId();
			
			String query = "SELECT COUNT(*) AS cnt FROM OMOK_USER WHERE user_id = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				result = rs.getInt("cnt");
			}
			
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 랭킹 조회
	 * @return
	 */
	public List<UserVO> getRanking() {
		List<UserVO> list = new ArrayList<>();

		try {
			con = dataFactory.getConnection();
			
			String query = "SELECT ou.idx, ou.user_id, os.win, os.lose FROM OMOK_USER ou " 
							+ " JOIN OMOK_SCORE os ON os.user_idx = ou.idx " 
							+ "WHERE os.win > 0 ORDER BY os.win DESC, os.lose";
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {	
				UserVO vo = new UserVO();
				vo.setUserId(rs.getString("user_id"));
				vo.setWin(rs.getInt("win"));
				vo.setLose(rs.getInt("lose"));
				
				list.add(vo);
			}
			
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 랭킹에 유저 유무 확인
	 * 없으면 OMOK_SCORE에 유저 추가 INSERT
	 * 있으면 OMOK_SCORE의 WIN(승) LOSE(패) 값 UPDATE
	 * @param userVO
	 * @return 있으면 1 or 없으면 0
	 */
	public int getUserRanking(UserVO userVO) {
		int result = 0;
		
		try {
			con = dataFactory.getConnection();
			int idx = userVO.getUseridx();
			
			String query = "SELECT COUNT(*) as cnt FROM OMOK_SCORE WHERE user_idx = ?";
			// OMOK_SCORE에 승패유무가 등록되어 있는 유저인지 확인하는 쿼리문 (있으면 1 반환, 없으면 0반환)
			pstmt = con.prepareStatement(query);  
			pstmt.setInt(1, idx);
			
			ResultSet rs = pstmt.executeQuery();  // 쿼리 실행 결과값 저장
			while(rs.next()) {
				result = rs.getInt("cnt");  // 쿼리 결과rs에서 조회한 cnt값 가져와서 result에 저장
			}
			
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;  // 있으면 1반환, 없으면 0반환
	}
	
	/**
	 * 랭킹 추가 (OMOK_SCORE에 승패유무가 등록되어 있지 않은 경우)
	 * @param userVO
	 * @return 없음
	 */
	public void addResult(UserVO userVO) {
		try {
			con = dataFactory.getConnection();
			
			int idx = userVO.getUseridx();
			String query = "INSERT INTO OMOK_SCORE(idx, user_idx, win, lose) VALUES (seq_omok_score.nextval, ?, 0, 0)";
			pstmt = con.prepareStatement(query); 
			pstmt.setInt(1, idx);  // 첫번째 ? 에 userVO객체에서 받은 userId값을 바인딩
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 랭킹 업데이트 (OMOK_SCORE에 승패유무가 등록되어 있는 유저의 경우. 승/패 횟수 데이터만 수정 UPDATE)
	 * @param userVO
	 * @return 없음
	 */
	public void updateResult(UserVO userVO) {
		try {
			con = dataFactory.getConnection();
			
			int idx = userVO.getUseridx();
			int win = userVO.getWin();
			int lose = userVO.getLose();
			
			String query = "";
			
			if(win == 1) {  // 이긴경우
				query = "UPDATE OMOK_SCORE SET win = win + 1 WHERE user_idx = ?";
			} else if(lose == 1) {  // 진경우
				query = "UPDATE OMOK_SCORE SET lose = lose + 1 WHERE user_idx = ?";
			}
			
			pstmt = con.prepareStatement(query); 
			pstmt.setInt(1, idx);
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
