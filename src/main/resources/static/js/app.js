// Wait for the DOM content to fully load
document.addEventListener('DOMContentLoaded', function () {
    // Define the maximum number of selectable checkboxes
    const maxSelected = 3;

    // Iterate over each label element within artist containers
    document.querySelectorAll('.artist label').forEach(function(label) {
        // Grab associated input and img elements from within the current label
        let input = label.querySelector('input');
        let img = label.querySelector('img');
        
        // Add event listener for change event on checkboxes
        input.addEventListener('change', function() {
            // Count checked checkboxes
            let checkedCount = document.querySelectorAll('input[type=checkbox]:checked').length;

            // Validate against maximum number of selectable checkboxes
            if (checkedCount > maxSelected) {
                // Uncheck the checkbox and display a warning
                input.checked = false;
                alert(`You can select up to ${maxSelected} artists only.`);
            }

            // Toggle the 'selected' class on the image, based on whether the checkbox is checked
            img.classList.toggle('selected', input.checked);
        });

        // Initialize the 'selected' class on the image based on whether the checkbox is checked on load
        img.classList.toggle('selected', input.checked);
    });

    document.getElementById('artistForm').addEventListener('submit', function(e) {
        // Clear old hidden inputs (if any)
        document.querySelectorAll('#artistForm input[name="genres"]').forEach(function(input) {
            input.remove();
        });

        // Get all checked checkboxes
        var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');
        
        // Check if at least one checkbox is checked
        if (checkboxes.length > 0) {
            // Create a Set to store unique genres
            var genres = new Set();
            
            // Iterate through checkboxes to collect genres
            checkboxes.forEach(function(checkbox) {
                // Add the genre to the Set
                genres.add(checkbox.getAttribute('data-genre'));
            });
            
            // Add hidden inputs for each genre to the form
            genres.forEach(function(genre) {
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'genres';
                input.value = genre;
                // Append the input to the form
                e.target.appendChild(input);
            });
            
            // Form will be submitted with the additional data
        } else {
            // Prevent form submission
            e.preventDefault();
            // Alert user to select at least one artist
            alert('Please select at least one artist.');
        }
    });
});