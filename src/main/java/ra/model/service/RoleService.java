package ra.model.service;

import org.springframework.stereotype.Service;
import ra.model.entity.ERole;
import ra.model.entity.Role;

import java.util.Optional;

public interface RoleService{
    Optional<Role> findByRoleName(ERole roleName);
}

