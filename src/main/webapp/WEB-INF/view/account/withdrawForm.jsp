<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>

        <h1>ATM 출금</h1>
        <hr />
        <form action="/account/withdraw" method="post">
            <input type="text" name="amount" placeholder="Enter 출금금액" /><br />
            <input type="text" name="wAccountNumber" placeholder="Enter 출금계좌번호" /><br />
            <input type="password" name="wAccountPassword" placeholder="Enter 출금계좌비밀번호" /><br />
            <button>출금</button>
        </form>
        </body>

        </html>