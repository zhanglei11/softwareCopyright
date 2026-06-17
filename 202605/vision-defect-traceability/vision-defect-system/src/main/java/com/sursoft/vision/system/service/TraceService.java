package com.sursoft.vision.system.service;

import com.sursoft.vision.system.vo.BatchTraceVO;
import com.sursoft.vision.system.vo.ProductTraceVO;

public interface TraceService {
    BatchTraceVO traceBatch(String batchNo, Long lineId);
    ProductTraceVO traceProduct(String serialNo);
}
