<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<%@ include file="../layouts/header.jsp"%>
<%@include file="../layouts/sidebar.jsp"%>
<link rel="stylesheet" href="/resources/css/best.css" />
<body>
	<div class="center">
		<h2>베스트셀러 TOP10</h2>
	</div>
	<div>
		<%@ include file="category_bar.jsp"%>
<hr>
		<div class="row">
			<c:forEach var="best" items="${list}" varStatus="status" begin="0"
				end="9">
				<div class="stylee">
					<div class="card-deck">
						<div class="card">

							<div class="card-header">
								<c:out value="${status.count}" />
								위
							</div>

							<%-- 						<a href="/best/get?column1=${best.column1}">  --%>
							<img class="card-img-top" src="${best.images}"
								alt="${best.title}">
							<!-- 						</a> -->
							<div class="card-body">
								<h6 class="card-title">
									<%-- 							<a href="/best/get?column1=${best.column1}">  --%>
									${best.title}
									<!-- 							</a> -->
								</h6>

								<p class="card-text">${best.author}</p>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>

	</div>
	<%-- <%@include file="pagination.jsp"%> --%>
	<%@ include file="../layouts/footer.jsp"%>
</body>