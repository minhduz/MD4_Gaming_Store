package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Game")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameID")
    private int gameID;
    @Column(name = "gameName",columnDefinition = "nvarchar(100)")
    private String gameName;
    @Column(name = "gamePrice")
    private float gamePrice;
    @Column(name = "gameDescription",columnDefinition = "text")
    private String gameDescription;
    @Column(name= "gameDeveloper",columnDefinition = "nvarchar(100)")
    private String gameDeveloper;
    @Column(name = "gamePulisher",columnDefinition = "nvarchar(100)")
    private String gamePublisher;
    @Column(name = "gameMainImage",columnDefinition = "text")
    private String gameMainImage;
    @Column(name = "gameReleaseDate",columnDefinition = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date gameReleaseDate;
    @Column(name = "gameDiscount")
    private int gameDiscount;
    @Column(name = "gameStatus")
    private boolean gameStatus;


    @ManyToMany
    @JoinTable(name = "game_category", joinColumns = @JoinColumn(name = "gameID"), inverseJoinColumns = @JoinColumn(name = "categoryID"))
    private List<Category> listCategory;

    @ManyToMany
    @JoinTable(name = "game_platform", joinColumns = @JoinColumn(name = "gameID"), inverseJoinColumns = @JoinColumn(name = "platformID"))
    private List<Platform> listPlatform;

    @OneToMany(mappedBy = "game")
    private List<Image> listImage;

    @JsonIgnore
    @ManyToMany(mappedBy = "listGame")
    private List<User> listUser;

    @OneToMany(mappedBy = "game")
    private List<Review> listReview;

    @OneToMany(mappedBy = "game")
    private List<Rating> listRating;

    @JsonIgnore
    @OneToMany(mappedBy = "game")
    private List<OrderDetail> listOrderDetail;

    @OneToOne(mappedBy = "game",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private CartItem cartItem;
}
























