<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/assets/css/guestbook-spa.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/assets/js/jquery/jquery-3.6.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
var startNo = 0;
var isEnd = false;

var messageBox = function(title, message, callback){
	$("#dialog-message p").text(message);
	$("#dialog-message")
		.attr("title", title)
		.dialog({
				modal: true,
				buttons: {
					"확인": function() {
						$(this).dialog( "close" );
					}
				},
				close: callback
		});
}

	var render = function(vo, mode) {
		var htmls = "<li data-no='" + vo.no + "'>" + "<strong>" + vo.name
				+ "</strong>" + "<p>" + vo.content + "</p>"
				+ "<strong></strong>"
				+ "<a href='' data-no='" + vo.no + "'>삭제</a>" + "</li>";

		$("#list-guestbook")[mode ? "append" : "prepend"](htmls);
	};

	var fetch = function() {
		if(isEnd){
			return;
		}
		
		$.ajax({
			url : "${pageContext.request.contextPath }/api/guestbook/list/" + startNo,
			type : "get",
			dataType : "json",
			data: '',
			success : function(response) {
				if (response.result !== 'success') {
					console.error(response.message);
					return;
				}
				
				// detect end
				if(response.data.length == 0){
					isEnd = true;
					return;
				}
				
				// rendering
				$.each(response.data, function(index, vo){
					render(vo, true);
				});

				// response.data.forEach(function(vo) {
				// 	render(vo, true);
				// });
				
				startNo = $('#list-guestbook li').last().data('no') || 0;
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});
	};

	$(function() {
		// scroll
		$(window).scroll(function() {
			var $window = $(this);
			var $document = $(document);
			var windowHeight = $window.height();
			var documentHeight = $document.height();
			var scrollTop = $window.scrollTop();
			
			if (documentHeight < windowHeight + scrollTop + 10) {
				fetch();
			}
		});
		
		// add
		$("#add-form").submit(function(event) {
			event.preventDefault();

			/* Validation */

			var vo = {};
			vo.name = $("#input-name").val();
			if(vo.name == ''){
				messageBox("방명록 글 남기기", "이름은 필수 항목 입니다.", function(){
					$("#input-name").focus();
				});
				return;
			}
			
			vo.password = $("#input-password").val();
			if(vo.password == ''){
				messageBox("방명록 글 남기기", "비밀번호는 필수 항목 입니다.", function(){
					$("#input-password").focus();
				});
				return;
			}
			
			vo.content = $("#tx-content").val();
			if(vo.message == ''){
				messageBox("방명록 글 남기기", "내용은 필수 항목 입니다.", function(){
					$("#tx-content").focus();
				});
				return;
			}

			$.ajax({
				url : "${pageContext.request.contextPath }/api/guestbook",
				type : "post",
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify(vo),
				success : function(response) {
					if (response.result !== 'success') {
						console.error(response.message);
						return;
					}
					render(response.data, true);
					
					// $("#input-name").val("");
					// $("#input-password").val("");
					// $("#tx-content").val("");
					$("#add-form")[0].reset();
				},
				error: function(xhr, status, e){
					console.error(status + ":" + e);
				}
			});
		});

		// 삭제 다이알로그 jQuery객체를 미리 만들기
		var dialogDelete = $("#dialog-delete-form").dialog({
			autoOpen : false,
			modal : true,
			buttons : {
				"삭제" : function() {
					console.log("AJAX 삭제 하기");
					$.ajax({
						url : "${pageContext.request.contextPath }/api/guestbook",
						type : "delete",
						dataType : "json",
						contentType : "application/x-www-form-urlencoded",
						data : "no=" + $("#hidden-no").val() + "&password=" + $("#password-delete").val(),
						success : function(response) {
							if (response.result !== 'success') {
								console.error(response.message);
								return;
							}
							console.log(response.data);
							if (response.data > 0) {
								$("#list-guestbook li[data-no=" + response.data + "]").remove();
								$("#dialog-delete-form").dialog('close');
							}

							$(".validateTips-error").show();
						},
						error: function(xhr, status, e){
							console.error(status + ":" + e);
						}
					});
				},
				"취소" : function() {
					$(this).dialog('close');
				}
			},
			close : function() {
				$("#hidden-no").val("");
				$("#password-delete").val("");
				$(".validateTips-error").hide();
			}
		});

		// 글 삭제 버튼 Click 이벤트 처리(Live Event)
		$(document).on('click', "#list-guestbook li a", function(event) {
			event.preventDefault();
			$('#hidden-no').val($(this).data('no'));
			dialogDelete.dialog('open');
		});
		// ...
		fetch();
	});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<h1>방명록</h1>
				<form id="add-form"
					action="${pageContext.request.contextPath }/api/guestbook"
					method="post">
					<input type="text" id="input-name" placeholder="이름"> <input
						type="password" id="input-password" placeholder="비밀번호">
					<textarea id="tx-content" placeholder="내용을 입력해 주세요."></textarea>
					<input type="submit" value="보내기" />
				</form>
				<ul id="list-guestbook"></ul>
			</div>
			<div id="dialog-delete-form" title="메세지 삭제" style="display: none">
				<p class="validateTips-normal">작성시 입력했던 비밀번호를 입력하세요.</p>
				<p class="validateTips-error" style="display: none">비밀번호가 틀립니다.</p>
				<form>
					<input type="password" id="password-delete" value=""
						class="text ui-widget-content ui-corner-all"> <input
						type="hidden" id="hidden-no" value=""> <input
						type="submit" tabindex="-1"
						style="position: absolute; top: -1000px">
				</form>
			</div>
			<div id="dialog-message" title="" style="display: none">
				<p></p>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>