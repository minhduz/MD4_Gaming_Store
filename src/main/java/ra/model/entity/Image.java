package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Image")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageID")
    private int imageID;
    @Column(name = "imageUrl",columnDefinition = "text")
    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;

    @Override
    public String toString(){
        return this.imageUrl;
    }
}
