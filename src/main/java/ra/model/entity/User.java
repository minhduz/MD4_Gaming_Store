package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int userID;
    @Column(name = "userName",columnDefinition = "nvarchar(50)",nullable = false)
    private String userName;
    @Column(name = "userPassword",columnDefinition = "nvarchar(255)",nullable = false)
    private String userPassword;
    @Column(name = "userFullName",columnDefinition = "nvarchar(100)")
    private String userFullName;
    @Column(name = "userAvatar",columnDefinition = "text")
    private String userAvatar;
    @Column(name = "userPhone",columnDefinition = "nvarchar(10)")
    private String userPhone;
    @Column(name = "userEmail",columnDefinition = "nvarchar(50)")
    private String userEmail;
    @Column(name = "userStatus")
    private boolean userStatus;

    @ManyToMany
    @JoinTable(name = "following", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "gameID"))
    private List<Game> listGame;

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Review> listReview;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "roleID"))
    private Set<Role> listRoles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Rating> listRating;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Orders> listOrders;

    @OneToMany(mappedBy = "user")
    private List<CartItem> listCartItem;
}
