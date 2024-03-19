package com.mcnc.assetmgmt.dashboard.service.impl;

import com.mcnc.assetmgmt.asset.entity.HardwareEntity;
import com.mcnc.assetmgmt.asset.entity.SoftwareEntity;
import com.mcnc.assetmgmt.assignment.entity.AssignmentEntity;
import com.mcnc.assetmgmt.assignment.repository.AssignmentRepository;
import com.mcnc.assetmgmt.code.entity.Code;
import com.mcnc.assetmgmt.code.repository.CodeRepository;
import com.mcnc.assetmgmt.dashboard.dto.DashboardDto;
import com.mcnc.assetmgmt.dashboard.repository.DashboardHardwareRepository;
import com.mcnc.assetmgmt.dashboard.repository.DashboardHistoryRepository;
import com.mcnc.assetmgmt.dashboard.repository.DashboardSoftwareRepository;
import com.mcnc.assetmgmt.history.entity.HistoryEntity;
import com.mcnc.assetmgmt.util.common.CodeAs;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * title : DashboardServiceImplMockTest
 * description : Dashboard entity 사용한 UserServiceImplTest test
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
class DashboardServiceImplMockTest {
    @Mock
    private CodeRepository codeRepository;
    @Mock
    private DashboardHistoryRepository dashboardHistoryRepository;
    @Mock
    private DashboardHardwareRepository dashboardHardwareRepository;
    @Mock
    private DashboardSoftwareRepository dashboardSoftwareRepository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @InjectMocks
    private DashboardServiceImpl dashboardService;

    List<Code> codeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Code code1 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("CTG010201")
                .upperCode("")
                .codeDepth(1)
                .codeType("AST")
                .codeName("모니터")
                .code("AST01")
                .build();

        Code code4 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("CTG010201")
                .upperCode("")
                .codeDepth(1)
                .codeType("AST")
                .codeName("노트북")
                .code("AST04")
                .build();

        Code code5 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("CTG010201")
                .upperCode("")
                .codeDepth(1)
                .codeType("AST")
                .codeName("서버")
                .code("AST05")
                .build();

        Code code2 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("CTG010301")
                .upperCode("")
                .codeDepth(1)
                .codeType("AST")
                .codeName("소프트웨어")
                .code("AST02")
                .build();

        Code code3 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("CTG010301")
                .upperCode("")
                .codeDepth(1)
                .codeType("AST")
                .codeName("운영체제")
                .code("AST03")
                .build();


        codeList.add(code1);
        codeList.add(code2);
        codeList.add(code3);
        codeList.add(code4);
        codeList.add(code5);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAssetCodeAndNameService() {
        //given
        Code code4 = Code.builder()
                .activeYn(true)
                .codeRemark("")
                .codeCtg("")
                .upperCode("CTG0102")
                .codeDepth(1)
                .codeType("CTG")
                .codeName("하드웨어")
                .code("CTG010201")
                .build();

        when(codeRepository.findByCode(any())).thenReturn(code4);
        when(codeRepository.findByCodeType(any())).thenReturn(codeList);
        //when
        DashboardDto.assetMapInfo assetCodeAndNameService = dashboardService.getAssetCodeAndNameService();
        log.info(assetCodeAndNameService.toString());
        //then
        assertThat(assetCodeAndNameService.getHwList().size()).isEqualTo(1);
        assertThat(assetCodeAndNameService.getSwList().size()).isEqualTo(2);
    }

    @Test
    void getInOutHistoryService() {
        //given
        List<HistoryEntity> list = new ArrayList<>();
        HistoryEntity history1 = HistoryEntity.builder()
                .id(1L)
                .assetNo("1")
                .build();

        HistoryEntity history2 = HistoryEntity.builder()
                .id(2L)
                .assetNo("2")
                .build();

        HistoryEntity history3 = HistoryEntity.builder()
                .id(3L)
                .assetNo("3")
                .build();

        list.add(history1);
        list.add(history2);
        list.add(history3);

        when(dashboardHistoryRepository.findAllByOrderByCreateDateDesc
                (PageRequest.of(any(), any()))).thenReturn((Page<HistoryEntity>) list);

        DashboardDto.rowInfo rowInfo = DashboardDto.rowInfo.builder()
                .rowCount(3)
                .build();

        //when
        List<DashboardDto.assetInfo> inOutHistoryService = dashboardService.getInOutHistoryService(rowInfo);

        //then
        assertThat(inOutHistoryService.size()).isEqualTo(list.size());
    }

    @Test
    void getHwDetailAssetListService() {
        //given
        List<HardwareEntity> ast01 = new ArrayList<>();
        HardwareEntity hard1 = HardwareEntity.builder()
                .hwNo("1")
                .produceDate(new Timestamp(System.currentTimeMillis()))
                .failureYn(true)
                .build();
        ast01.add(hard1);
        HardwareEntity hard12 = HardwareEntity.builder()
                .hwNo("2")
                .produceDate(new Timestamp(System.currentTimeMillis()))
                .failureYn(true)
                .build();
        ast01.add(hard12);

        // 현재 시간 가져오기
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        // Calendar 객체 생성 및 현재 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimestamp.getTime());
        // 5년 전으로 이동
        calendar.add(Calendar.YEAR, -5);
        List<HardwareEntity> ast02 = new ArrayList<>();
        HardwareEntity hard2 = HardwareEntity.builder()
                .hwNo("2")
                .produceDate(new Timestamp(calendar.getTimeInMillis()))
                .failureYn(true)
                .build();
        ast02.add(hard2);

        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        when(dashboardHardwareRepository.findByHwDivAndDeleteYn(codeList.get(0), false)).thenReturn(ast01);
        when(dashboardHardwareRepository.findByHwDivAndDeleteYn(codeList.get(1), false)).thenReturn(new ArrayList<>());
        when(dashboardHardwareRepository.findByHwDivAndDeleteYn(codeList.get(2), false)).thenReturn(new ArrayList<>());
        when(dashboardHardwareRepository.findByHwDivAndDeleteYn(codeList.get(3), false)).thenReturn(ast02);

        List<AssignmentEntity> assignmentList = new ArrayList<>();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
//                .usageName("임현영")
                .build();

        assignmentList.add(assignmentEntity);

        when(assignmentRepository.findByAssetNoAndDeleteYnOrderByCreateDateDesc
                (any(), any())).thenReturn(assignmentList);

        //when
        List<DashboardDto.hwListInfo> hwDetailAssetListService = dashboardService.getHwDetailAssetListService();
        for(DashboardDto.hwListInfo i : hwDetailAssetListService){
            log.info(i.toString());
        }


        //then
        assertThat(hwDetailAssetListService.size()).isEqualTo(codeList.size());
    }

    @Test
    void getSwDetailAssetListService() {
        //given
        List<SoftwareEntity> ast01 = new ArrayList<>();
        SoftwareEntity software1 = SoftwareEntity.builder()
                .swNo("1")
                .expireDate(new Timestamp(System.currentTimeMillis()))
                .build();
        SoftwareEntity software2 = SoftwareEntity.builder()
                .swNo("2")
                .expireDate(new Timestamp(System.currentTimeMillis()))
                .build();

        ast01.add(software1);
        ast01.add(software2);

        // 현재 시간 가져오기
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        // Calendar 객체 생성 및 현재 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimestamp.getTime());
        // 2개월 후로 이동
        calendar.add(Calendar.MONTH, 2);
        // 변경된 시간을 Timestamp로 설정
        Timestamp twoMonthsLaterTimestamp = new Timestamp(calendar.getTimeInMillis());

        List<SoftwareEntity> ast02 = new ArrayList<>();
        SoftwareEntity software3 = SoftwareEntity.builder()
                .swNo("3")
                .expireDate(twoMonthsLaterTimestamp)
                .build();
        SoftwareEntity software4 = SoftwareEntity.builder()
                .swNo("4")
                .expireDate(new Timestamp(System.currentTimeMillis()))
                .build();

        ast02.add(software3);
        ast02.add(software4);

        when(codeRepository.findByCodeCtg(any())).thenReturn(codeList);
        when(dashboardSoftwareRepository.findBySwDivAndDeleteYn(codeList.get(0), false)).thenReturn(ast01);
        when(dashboardSoftwareRepository.findBySwDivAndDeleteYn(codeList.get(1), false)).thenReturn(new ArrayList<>());
        when(dashboardSoftwareRepository.findBySwDivAndDeleteYn(codeList.get(2), false)).thenReturn(new ArrayList<>());
        when(dashboardSoftwareRepository.findBySwDivAndDeleteYn(codeList.get(3), false)).thenReturn(ast02);


        List<AssignmentEntity> assignmentList = new ArrayList<>();
        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
                .usageName("임현영")
                .build();
        AssignmentEntity assignmentEntity2 = AssignmentEntity.builder()
                .usageName("임현영")
                .build();

        assignmentList.add(assignmentEntity);
        assignmentList.add(assignmentEntity2);

        when(assignmentRepository.findByAssetNoAndDeleteYnOrderByCreateDateDesc
                (any(), any())).thenReturn(assignmentList);
        //when
        List<DashboardDto.swListInfo> swDetailAssetListService = dashboardService.getSwDetailAssetListService();
        for(DashboardDto.swListInfo i : swDetailAssetListService){
            log.info(i.toString());
        }

        //then
        assertThat(swDetailAssetListService.size()).isEqualTo(codeList.size());
    }
}