<!-- 랭킹 조회 화면 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.util.*"%>
<%@ page import="omok.OmokDAO"%>
<%@ page import="omok.UserVO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width" initial-scale="1">
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="css/ranking.css">
<title>오목 랭킹</title>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="js/common.js"></script>
</head>
<body>
   <%
   String userID = null;
   
   if (session.getAttribute("userID") != null) { // 세션이 있으면
      userID = (String) session.getAttribute("userID"); // userID에 해당 세션 값을 넣어줌
   }
   
   PrintWriter script = response.getWriter();
	if (userID == null) {  // 세션이 없을 경우 로그인 X
	    script.println("<script>");
	    script.println("alert('로그인을 진행해주세요!');");
	    script.println("location.href = 'index.jsp';"); // index.jsp로 돌려보냄
	    script.println("</script>");
	}
   %>
   <div class="container">
      <!-- 네비게이션 바 -->
      <nav class="navbar navbar-default">
         <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
               <span class="icon-bar"></span>
               <span class="icon-bar"></span>
               <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">오목게임</a>
         </div>
         <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
               <li><a href="omok.jsp">게임하기</a></li>
               <li><a href="ranking.jsp">랭킹조회</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
               <li class="dropdown">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"aria-expanded="false" id="logout">
                     로그아웃
                  </a>
               </li>
            </ul>
         </div>
      </nav>
      <!-- 랭킹 조회 테이블 -->
      <div class="ranking-container">
          <div class="ranking-header">랭킹 순위표</div>
          <div class="table-container">
              <table class="table table-striped">
                  <thead>
                      <tr>
                          <th>순위</th>
                          <th>아이디</th>
                          <th>WIN</th>
                          <th>LOSE</th>
                      </tr>
                  </thead>
                  <tbody>
				   <%
				      OmokDAO dao = new OmokDAO();  // DAO 객체 생성
				      ArrayList<UserVO> list = (ArrayList<UserVO>) dao.getRanking();  // 랭킹 정보 가져오기
				     
				      // 데이터 디버깅 로그 추가
				      for (int i = 0; i < list.size(); i++) {
				          System.out.println("===== Row " + (i + 1) + " =====");
				          System.out.println("User ID: " + list.get(i).getUserId());
				          System.out.println("Win: " + list.get(i).getWin());
				          System.out.println("Lose: " + list.get(i).getLose());
				   %>
				      <tr>
				         <td><%= i + 1 %></td>
				         <td><%= list.get(i).getUserId() %></td>
				         <td><%= list.get(i).getWin() %></td>
				         <td><%= list.get(i).getLose() %></td>
				      </tr>
				   <%
				      }
				   %>
				</tbody>
              </table>
          </div>
      </div>
   </div>
</body>
</html>