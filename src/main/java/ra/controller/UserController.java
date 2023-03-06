package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ra.dev.ProvideSendMail;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.ERole;
import ra.model.entity.Game;
import ra.model.entity.Role;
import ra.model.entity.User;
import ra.model.service.FileStorageService;
import ra.model.service.RoleService;
import ra.model.service.UserService;
import ra.payload.request.LoginRequest;
import ra.payload.request.SignupRequest;
import ra.payload.request.UserDTO;
import ra.payload.response.*;
import ra.security.CustomUserDetails;


import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService<User,Integer> userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ProvideSendMail provideSendMail;

    @GetMapping("testMail")
    public ResponseEntity<?> sendEmail(@RequestParam("email") String email){
        try {
            String jwt = tokenProvider.generateTokenEmail(email);
            provideSendMail.sendSimpleMessage(email,"Token",jwt);
            return ResponseEntity.ok("Send email successfully");
        }catch (Exception e){
            return ResponseEntity.ok("Failed to send email");

        }
    }

    @PostMapping("resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("token")String token,@RequestBody String newPassword){
        String username = tokenProvider.getUserNameFromJwt(token);
        User user = userService.findByUserName(username);
        user.setUserPassword(encoder.encode(newPassword));
        userService.saveOrUpdate(user);
        return ResponseEntity.ok("Update password successfully");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usermame is already"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setUserPassword(encoder.encode(signupRequest.getPassword()));
        user.setUserEmail(signupRequest.getEmail());
        user.setUserPhone(signupRequest.getPhone());
        user.setUserStatus(true);
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Role> listRoles = new HashSet<>();
        if (strRoles==null){
            //User quyen mac dinh
            Role userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Role modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                    case "user":
                        Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
        if(customUserDetail.isUserStatus()){
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua user
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item->item.getAuthority()).collect(Collectors.toList());

            User user = userService.findByUserName(customUserDetail.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt,user.getUserID(), user.getUserAvatar(), customUserDetail.getUsername(),customUserDetail.getUserEmail(),
                    customUserDetail.getUserPhone(),listRoles));
        }else {
            return ResponseEntity.ok("Your account have been lock");
        }

    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllUser")
    public List<UserDTO2> getAllUser(){
        List<User> listAllUser = userService.findAll();
        List<User> listUser = new ArrayList<>();
        List<UserDTO2> listUserDTO2 = new ArrayList<>();
        for (User user:listAllUser) {
            if(user.getListRoles().size()==1){
                listUser.add(user);
            }
        }
        for (User user:listUser) {
            UserDTO2 userDTO2 = new UserDTO2();
            userDTO2.setUserID(user.getUserID());
            userDTO2.setUserName(user.getUserName());
            userDTO2.setUserPassword(user.getUserPassword());
            userDTO2.setUserPassword(user.getUserPassword());
            List<GameDTO> listGameDTO = new ArrayList<>();
            for (Game game:user.getListGame()) {
                GameDTO gameDTO = new GameDTO();
                gameDTO.setGameID(game.getGameID());
                gameDTO.setGameMainImage(game.getGameMainImage());
                gameDTO.setGameName(game.getGameName());
                gameDTO.setGamePrice(game.getGamePrice());
                gameDTO.setGameStatus(game.isGameStatus());
                listGameDTO.add(gameDTO);
            }
            userDTO2.setListGameDTO(listGameDTO);
            listUserDTO2.add(userDTO2);
        }

        return listUserDTO2;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllMod")
    public List<User> getAllMod(){
        List<User> listAllUser = userService.findAll();
        List<User> listUser = new ArrayList<>();
        for (User user:listAllUser) {
            if(user.getListRoles().size()==2){
                listUser.add(user);
            }
        }
        return listUser;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("userDetail")
    public User getUserByID(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.findByID(userDetails.getUserID());
    }


    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/updateStatus/{userID}")
    public void updateUser(@PathVariable("userID")int userID, @RequestBody User user){
        User userUpdate = userService.findByID(userID);
        userUpdate.setUserStatus(user.isUserStatus());
        userService.saveOrUpdate(userUpdate);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("updateRole/{userID}")
    public User updateRole(@PathVariable("userID")int userID,@RequestBody SignupRequest signupRequest){
        User userUpdate = userService.findByID(userID);
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Role> listRoles = new HashSet<>();
        if (strRoles==null){
            //User quyen mac dinh
            Role userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch (role){
                    case "admin":
                        Role adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRole);
                    case "moderator":
                        Role modRole = roleService.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(modRole);
                    case "user":
                        Role userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRole);
                }
            });
        }
        userUpdate.setListRoles(listRoles);
        userService.saveOrUpdate(userUpdate);
        return userUpdate;
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/updateInfo")
    public void updateUserInfo(@RequestBody User user){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userUpdate = userService.findByID(userDetails.getUserID());
        userUpdate.setUserAvatar(user.getUserAvatar());
        userUpdate.setUserEmail(user.getUserEmail());
        userUpdate.setUserFullName(user.getUserFullName());
        userUpdate.setUserPhone(user.getUserPhone());
        userService.saveOrUpdate(userUpdate);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/updatePassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody UserDTO userDTO){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userUpdate = userService.findByID(userDetails.getUserID());
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        boolean checkOldPass = bc.matches(userDTO.getOldPassword(), userUpdate.getUserPassword());
        if(!checkOldPass){
            return ResponseEntity.ok("Incorrect Password");
        }
        boolean checkNewPass = bc.matches(userDTO.getNewPassword(), userUpdate.getUserPassword());
        if(checkNewPass){
            return ResponseEntity.ok("The new password is the same as the old password");
        }
        userUpdate.setUserPassword(encoder.encode(userDTO.getNewPassword()));
        userService.saveOrUpdate(userUpdate);
        return ResponseEntity.ok("The new password has been changed successfully");
    }

}
