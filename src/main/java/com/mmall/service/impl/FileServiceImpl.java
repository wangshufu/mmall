package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by wangshufu on 2017/8/3.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        //获取用户上传图片的原始文件名
        String originalFilename = file.getOriginalFilename();
        //原图片名为abc.fff.jpg,originalFilename.substring(originalFilename.lastIndexOf("."))--->.jpg;所以得+1才能得到jpg
        //图片后缀/格式-->jpg
        String extensionFile = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        //因为不同的用户有可能上传的图片名一样,所以我们的图片名得改成唯一的
        String fileName = UUID.randomUUID()+"."+extensionFile;
        logger.info("开始上传文件,上传文件的文件名为{},上传的路径为{},新的文件名为{}",originalFilename,path,fileName);
        File fileDir = new File(path);
        if (!fileDir.exists()){
            //设置文件可写权限
            fileDir.setWritable(true);
            //多个级别的文件夹
            fileDir.mkdirs();
        }
        //目标文件
        File targetFile = new File(path,fileName);

        try {
            //将文件上传到tomcat的upload文件夹下
            file.transferTo(targetFile);
            //将tomcat上的文件上传到FTP文件服务器上
            FTPUtil.uploadFiles(Lists.newArrayList(targetFile));
            //删除tomcat下的文件,防止tomcat负载过大
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件失败",e);
            return null;
        }
        return targetFile.getName();
    }
}
