package ra.payload.response;

import lombok.Data;

@Data
public class ReviewDTO {
    private int reviewID;
    private String reviewContent;
    private boolean reviewStatus;
    private UserDTO2 userDTO2;
    private GameDTO gameDTO;
}
