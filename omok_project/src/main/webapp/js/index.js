$(document).ready(function() {
	// 회원가입 버튼 클릭
	$("#joinBtn").on("click", function() {
		reset();
		$("#loginDiv").css("display", "none");
		$("#joinDiv").css("display", "block");
		$("#title").html("회원가입");
	});
	
	// 로그인 버튼 클릭
	$("#loginBtn").on("click", function() {
		$("#joinDiv").css("display", "none");
		$("#loginDiv").css("display", "block");
		$("#title").html("로그인");
	});

	// 아이디 중복확인 버튼 클릭
	$("#dupleBtn").on("click", function() {
		$.ajax({
			url: "duple",
			method: "post",
			data: {
				"user_id" : $("#joinId").val()
			},
			success: function(resp) {
				if(resp == 1) {
					alert("이미 사용 중인 아이디입니다.");
					$("#join").attr("disabled", "disabled");
				} else {
					alert("사용 가능한 아이디입니다.");
					$("#join").removeAttr("disabled");
				}
			}
		});
	});
	
	// 폼 리셋
	function reset() {
		$("input[name=user_id]").val("");
		$("input[name=user_pw]").val("");
		$("#join").attr("disabled", "disabled");
	}
});