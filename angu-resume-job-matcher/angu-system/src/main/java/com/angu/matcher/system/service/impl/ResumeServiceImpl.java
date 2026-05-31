package com.angu.matcher.system.service.impl;

import com.angu.matcher.common.constant.Constants;
import com.angu.matcher.common.exception.ServiceException;
import com.angu.matcher.system.domain.*;
import com.angu.matcher.system.dto.ResumeRequest;
import com.angu.matcher.system.mapper.*;
import com.angu.matcher.system.service.IResumeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements IResumeService {

    private final ResumeMainMapper resumeMapper;
    private final ResumeEducationMapper eduMapper;
    private final ResumeWorkExpMapper workMapper;
    private final ResumeSkillMapper skillMapper;

    @Value("${app.upload.path:./uploads}")
    private String uploadPath;

    @Override
    public List<ResumeMain> listResumes(String name, String phone, String highestEdu,
                                         String source, String skill) {
        List<ResumeMain> list = resumeMapper.selectList(name, phone, highestEdu, source, skill);
        list.forEach(this::fillSubData);
        return list;
    }

    @Override
    public ResumeMain getById(Long id) {
        ResumeMain resume = resumeMapper.selectById(id);
        if (resume == null) throw new ServiceException(404, "简历不存在");
        fillSubData(resume);
        return resume;
    }

    private void fillSubData(ResumeMain resume) {
        resume.setEducations(eduMapper.selectByResumeId(resume.getId()));
        resume.setWorkExps(workMapper.selectByResumeId(resume.getId()));
        resume.setSkills(skillMapper.selectByResumeId(resume.getId()));
    }

    @Override
    @Transactional
    public ResumeMain createResume(ResumeRequest req, Long creatorId) {
        ResumeMain resume = buildFromRequest(req);
        resume.setCreatorId(creatorId);
        resume.setSource(req.getSource() != null ? req.getSource() : "MANUAL");
        resumeMapper.insert(resume);
        saveSubData(resume.getId(), req);
        return getById(resume.getId());
    }

    @Override
    @Transactional
    public void updateResume(Long id, ResumeRequest req) {
        ResumeMain resume = getById(id);
        ResumeMain updated = buildFromRequest(req);
        updated.setId(resume.getId());
        resumeMapper.update(updated);
        eduMapper.deleteByResumeId(id);
        workMapper.deleteByResumeId(id);
        skillMapper.deleteByResumeId(id);
        saveSubData(id, req);
    }

    @Override
    public void deleteResume(Long id) {
        getById(id);
        resumeMapper.deleteById(id);
    }

    @Override
    @Transactional
    public ResumeMain uploadFile(MultipartFile file, Long creatorId) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || !isAllowedExt(originalName)) {
            throw new ServiceException(400, "仅支持 PDF、DOC、DOCX 格式");
        }
        if (file.getSize() > Constants.MB_10) {
            throw new ServiceException(400, "文件大小不能超过 10MB");
        }
        String storedName = UUID.randomUUID() + getExt(originalName);
        Path dir = Paths.get(uploadPath);
        try {
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), dir.resolve(storedName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ServiceException(500, "文件保存失败");
        }
        ResumeMain resume = new ResumeMain();
        resume.setSource("FILE");
        resume.setCreatorId(creatorId);
        resume.setFilePath(storedName);
        resume.setParseSuccess(0);
        resumeMapper.insert(resume);
        return resume;
    }

    @Override
    public void downloadFile(Long id, HttpServletResponse response) {
        ResumeMain resume = getById(id);
        if (resume.getFilePath() == null) throw new ServiceException(404, "无附件");
        Path filePath = Paths.get(uploadPath, resume.getFilePath());
        if (!Files.exists(filePath)) throw new ServiceException(404, "文件不存在");
        response.setContentType("application/octet-stream");
        String encodedName = URLEncoder.encode(resume.getFilePath(), StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");
        try (InputStream in = Files.newInputStream(filePath);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        } catch (IOException e) {
            throw new ServiceException(500, "文件下载失败");
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        List<ResumeMain> list = resumeMapper.selectAllValid();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("简历列表");
            String[] headers = {"ID","姓名","手机","邮箱","最高学历","期望职位","来源","创建时间"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            int rowNum = 1;
            for (ResumeMain r : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId());
                row.createCell(1).setCellValue(r.getName() != null ? r.getName() : "");
                row.createCell(2).setCellValue(r.getPhone() != null ? r.getPhone() : "");
                row.createCell(3).setCellValue(r.getEmail() != null ? r.getEmail() : "");
                row.createCell(4).setCellValue(r.getHighestEdu() != null ? r.getHighestEdu() : "");
                row.createCell(5).setCellValue(r.getDesiredPosition() != null ? r.getDesiredPosition() : "");
                row.createCell(6).setCellValue(r.getSource() != null ? r.getSource() : "");
                row.createCell(7).setCellValue(r.getCreatedTime() != null ? r.getCreatedTime().toString() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = URLEncoder.encode("简历列表.xlsx", StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            throw new ServiceException(500, "导出失败");
        }
    }

    private ResumeMain buildFromRequest(ResumeRequest req) {
        ResumeMain r = new ResumeMain();
        r.setName(req.getName());
        r.setPhone(req.getPhone());
        r.setEmail(req.getEmail());
        r.setGender(req.getGender());
        r.setBirthDate(req.getBirthDate());
        r.setCity(req.getCity());
        r.setDesiredPosition(req.getDesiredPosition());
        r.setDesiredCity(req.getDesiredCity());
        r.setDesiredSalaryMin(req.getDesiredSalaryMin());
        r.setDesiredSalaryMax(req.getDesiredSalaryMax());
        r.setJobStatus(req.getJobStatus());
        r.setSelfIntro(req.getSelfIntro());
        return r;
    }

    private void saveSubData(Long resumeId, ResumeRequest req) {
        if (req.getEducations() != null) {
            req.getEducations().forEach(e -> { e.setResumeId(resumeId); eduMapper.insert(e); });
        }
        if (req.getWorkExps() != null) {
            req.getWorkExps().forEach(e -> { e.setResumeId(resumeId); workMapper.insert(e); });
        }
        if (req.getSkills() != null) {
            req.getSkills().forEach(s -> {
                ResumeSkill skill = new ResumeSkill();
                skill.setResumeId(resumeId);
                skill.setSkillName(s);
                skillMapper.insert(skill);
            });
        }
    }

    private boolean isAllowedExt(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".pdf") || lower.endsWith(".doc") || lower.endsWith(".docx");
    }

    private String getExt(String name) {
        int dot = name.lastIndexOf('.');
        return dot >= 0 ? name.substring(dot) : "";
    }
}
