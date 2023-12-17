document.addEventListener('DOMContentLoaded', function () {
    if (Hls.isSupported()) {
        var video = document.getElementById('videoPlayer');
        var hls = new Hls();

        // Thay đổi đường dẫn theo đường dẫn M3U8 của bạn
        var streamURL = 'http://localhost:8080/hls/test.m3u8';

        hls.loadSource(streamURL);
        hls.attachMedia(video);
    } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        // Đối với Safari
        video.src = 'http://localhost:8080/hls/test.m3u8';
    }
});
