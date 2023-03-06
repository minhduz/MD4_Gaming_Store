package ra.payload.request;

import lombok.Data;

@Data
public class UserDTO {
    private String oldPassword;
    private String newPassword;
}
