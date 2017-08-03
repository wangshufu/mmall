package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wangshufu on 2017/8/3.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
