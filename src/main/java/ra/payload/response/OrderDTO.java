package ra.payload.response;

import lombok.Data;
import ra.model.entity.OrderDetail;

import java.time.LocalDate;
import java.util.List;
@Data
public class OrderDTO {
    private int orderID;
    private LocalDate orderDate;
    private float orderTotalAmount;
    private boolean orderStatus;
    private UserDTO2 userDTO2;
    private List<OrderDetailDTO> listOrderDetailDTO;
}
