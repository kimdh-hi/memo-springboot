isAsc = false
sortProperty = "createdAt"
isClickCountAsc = false
isDateAsc = false
size = 5
token = null
type = "all"
current_memo_id = null
current_page = 1
isPublic=true

$(document).ready(function() {

    if (localStorage.getItem('token') != null) {
        token = localStorage.getItem('token')
        //decodeToken(token)
        $('#signin_btn').hide()
        $('#signup_btn').hide()
        $('#logout_btn').show()

    } else {
        $('#signin_btn').show()
        $('#signup_btn').show()
        $('#logout_btn').hide()
    }

    $("#select_page_limit").change(function () {
        size = $(this).val();
        getMemoList(current_page)
    })

    getMemoList(1)

    $.ajaxSetup({
        error: function (jqXHR, exception) {
            switch (jqXHR.status) {
                case 401:
                    alert('인증 에러!!');
                    break;
                case 423:
                    alert('중복된 id!!');
                    break;
            }
        },
        beforeSend: function (xhr) {
            if (localStorage.getItem('token') != null) {
                xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('token'));
            }
        }
    });
})


function toggleButton() {
    if ($("#post-box").css("display") == "none") {
        $('#edit-button').hide()
        $('#save-button').show()
        $("#post-box").show()
        $("#btn-post-box").text("포스트 박스 닫기")
    } else {
        $('#edit-button').hide()
        $('#save-button').show()
        $("#post-box").hide()
        $('#post-title').val('');
        $('#post-comment').val('');
        $("#btn-post-box").text("포스트 박스 열기")
    }
}

function open_signin_modal() {
    $('#signinModal').modal('show');
}

function open_signup_modal() {
    $('#signupModal').modal('show');
}

function signup() {
    console.log('회원가입')
    let signup_id = $('#signup-id').val()
    let signup_pw = $('#signup-pw').val()

    let doc = {
        "username": signup_id,
        "password": signup_pw
    }

    $.ajax({
        type: "POST",
        url: "/user/signup",
        contentType: "application/json;charset=utf8;",
        data: JSON.stringify(doc),
        success: function (res) {
            alert(res['message'])
            window.location.reload();
        }
    })
}

function signin() {
    console.log('로그인')
    let signup_id = $('#username').val()
    let signup_pw = $('#password').val()

    let doc = {
        "username": signup_id,
        "password": signup_pw
    }

    $.ajax({
        type: "POST",
        url: "/user/signin",
        contentType: "application/json;charset=utf8;",
        data: JSON.stringify(doc),
        success: function (res) {
            localStorage.setItem("token", res['token'])
            alert(res['message'])
            window.location.reload()
        }
    })
}

function logout() {
    localStorage.clear()
    window.location.reload()
}

function postMemo() {
    title = $("#post-title").val()
    contents = $("#post-comment").val()

    doc = {"title": title, "contents": contents}

    $.ajax({
        type: "POST",
        url: "/memo",
        contentType: "application/json;charset=utf-8;",
        data: JSON.stringify(doc),
        success: function (res) {
            alert(res)
            window.location.reload()
        }
    })
}

function getMemo(memo_id) {
    current_memo_id = memo_id

    $.ajax({
        type: "GET",
        url: `/memo/${memo_id}`,
        success: function (res) {
            console.log(res)
            let title = res['title']
            let contents = res['contents']
            $('#modal-title').html(title);
            $('#modal-content').html(contents);
            $('#articleModal').modal('show');

            let comments = res['comments']

            $('#modal-comment').empty()
            for (let i=0;i<comments.length;i++) {
                let tmp_html = `<li class="list-group-item">${comments[i]}</li>`
                $('#modal-comment').append(tmp_html)
            }

            getMemoList(current_page)
        }
    })
}

function getMemoList(page) {
    $('#memo-body').empty()

    $.ajax({
        type: "GET",
        url: `/memos?page=${page}&size=${size}&isAsc=${isAsc}&field=${sortProperty}&isPublic=${isPublic}`,
        success: function (res) {
            console.log(res)
            memos = res['content']
            current_page = memos['number'] + 1
            for (let i = 0; i < memos.length; i++) {
                memo_number = (i + 1) + res['size'] * res['number']
                let username = memos[i]['isAnonymous'] ? "비회원" : memos[i]['user']['username']
                let tmp_html = `<tr>
                                  <th scope="row">${memo_number}</th>
                                  <td><a onclick="getMemo('${memos[i]['id']}')">${memos[i]['title']}</a></td>
                                  <td>${username}</td>
                                  <td>${memos[i]['createdAt']}</td>
                                  <td>${memos[i]['clickCount']}</td>
                                  <td>
                                    <button onclick="deleteMemo('${memos[i]['id']}')" type="button" class="btn btn-danger">삭제</button>
                                  </td>
                                  <td>
                                    <button onclick="showUpdateMemoForm('${memos[i]['id']}')" type="button" class="btn btn-primary">수정</button>
                                  </td>
                                </tr>`
                $('#memo-table').append(tmp_html)

                makePagingButtons(res['totalPages'], res['number']+1)
            }
        }
    })
}

function makePagingButtons(total_page, page) {
    $('#pagination-buttons').empty()
    current_page = page
    tmp_html = ``
    for (let i = 0; i < total_page; i++) {
        if (current_page == i + 1) {
            tmp_html = `<li class="page-item active"><a class="page-link" onclick="getMemoList('${i + 1}')">${i + 1}</a></li>`
        } else {
            tmp_html = `<li class="page-item"><a class="page-link" onclick="getMemoList('${i + 1}')">${i + 1}</a></li>`
        }

        $('#pagination-buttons').append(tmp_html)
    }
}

function deleteMemo(memoId) {
    $.ajax({
        type: "DELETE",
        url: `/memo/${memoId}`,
        success: function(res) {
            console.log(res)
            getMemoList(current_page)
        }
    })
}

function showUpdateMemoForm(memo_id) {
    $.ajax({
        type: "GET",
        url: `/memo/${memo_id}`,
        success: function (res) {
            let memo_id = res['id']
            let title = res['title']
            let contents = res['contents']

            current_memo_id = memo_id

            $("#post-id").val(memo_id)
            $("#post-title").val(title)
            $('#post-comment').val(contents)
            $('#btn-post-box').text("포스트 박스 닫기")

            $('#edit-button').show()
            $('#save-button').hide()

            $('#post-box').show()
        }
    })
}

function memoUpdate() {
    memo_id = $('#post-id').val()
    title = $('#post-title').val()
    contents = $('#post-comment').val()
    dic = {
        "title": title,
        "contents": contents
    }
    $.ajax({
        type: "PUT",
        url: `/memo/${memo_id}`,
        contentType: "application/json; charset=utf-8;",
        data: JSON.stringify(dic),
        success: function (res) {
            alert(res)
            window.location.reload()
        }
    })
}

function sortByClickCount() {
    if (isClickCountAsc) {
        isClickCountAsc = false
        isAsc = false
        sortProperty = "clickCount"
        $('#sort-click-count-icon').text("⬇️")
    } else {
        isClickCountAsc = true
        isAsc = true
        sortProperty = "clickCount"
        $('#sort-click-count-icon').text("⬆️")
    }
    getMemoList(current_page)
}

function sortByDate() {
    if (isDateAsc) {
        isDateAsc = false
        isAsc = false
        sortProperty = "createdAt"
        $('#sort-date-icon').text("⬇️")
    } else {
        isDateAsc = true
        isAsc = true
        sortProperty = "createdAt"
        $('#sort-date-icon').text("⬆️")
    }
    getMemoList(current_page)
}

function setViewType(flag) {
    isPublic = flag
    getMemoList(current_page)
}

function saveComment() {
    let contents = $('#comment').val()
    dic = {
        "contents": contents,
    }

    $.ajax({
        type: "POST",
        url: `/comment/${current_memo_id}`,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(dic),
        success: function (res) {
            if (res.result == "success") {
                alert(res.message)
                $('#comment').val(' ')
                let tmp_html = `<li class="list-group-item">${contents}</li>`
                $('#modal-comment').append(tmp_html)
            }
        }
    })
}

