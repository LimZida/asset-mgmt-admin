package com.mcnc.assetmgmt.dashboard.repository;

import com.mcnc.assetmgmt.history.entity.HistoryEntity;
import com.mcnc.assetmgmt.history.repository.HistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 * title : DashboardHistoryRepositoryTest
 * description : History entity 사용한 DashboardHistoryRepositoryTest test
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
class DashboardHistoryRepositoryTest {
    @Autowired
    private DashboardHistoryRepository dashboardHistoryRepository;
    @Autowired
    private HistoryRepository historyRepository;

    @BeforeEach
    void before(){
        HistoryEntity history1 = HistoryEntity.builder()
                .assetNo("test--01")
                .id(1L)
                .assetStatus("반입")
                .assetCategory("HW")
                .build();

        HistoryEntity history2 = HistoryEntity.builder()
                .assetNo("test--02")
                .id(2L)
                .assetStatus("반입")
                .assetCategory("HW")
                .build();

        HistoryEntity history3 = HistoryEntity.builder()
                .assetNo("test--03")
                .id(3L)
                .assetStatus("반입")
                .assetCategory("HW")
                .build();

        historyRepository.save(history1);
        historyRepository.save(history2);
        historyRepository.save(history3);

    }

    @Test
    void findAllByOrderByCreateDateDesc() {
        //given
        //when
        Page<HistoryEntity> pageList = dashboardHistoryRepository.findAllByOrderByCreateDateDesc(PageRequest.of(1, 1));
        for( HistoryEntity i : pageList){
            log.info(i.getAssetNo());
        }
        //then
        assertThat(pageList.getSize()).isEqualTo(1);
    }
}