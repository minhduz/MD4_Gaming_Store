package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.ERole;
import ra.model.entity.Role;
import ra.model.repository.RoleRepository;
import ra.model.service.RoleService;

import java.util.Optional;
@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Optional<Role> findByRoleName(ERole roleName) {

        return roleRepository.findByRoleName(roleName);
    }
}
