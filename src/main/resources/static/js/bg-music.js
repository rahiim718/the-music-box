// Background music autoplay helper.
//
// Browsers block audio autoplay-with-sound until the user has interacted
// with the page, so a plain <audio autoplay> tag frequently fails silently
// (this is what caused the original background music to appear broken).
// This tries to play immediately, and if the browser blocks it, starts
// playback on the user's first click/tap/keypress instead.
(function () {
    var audio = document.getElementById("bg-music");
    if (!audio) return;

    var tryPlay = function () {
        var playPromise = audio.play();
        if (playPromise !== undefined) {
            playPromise.catch(function () {
                // Autoplay was blocked - wait for the first user interaction.
                var start = function () {
                    audio.play().catch(function () {});
                    document.removeEventListener("click", start);
                    document.removeEventListener("keydown", start);
                    document.removeEventListener("touchstart", start);
                };
                document.addEventListener("click", start, { once: true });
                document.addEventListener("keydown", start, { once: true });
                document.addEventListener("touchstart", start, { once: true });
            });
        }
    };

    tryPlay();
})();
