package me.zhengjie.modules.jdfq.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.service.HxUserService;
import me.zhengjie.service.dto.ChannelQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stat")
public class StatController {

    /**
     * 渠道UV
     * 新用户注册
     * 产品UV
     */
    private final HxUserService hxUserService;
    @GetMapping("/user/register")
    public ResponseEntity<Object> userRegister(ChannelQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(hxUserService.statUserRegisterNum(), HttpStatus.OK);
    }
}
