package inz_proj_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NumberOfExpiredPasswords {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer counter;
    private LocalDateTime date;

    private Long userID;

    public NumberOfExpiredPasswords(Integer counter, Long userID) {
        this.counter = counter;
        this.date = LocalDateTime.now();
        this.userID = userID;
    }
}
