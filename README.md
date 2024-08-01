
The Music Box

The purpose of this project is to help users discover up-and-coming artists based on the current artists they admire. 
Think of it as a musical matchmaker, tailored to individual tastes. Our goal is to introduce users to amazing new 
artists while supporting those artists on their journey to fame. 

We adopted the Model-View-Controller (MVC) design pattern to create a scalable and maintainable application. In this 
framework, the 'FeaturedArtists' model represents the featured artists and is displayed in the view, which is rendered
at an endpoint defined in the 'FeaturedArtist' controller.

For the backend, we used Spring Boot with an H2 database, Thymeleaf, and Java. Our models represent the data, the controllers 
handle user interactions, and Thymeleaf integrates backend data into the view. On the frontend, we crafted a user-friendly 
interface using HTML, CSS, and JavaScript, providing a visually appealing and intuitive experience for our users. As you'll 
soon see, when a user interacts with our featured artists page and selects an artist, the controller processes the request, 
updates the model data, and our frontend seamlessly presents recommendations for new favorite artists.
