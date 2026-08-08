// Click-to-play video previews for the recommendations page.
//
// Loading a live YouTube iframe for every single recommendation card at once
// is unnecessary(each one pulls in its own player script and network
// requests) and can cause some of the videos to fail to render, especially
// when several cards are on screen together. Instead we show a lightweight
// thumbnail with a play button, and only create the real embed once the
// visitor actually wants to watch that video.
document.addEventListener("DOMContentLoaded", function () {
  var videoContainers = document.querySelectorAll(".rec-card-video[data-embed]");

  videoContainers.forEach(function (container) {
    var embedUrl = container.getAttribute("data-embed");
    var videoId = extractYouTubeId(embedUrl);

    if (videoId) {
      container.style.backgroundImage =
        "url(https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg)";
    }

    var playButton = container.querySelector(".video-play-btn");
    if (!playButton) {
      return;
    }

    playButton.addEventListener("click", function () {
      var iframe = document.createElement("iframe");
      iframe.src = embedUrl + (embedUrl.indexOf("?") === -1 ? "?" : "&") + "autoplay=1";
      iframe.frameBorder = "0";
      iframe.allow = "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture";
      iframe.allowFullscreen = true;
      iframe.title = "Video player";

      container.classList.add("is-playing");
      container.innerHTML = "";
      container.appendChild(iframe);
    });
  });

  function extractYouTubeId(url) {
    if (!url) {
      return null;
    }
    var match = url.match(/embed\/([a-zA-Z0-9_-]+)/);
    return match ? match[1] : null;
  }
});
