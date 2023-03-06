package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Platform;
import ra.model.service.PlatformService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/platform")
public class PlatformController {
    @Autowired
    PlatformService<Platform,Integer> platformService;
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("getAll")
    public List<Platform> getAll(){
        return platformService.findAll();
    }
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("create")
    public void createPlatform(@RequestBody Platform platform){
        platformService.saveOrUpdate(platform);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{platformID}")
    public void updatePlatform(@PathVariable("platformID") int platformID,@RequestBody Platform platform){
        Platform platformUpdate = platformService.findByID(platformID);
        platformUpdate.setPlatformName(platform.getPlatformName());
        platformService.saveOrUpdate(platformUpdate);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{platformID}")
    public void deletePlatform(@PathVariable("platformID") int platformID){
        platformService.deleteByID(platformID);
    }

}
