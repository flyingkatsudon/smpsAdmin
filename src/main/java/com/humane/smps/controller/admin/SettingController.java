package com.humane.smps.controller.admin;

import com.blogspot.na5cent.exom.ExOM;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.smps.dto.UploadExamineeDto;
import com.humane.smps.dto.UploadHallDto;
import com.humane.smps.dto.UploadItemDto;
import com.humane.smps.model.Admission;
import com.humane.smps.model.Exam;
import com.humane.smps.model.ExamHall;
import com.humane.smps.model.Hall;
import com.humane.smps.repository.AdmissionRepository;
import com.humane.smps.repository.ExamHallRepository;
import com.humane.smps.repository.ExamRepository;
import com.humane.smps.repository.HallRepository;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "setting")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettingController {
    private final ExamRepository examRepository;
    private final AdmissionRepository admissionRepository;
    private final HallRepository hallRepository;
    private final ExamHallRepository examHallRepository;

    @RequestMapping(value = "uploadItem", method = RequestMethod.POST)
    public void uploadItem(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("test", ".tmp");
        multipartFile.transferTo(tempFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1. excel 변환
            List<UploadItemDto> itemList = ExOM.mapFromExcel(tempFile).to(UploadItemDto.class).map(1);
            itemList.forEach(uploadItemDto -> {

                // 2. object 변환
                Admission admission = mapper.convertValue(uploadItemDto, Admission.class);

                // 3. object 저장
                admission = admissionRepository.save(admission);

                Exam exam = mapper.convertValue(uploadItemDto, Exam.class);
                exam.setAdmission(admission);
                exam = examRepository.save(exam);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            tempFile.delete();
        }
    }

    @RequestMapping(value = "uploadHall", method = RequestMethod.POST)
    public void uploadHall(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("test", ".tmp");
        multipartFile.transferTo(tempFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<UploadHallDto> hallList = ExOM.mapFromExcel(tempFile).to(UploadHallDto.class).map(1);
            hallList.forEach(uploadHallDto -> {
                /**
                 * 제약조건 : 시험정보는 반드시 업로드 되어 있어야 한다.
                 */

                // 1. 시험정보 생성
                Exam exam = mapper.convertValue(uploadHallDto, Exam.class);
                exam = examRepository.findOne(exam.getExamCd());

                // 2. 고사실정보 생성
                Hall hall = mapper.convertValue(uploadHallDto, Hall.class);
                hall = hallRepository.save(hall);

                // 3. 응시고사실 생성
                ExamHall examHall = new ExamHall();
                examHall.setExam(exam);
                examHall.setHall(hall);

                // 4. 응시고사실 저장
                examHallRepository.save(examHall);
            });
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
        } finally {
            tempFile.delete();
        }
    }

    @RequestMapping(value = "uploadExaminee", method = RequestMethod.POST)
    public void uploadExaminee(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("test", ".tmp");
        multipartFile.transferTo(tempFile);

        try {
            List<UploadExamineeDto> examineeList = ExOM.mapFromExcel(tempFile).to(UploadExamineeDto.class).map(1);
            examineeList.forEach(uploadExamineeDto -> log.debug("{}", uploadExamineeDto));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            tempFile.delete();
        }
    }

    @RequestMapping(value = "downloadItem", method = RequestMethod.GET)
    public ResponseEntity downloadItem(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-item.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "downloadHall", method = RequestMethod.GET)
    public ResponseEntity downloadHall(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-hall.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "downloadExaminee", method = RequestMethod.GET)
    public ResponseEntity downloadExaminee(HttpServletResponse response) {
        return JasperReportsExportHelper.toResponseEntity(response,
                "jrxml/setting-examinee.jrxml",
                "xlsx",
                null
        );
    }
}