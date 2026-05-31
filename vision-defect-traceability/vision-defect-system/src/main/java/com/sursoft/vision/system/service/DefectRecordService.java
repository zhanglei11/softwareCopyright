package com.sursoft.vision.system.service;

import com.sursoft.vision.common.core.TableDataInfo;
import com.sursoft.vision.system.domain.DefectImage;
import com.sursoft.vision.system.dto.DefectDisposeDTO;
import com.sursoft.vision.system.query.DefectRecordQuery;
import com.sursoft.vision.system.vo.DefectRecordVO;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface DefectRecordService {
    TableDataInfo<DefectRecordVO> list(DefectRecordQuery query);
    DefectRecordVO getById(Long id);
    void dispose(Long id, DefectDisposeDTO dto, Long operatorId);
    List<DefectImage> getImages(Long id);
    void exportExcel(DefectRecordQuery query, HttpServletResponse response);
}
