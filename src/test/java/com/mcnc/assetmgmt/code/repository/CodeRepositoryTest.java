package com.mcnc.assetmgmt.code.repository;

import com.mcnc.assetmgmt.code.entity.Code;
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
 * title : CodeRepositoryTest
 * description : Code entity 사용한 codeRepository test
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
class CodeRepositoryTest {

    @Autowired
    private CodeRepository codeRepository;
    public Code code;
    public Code code2;
    public Code save;
    public Code save2;
    public List<Code> saveList = new ArrayList<>();

    @BeforeEach
    void before(){
        code = Code.builder()
                .codeName("천국팀")
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

        code2 = Code.builder()
                .codeName("지옥팀")
                .codeType("DPT")
                .codeDepth(null)
                .codeCtg("CTG1111")
                .upperCode("")
                .codeRemark("너의소속팀")
                .code("DPT98")
                .activeYn(true)
                .updateId("admin")
                .createId("admin")
                .createDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .sortOrder(8)
                .build();

        save = codeRepository.save(code);
        save2 = codeRepository.save(code2);

        saveList.add(save);
        saveList.add(save2);

        log.info(save.toString());
    }

    @Test
    void save() {
        //given
        //when
        Code save = codeRepository.save(code);
        log.info(save.toString());
        //then
        assertThat(save.getCode()).isEqualTo(code.getCode());
    }

    @Test
    void findByCodeType() {
        //given
        codeRepository.save(code);
        //when
        List<Code> type = codeRepository.findByCodeType("ASSET");
        for(Code i : type){
            log.info(i.getCode()+" "+i.getCodeName());
        }
        //then
        assertThat(type.size()).isEqualTo(29);
    }

    @Test
    void findByCodeTypeAndActiveYn() {
        //given
        Code save = codeRepository.save(code);
        //when
        List<Code> byCodeTypeAndActiveYn = codeRepository.findByCodeTypeAndActiveYn(save.getCodeType(), true);
        for(Code i : byCodeTypeAndActiveYn){
            log.info(i.getCode()+" "+i.getCodeName());
        }
        //then
        assertThat(byCodeTypeAndActiveYn.size()).isEqualTo(29);
    }

    @Test
    void findByCodeCtg() {
        //given
        Code save = codeRepository.save(code);
        //when
        List<Code> byCodeCtg = codeRepository.findByCodeCtg(save.getCodeCtg());
        log.info(byCodeCtg.toString());
        //then
        assertThat(byCodeCtg.size()).isEqualTo(99);
    }

    @Test
    void findByCodeCtgAndActiveYn() {
        //given
        Code save = codeRepository.save(code);
        //when
        List<Code> byCodeCtg = codeRepository.findByCodeCtgAndActiveYn(save.getCodeCtg(),true);
        log.info(byCodeCtg.toString());
        //then
        assertThat(byCodeCtg.size()).isEqualTo(1);
    }

    @Test
    void findByUpperCode() {
        //given
        //when
        List<Code> code = codeRepository.findByUpperCode(save.getUpperCode());
        log.info(code.toString());
        //then
        assertThat(code.size()).isEqualTo(3);
    }

    @Test
    void findByCode() {
        //given
        //when
        Code byCode = codeRepository.findByCode(save.getCode());
        //then
        assertThat(byCode.getCode()).isEqualTo(save.getCode());
    }

    @Test
    void findByCodeAndActiveYn() {
        //given
        //when
        Code byCode = codeRepository.findByCodeAndActiveYn(save.getCode(),false);
        log.info("#### {}",byCode);
        //then
        assertThat(byCode).isEqualTo(null);
    }

    @Test
    void findByCodeTypeAndCodeNameContaining() {
        //given
        //when
        List<Code> codeList = codeRepository.findByCodeTypeAndCodeNameContaining(save.getCodeType(), "천국");
        log.info(codeList.toString());
        //then
        assertThat(codeList.size()).isEqualTo(1);
    }

    @Test
    void findByCodeTypeAndCodeNameContainingAndCodeCtg() {
        //given
        //when
        List<Code> codeList = codeRepository.findByCodeTypeAndCodeNameContainingAndCodeCtg(save.getCodeType(), "천국", save.getCodeCtg());
        log.info(codeList.toString());
        //then
        assertThat(codeList.size()).isEqualTo(1);

    }

    @Test
    void findByCodeTypeAndCodeNameContainingAndCodeIsNotAndCodeCtg() {
        //given
        //when
        List<Code> dupList = codeRepository.findByCodeTypeAndCodeNameContainingAndCodeIsNotAndCodeCtg(saveList.get(0).getCodeType(), "지옥"
                , saveList.get(0).getCode(), saveList.get(0).getCodeCtg());
        log.info("#### : {}",dupList.toString());
        //then
        assertThat(dupList.size()).isEqualTo(1);

    }

    @Test
    void findByCodeTypeAndUpperCodeAndCodeNameContainingAndCodeIsNot() {
        //given
        //when
        List<Code> codeList = codeRepository.findByCodeTypeAndUpperCodeAndCodeNameContainingAndCodeIsNot(saveList.get(0).getCodeType(),
                saveList.get(0).getUpperCode(), "지옥", saveList.get(0).getCode());
        log.info(codeList.toString());
        //then
        assertThat(codeList.size()).isEqualTo(1);
    }

    @Test
    void findByUpperCodeAndCodeNameContaining() {
        //given
        //when
        List<Code> codeList = codeRepository.findByUpperCodeAndCodeNameContaining(saveList.get(0).getUpperCode(), "지");
        log.info(codeList.toString());
        //then
        assertThat(codeList.size()).isEqualTo(1);
    }
}