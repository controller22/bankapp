<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>

        <h1>계좌상세보기</h1>
        <hr />
        <div class="user-box">
            ${aDto.fullname}<br />
            계좌번호 : ${aDto.number}<br />
            잔액 : ${aDto.balance}원
        </div>
        <div class="list-box">
            <div style="display: flex; ">
                <!-- <a href="/account/${aDto.id}?gubun=all">전체</a>
                <a href="/account/${aDto.id}?gubun=deposit">입금</a>
                <a href="/account/${aDto.id}?gubun=withdraw">출금</a> -->
                <button id="all" style="margin-right: 20px;" onclick="getgubun('all')">전체</button>
                <button id="deposit" style="margin-right: 20px;" onclick="getgubun('deposit')">입금</button>
                <button id="withdraw" style="margin-right: 20px;" onclick="getgubun('withdraw')">출금</button>
                <br />
            </div>
            <script>
                var page = 1; // 현재 페이지
                var size = 5; // 페이지당 보여줄 항목 개수


                function getgubun(gubun) {
                    $.ajax({
                        type: 'GET',
                        url: '/account/${aDto.id}?gubun=' + gubun,

                        success: function (data) {
                            // 거래 내역만 업데이트
                            $('#account-data').empty();
                            $('#account-data').append($(data).find('#account-data').html());
                            // $('#account-data').append($newRows);
                            console.log(data);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.error(textStatus + ' : ' + errorThrown);
                        }
                    });
                }
            </script>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>날짜</th>
                        <th>보낸이</th>
                        <th>받은이</th>
                        <th>입출금금액</th>
                        <th>계좌잔액</th>
                    </tr>
                </thead>
                <tbody id="account-data">
                    <c:forEach items="${hDtoList}" var="history">
                        <tr>
                            <td>${history.createdAt}</td>
                            <td>${history.sender}</td>
                            <td>${history.receiver}</td>
                            <td>${history.amount}원</td>
                            <td>${history.balance}원</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <li class="page-item"><a class="page-link" href="#">Previous</a></li>
                <li class="page-item"><a class="page-link" href="#">Next</a></li>
            </ul>
        </nav>

        </body>

        </html>