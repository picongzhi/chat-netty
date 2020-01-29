package com.pcz.chat.utils;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author picongzhi
 */
@Component
public class FastDFSClient {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传文件
     *
     * @param multipartFile MultipartFile
     * @return 文件路径
     * @throws IOException
     */
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadFile(
                multipartFile.getInputStream(),
                multipartFile.getSize(),
                FilenameUtils.getExtension(multipartFile.getOriginalFilename()),
                null);

        return storePath.getPath();
    }

    /**
     * 上传二维码
     *
     * @param multipartFile MultipartFile
     * @return 文件路径
     * @throws IOException
     */
    public String uploadQRCode(MultipartFile multipartFile) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadFile(
                multipartFile.getInputStream(),
                multipartFile.getSize(),
                "png",
                null);

        return storePath.getPath();
    }

    /**
     * 上传base64图片并生成缩略图
     *
     * @param multipartFile MultipartFile
     * @return 文件路径
     * @throws IOException
     */
    public String uploadBase64(MultipartFile multipartFile) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                multipartFile.getInputStream(),
                multipartFile.getSize(),
                "png",
                null);

        return storePath.getPath();
    }
}
