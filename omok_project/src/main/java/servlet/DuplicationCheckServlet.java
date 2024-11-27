package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import omok.OmokDAO;
import omok.UserVO;

@WebServlet("/duple")
public class DuplicationCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String userId = request.getParameter("user_id");
		
		UserVO user = new UserVO();
		user.setUserId(userId);

		OmokDAO dao = new OmokDAO();
		int result = dao.duplicationCheckId(user);

		PrintWriter out = response.getWriter();
	
		if(result > 0) {
			out.print(1);
		}
	}

}
