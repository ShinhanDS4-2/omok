package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import omok.OmokDAO;
import omok.UserVO;

@WebServlet("/join")
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userId = request.getParameter("user_id");
		String userPw = request.getParameter("user_pw");
		
		UserVO user = new UserVO();
		user.setUserId(userId);
		user.setUserPw(userPw);

		OmokDAO dao = new OmokDAO();
		dao.userSignUp(user);
		
		response.sendRedirect("index.jsp");
	}
}
