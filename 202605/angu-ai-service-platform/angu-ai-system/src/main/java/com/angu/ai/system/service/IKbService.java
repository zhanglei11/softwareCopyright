package com.angu.ai.system.service;

import com.angu.ai.system.domain.dto.KbDTO;
import com.angu.ai.system.domain.entity.KbDocument;
import com.angu.ai.system.domain.entity.KbKnowledgeBase;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IKbService {
    List<KbKnowledgeBase> listKb();
    void createKb(KbDTO dto, Long creatorId);
    void updateKb(Long id, KbDTO dto);
    void deleteKb(Long id);
    List<KbDocument> listDocuments(Long kbId);
    void uploadDocuments(Long kbId, MultipartFile[] files);
    void deleteDocument(Long kbId, Long docId);
    void retryParse(Long kbId, Long docId);
}
