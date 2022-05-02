package com.ssafy.helpus.donation.controller;

import com.ssafy.helpus.donation.dto.DonationReqDto;
import com.ssafy.helpus.donation.dto.DonationUpdateReqDto;
import com.ssafy.helpus.donation.service.DonationService;
import com.ssafy.helpus.donation.service.FileService;
import com.ssafy.helpus.utils.Message;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/donation")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, maxAge = 6000)
public class DonationController {

    private final DonationService donationService;
    private final FileService fileService;

    @ApiOperation(value = "기부 글 등록")
    @PostMapping
    public ResponseEntity registerDonation(@Valid @RequestPart DonationReqDto donation, @RequestPart List<MultipartFile> files) {
        log.info("DonationController registerDonation call");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.CREATED;
        try {
            //게시글 파일 확장자 확인
            if(!fileService.fileExtensionCheck(files)) {
                resultMap.put("message", Message.FILE_EXTENSION_EXCEPTION);
                status = HttpStatus.BAD_REQUEST;
            } else {
                resultMap = donationService.registerDonation(donation, files);
            }
        } catch (Exception e) {
            log.error(Message.DONATION_REGISTER_FAIL+" : {}", e.getMessage());

            resultMap.put("message", Message.DONATION_REGISTER_FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(resultMap, status);
    }

    @ApiOperation(value = "기부 글 수정")
    @PutMapping
    public ResponseEntity updateDonation(@Valid @RequestPart DonationUpdateReqDto donation, @RequestPart List<MultipartFile> files) {
        log.info("DonationController updateDonation call");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.CREATED;
        try {
            //게시글 파일 확장자 확인
            if(!fileService.fileExtensionCheck(files)) {
                resultMap.put("message", Message.FILE_EXTENSION_EXCEPTION);
                status = HttpStatus.BAD_REQUEST;
            } else {
                resultMap = donationService.updateDonation(donation, files);
            }
        } catch (Exception e) {
            log.error(Message.DONATION_UPDATE_FAIL+" : {}", e.getMessage());

            resultMap.put("message", Message.DONATION_UPDATE_FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(resultMap, status);
    }

    @ApiOperation(value = "기부 글 조회")
    @GetMapping("{donationId}")
    public ResponseEntity getDonation(@PathVariable Integer donationId) {
        log.info("DonationController getDonation call");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            resultMap = donationService.getDonation(donationId);
        } catch (Exception e) {
            log.error(Message.DONATION_FIND_FAIL+" : {}", e.getMessage());

            resultMap.put("message", Message.DONATION_FIND_FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(resultMap, status);
    }

    @ApiOperation(value = "기부 글 마감")
    @DeleteMapping("{donationId}")
    public ResponseEntity endDonation(@PathVariable Integer donationId) {
        log.info("DonationController endDonation call");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            resultMap = donationService.endDonation(donationId);
        } catch (Exception e) {
            log.error(Message.DONATION_END_FAIL+" : {}", e.getMessage());

            resultMap.put("message", Message.DONATION_END_FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(resultMap, status);
    }

    @ApiOperation(value = "기부 글 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "작성자 고유 번호", required = false,
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "정렬 순서", required = false,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "word", value = "검색어", required = false,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "페이지 번호", required = false,
                    dataType = "int", paramType = "query")
    })
    @GetMapping
    public ResponseEntity listDonation(@RequestParam(required = false) Integer memberId,
                                       @RequestParam(required = false, defaultValue = "최신순") String order,
                                       @RequestParam(required = false, defaultValue = "1") int page) {
        log.info("DonationController listDonation call");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            resultMap = donationService.listDonation(memberId, order, page-1);
        } catch (Exception e) {
            log.error(Message.DONATION_FIND_FAIL+" : {}", e.getMessage());

            resultMap.put("message", Message.DONATION_FIND_FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity(resultMap, status);
    }
}
