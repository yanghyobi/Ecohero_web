$(document).ready(function () {
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $('.like-button').click(function () {
        const postId = $(this).data('id');
        const icon = $(this).find('img'); // 이미지 태그 선택

        $.ajax({
            url: `/posts/${postId}/like`,
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (response) {
                // 좋아요 상태에 따라 이미지 변경
                if (response.isLiked) {
                    icon.attr('src', '/images/heartstrock.svg');
                    icon.attr('alt', '좋아요 취소');
                } else {
                    icon.attr('src', '/images/heart.svg');
                    icon.attr('alt', '좋아요');
                }

                // 좋아요 수 업데이트
                const likesCountElement = $(`#likes-count-${postId}`);
                likesCountElement.text(response.likesCount);
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert('로그인이 필요합니다.');
                    window.location.href = '/user/login';
                } else {
                    alert('좋아요 요청에 실패했습니다.');
                }
            },
        });
    });
});
