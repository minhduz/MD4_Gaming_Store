package ra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO3 {
    private int catID;
    private String catName;
    private boolean catStatus;
    private String catParentName;
    private int parentID;
}
