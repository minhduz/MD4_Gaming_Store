package ra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO2 {
    private int categoryID;
    private String categoryName;
    private boolean categoryStatus;
    private List<GameDTO> listGameDTO;
}
