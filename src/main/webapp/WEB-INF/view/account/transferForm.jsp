<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>
        <h1>이체</h1>
        <hr />
        <form action="/account/transfer" method="post">
            <input type="text" name="amount" placeholder="Enter 이체금액" /><br />
            <input type="text" name="wAccountNumber" placeholder="Enter 출금계좌" /><br />
            <input type="text" name="dAccountNumber" placeholder="Enter 입금계좌" /><br />
            <input type="text" name="wAccountPassword" placeholder="Enter 출금계좌비밀번호" /><br />
            <button>이체</button>
        </form>
        </body>

        </html>