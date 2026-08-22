document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('artistForm');

    if (!form) {
        return;
    }

    const maxSelected = Number(form.dataset.maxSelected || 3);
    const artistCards = Array.from(form.querySelectorAll('.artist'));
    const checkboxes = Array.from(form.querySelectorAll('input[name="artist"]'));
    const selectedCount = form.querySelector('[data-selected-count]');
    const selectionSummary = form.querySelector('[data-selection-summary]');
    const submitButtons = Array.from(form.querySelectorAll('button[type="submit"]'));
    const searchInput = form.querySelector('[data-artist-search]');
    const genreFilters = form.querySelector('[data-genre-filters]');
    let activeGenre = 'All';

    function checkedArtists() {
        return checkboxes.filter(function (checkbox) {
            return checkbox.checked;
        });
    }

    function updateSelectionState() {
        const selected = checkedArtists();

        artistCards.forEach(function (card) {
            const checkbox = card.querySelector('input[name="artist"]');
            card.classList.toggle('is-selected', Boolean(checkbox && checkbox.checked));
        });

        if (selectedCount) {
            selectedCount.textContent = String(selected.length);
        }

        if (selectionSummary) {
            const names = selected.map(function (checkbox) {
                return checkbox.dataset.artistName || checkbox.value.split('|')[0];
            });

            selectionSummary.textContent = names.length
                ? names.join(' · ')
                : 'Choose at least one artist to continue.';
        }

        submitButtons.forEach(function (button) {
            button.disabled = selected.length === 0;
        });
    }

    function applyFilters() {
        const query = searchInput ? searchInput.value.trim().toLowerCase() : '';

        artistCards.forEach(function (card) {
            const name = (card.dataset.artistName || '').toLowerCase();
            const genre = card.dataset.genre || '';
            const matchesSearch = !query || name.includes(query) || genre.toLowerCase().includes(query);
            const matchesGenre = activeGenre === 'All' || genre === activeGenre;
            card.hidden = !(matchesSearch && matchesGenre);
        });
    }

    function buildGenreFilters() {
        if (!genreFilters) {
            return;
        }

        const genres = Array.from(new Set(artistCards.map(function (card) {
            return card.dataset.genre;
        }).filter(Boolean))).sort();

        ['All'].concat(genres).forEach(function (genre) {
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'genre-filter-chip';
            button.textContent = genre;
            button.dataset.genreFilter = genre;
            button.setAttribute('aria-pressed', genre === 'All' ? 'true' : 'false');
            button.addEventListener('click', function () {
                activeGenre = genre;
                genreFilters.querySelectorAll('.genre-filter-chip').forEach(function (chip) {
                    chip.setAttribute('aria-pressed', String(chip === button));
                });
                applyFilters();
            });
            genreFilters.appendChild(button);
        });
    }

    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            if (checkedArtists().length > maxSelected) {
                checkbox.checked = false;
                window.alert(`You can select up to ${maxSelected} artist${maxSelected === 1 ? '' : 's'} only.`);
            }

            updateSelectionState();
        });
    });

    if (searchInput) {
        searchInput.addEventListener('input', applyFilters);
    }

    form.addEventListener('submit', function (event) {
        form.querySelectorAll('input[name="genres"]').forEach(function (input) {
            input.remove();
        });

        const selected = checkedArtists();

        if (selected.length === 0) {
            event.preventDefault();
            window.alert('Please select at least one artist.');
            return;
        }

        const genres = new Set(selected.map(function (checkbox) {
            return checkbox.dataset.genre;
        }).filter(Boolean));

        genres.forEach(function (genre) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'genres';
            input.value = genre;
            form.appendChild(input);
        });
    });

    buildGenreFilters();
    updateSelectionState();
    applyFilters();
});
