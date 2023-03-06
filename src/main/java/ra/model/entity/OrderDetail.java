package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "OrderDetail")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetailID")
    private int orderDetailID;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "orderID")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;

    @Column(name = "orderPrice")
    private float orderPrice;
    @Column(name = "orderQuantity")
    private int orderQuantity;
    @Column(name = "orderAmount")
    private float orderAmount;

}
