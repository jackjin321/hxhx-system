package me.zhengjie.modules.jdfq.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface XfProductService {

    Map<String, String> updateAvatar(MultipartFile multipartFile);
}
