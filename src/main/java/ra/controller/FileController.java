package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ra.payload.response.ResponseFileInfo;
import ra.model.service.FileStorageService;

import java.nio.file.Path;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/uploadFile")
    public ResponseEntity<List<ResponseFileInfo>> uploadFile(@RequestParam("files")MultipartFile[] files){
        Map<String,Path> listPath = new HashMap<>();
        Arrays.asList(files).forEach(file->{
            Path path = fileStorageService.uploadFile(file);
            listPath.put(file.getOriginalFilename(),path);
        });
        List<ResponseFileInfo> listFileInfo = new ArrayList<>();
        for (String key:listPath.keySet()) {
            String url = MvcUriComponentsBuilder.fromMethodName(FileController.class,"getFile",
                    listPath.get(key).getFileName().toString()).build().toString();
            listFileInfo.add(new ResponseFileInfo(key,url));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listFileInfo);
    }



    @GetMapping("/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName){
        Resource file = fileStorageService.load(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+file.getFilename()+"\"").body(file);
    }
}
