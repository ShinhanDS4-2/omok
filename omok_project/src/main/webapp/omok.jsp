<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width" initial-scale="1">
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="css/omok.css">
<title>오목</title>
<!-- jQuery CDN -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- JavaScript -->
<script src="js/omok.js"></script>
<script src="js/common.js"></script>
</head>
<body>
	<% 
		/* ##새로추가 -- 로그인 되어있지 않은 경우 오목 페이지 접근 못하게 만들기 */
		String userIDX = null; 
		String userID = null; 
	
		if(session.getAttribute("userID") != null){  // 세션이 있으면
			userIDX = String.valueOf(session.getAttribute("userIDX"));
			userID = (String) session.getAttribute("userID");  // userID에 해당 세션 값을 넣어줌
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
		<div class="wrap">
			<div class="row">
				<div class="user-field">
					<h5>USER 1</h5>
					<div id="user1">대기중</div>
				</div>
				<div class="board-field">
					<img src="img/타이틀.png">
					<!-- 오목판이 들어갈 자리 -->
					<div class="board"></div>
				</div>
				<div class="user-field">
					<h5>USER 2</h5>
					<div id="user2">대기중</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>