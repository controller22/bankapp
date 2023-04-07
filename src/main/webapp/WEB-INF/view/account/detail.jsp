<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@include file="../layout/header.jsp" %>
        <h1>계좌상세보기</h1>
        <hr />
        <div id="user-box" class="user-box">
        </div>

          <div class="input-group" style="width: 30%; float: right;">
                    <input type="text" class="form-control" id="search" placeholder="계좌번호로 검색">
                    <div class="input-group-append">
                        <button type="button" id="searchButton" onclick="search()">검색</button>
                    </div>
                </div>



        <div class="list-box">
            <div style="display: flex; ">
                <button id="all" style="margin-right: 20px;" onclick="getgubun('all')">전체</button>
                <button id="deposit" style="margin-right: 20px;" onclick="getgubun('deposit')">입금</button>
                <button id="withdraw" style="margin-right: 20px;" onclick="getgubun('withdraw')">출금</button>
                <br />
            </div>

            <div id="tes-box">
            </div>

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
                </tbody>
            </table>
        </div>
       <div id="listbutton">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <button id="previous" type="button" class="btn btn-outline-primary" disabled onclick="page('previous')">previous</button>
                <button id="next" type="button" class="btn btn-outline-primary" onclick="page('next')">next</button>
            </ul>
        </nav>
        </div>
        <script>
        
            let localPage = 1;
            let gubun = "all";

         function getAccountData() {
            let id = $('#accountId').val();
                
            $.ajax({
                type: 'GET',
                url: `/api/account/${id}?gubun`+gubun,
            }).done(function (data) {
                
                let userBox = `
                <div class="user-box">`+
                data.data.adto.fullname+`<br />
                    계좌번호 : `+data.data.adto.number+`<br />
                    잔액 : `+ data.data.adto.balance +`원
                </div>`;
                $('#user-box').html(userBox);
                console.log(data.data.lastpage);

               
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.error(textStatus + ' : ' + errorThrown);
            });
        }

        $(document).ready(function() {
        let accountId = $('#accountId').val();
        
        // 계좌 정보 출력
        getAccountData();

        // 거래 내역 조회
        getgubun('all');
        });



            function getgubun(selectedGubun) {
                gubun = selectedGubun;

                $.ajax({
                    type: 'GET',
                    url: `/api/account/${id}?gubun=` + gubun,
                }).done(function (data) {
                    // Update only transaction history
                    $('#account-data').empty();

                    let hdtoList = data.data.hdtoList;
                    // let lastpage = data.data.lastpage;
                    
                    let ta = data.data.lastpage;
                    let ela = '<input type="hidden" id="lastpage" value=' + ta + '>';
                    $('#tes-box').empty();
                    $('#tes-box').append(ela);
                    $(document).ready(function() {
                    $("#tes").val(data.data.lastpage);
                    });
                // $("#tes").val(data.data.lastpage);



                        for (let i = 0; i < hdtoList.length; i++) {
                            let el =
                                `<tr>
                                <td>`+ hdtoList[i].createdAt + `</td>
                                <td>`+ hdtoList[i].sender + `</td>
                                <td>`+ hdtoList[i].receiver + `</td>
                                <td>`+ hdtoList[i].amount + `원</td>
                                <td>`+ hdtoList[i].balance + `원</td>
                                </tr>`;
                            $('#account-data').append(el);
                        }
                    
                    $('#previous').attr('disabled', 'disabled');
                    if($('#lastpage').val()==1){
                            $('#next').attr('disabled', 'disabled');
                        }
                    $('#next').removeAttr('disabled');
                    
                    localPage = 1;
                    $("#search").val("");
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error(textStatus + ' : ' + errorThrown);
                });
            }



            function page(direction) {
                if (direction == 'next') {
                    localPage = localPage + 1;
                } else if (direction == 'previous') {
                    localPage = localPage - 1;
                }
            
                $.ajax({
                    type: 'get',
                    url: `/api/account/${id}/next?page=` + localPage + "&gubun=" + gubun,
                })
                .done(function (data) {
                    if (direction == 'next') {
                        $('#previous').removeAttr('disabled');
                        if (localPage == $('#lastpage').val() || $('#lastpage').val()==1) {
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
                
                    for (let i = 0; i < data.data.length; i++) {
                        let el =
                            `<tr>
                            <td>`+ data.data[i].createdAt + `</td>
                            <td>`+ data.data[i].sender + `</td>
                            <td>`+ data.data[i].receiver + `</td>
                            <td>`+ data.data[i].amount + `원</td>
                            <td>`+ data.data[i].balance + `원</td>
                        </tr>`;
                        $('#account-data').append(el);
                    }
                    console.log(data.data);
                    console.log(data);
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error(textStatus + ' : ' + errorThrown);
                });
            }


            function search() {
                let data = {
                    searchString: $("#search").val(),
                    gubun: gubun,
                    localPage: localPage
                };
                $.ajax({
                    type: "post",
                    url: `/api/account/${id}/search` ,
                    contentType: "application/json;charset=UTF-8",
                    data: JSON.stringify(data),
                    dataType: "json"
                })
                    .done(function (data){
                        let ta = data.data.lastpage;
                        let elb = '<input type="hidden" id="lastpage" id="lastpage" value=' + ta + '>';
                        console.log(localPage);
                        // let lastpage = data.data.lastpage;
                        $('#tes-box').empty();
                        $('#tes-box').append(elb);
                        $('#account-data').empty();

                        for (let i = 0; i < data.data.dsDto.length; i++) {
                            let el =
                            `<tr>
                                <td>`+ data.data.dsDto[i].createdAt + `</td>
                                <td>`+ data.data.dsDto[i].sender + `</td>
                                <td>`+ data.data.dsDto[i].receiver + `</td>
                                <td>`+ data.data.dsDto[i].amount + `원</td>
                                <td>`+ data.data.dsDto[i].balance + `원</td>
                            </tr>`;
                            $("#account-data").append(el);
                        }

                        $('#listbutton').empty();
                        let ela =
                        `<nav aria-label="Page navigation example">
                        <ul class="pagination justify-content-center">
                        <button id="previous"  type="button" class="btn btn-outline-primary" disabled onclick="page1('previous')">previous</button>
                        <button id="next" type="button" class="btn btn-outline-primary" onclick="page1('next')">next</button>
                        </ul>
                        </nav>`;
                        $("#listbutton").append(ela);
                       
                    $('#previous').attr('disabled', 'disabled');
                    if($('#lastpage').val()==1){
                            $('#next').attr('disabled', 'disabled');
                        }
                    $('#next').removeAttr('disabled');
                    localPage = 1;
                    
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error(textStatus + ' : ' + errorThrown);
                });

            }
    
            function page1(direction) {
                if (direction == 'next') {
                    localPage = localPage + 1;
                } else if (direction == 'previous') {
                    localPage = localPage - 1;
                }
                let data = {
                    searchString: $("#search").val(),
                    gubun: gubun,
                    localPage: localPage
                };
            
                $.ajax({
                    type: "post",
                    url: `/api/account/${id}/next1` ,
                    contentType: "application/json;charset=UTF-8",
                    data: JSON.stringify(data),
                    dataType: "json"
                })
                .done(function (data) {
                    if (direction == 'next') {
                        $('#previous').removeAttr('disabled');
                        if (localPage == $('#lastpage').val() || $('#lastpage').val()==1) {
                            $('#next').attr('disabled', 'disabled');
                        }
                        if (localPage == 1 && $('#lastpage').val()==1) {
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
                
                    for (let i = 0; i < data.data.length; i++) {
                        let el =
                            `<tr>
                            <td>`+ data.data[i].createdAt + `</td>
                            <td>`+ data.data[i].sender + `</td>
                            <td>`+ data.data[i].receiver + `</td>
                            <td>`+ data.data[i].amount + `원</td>
                            <td>`+ data.data[i].balance + `원</td>
                        </tr>`;
                        $('#account-data').append(el);
                    }
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.error(textStatus + ' : ' + errorThrown);
                });
            }
            


        </script>

        </body>

        </html>