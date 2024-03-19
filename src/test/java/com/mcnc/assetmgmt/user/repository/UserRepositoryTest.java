package com.mcnc.assetmgmt.user.repository;

import com.mcnc.assetmgmt.code.entity.Code;
import com.mcnc.assetmgmt.code.repository.CodeRepository;
import com.mcnc.assetmgmt.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * title : UserRepositoryTest
 * description : User entity 사용한 userRepository test
 *
 * reference : 테스트 예제 https://www.wool-dev.com/backend-engineering/spring/spring-jpa-repo-simple-test
 *             테스트를 위한 다양한 어노테이션들: https://mangkyu.tistory.com/242
 *             단위 테스트 https://jiminidaddy.github.io/dev/2021/05/20/dev-spring-%EB%8B%A8%EC%9C%84%ED%85%8C%EC%8A%A4%ED%8A%B8-Repository/
 *             after, before 어노테이션 https://mimah.tistory.com/entry/Spring-Boot-AfterEach-BeforeEach-%EC%98%88%EC%A0%9C
 *                                    https://bcp0109.tistory.com/245
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
//@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodeRepository codeRepository;

    public Code code;
    public User user;

    @BeforeEach
    void before(){
        code = Code.builder()
                .codeName("전략채널팀")
                .codeType("DPT")
                .codeDepth(null)
                .codeCtg("CTG1111")
                .upperCode("")
                .codeRemark("나의소속팀")
                .code("DPT99")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();
        codeRepository.save(code);

        user = User.builder()
                .userId("hylim3")
                .userPw("2982")
                .adminYn("Y")
                .code(code)
                .userName("임현영")
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .createDate(new Timestamp(System.currentTimeMillis()))
                .createId("admin")
                .updateId("admin")
                .activeYn(true)
                .build();
    }

    @Test
    void save() {
        //given

        //when
        User saveResult = userRepository.save(user);
        log.info(saveResult.getUserId());
        //then
        assertThat(saveResult.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void findByUserId() {
        //given
        User saveResult = userRepository.save(user);
        //when
        User byUserId = userRepository.findByUserId(saveResult.getUserId());
        //then
        assertThat(byUserId.getUserId()).isEqualTo(saveResult.getUserId());
    }

    @Test
    void findByUserIdAndAdminYn() {
        //given
        User save = userRepository.save(user);
        //when
        User byUserIdAndAdminYn = userRepository.findByUserIdAndAdminYn(save.getUserId(), save.getAdminYn());
        //then
        assertThat(byUserIdAndAdminYn.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void findByUserIdAndActiveYn() {
        //given
        User save = userRepository.save(user);
        //when
        User byUserIdAndActiveYn = userRepository.findByUserIdAndActiveYn(save.getUserId(), save.isActiveYn());
        //then
        assertThat(byUserIdAndActiveYn.getUserId()).isEqualTo(save.getUserId());
    }

    @Test
    void findByCode() {
        //given
        User save = userRepository.save(user);
        //when
        List<User> byCode = userRepository.findByCode(code);
        log.info("##### : {}",byCode.size());
        //then
        assertThat(byCode.get(0).getCode().getCode()).isEqualTo(save.getCode().getCode());
    }

    @Test
    void findByCodeAndActiveYn() {
        //given
        User save = userRepository.save(user);
        //when
        List<User> byCodeAndActiveYn = userRepository.findByCodeAndActiveYn(code, true);
        log.info("#### : {}",byCodeAndActiveYn.size());
        //then
        assertThat(byCodeAndActiveYn.get(0).getCode().getCode()).isEqualTo(save.getCode().getCode());
    }

    @Test
    void findByActiveYn() {
        //given
        User save = userRepository.save(user);
        //when
        List<User> byActiveYn = userRepository.findByActiveYn(true);
        for(User i : byActiveYn){
            log.info("###### : {}",i.getUserName());
        }
        //then
        assertThat(byActiveYn.get(0).getUserName()).isEqualTo(save.getUserName());
    }
}