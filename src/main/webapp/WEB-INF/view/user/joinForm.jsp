<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>
        <h1>회원가입페이지</h1>
        <hr />
        <form action="/join" method="post">
            <input type="text" name="username" placeholder="Enter username" /><br />
            <input type="password" name="password" placeholder="Enter password" /><br />
            <input type="text" name="fullname" placeholder="Enter fullname" /><br />
            <button>회원가입</button>
        </form>

        </body>

        </html>