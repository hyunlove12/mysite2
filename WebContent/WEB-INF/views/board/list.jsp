<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.bigdata2019.mysite.vo.BoardVo"%>
<%
	//getAttribute vs getParameter
	String flag = request.getAttribute("result").toString();
	String selectedPage = request.getParameter("selectedPage");
	String currentPage = request.getParameter("currentPage");
	List<BoardVo> list = new ArrayList<BoardVo>();
	list = (List<BoardVo>)request.getAttribute("list");
%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath() %>/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="" method="post">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
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
					<%
						for( BoardVo vo : list ){
					%>		
							<tr onclick="javascript:fn_detail(<%=vo.getNo() %>)">
								<td><%=vo.getNo() %></td>
								<td><%=vo.getTitle() %></td>
								<td><%=vo.getName() %></td>
								<td><%=vo.getHit() %></td>
								<td><%=vo.getRegDate() %></td>
								<td></td>
							</tr>
					<%
						}
					%>
				</table>
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<li><a href="javascript:fn_prev()">◀</a></li>
						<%	
						
						for( int b = 1; b < 6; b++ ){
						%>
							<li id="<%=b %>"><a href="javascript:fn_paging(<%=b %>)"><%=b %></a></li>
							<%-- <li id="<%=b %>"><a href="javascript:fn_paging(this)"><%=b %></a></li> --%>
						<%
						}
						%>
						<li><a href="javascript:fn_next()">▶</a></li>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<div class="bottom">
					<a href="<%=request.getContextPath() %>/board?a=writeform" id="new-book">글쓰기</a>
				</div>				
			</div>
		</div>
		<script type="text/javascript">
		var selectedPage = 0;
		window.onload = function () {		
			
			selectedPage = '<%=selectedPage%>';							
			document.getElementById(selectedPage).classList.add('selected');
			
			
			var flag = '<%=flag%>';
			if(flag == 'suc'){
				alert('게시글 등록에 성공했습니다.');	
			} else if(flag == 'fail'){
				alert('게시글 등록에 실패했습니다.');
			} else if(flag == 'noUser'){
				alert('로그인을 해주세요!');
			}			
		}
		function fn_detail(no_){
			var no = no_;
			//href
			//window.location.href = '<%=request.getContextPath() %>/board?no=' + no;
			//submit
		}
		function fn_paging(selectedPage){
			//console.log(this);
			//console.log(selectedPage);					
			window.location.href = '<%=request.getContextPath() %>/board?selectedPage=' + selectedPage;
		}
		
		function fn_prev(){
			
			
		}
		
		function fn_next(){
			
			
		}
		
		</script>
		<jsp:include page="/WEB-INF/views/includes/navigation.jsp"/>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>