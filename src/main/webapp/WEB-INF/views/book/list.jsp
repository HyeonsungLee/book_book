<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="../layouts/header.jsp"%>
 
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<!-- <h1>페이지 타이틀</h1> -->

<script type="text/javascript">
	function likeBook(bookId, title, icon) {
		// 여기에 AJAX 요청 코드 추가
		console.log("")
		$.ajax({			
			type : 'POST',
			url : '/api/addLike', // 좋아요를 처리하는 컨트롤러의 URL
			data : {
				userId : 'test97', // 사용자 아이디를 동적으로 설정하거나 가져와야 합니다.
				bookId : bookId, // 좋아요를 누른 도서의 아이디
				title : title
			},
			success : function(response) {
				// 성공적으로 요청이 완료된 경우 실행할 코드
				console.log(response);
				// 이 부분에 필요한 업데이트 로직을 추가할 수 있습니다.

	
				// 아이콘의 클래스를 토글
	            $(icon).toggleClass('bi-heart bi-heart-fill');
			}
		})
	}
	</script>

<style>
.card-container {
	display: flex;
	flex-wrap: wrap;
	gap: 20px;
}

.card {
	width: 200px;
	border: 1px solid #ccc;
	border-radius: 8px;
	overflow: hidden;
}

.card-img-top {
	width: 100%;
	height: 150px;
	object-fit: cover;
}

.card-body {
	padding: 10px;
	text-align: center;
}

.pageInfo {
	list-style: none;
	display: flex;
	justify-content: center; /* 가로 중앙 정렬 */
	align-items: center; /* 세로 중앙 정렬 */
	margin: 50px 0 0 0;
	justify-content: center; /* 가로 중앙 정렬 */
	align-items: center; /* 수정된 여백 설정 */
}

.pageInfo li {
	float: left;
	font-size: 20px;
	margin-left: 18xp;
	padding: 20px;
	font-weight: 500;
}

a:link {
	color: black;
	text-decoration: none;
}

a:visited {
	color: black;
	text-decoration: none;
}

a:hover {
	color: black;
	text-decoration: underline;
}

.active {
	background-color: #cdd5ec;
}
</style>

<h1>Book List</h1>




<c:if test="${not empty list}">
	<ul>

		<li>${book}</li>
		<div class="card-container">
			<c:forEach var="book" items="${list}">
				<div class="card">
					<a href="/book/detail?bookid=${book.bookid}"> <img
						src="${book.imageUrl}" alt="${book.title}" class="card-img-top">
					</a>
					<div class="card-body">
						<a href="/book/detail?bookid=${book.bookid}">
							<h5 class="card-title">${book.title}</h5>
						</a>
						<p class="card-text">저자: ${book.author}</p>
						<p class="card-text">출판사: ${book.publisher}</p>
						<p class="card-text">장르: ${book.genre}</p>
						<p class="card-text">카테고리: ${book.category}</p>
						<p class="card-text"></p>
						<i class="bi bi-heart text-danger" style="cursor: pointer;" onclick="likeBook('${book.bookid}', '${book.title}', this)"></i>
						
					</div>
				</div>
			</c:forEach>
		</div>

	</ul>

	<form>
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum }">
		<input type="hidden" name="amount" value="${pageMaker.cri.amount }">
	</form>

	<div class="pageInfo_wrap">
		<div class="pageInfo_area">
			<ul id="pageInfo" class="pageInfo">

				<!-- 이전페이지 버튼 -->
				<c:if test="${pageMaker.prev}">
					<li class="pageInfo_btn previous"><a
						href="/book/list?pageNum=${pageMaker.startPage-1}">Previous</a></li>
				</c:if>

				<!-- 각 번호 페이지 버튼 -->
				<c:forEach var="num" begin="${pageMaker.startPage}"
					end="${pageMaker.endPage}">
					<li class="pageInfo_btn"
						${pageMaker.cri.pageNum == num ? "active":"${num}"}><a
						href="/book/list?pageNum=${num}">${num}</a></li>
				</c:forEach>

				<!-- 다음페이지 버튼 -->
				<c:if test="${pageMaker.next}">
					<li class="pageInfo_btn next"><a
						href="/book/list?pageNum=${pageMaker.endPage + 1 }">Next</a></li>
				</c:if>

			</ul>
		</div>
	</div>

</c:if>
<c:if test="${empty list}">
	<p>No books found.</p>
</c:if>






<h1>recommend book</h1>

<c:if test="${not empty bookByCBF}">
	<ul>
		<div class="card-container">
			<c:forEach var="bookAI" items="${bookByCBF}">
				<div class="card">
					<a href="/book/detail?bookid=${bookAI.bookid}"> <img
						src="${bookAI.imageUrl}" alt="${bookAI.title}"
						class="card-img-top">
					</a>
					<div class="card-body">
						<a href="/book/detail?bookid=${bookAI.bookid}">
							<h5 class="card-title">${bookAI.title}</h5>
						</a>
						<p class="card-text">저자: ${bookAI.author}</p>
						<p class="card-text">출판사: ${bookAI.publisher}</p>
						<p class="card-text">장르: ${bookAI.genre}</p>
						<p class="card-text">카테고리: ${bookAI.category}</p>
						<p class="card-text"></p>
						<button class="btn btn-primary"
							onclick="likeBook('${bookAI.bookid}')">
							<i class="bi-heart"></i>좋아요
						</button>
						<button class="btn btn-primary" onclick="addToFavorites('')">즐겨찾기</button>
					</div>
				</div>
			</c:forEach>
		</div>
	</ul>

</c:if>

<c:if test="${empty bookByCBF}">
	<ul>
		<div class="card-container">
			<c:forEach var="best" items="${best}">
				<div class="card">
					<a href="/best/get?column1=${best.column1}"> <img
						src="${best.images}" alt="${best.title}" class="card-img-top">
					</a>
					<div class="card-body">
						<a href="/best/get?column1=${best.column1}">
							<h5 class="card-title">${best.title}</h5>
						</a>

						<p class="card-text">저자: ${best.author}</p>
						<p class="card-text">출판사: ${best.publisher}</p>
						<p class="card-text"></p>
					</div>
				</div>
			</c:forEach>
		</div>
	</ul>
</c:if>

<%@include file="../layouts/footer.jsp"%>
