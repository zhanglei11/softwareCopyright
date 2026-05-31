package com.sva.system.service;

import com.sva.system.domain.ImageCategory;
import java.util.List;

public interface IImageCategoryService {
    List<ImageCategory> tree();
    void add(ImageCategory category, Long operatorId);
    void update(ImageCategory category);
    void deleteById(Long id);
}
