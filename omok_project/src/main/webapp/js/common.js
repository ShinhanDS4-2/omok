$(document).ready(function() {
	/**
	 * 로그아웃
	 */
	$("#logout").on("click", function() {
		$.ajax({
			url: "logout",
			method: "post",
			success: function(data) {
				console.log(data);
				alert("로그아웃 되었습니다.");
				location.href = "/omok_project/";
			}
		});
	});
});
