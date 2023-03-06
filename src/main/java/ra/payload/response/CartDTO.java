package ra.payload.response;

import lombok.Data;

@Data
public class CartDTO {
    private int cartID;
    private String gameName;
    private String gameMainImage;
    private int cartQuantity;
    private float cartPrice;
}
