package com.mcnc.assetmgmt.code.service.impl;

import com.mcnc.assetmgmt.code.dto.CodeDto;
import com.mcnc.assetmgmt.code.entity.Code;
import com.mcnc.assetmgmt.code.repository.CodeRepository;
import com.mcnc.assetmgmt.code.service.CodeService;
import com.mcnc.assetmgmt.util.common.CodeAs;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * title : CodeServiceImplMockTest
 * description : code entity 사용한 CodeServiceImplMockTest test
 *
 * reference : static method 모킹 https://hides.kr/1116 , https://middleearth.tistory.com/40
 *             static method 제어가 힘든 이유 https://wonit.tistory.com/631
 *
 *             Mock 개념 : https://www.crocus.co.kr/1555
 *             Mock 주의사항 : https://wiki.yowu.dev/ko/dev/Mockito/Spring-Boot-Mockito-Series/10-Frequently-encountered-problems-and-solutions
 *
 *             단위 테스트 방법 : https://cocococo.tistory.com/entry/Spring-Boot-%EB%8B%A8%EC%9C%84-%ED%85%8C%EC%8A%A4%ED%8A%B8JUnit-%EC%82%AC%EC%9A%A9-%EB%B0%A9%EB%B2%95
 * author : 임현영
 * date : 2024.02.14
 **/
@ExtendWith(MockitoExtension.class)
@Slf4j
class CodeServiceImplMockTest {
    @Mock
    private CodeRepository codeRepository;

    @InjectMocks
    private CodeServiceImpl codeService;

    List<Code> codeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Code code1 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("")
                .upperCode("CTG01")
                .codeDepth(1)
                .codeType("CTG")
                .codeName("전략운영")
                .code("CTG0101")
                .build();

        Code code2 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("")
                .upperCode("CTG01")
                .codeDepth(1)
                .codeType("CTG")
                .codeName("전략채널")
                .code("CTG0102")
                .build();

        Code code3 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("")
                .upperCode("CTG01")
                .codeDepth(1)
                .codeType("CTG")
                .codeName("경영지원")
                .code("CTG0103")
                .build();

        codeList.add(code1);
        codeList.add(code2);
        codeList.add(code3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCategoryService() {
        //given
        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        //when
        List<CodeDto.categoryInfo> allCategoryService = codeService.getAllCategoryService();
        for( CodeDto.categoryInfo i : allCategoryService){
            log.info("##### : {}", i.getCode());
        }
        //then
        assertThat(allCategoryService.size()).isEqualTo(codeList.size());
    }

    @Test
    void createCategoryService() {
        //given
        CodeDto.categoryCreateInfo categoryCreateInfo = CodeDto.categoryCreateInfo.builder()
                .codeType("CTG")
                .codeDepth(1)
                .codeName("비품")
                .code("CTG010105")
                .build();

        when(codeRepository.save(any())).thenReturn(codeList.get(0));
        //타입리스트
        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
//        when(codeRepository.findByCodeType(any())).thenReturn(new ArrayList<>());

        //네임리스트
//        when(codeRepository.findByCodeTypeAndCodeNameContainingAndCodeCtg(any(), any(), any())).thenReturn(codeList);
        when(codeRepository.findByCodeTypeAndCodeNameContainingAndCodeCtg(any(), any(), any())).thenReturn(new ArrayList<>());

        //when
        CodeDto.resultInfo categoryService = codeService.createCategoryService(categoryCreateInfo);

        //then
        assertThat(categoryService.getResult()).isEqualTo(true);
    }

    @Test
    void validateCodeTypeService() {
        //given
        CodeDto.codeTypeInfo codeTypeInfo = CodeDto.codeTypeInfo.builder()
                .codeType("CTG")
                .build();

//        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
        when(codeRepository.findByCodeType(any())).thenReturn(new ArrayList<>());

        //when
        CodeDto.codeTypeResultInfo codeTypeResultInfo = codeService.validateCodeTypeService(codeTypeInfo);
        //then
        assertThat(codeTypeResultInfo.getResult()).isEqualTo("notDup");
    }

    @Test
    void validateCategoryCodeService() {
        //given
        CodeDto.validationCategoryCodeInfo validationCategoryCodeInfo = CodeDto.validationCategoryCodeInfo.builder()
                .codeName("경영지원")
                .codeType("CTG")
                .upperCode("CTG01")
                .build();

        when(codeRepository.findByUpperCode(any())).thenReturn(new ArrayList<>());
        when(codeRepository.findByUpperCodeAndCodeNameContaining(any(),any())).thenReturn(new ArrayList<>());
        //when
        CodeDto.codeInfo codeInfo = codeService.validateCategoryCodeService(validationCategoryCodeInfo);
        //then
        assertThat(codeInfo.getCode()).isEqualTo("CTG0101");
    }

    @Test
    void updateCategoryService() {
        //given
        CodeDto.codeModifyInfo codeModifyInfo = CodeDto.codeModifyInfo.builder()
                .code("CTG0101")
                .codeName("비비품품")
                .codeRemark("짜증나")
                .build();

        when(codeRepository.findByCode(any())).thenReturn(codeList.get(0));
        when(codeRepository.findByCodeTypeAndUpperCodeAndCodeNameContainingAndCodeIsNot(any(),
                any(),any(),any())).thenReturn(codeList);
        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        //when
        List<CodeDto.categoryInfo> categoryInfo = codeService.updateCategoryService(codeModifyInfo);
        for(CodeDto.categoryInfo i : categoryInfo){
            log.info("#### : {}",i.toString());
        }
        //then
        assertThat(codeList.size()).isEqualTo(categoryInfo.size());
    }

    @Test
    void activateCategoryService() {
        //given
        CodeDto.codeInfo codeInfo = CodeDto.codeInfo.builder()
                .code("CTG0101")
                .build();
        when(codeRepository.findByCode(any())).thenReturn(codeList.get(0));
        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        //when
        List<CodeDto.categoryInfo> category = codeService.activateCategoryService(codeInfo);
        for(CodeDto.categoryInfo i : category) {
            log.info("#### : {}", i.toString());
        }
        //then
        assertThat(category.size()).isEqualTo(codeList.size());
    }

    @Test
    void createCodeService() {
        //given
        CodeDto.codeCreateInfo codeCreateInfo = CodeDto.codeCreateInfo.builder()
                .code("CTG0102")
                .codeCtg("")
                .codeName("허깨비")
                .codeRemark("짜잉나")
                .codeType("CTG")
                .build();

        when(codeRepository.save(any())).thenReturn(codeList.get(0));
        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        //when
        CodeDto.codeListMapInfo codeResult = codeService.createCodeService(codeCreateInfo);
        log.info(codeResult.toString());
        //then
        assertThat(codeResult.getCodeList().size()).isEqualTo(codeList.size());
    }

    @Test
    void validateCodeService() {
        //given
        CodeDto.validationCodeInfo validationCodeInfo = CodeDto.validationCodeInfo.builder()
                .codeType("CTG")
                .codeName("귀염둥이")
                .codeCtg("CTG0101")
                .build();

        when(codeRepository.findByCodeType(any())).thenReturn(new ArrayList<>());
        when(codeRepository.findByCodeTypeAndCodeNameContainingAndCodeCtg(any(),any(),any())).thenReturn(new ArrayList<>());

        //when
        CodeDto.codeInfo codeInfo = codeService.validateCodeService(validationCodeInfo);
        //then
        assertThat(codeInfo.getCode()).isEqualTo("CTG01");
    }

    @Test
    void getCodeListService() {
        //given
        CodeDto.codeInfo codeInfo = CodeDto.codeInfo.builder()
                .code("CTG0101")
                .build();

        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        //when
        CodeDto.codeListMapInfo codeListService = codeService.getCodeListService(codeInfo);
        log.info(codeListService.getCodeList().toString());

        //then
        assertThat(codeListService.getCodeList().size()).isEqualTo(codeList.size());
    }

    @Test
    void activateCodeService() {
        //given
        CodeDto.codeInfo codeInfo = CodeDto.codeInfo.builder()
                .code("CTG0101")
                .build();

        when(codeRepository.findByCode(any())).thenReturn(codeList.get(0));
        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        //when
        CodeDto.codeListMapInfo codeListMapInfo = codeService.activateCodeService(codeInfo);

        //then
        assertThat(codeListMapInfo.getCodeList().size()).isEqualTo(codeList.size());
    }

    @Test
    void updateCodeService() {
        //given
        CodeDto.codeModifyInfo codeModifyInfo = CodeDto.codeModifyInfo.builder()
                .codeRemark("와우와우")
                .codeName("우와우와")
                .code("CTG0101")
                .build();

        when(codeRepository.findByCode(any())).thenReturn(codeList.get(0));
        when(codeRepository.findByCodeTypeAndCodeNameContainingAndCodeIsNotAndCodeCtg(any(),
                any(),any(),any())).thenReturn(new ArrayList<>());
        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);

        //when
        CodeDto.codeListMapInfo codeListMapInfo = codeService.updateCodeService(codeModifyInfo);
        log.info(codeListMapInfo.getCodeList().get(0).toString());
        log.info(codeModifyInfo.toString());

        //then
        assertThat(codeListMapInfo.getCodeList().size()).isEqualTo(codeList.size());
    }
}