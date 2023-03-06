package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Platform")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platformID")
    private int platformID;
    @Column(name = "platformName",columnDefinition = "nvarchar(100)")
    private String platformName;

    @JsonIgnore
    @ManyToMany(mappedBy = "listPlatform")
    private List<Game> listGame;
}
