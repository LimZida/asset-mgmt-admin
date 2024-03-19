package com.mcnc.assetmgmt.dashboard.repository;

import com.mcnc.assetmgmt.asset.entity.SoftwareEntity;
import com.mcnc.assetmgmt.asset.repository.SoftwareRepository;
import com.mcnc.assetmgmt.code.entity.Code;
import com.mcnc.assetmgmt.code.repository.CodeRepository;
import lombok.extern.slf4j.Slf4j;
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
 * title : DashboardSoftwareRepositoryTest
 * description : Software entity 사용한 Dashboard test
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
class DashboardSoftwareRepositoryTest {
    @Autowired
    private DashboardSoftwareRepository dashboardSoftwareRepository;
    @Autowired
    private SoftwareRepository softwareRepository;
    @Autowired
    private CodeRepository codeRepository;

    public List<SoftwareEntity> softList = new ArrayList<>();
    public SoftwareEntity s1;
    public SoftwareEntity s2;
    public SoftwareEntity s3;
    public Code swDiv;

    @BeforeEach
    void before(){
        Code swCategory = Code.builder()
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

        swDiv = Code.builder()
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

        Code swLicense = Code.builder()
                .codeName("영구")
                .codeType("LIC")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("10년")
                .code("LIC99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code swLocation = Code.builder()
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

        Code swMfr = Code.builder()
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

        Code swOS = Code.builder()
                .codeName("이클")
                .codeType("OS")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("")
                .code("OS99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code swOSVersion = Code.builder()
                .codeName("2012")
                .codeType("OSV")
                .codeDepth(null)
                .codeCtg("")
                .upperCode("")
                .codeRemark("")
                .code("OSV99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        Code swUsage = Code.builder()
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

        codeRepository.save(swCategory);
        codeRepository.save(swDiv);
        codeRepository.save(swLicense);
        codeRepository.save(swLocation);
        codeRepository.save(swMfr);
        codeRepository.save(swOS);
        codeRepository.save(swOSVersion);
        codeRepository.save(swUsage);


        s1 = SoftwareEntity.builder()
                .swCategory(swCategory)
                .swDiv(swDiv)
                .swLicense(swLicense)
                .swLocation(swLocation)
                .swMfr(swMfr)
                .swOS(swOS)
                .swOSVersion(swOSVersion)
                .swUsage(swUsage)
                .expireDate(new Timestamp(System.currentTimeMillis()))
                .swNo("test-001")
                .createId("admin")
                .deleteYn(false)
                .expireYn(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .swName("이클립스")
                .swQuantity(1)
                .swRemarks("구림")
                .swSN("SW-TEST-0111")
                .updateId("admin")
                .createDate(null)
                .updateDate(null)
                .build();

        s2 = SoftwareEntity.builder()
                .swCategory(swCategory)
                .swDiv(swDiv)
                .swLicense(swLicense)
                .swLocation(swLocation)
                .swMfr(swMfr)
                .swOS(swOS)
                .swOSVersion(swOSVersion)
                .swUsage(swUsage)
                .expireDate(new Timestamp(System.currentTimeMillis()))
                .swNo("test-002")
                .createId("admin")
                .deleteYn(false)
                .expireYn(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .swName("똥클립스")
                .swQuantity(1)
                .swRemarks("구림")
                .swSN("SW-TEST-0112")
                .updateId("admin")
                .createDate(null)
                .updateDate(null)
                .build();

        s3 = SoftwareEntity.builder()
                .swCategory(swCategory)
                .swDiv(swDiv)
                .swLicense(swLicense)
                .swLocation(swLocation)
                .swMfr(swMfr)
                .swOS(swOS)
                .swOSVersion(swOSVersion)
                .swUsage(swUsage)
                .expireDate(new Timestamp(System.currentTimeMillis()))
                .swNo("test-003")
                .createId("admin")
                .deleteYn(false)
                .expireYn(false)
                .purchaseDate(new Timestamp(System.currentTimeMillis()))
                .swName("달클립스")
                .swQuantity(1)
                .swRemarks("구림")
                .swSN("SW-TEST-0113")
                .updateId("admin")
                .createDate(null)
                .updateDate(null)
                .build();

        SoftwareEntity save1 = softwareRepository.save(s1);
        SoftwareEntity save2 = softwareRepository.save(s2);
        SoftwareEntity save3 = softwareRepository.save(s3);

        softList.add(save1);
        softList.add(save2);
        softList.add(save3);
    }

    @Test
    void findAll() {
        //given
        //when
        List<SoftwareEntity> all = dashboardSoftwareRepository.findAll();
        for(SoftwareEntity i : all){
            log.info(i.getSwNo());
        }
        //then
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    void findBySwDivAndDeleteYn() {
        //given
        //when
        List<SoftwareEntity> all = dashboardSoftwareRepository.findBySwDivAndDeleteYn(swDiv, true);
        for(SoftwareEntity i : all){
            log.info(i.getSwNo());
        }
        //then
        assertThat(all.size()).isEqualTo(3);
    }
}