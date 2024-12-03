$(document).ready(function () {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    $('#commentForm').submit(function (e) {
        e.preventDefault(); // 기본 폼 제출 동작 중단

        const postId = /*[[${post.id}]]*/ 0;
        const content = $('#commentContent').val();

        if (!content.trim()) {
            alert('댓글 내용을 입력하세요.');
            return;
        }

        $.ajax({
            url: `/posts/${postId}/comments`,
            type: 'POST',
            data: { content: content },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (response) {
                alert(response);
                $('#commentContent').val(''); // 댓글 입력란 초기화
                location.reload(); // 새로고침으로 댓글 갱신
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert('로그인이 필요합니다.');
                    window.location.href = '/user/login';
                } else {
                    alert('댓글 작성에 실패했습니다.');
                }
            },
        });
    });
});

$(document).ready(function () {
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $('.comment-like-button').click(function () {
        const commentId = $(this).data('id');
        const button = $(this);

        $.ajax({
            url: `/comments/${commentId}/like`,
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (response) {
                const likesCountElement = $(`#comment-likes-count-${commentId}`);
                let likesCount = parseInt(likesCountElement.text());
                if (button.text() === '좋아요') {
                    button.text('좋아요 취소');
                    likesCountElement.text(likesCount + 1);
                } else {
                    button.text('좋아요');
                    likesCountElement.text(likesCount - 1);
                }
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert('로그인이 필요합니다.');
                    window.location.href = '/user/login';
                } else {
                    alert('좋아요 요청에 실패했습니다.');
                }
            }
        });
    });
});
