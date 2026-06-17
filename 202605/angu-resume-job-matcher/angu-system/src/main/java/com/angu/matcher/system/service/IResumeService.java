package com.angu.matcher.system.service;

import com.angu.matcher.system.domain.ResumeMain;
import com.angu.matcher.system.dto.ResumeRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface IResumeService {
    List<ResumeMain> listResumes(String name, String phone, String highestEdu,
                                  String source, String skill);
    ResumeMain getById(Long id);
    ResumeMain createResume(ResumeRequest req, Long creatorId);
    void updateResume(Long id, ResumeRequest req);
    void deleteResume(Long id);
    ResumeMain uploadFile(MultipartFile file, Long creatorId);
    void downloadFile(Long id, HttpServletResponse response);
    void exportExcel(HttpServletResponse response);
}
