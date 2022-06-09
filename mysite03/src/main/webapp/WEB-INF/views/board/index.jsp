<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link
	href="${pageContext.servletContext.contextPath }/assets/css/board.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form"
					action='${pageContext.request.contextPath }/board' method="get">
					<input type="text" id="kwd" name="kwd" value=""> <input
						type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:forEach items='${list }' var='vo' varStatus='status'>
						<tr>
							<td>${count - status.index }</td>

							<td style='text-align: left; padding-left: ${(vo.depth-1)*10}px'>
								<c:choose>
									<c:when test='${vo.depth > 1 }'>
										<img
											src='${pageContext.servletContext.contextPath }/assets/images/reply.png' />
									</c:when>
								</c:choose><a
								href="${pageContext.request.contextPath }/board/view/${vo.no }">${vo.title }</a>
							</td>
							<td>${vo.name }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<c:if test='${authUser.no == vo.userNo }'>
								<td><a
									href="${pageContext.request.contextPath }/board/delete/${vo.no }"
									class="del">삭제</a></td>
							</c:if>
						</tr>
					</c:forEach>
				</table>

				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<c:choose>
							<c:when test="${currentPage == 1 }">
								<li>◀</li>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${empty kwd  }">
										<li><a
											href="${pageContext.request.contextPath }/board?p=${currentPage-1 }">◀</a></li>
									</c:when>
									<c:otherwise>
										<li><a
											href="${pageContext.request.contextPath }/board?p=${currentPage-1 }&kwd=${kwd }">◀</a></li>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>

						<c:forEach begin="${startPage }" end="${endPage }" var="page"
							step="1">
							<c:choose>
								<c:when test="${currentPage == page }">
									<li class="selected">${page }</li>
								</c:when>
								<c:when test="${page > lastPage }">
									<li>${page }</li>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${empty kwd }">
											<li><a
												href="${pageContext.request.contextPath }/board?p=${page }">${page }</a></li>
										</c:when>
										<c:otherwise>
											<li><a
												href="${pageContext.request.contextPath }/board?p=${page }&kwd=${kwd }">${page }</a></li>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:choose>
							<c:when test="${currentPage != lastPage }">
								<c:choose>
									<c:when test="${empty kwd }">
										<li><a
											href="${pageContext.request.contextPath }/board?p=${currentPage+1 }">▶</a></li>
									</c:when>
									<c:otherwise>
										<li><a
											href="${pageContext.request.contextPath }/board?p=${currentPage+1 }&kwd=${kwd }">▶</a></li>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<li>▶</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
				<!-- pager 추가 -->

				<c:if test='${not empty authUser }'>
					<div class="bottom">
						<a href="${pageContext.request.contextPath }/board/write"
							id="new-book">글쓰기</a>
					</div>
				</c:if>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board" />
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>