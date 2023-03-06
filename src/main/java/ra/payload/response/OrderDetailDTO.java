package ra.payload.response;

import lombok.Data;

import java.util.List;
@Data
public class OrderDetailDTO {
    private int orderDetailID;
    private float orderPrice;
    private int orderQuantity;
    private float orderAmount;
    private GameDTO gameDTO;
}
