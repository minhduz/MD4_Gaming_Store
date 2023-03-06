package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Category")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryID")
    private int categoryID;
    @Column(name = "categoryName",columnDefinition = "nvarchar(50)")
    private String categoryName;
    @Column(name = "categoryStatus")
    private boolean categoryStatus;
    @Column(name = "parentID")
    private Integer parentID;

    @JsonIgnore
    @ManyToMany(mappedBy = "listCategory")
    private List<Game> listGame;

}
