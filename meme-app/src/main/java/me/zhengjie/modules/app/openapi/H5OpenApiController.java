package me.zhengjie.modules.app.openapi;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.result.ResultBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open")
@Slf4j
public class H5OpenApiController {

    @Log("渠道撞库")
    @ApiOperation("渠道撞库")
    @PostMapping("/access/check/{accessCode}")
    public ResponseEntity<Object> accessCheck(@RequestParam MultipartFile multipartFile) {
        return ResponseEntity.ok("success");
    }

    @Log("快速联登")
    @ApiOperation("快速联登")
    @PostMapping("/access/register/{accessCode}")
    public ResponseEntity<Object> accessRegister(@RequestParam MultipartFile multipartFile) {
        return ResponseEntity.ok("success");
    }
}
