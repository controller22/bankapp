<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>

        <h1>계좌생성</h1>
        <hr />
        <form action="/account" method="post">
            <input type="text" name="number" placeholder="Enter 계좌번호" /><br />
            <input type="password" name="password" placeholder="Enter 계좌비밀번호" /><br />
            <button>계좌생성</button>
        </form>
        </body>

        </html>