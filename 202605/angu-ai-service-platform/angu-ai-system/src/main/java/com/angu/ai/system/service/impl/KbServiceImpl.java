package com.angu.ai.system.service.impl;

import com.angu.ai.common.exception.ServiceException;
import com.angu.ai.system.domain.dto.KbDTO;
import com.angu.ai.system.domain.entity.KbDocument;
import com.angu.ai.system.domain.entity.KbKnowledgeBase;
import com.angu.ai.system.mapper.KbDocumentMapper;
import com.angu.ai.system.mapper.KbKnowledgeBaseMapper;
import com.angu.ai.system.service.IKbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KbServiceImpl implements IKbService {
    private final KbKnowledgeBaseMapper kbMapper;
    private final KbDocumentMapper docMapper;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/angu-kb-uploads/";

    @Override
    public List<KbKnowledgeBase> listKb() { return kbMapper.selectAll(); }

    @Override
    public void createKb(KbDTO dto, Long creatorId) {
        KbKnowledgeBase kb = new KbKnowledgeBase();
        kb.setName(dto.getName()); kb.setDescription(dto.getDescription());
        kb.setCreatorId(creatorId); kb.setDocCount(0);
        kbMapper.insert(kb);
    }

    @Override
    public void updateKb(Long id, KbDTO dto) {
        KbKnowledgeBase kb = kbMapper.selectById(id);
        if (kb == null) throw new ServiceException(404, "知识库不存在");
        kb.setName(dto.getName()); kb.setDescription(dto.getDescription());
        kbMapper.updateById(kb);
    }

    @Override
    public void deleteKb(Long id) {
        if (kbMapper.selectById(id) == null) throw new ServiceException(404, "知识库不存在");
        kbMapper.deleteById(id);
    }

    @Override
    public List<KbDocument> listDocuments(Long kbId) {
        return docMapper.selectByKbId(kbId);
    }

    @Override
    @Transactional
    public void uploadDocuments(Long kbId, MultipartFile[] files) {
        if (kbMapper.selectById(kbId) == null) throw new ServiceException(404, "知识库不存在");
        File dir = new File(UPLOAD_DIR + kbId + "/");
        dir.mkdirs();
        for (MultipartFile file : files) {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(dir, filename);
            try { file.transferTo(dest); } catch (IOException e) { throw new ServiceException(500, "文件上传失败: " + e.getMessage()); }
            KbDocument doc = new KbDocument();
            doc.setKbId(kbId); doc.setFileName(file.getOriginalFilename());
            doc.setFilePath(dest.getAbsolutePath()); doc.setFileSize(file.getSize());
            doc.setFileType(getExtension(file.getOriginalFilename()));
            doc.setParseStatus("PENDING"); doc.setChunkCount(0);
            docMapper.insert(doc);
        }
        kbMapper.incrementDocCount(kbId, files.length);
    }

    @Override
    public void deleteDocument(Long kbId, Long docId) {
        KbDocument doc = docMapper.selectById(docId);
        if (doc == null || !doc.getKbId().equals(kbId)) throw new ServiceException(404, "文档不存在");
        docMapper.deleteById(docId);
        kbMapper.decrementDocCount(kbId, 1);
    }

    @Override
    public void retryParse(Long kbId, Long docId) {
        KbDocument doc = docMapper.selectById(docId);
        if (doc == null || !doc.getKbId().equals(kbId)) throw new ServiceException(404, "文档不存在");
        docMapper.updateParseStatus(docId, "PENDING", null);
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf('.');
        return idx >= 0 ? filename.substring(idx + 1).toLowerCase() : "";
    }
}
