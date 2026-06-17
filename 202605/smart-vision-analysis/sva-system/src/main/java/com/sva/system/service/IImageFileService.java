package com.sva.system.service;

import com.sva.system.domain.ImageFile;
import com.sva.system.query.ImageQuery;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IImageFileService {
    List<ImageFile> list(ImageQuery query);
    ImageFile getById(Long id);
    ImageFile upload(MultipartFile file, Long categoryId, Long uploadedBy);
    void deleteById(Long id);
}
