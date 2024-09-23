package com.stedu.mall.goodsservice.config;

import cn.hutool.core.util.IdUtil;
import com.stedu.mall.common.bean.RespBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping({"/category", "/goods"})
public class UploadController {
    @Value("${stedu.picDir}")
    private String picDir;

    @RequestMapping("/upload")
    public RespBean upload(MultipartFile pic) throws IOException {
        //生成图片的名字 - 唯一 - 雪花算法id.后缀
        String filename = pic.getOriginalFilename();
        //获取图片名的后缀
        String suffix = filename.substring(filename.lastIndexOf('.'));
        //生成唯一的文件名
        filename = IdUtil.getSnowflakeNextIdStr() + suffix;
        //保存图片
        pic.transferTo(new File(picDir + filename));
        //返回
        return RespBean.ok("上传成功", filename);
    }
}
