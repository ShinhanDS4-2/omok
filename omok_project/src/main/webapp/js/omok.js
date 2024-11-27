// 웹 실행
$(document).ready(function() {
	let webSocket = new WebSocket("ws://localhost:8090/omok_project/socket");

	// 오목판 사이즈
	const size = 19;
	
	// 알파벳 아스키코드
	let ascii = 65;
	
	// 오목판 
	let board_o = $(".board");
	
	// 중첩 for문을 돌려 가로 세로 19개의 오목판 생성
	for(let i = 0; i < size; i++) {
		// 하나의 행이 될 div 엘리먼트 생성
		let row_o = $("<div>"); 
		board_o.append(row_o);

		// 왼쪽 알파벳
		let row_num_o = $("<div>");
		row_o.append(row_num_o);

		// 아스키코드를 문자열로 바꿔주는 함수
		let str = String.fromCharCode(ascii);
		row_num_o.addClass("num").html(str); 

		for(let j = 0; j < size; j++) {
			// 점 하나 영역 div 엘리먼트 생성
			let dot_o = $("<div>"); 
			row_o.append(dot_o);
			
			// 각각의 점에 좌표값으로 아이디 지정
			dot_o.attr("id", str + j);
			dot_o.addClass("dot").html("+");
		}
		
		// 알파벳 +1
		ascii++;
	}
	
	// 마지막 줄
	let last_row_o = $("<div>");
	board_o.append(last_row_o);

	// 마지막줄 왼쪽 공백
	last_row_o.append($("<div>").addClass("num").html(" "));
	
	for(let i = 0; i < size; i++) {
		let last_num_o = $("<div>");
		last_row_o.append(last_num_o);

		last_num_o.addClass("num").html(i);
	}
	
	
	/**
	 * 좌표클릭 이벤트
	 */
	$(".dot").on("click", function() {
		// 클릭한 요소의 id 속성을 가져온다.
		let point = $(this).attr("id");
		console.log("클릭한 좌표: " + point);
		
		// 내가 클릭한 위치에 바둑돌 놓기
		let stone = sessionStorage.getItem("stone");
		$(this).html(stone == "O" ? "◯" : "⚫")
			   .removeClass("dot")
			   .addClass("dot_set")
			   .off("click");
			   
		let json = {
			"point" : point,
			"turn" : true
		}

		// json 형태의 데이터를 문자열로 변환하여 전송 (웹소켓 함수가 파라미터를 String 타입으로 받기 때문)
		webSocket.send(JSON.stringify(json));
	});

	
	/**
	 * 전송받은 데이터
	 */
	webSocket.onmessage = function(message) {
	    // 메시지 파싱
	    let data = JSON.parse(message.data);
	    console.log(data);
	    
	    // 플레이어 리스트를 받아 user[i] 엘리먼트에 아이디 추가
	    if(data.playerList) {
			for(let i = 0; i < data.playerList.length; i++) {
				$("#user" + (i + 1)).html(data.playerList[i].name);
			}			
		}

	    let player = data.player;
	    if(player) {
			sessionStorage.setItem("stone", player.stone);
		}

	    // 메시지 타입이 "end"이면 게임 종료 알림
	    if (data.type === "end") {
	        alert(data.winner + "의 승리입니다!");
	        $(".dot").off("click");
	       
	    } else {
			/*
			if(data.turn == true) {
				console.log("your turn");
			} else {
				$(".dot").off("click");
			}
			*/
			
	        // 기존 좌표 처리 로직
	        let point = data.point;
	        console.log("받은 좌표: " + point);
	        // 받은 좌표를 id로 가진 엘리먼트에 바둑돌 올리기
	        let stone = data.userName == "user1" ? "◯" : "⚫";
	        $("#" + point).html(stone)
	                      .removeClass("dot")
	                      .addClass("dot_set")
	                      .off("click");
	    }
	};
});