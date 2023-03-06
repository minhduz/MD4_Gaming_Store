package ra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO2 {
    private int userID;
    private String userName;
    private String userPassword;
    private String userEmail;
    private List<GameDTO> listGameDTO;
}
