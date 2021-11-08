isAsc = true
size = 5
token = null
type = "all"
current_memo_id = null

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

function getMemoList(page) {
    $('#memo-body').empty()

    $.ajax({
        type: "GET",
        url: `/memos?page=${page}&size=${size}&isAsc=${isAsc}&type=${type}`,
        success: function (res) {
            console.log(res)
            memos = res['memos']
            for (let i = 0; i < memos.length; i++) {
                memo_number = (i + 1) + limit * (current_page - 1)
                let tmp_html = `<tr>
                                  <th scope="row">${memo_number}</th>
                                  <td><a onclick="getMemo('${memos[i]['memo_id']}')">${memos[i]['title']}</a></td>
                                  <td>${memos[i]['writer_id']}</td>
                                  <td>${memos[i]['date']}</td>
                                  <td>${memos[i]['clickCount']}</td>
                                  <td>
                                    <button onclick="delete_memo('${memos[i]['memo_id']}')" type="button" class="btn btn-danger">삭제</button>
                                  </td>
                                  <td>
                                    <button onclick="show_memo_edit_form('${memos[i]['memo_id']}')" type="button" class="btn btn-primary">수정</button>
                                  </td>
                                </tr>`
                $('#memo-table').append(tmp_html)

                makePagingButtons(res['paging'])
            }

        }
    })
}

function set_type(type_param) {
    if (type_param == "my" && token == null) return false

    if (type_param == type) return false

    type = type_param
    getMemos(1)
}

function saveComment() {

    let comment = $('#comment').val()

    console.log(comment)

    $.ajax({
        type: "POST",
        url: "/comment",
        data: {comment: comment, memo_id: current_memo_id},
        success: function (res) {
            if (res.result == "success") {
                alert(res.msg)
                $('#comment').val(' ')
                let tmp_html = `<li class="list-group-item">${comment}</li>`
                $('#modal-comment').append(tmp_html)
            }
        }
    })
}

