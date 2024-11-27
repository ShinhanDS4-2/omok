package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import omok.OmokDAO;
import omok.UserVO;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String userId = request.getParameter("user_id");
		String userPw = request.getParameter("user_pw");
		
		UserVO user = new UserVO();
		user.setUserId(userId);
		user.setUserPw(userPw);

		OmokDAO dao = new OmokDAO();
		UserVO userVO = dao.userLogin(user);
		int user_idx = userVO.getUseridx();
		String user_id = (String) userVO.getUserId();

		PrintWriter out = response.getWriter();
		
		if(user_idx > 0) {
			HttpSession session = request.getSession();
			session.setAttribute("userIDX", user_idx);
			session.setAttribute("userID", user_id);
			
			response.sendRedirect("ranking.jsp");
		} else {
			out.println("<script>");
			out.println("alert('아이디 또는 비밀번호가 일치하지 않습니다.')");
			out.println("history.back()");  // 이전페이지로 사용자를 돌려보냄 (index.jsp로 돌려보냄)
			out.println("</script>");
		}
	}
}
