package com.mcnc.assetmgmt.dashboard.repository;

import com.mcnc.assetmgmt.asset.entity.HardwareEntity;
import com.mcnc.assetmgmt.asset.repository.HardwareRepository;
import com.mcnc.assetmgmt.asset.repository.SoftwareRepository;
import com.mcnc.assetmgmt.code.entity.Code;
import com.mcnc.assetmgmt.code.repository.CodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 * title : DashboardHardwareRepositoryTest
 * description : Hardware entity 사용한 DashboardHardwareRepositoryTest
 *
 * reference :
 *             DataJpaTest 정리 https://insanelysimple.tistory.com/338
 *                             https://webcoding-start.tistory.com/20
 *             Transactional 정리 https://imiyoungman.tistory.com/9
 *
 *             TestPropertySource으로 Junit에서 프로퍼티 사용하기: https://jaehoney.tistory.com/218
 *
 *            참고: @DataJpaTest 어노테이션은 JPA 관련 컴포넌트를 테스트하기 위한 슬라이스 테스트입니다.
 *            따라서 JPA 관련 컴포넌트에 대한 빈만 자동으로 등록됩니다. JwtService는 JPA 관련 컴포넌트가 아니므로 자동으로 등록되지 않습니다.
 *
 *            DataJpaTest시 h2 가상메모리DB가 아닌 실제 물리 DB로 접근하는 방법 :
 *            https://kangwoojin.github.io/programing/auto-configure-test-database/
 *
 *            Junit 5에서 DI 사용법 https://pinokio0702.tistory.com/189 , https://velog.io/@codren/Junit-Autowired-%ED%95%84%EC%88%98
 *                                https://minkukjo.github.io/framework/2020/06/28/JUnit-23/
 *
 * author : 임현영
 * date : 2024.02.14
 **/
@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DashboardHardwareRepositoryTest {
    @Autowired
    private DashboardHardwareRepository dashboardHardwareRepository;
    @Autowired
    private HardwareRepository hardwareRepository;
    @Autowired
    private CodeRepository codeRepository;

    public List<HardwareEntity> hardList = new ArrayList<>();
    public Code hwDiv;

    @BeforeEach
    void before(){
        Code hwCategory = Code.builder()
                .codeName("소프트웨어")
                .codeType("DIV")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("HW")
                .code("DIV98")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        hwDiv = Code.builder()
                .codeName("소프트웨어")
                .codeType("DIV")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("HW")
                .code("DIV99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code hwCpu = Code.builder()
                .codeName("INTEL")
                .codeType("CPU")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("HW")
                .code("CPU98")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code hwLocation = Code.builder()
                .codeName("본사")
                .codeType("LOC")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("")
                .code("LOC99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code hwMfr = Code.builder()
                .codeName("삼성")
                .codeType("MFR")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("")
                .code("LOC01")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code hwUsage = Code.builder()
                .codeName("서브")
                .codeType("USE")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("")
                .code("USE99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        codeRepository.save(hwCategory);
        codeRepository.save(hwDiv);
        codeRepository.save(hwLocation);
        codeRepository.save(hwMfr);
        codeRepository.save(hwUsage);
        codeRepository.save(hwCpu);


        HardwareEntity h1 = HardwareEntity.builder()
                .hwCategory(hwCategory)
                .hwCpu(hwCpu)
                .hwLocation(hwLocation)
                .hwMfr(hwMfr)
                .hwDiv(hwDiv)
                .hwUsage(hwUsage)
                .failureYn(false)
                .produceDate(new Timestamp(System.currentTimeMillis()))
                .hwNo("hw-test-001")
                .hwRemarks("")
                .updateId("admin")
                .createId("admin")
                .deleteYn(false)
                .hwHdd1(0)
                .hwHdd2(0)
                .hwModel("GRAM")
                .hwRam1(0)
                .hwRam2(0)
                .hwSN("hw-test-001")
                .hwSsd1(0)
                .hwSsd2(0)
                .oldYn(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(null)
                .build();

        HardwareEntity h2 = HardwareEntity.builder()
                .hwCategory(hwCategory)
                .hwCpu(hwCpu)
                .hwLocation(hwLocation)
                .hwMfr(hwMfr)
                .hwDiv(hwDiv)
                .hwUsage(hwUsage)
                .failureYn(false)
                .produceDate(new Timestamp(System.currentTimeMillis()))
                .hwNo("hw-test-002")
                .hwRemarks("")
                .updateId("admin")
                .createId("admin")
                .deleteYn(false)
                .hwHdd1(0)
                .hwHdd2(0)
                .hwModel("GRAM")
                .hwRam1(0)
                .hwRam2(0)
                .hwSN("hw-test-002")
                .hwSsd1(0)
                .hwSsd2(0)
                .oldYn(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(null)
                .build();

        HardwareEntity h3 = HardwareEntity.builder()
                .hwCategory(hwCategory)
                .hwCpu(hwCpu)
                .hwLocation(hwLocation)
                .hwMfr(hwMfr)
                .hwDiv(hwDiv)
                .hwUsage(hwUsage)
                .failureYn(false)
                .produceDate(new Timestamp(System.currentTimeMillis()))
                .hwNo("hw-test-003")
                .hwRemarks("")
                .updateId("admin")
                .createId("admin")
                .deleteYn(false)
                .hwHdd1(0)
                .hwHdd2(0)
                .hwModel("GRAM")
                .hwRam1(0)
                .hwRam2(0)
                .hwSN("hw-test-003")
                .hwSsd1(0)
                .hwSsd2(0)
                .oldYn(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(null)
                .build();

        HardwareEntity save1 = hardwareRepository.save(h1);
        HardwareEntity save2 = hardwareRepository.save(h2);
        HardwareEntity save3 = hardwareRepository.save(h3);

        hardList.add(save1);
        hardList.add(save2);
        hardList.add(save3);
    }

    @Test
    void findByHwDivAndDeleteYn() {
        //given
        //when
        List<HardwareEntity> list = dashboardHardwareRepository.findByHwDivAndDeleteYn(hwDiv, true);
        for(HardwareEntity i : list){
            log.info(i.getHwNo());
        }
        //then
        assertThat(list.size()).isEqualTo(3);
    }
}