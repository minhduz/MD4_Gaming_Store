package ra.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "Orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private int orderID;
    @Column(name = "orderDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderDate;
    @Column(name = "orderTotalAmount")
    private float orderTotalAmount;
    @Column(name = "orderStatus")
    private boolean orderStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> listOrderDetail;
}
