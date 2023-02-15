<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>

        <h1>계좌상세보기</h1>
        <hr />
        <div class="user-box">
            fullname님 계좌<br />
            계좌번호 : 1111<br />
            잔액 : 1000원
        </div>
        <div class="list-box">
            <a href="#">전체</a> <a href="#">입금</a> <a href="#">출금</a>
            <br />
            <table border="1">
                <thead>
                    <tr>
                        <th>날짜</th>
                        <th>보낸이</th>
                        <th>받은이</th>
                        <th>입출금금액</th>
                        <th>계좌잔액</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>2022.10.01</td>
                        <td>ATM</td>
                        <td>1111계좌</td>
                        <td>500원</td>
                        <td>1500원</td>
                    </tr>
                </tbody>
            </table>
        </div>

        </body>

        </html>