package ra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO2 {
    private int gameID;
    private String gameName;
    private float gamePrice;
    private String gameMainImage;
}
