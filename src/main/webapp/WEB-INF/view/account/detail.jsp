<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>
        <input type="hidden" id="tes" value="`${Criteria.page}`">
        <h1>계좌상세보기</h1>
        <hr />
        <div class="user-box">
            ${aDto.fullname}<br />
            계좌번호 : ${aDto.number}<br />
            잔액 : ${aDto.balance}원
        </div>
        <div class="list-box">
            <div style="display: flex; ">
                <button id="all" style="margin-right: 20px;" onclick="getgubun('all')">전체</button>
                <button id="deposit" style="margin-right: 20px;" onclick="getgubun('deposit')">입금</button>
                <button id="withdraw" style="margin-right: 20px;" onclick="getgubun('withdraw')">출금</button>
                <br />
            </div>
            <input type="hidden" name="lastpage" id="lastpage" value="${lastpage}" />
            

            <script>
                let localPage = 1;
                let gubun = "all";

                function getgubun(selectedGubun) {
                    gubun = selectedGubun;
                    $.ajax({

                        type: 'GET',
                        url: `/account/${aDto.id}?gubun=` + gubun,
                        // url: `/api/account/${aDto.id}?gubun=` + gubun,


                        success: function (data) {
                            console.log("data",data);1

                            // 거래 내역만 업데이트
                            $('#account-data').empty();
                            $('#account-data').append($(data).find('#account-data').html());
                            $('#lastpage').val($(data).find('#lastpage').val());
                            $('#lastpage').append($(data).find('#lastpage').html());

                            // console.log()
                            $('#previous').attr('disabled', 'disabled');
                            $('#next').removeAttr('disabled');
                            localPage = 1;
                            
                            // $.get(`/account/${aDto.id}?gubun=${gubun}&page=${localPage}`, function (data) {
                            //     endpage = $(data).find('#lastpage').val();
                            //     console.log(endpage);
                            // });
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.error(textStatus + ' : ' + errorThrown);
                        }
                    });
                }


                function page(direction) {
                    if (direction == 'next') {
                        localPage = localPage + 1;
                    } else if (direction == 'previous') {
                        localPage = localPage - 1;
                    }

                    $.ajax({
                        type: 'GET',
                        url: `/account/${aDto.id}/next?page=` + localPage + "&gubun=" + gubun,

                        success: function (data) {
                            if (direction == 'next') {
                                $('#previous').removeAttr('disabled');
                                if (localPage == $('#lastpage').val()) {
                                    $('#next').attr('disabled', 'disabled');
                                }
                            } else if (direction == 'previous') {
                                $('#next').removeAttr('disabled');
                                if (localPage <= 1) {
                                    $('#previous').attr('disabled', 'disabled');
                                }
                            }
                            // Update only the transaction history
                            $('#account-data').empty();

                            for (let i = 0; i < data.length; i++) {
                                let el =
                                    `<tr>
                                    <td>`+ data[i].createdAt + `</td>
                                    <td>`+ data[i].sender + `</td>
                                    <td>`+ data[i].receiver + `</td>
                                    <td>`+ data[i].amount + `원</td>
                                    <td>`+ data[i].balance + `원</td>
                                </tr>`;
                                $('#account-data').append(el);
                            }
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
                <button id="previous" type="button" class="btn btn-outline-primary" disabled onclick="page('previous')">previous</button>
                <button id="next" type="button" class="btn btn-outline-primary" onclick="page('next')">next</button>
            </ul>
        </nav>




        </body>

        </html>