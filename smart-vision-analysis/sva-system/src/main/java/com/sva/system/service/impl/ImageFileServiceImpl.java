package com.sva.system.service.impl;

import com.sva.common.exception.ServiceException;
import com.sva.system.domain.ImageFile;
import com.sva.system.mapper.ImageFileMapper;
import com.sva.system.query.ImageQuery;
import com.sva.system.service.IImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageFileServiceImpl implements IImageFileService {

    private final ImageFileMapper imageMapper;

    @Value("${sva.upload.path:./uploads}")
    private String uploadPath;

    @Override
    public List<ImageFile> list(ImageQuery query) {
        return imageMapper.selectList(query);
    }

    @Override
    public ImageFile getById(Long id) {
        ImageFile f = imageMapper.selectById(id);
        if (f == null) throw new ServiceException(404, "图像不存在");
        return f;
    }

    @Override
    public ImageFile upload(MultipartFile file, Long categoryId, Long uploadedBy) {
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".jpg";
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = "images/" + datePath + "/" + UUID.randomUUID() + ext;
        File dest = new File(uploadPath, relativePath);
        dest.getParentFile().mkdirs();
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new ServiceException(500, "文件上传失败: " + e.getMessage());
        }
        ImageFile imageFile = new ImageFile();
        imageFile.setImageNo("IMG-" + System.currentTimeMillis());
        imageFile.setFileName(originalName);
        imageFile.setFilePath("/" + relativePath);
        imageFile.setFileFormat(ext.replace(".", "").toUpperCase());
        imageFile.setFileSize(file.getSize());
        imageFile.setCategoryId(categoryId);
        imageFile.setUploadedBy(uploadedBy);
        imageMapper.insert(imageFile);
        return imageFile;
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        imageMapper.deleteById(id);
    }
}
