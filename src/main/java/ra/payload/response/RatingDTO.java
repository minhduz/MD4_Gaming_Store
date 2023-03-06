package ra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private int ratingID;
    private int ratingValue;
    private UserDTO2 userDTO2;
    private GameDTO gameDTO;
}
