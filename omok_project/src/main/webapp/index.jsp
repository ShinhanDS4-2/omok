<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width" initial-scale="1">
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="css/index.css">
<title>오목</title>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="js/index.js"></script>
</head>
<body>
	<%
		if(session.getAttribute("userID") != null) {
			response.sendRedirect("ranking.jsp");
		}
	%>
	<div class="login_container">
		<div class="wrapper">
			<div class="text-field">
				<h2><b id="title">로그인</b></h2>
			</div>
			<div id="loginDiv">
				<form method="post" action="login">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="아이디" name="user_id">
					</div>
					<div class="form-group">
						<input type="password" class="form-control"placeholder="비밀번호" name="user_pw">
					</div>
					<button type="submit" class="btn">로그인</button>
					<span id="joinBtn">회원가입하기</span>
				</form>
			</div>
			<div id="joinDiv">
				<form method="post" action="join">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="아이디" name="user_id" id="joinId">
						<button type="button" class="btn" id="dupleBtn">중복확인</button>
					</div>
					<div class="form-group">
						<input type="password" class="form-control" placeholder="비밀번호" name="user_pw">
					</div>
					<button type="submit" class="btn" disabled="disabled" id="join">회원가입</button>
					<span id="loginBtn">로그인하기</span>
				</form>
			</div>
		</div>
	</div>
</body>
</html>