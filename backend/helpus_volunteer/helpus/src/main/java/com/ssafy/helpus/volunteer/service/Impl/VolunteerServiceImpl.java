package com.ssafy.helpus.volunteer.service.Impl;

import com.ssafy.helpus.volunteer.dto.ListVolunteerResDto;
import com.ssafy.helpus.volunteer.dto.VolunteerReqDto;
import com.ssafy.helpus.volunteer.dto.VolunteerResDto;
import com.ssafy.helpus.volunteer.dto.VolunteerUpdateReqDto;
import com.ssafy.helpus.volunteer.entity.Volunteer;
import com.ssafy.helpus.volunteer.entity.VolunteerApply;
import com.ssafy.helpus.volunteer.repository.VolunteerApplyRepository;
import com.ssafy.helpus.volunteer.repository.VolunteerRepository;
import com.ssafy.helpus.volunteer.service.FileService;
import com.ssafy.helpus.volunteer.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService{

    private final VolunteerRepository volunteerRepository;
    private final FileService fileService;
    private final VolunteerApplyRepository volunteerApplyRepository;

    @Override
    public Map<String, Object> registerVoluneer(VolunteerReqDto volunteerReqDto, Long memberId, MultipartFile[] files, String role) throws Exception {
        log.info("VolunteerService registerVolunteer call");

        Map<String, Object> resultMap = new HashMap<>();

        if(files != null && !fileService.fileExtensionCheck(files)) {
            resultMap.put("message", "파일확장자 불량");
            return resultMap;
        }

        Volunteer volunteer = Volunteer.builder()
                .memberId(memberId)
                .title(volunteerReqDto.getTitle())
                .content(volunteerReqDto.getContent())
                .volZipcode(volunteerReqDto.getVolZipcode())
                .volAddress(volunteerReqDto.getVolAddress())
                .people(volunteerReqDto.getPeople())
                .applicant(0)
                .volDate(volunteerReqDto.getVolDate())
                .category(role)
                .build();
        volunteerRepository.save(volunteer);

        if(files != null){
            fileService.volunteerFileSave(volunteer, files);
        }

        resultMap.put("message", "성공");
        resultMap.put("volunteerId", volunteer.getVolunteerId());

        return resultMap;

    }

    @Override
    @Transactional
    public Map<String, Object> updateVolunteer(VolunteerUpdateReqDto volunteerUpdateReqDto, Long memberId, MultipartFile[] files, String role) throws Exception {
        log.info("VolunteerService updateVolunteer call");

        Map<String, Object> resultMap = new HashMap<>();

        if(files != null && !fileService.fileExtensionCheck(files)) {
            resultMap.put("message", "파일확장자 불량");
            return resultMap;
        }

        Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerUpdateReqDto.getVolunteerId());

        volunteer.get().setTitle(volunteerUpdateReqDto.getTitle());
        volunteer.get().setContent(volunteerUpdateReqDto.getContent());
        volunteer.get().setVolZipcode(volunteerUpdateReqDto.getVolZipcode());
        volunteer.get().setVolAddress(volunteerUpdateReqDto.getVolAddress());
        volunteer.get().setPeople(volunteerUpdateReqDto.getPeople());
        volunteer.get().setVolDate(volunteerUpdateReqDto.getVolDate());
        volunteer.get().setUpdateDate(LocalDateTime.now());

        if(files == null){
            fileService.volunteerFileDelete(volunteer.get().getImages());
        }
        else{
            fileService.volunteerFileDelete(volunteer.get().getImages());
            fileService.volunteerFileSave(volunteer.get(), files);
        }

//        if(files != null){
//            fileService.volunteerFileDelete(volunteer.get().getImages());
//            fileService.volunteerFileSave(volunteer.get(), files);
//        }

        resultMap.put("message", "성공");
        return resultMap;

    }

    @Override
    @Transactional
    public Map<String, Object> getVoluneer(Long volunteerId) throws Exception {
        log.info("VolunteerService getVolunteer call");

        Map<String, Object> resultMap = new HashMap<>();

        Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);
        if(!volunteer.isPresent()){
            resultMap.put("message", "게시물 없음");
            return resultMap;
        }

        VolunteerResDto volunteerResDto = VolunteerResDto.builder()
                .memberId(volunteer.get().getMemberId())
                .title(volunteer.get().getTitle())
                .content(volunteer.get().getContent())
                .createDate(volunteer.get().getCreateDate())
                .updateDate(volunteer.get().getUpdateDate())
                .volDate(volunteer.get().getVolDate())
                .volAddress(volunteer.get().getVolAddress())
                .volZipcode(volunteer.get().getVolZipcode())
                .applicant(volunteer.get().getApplicant())
                .people(volunteer.get().getPeople())
                .percent(volunteer.get().getPercent())
                .images(fileService.getVolunteerFileList(volunteer.get().getImages())).build();

        resultMap.put("message", "조회 성공");
        resultMap.put("volunteer", volunteerResDto);
        return resultMap;
    }

    @Override
    @Transactional
    public Map<String, Object> endVolunteer(Long volunteerId) throws Exception {
        log.info("VolunteerService endVolunteer call");
        Map<String, Object> resultMap = new HashMap<>();

        Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);

        if(!volunteer.isPresent() || volunteer.get().getStatus()==1){
            resultMap.put("message", "조회불가");
            return resultMap;
        }

        volunteer.get().setStatus(1);

        resultMap.put("message", "마감처리 성공");
        return  resultMap;
    }

    @Override
    @Transactional
    public Map<String, Object> applyVolunteer(Long volunteerId, Long memberId, String role) throws Exception {
        log.info("VolunteerService applyVolunteer call");
        Map<String, Object> resultMap = new HashMap<>();

        if(!role.equals("USER")){
            resultMap.put("message", "개인회원이 아닙니다");
            return resultMap;
        }

        Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);

        if(volunteer.get().getPeople()<=volunteer.get().getApplicant()){
            resultMap.put("message", "가득차서 지원불가");
            return resultMap;
        }

        int now = volunteer.get().getApplicant();
        volunteer.get().setApplicant(now+1);

        int now_people = volunteer.get().getPeople();
        double now_percent = now+1/now_people;
        volunteer.get().setPercent(now_percent);

        VolunteerApply volunteerApply = VolunteerApply.builder()
               .status(1)
               .volunteer(volunteer.get())
               .memberId(memberId).build();

        volunteerApplyRepository.save(volunteerApply);

        resultMap.put("message", "지원성공");
        return resultMap;
    }

    @Override
    public Map<String, Object> listVolunteer(String category, int page) throws Exception {
        log.info("VolunteerService listVolunteer call");

        Page<Volunteer> volunteers;
        volunteers = volunteerRepository.findByCategory(category, PageRequest.of(page,10, Sort.by(Sort.Direction.DESC, "volunteerId")));

        return makeListVolunteer(volunteers);
    }

    @Override
    public Map<String, Object> makeListVolunteer(Page<Volunteer> volunteers) throws Exception {
        log.info("VolunteerService makerVolunteerList call");

        Map<String, Object> resultMap = new HashMap<>();

        if(volunteers.isEmpty()){
            resultMap.put("message", "게시물없음");
            return resultMap;
        }
        List<ListVolunteerResDto> list = new ArrayList<>();
        // 기관명 추가
        for(Volunteer volunteer : volunteers){
            ListVolunteerResDto listVolunteerResDto = ListVolunteerResDto.builder()
                    .volunteerId(volunteer.getVolunteerId())
                    .title(volunteer.getTitle())
                    .content(volunteer.getContent())
                    .applicant(volunteer.getApplicant())
                    .people(volunteer.getPeople())
                    .percent(volunteer.getPercent())
                    .volDate(volunteer.getVolDate())
                    .volAddress(volunteer.getVolAddress())
                    .volZipcode(volunteer.getVolZipcode())
                    .createDate(volunteer.getCreateDate()).build();
            list.add(listVolunteerResDto);
        }
        resultMap.put("listVolunteer", list);
        resultMap.put("totalPage", volunteers.getTotalPages());
        resultMap.put("message", "성공");
        return resultMap;
    }


}
