

// import org.springframework.boot.convert.DataSizeUnit;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// //import lombok.Data;

// //@Data
// @Entity(name = "Genres")
// @Table(name = "Genres")
// public class Genres {
//     @Id
//     @GeneratedValue
//     @Column(nullable = false)

//     private long id;

//     @Column(nullable = false, columnDefinition = "TEXT")
//     private String Hip_Hop;
    
//     @Column(nullable = false, columnDefinition = "TEXT")
//     private String Country;
    
//     @Column(nullable = false, columnDefinition = "TEXT")
//     private String Rock;
    
//     @Column(nullable = false, columnDefinition = "TEXT")
//     private String Rhythm_and_Blues;
    
//     @Column(nullable = false, columnDefinition = "TEXT")
//     private String Pop;

//     @Column(nullable = false, columnDefinition = "TEXT")
//     private String Reggaeton;

//     public Genres(){

//     };

//     public Genres(String Hip_Hop, String Country, String Rock, String Rhythm_and_Blues, String Pop) {
//         this.Hip_Hop = Hip_Hop;
//         this.Country = Country;
//         this.Rock = Rock;
//         this.Rhythm_and_Blues = Rhythm_and_Blues;
//         this.Pop = Pop;
//         this.Reggaeton = Reggaeton;
//     }
// }