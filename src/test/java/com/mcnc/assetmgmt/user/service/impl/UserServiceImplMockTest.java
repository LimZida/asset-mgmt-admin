package com.mcnc.assetmgmt.user.service.impl;

import com.mcnc.assetmgmt.assignment.repository.AssignmentRepository;
import com.mcnc.assetmgmt.code.dto.CodeDto;
import com.mcnc.assetmgmt.code.entity.Code;
import com.mcnc.assetmgmt.code.repository.CodeRepository;
import com.mcnc.assetmgmt.token.entity.Token;
import com.mcnc.assetmgmt.token.repository.TokenRepository;
import com.mcnc.assetmgmt.token.security.JwtProvider;
import com.mcnc.assetmgmt.user.dto.UserDto;
import com.mcnc.assetmgmt.user.entity.User;
import com.mcnc.assetmgmt.user.repository.UserRepository;
import com.mcnc.assetmgmt.util.ldap.MCNCLdap;
import com.mcnc.assetmgmt.util.common.CodeAs;
import com.mcnc.assetmgmt.util.crypto.CryptAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * title : UserServiceImplTest
 * description : User entity 사용한 UserServiceImplTest test
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
class UserServiceImplMockTest {
    @Mock
    private CodeRepository codeRepository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;
//    @Mock
//    private com.mcnc.assetmgmt.util.common.Response res;
//    MockedStatic<com.mcnc.assetmgmt.util.common.Response> mockedStatic;

    @Mock
    private MCNCLdap mcncLdap;
    @Mock
    private CryptAlgorithm cryptAlgorithm;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;


    private List<User> userList = new ArrayList<>();
    List<Code> codeList = new ArrayList<>();
    private MockHttpServletResponse response = new MockHttpServletResponse();
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @BeforeEach
    void setUp(){
        Code code = Code.builder()
                .code("DPT01")
                .codeName("물빵제조학과")
                .codeType("DPT")
                .codeDepth(-1)
                .upperCode("")
                .codeCtg("CTG0202002")
                .codeRemark("-")
                .activeYn(true)
                .build();

        Code code2 = Code.builder()
                .code("DPT02")
                .codeName("내거친생각과")
                .codeType("DPT")
                .codeDepth(-1)
                .upperCode("")
                .codeCtg("CTG0202002")
                .codeRemark("-")
                .activeYn(true)
                .build();

        User user = User.builder()
                .userId("hylim")
                .userName("임현영")
                .userPw("")
                .activeYn(true)
                .code(code)
                .adminYn("Y")
                .build();

        User user2 = User.builder()
                .userId("cslim")
                .userName("임채성")
                .activeYn(true)
                .code(code)
                .adminYn("Y")
                .build();

        User user3 = User.builder()
                .userId("trkim")
                .userName("김태련")
                .activeYn(true)
                .code(code)
                .adminYn("Y")
                .build();

        User user4 = User.builder()
                .userId("hschoi")
                .userName("최호석")
                .activeYn(true)
                .code(code)
                .adminYn("Y")
                .build();


        userList.add(user);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        codeList.add(code);
        codeList.add(code2);


//        mockedStatic = mockStatic(com.mcnc.assetmgmt.util.common.Response.class);
//        when(res.makeFailResponse(any(),any(),any(),any())).thenReturn(new CustomException("", HttpStatus.BAD_REQUEST
//                ,"",null));
//        res.createTrLog(any(),any());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUserByLoginInfoService() throws Exception {
        //given
        UserDto.loginInfo loginInfo = UserDto.loginInfo.builder()
                .userId("hylim")
                .userPw("bin135462**")
                .build();

//        Token token= Token.builder()
//                .userId(loginInfo.getUserId())
//                .refreshToken(CodeAs.JWT_HEADER+"")
//                .build();

        Map userMap = new HashMap();
        userMap.put(CodeAs.LDAP_RESULT_ID , "hylim");
        userMap.put(CodeAs.LDAP_RESULT_NAME , "임현영");
        when(cryptAlgorithm.decrypt(any())).thenReturn(loginInfo.getUserPw());
        when(mcncLdap.ldapLogin(any(), any())).thenReturn(userMap);

        when(userRepository.findByUserId(loginInfo.getUserId())).thenReturn(userList.get(0));

//        when(passwordEncoder.encode(any())).thenReturn(loginInfo.getUserPw());
//        when(jwtProvider.generateAccessToken(any())).thenReturn("");
//        when(jwtProvider.generateRefreshToken(any())).thenReturn("");
//        when(tokenRepository.save(any())).thenReturn(token);


        //when
        UserDto.resultInfo userByLoginInfoService = userService.getUserByLoginInfoService(loginInfo, response);

        //then
        assertThat(userByLoginInfoService.getResult()).isEqualTo(true);
    }

    @Test
    void getUserByLogoutService() {
        //given
        Token token = Token.builder()
                .refreshToken("tt")
                .userId("hylim")
                .build();

        log.info("###### 변경 전: {}",token.getRefreshToken());

        when(tokenRepository.findTokenByUserId(any())).thenReturn(token);
        UserDto.logoutInfo logoutInfo = UserDto.logoutInfo.builder()
                .accessToken("z")
                .userId("hylim")
                .build();

        //when
        UserDto.resultInfo userByLogoutService = userService.getUserByLogoutService(logoutInfo, response);
        log.info(userByLogoutService.toString());

        //then
        assertThat(true).isEqualTo(userByLogoutService.getResult());
    }

    @Test
    void getAllEmployeeService() {
        //given
        when(userRepository.findAll()).thenReturn(userList);


        //when
        List<UserDto.userListInfo> list = userService.getAllEmployeeService();

        //then
        for (UserDto.userListInfo i : list){
            log.info("###### {}",i.toString());
        }

        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    void upsertEmployeeService() {
        //given
        Code code = Code.builder()
                .code("DPT01")
                .codeName("물빵제조학과")
                .codeType("DPT")
                .codeDepth(-1)
                .upperCode("")
                .codeCtg("CTG0202002")
                .codeRemark("-")
                .activeYn(true)
                .build();


        Code code2 = Code.builder()
                .code("DPT02")
                .codeName("내거친생각과")
                .codeType("DPT")
                .codeDepth(-1)
                .upperCode("")
                .codeCtg("CTG0202002")
                .codeRemark("-")
                .activeYn(true)
                .build();

        Code statusCode1 = Code.builder()
                .code("STS02")
                .codeName("반입")
                .codeType("STS")
                .codeDepth(-1)
                .upperCode("")
                .codeCtg("CTG0202003")
                .codeRemark("-")
                .activeYn(true)
                .build();

        Code statusCode2 = Code.builder()
                .code("STS03")
                .codeName("반출")
                .codeType("STS")
                .codeDepth(-1)
                .upperCode("")
                .codeCtg("CTG0202003")
                .codeRemark("-")
                .activeYn(true)
                .build();


        UserDto.userInfo userInfo = UserDto.userInfo.builder()
                .userId("hylim")
                .userDept("DPT02")
                .userName("임멋진")
                .build();

        String type1 = CodeAs.UPDATE;
        String type2 = CodeAs.INSERT;


        //코드조회
        when(codeRepository.findByCode(any())).thenReturn(code2);

        User upsertUser = User.builder()
                .userId(userInfo.getUserId())
                .userName(userInfo.getUserName())
                .userPw("")
                .activeYn(true)
                .code(code2)
                .adminYn("Y")
                .build();

        //유저 저장
        when(userRepository.save(any())).thenReturn(upsertUser);

        //업데이트 시 기존 정보를 사용하기 위해 쿼리 조회 및
        when(userRepository.findByUserId(any())).thenReturn(upsertUser);
        //할당테이블 수정
//        List<AssignmentEntity> list = new ArrayList<>();
//        AssignmentEntity assignmentEntity1 = AssignmentEntity.builder()
//                .assetNo("NT950-0")
//                .createId("admin")
//                .assetStatus(statusCode1)
//                .deleteYn("N")
//                .id(1L)
//                .usageDept(code)
//                .updateId("admin")
//                .usageName("임현영")
//                .usageId("hylim")
//                .build();

//        AssignmentEntity assignmentEntity2 = AssignmentEntity.builder()
//                .assetNo("NT950-1")
//                .createId("admin")
//                .assetStatus(statusCode2)
//                .deleteYn("N")
//                .id(1L)
//                .usageDept(code)
//                .updateId("admin")
//                .usageName("임채성")
//                .usageId("cslim")
//                .build();
//        list.add(assignmentEntity1);
//        list.add(assignmentEntity2);
//        when(assignmentRepository.findByUsageId(any())).thenReturn(list);
        //모든 유저 조회
//        userList.add(upsertUser);
        when(userRepository.findAll()).thenReturn(userList);

        //when
        List<UserDto.userListInfo> userListInfo = userService.upsertEmployeeService(userInfo, type2);


        //then
        for(UserDto.userListInfo i : userListInfo){
            log.info("##### : {}" , i.toString());
        }
//        for(AssignmentEntity i : list){
//            log.info("##### : {}" , i.getUsageDept().getCodeName() + " " + i.getUsageName());
//        }

        assertThat(userListInfo.size()).isEqualTo(4);
    }

    @Test
    void activateEmployee() {
        //given
        UserDto.userIdInfo userIdInfo = UserDto.userIdInfo.builder()
                .userId("hylim")
                .build();

        when(userRepository.findByUserId(userIdInfo.getUserId())).thenReturn(userList.get(0));
        boolean originStatus = userList.get(0).isActiveYn();
        log.info("##### 수정 전: {}", userList.get(0).getUserName() + " " +userList.get(0).isActiveYn());
        when(userRepository.findAll()).thenReturn(userList);

        //when
        List<UserDto.userListInfo> userListInfo = userService.activateEmployee(userIdInfo);
        UserDto.userListInfo userInfo = userListInfo.get(0);
        log.info("##### 수정 후: {}", userInfo.getUserName() + " " +userInfo.isActiveYn());

        //then
        assertThat(userInfo.isActiveYn()).isNotEqualTo(originStatus);
    }

    @Test
    void validateEmployeeService() throws Exception {
        //given
        UserDto.userIdInfo userIdInfo = UserDto.userIdInfo.builder()
                .userId("hylim")
                .build();

        // User null x && ldap true => dup
        // user null x && ldap false => dup
        // user null && ldap true => dup
        // user null && ldap false 해당경우에만 notDup
        when(userRepository.findByUserId(userIdInfo.getUserId())).thenReturn(null);
        when(mcncLdap.validateLdapUser(userIdInfo.getUserId())).thenReturn(true);

        //when
        UserDto.validateInfo validateInfo = userService.validateEmployeeService(userIdInfo);
        //then
        assertThat(validateInfo.getUserSelectResult()).isEqualTo("dup");
    }

    @Test
    void allDeptCodeService() {
        //given
        when(codeRepository.findByCodeTypeAndActiveYn(CodeAs.DEPARTMENT,true)).thenReturn(codeList);
        //when
        List<CodeDto.deptInfo> deptInfo = userService.allDeptCodeService();
        for(CodeDto.deptInfo i : deptInfo){
            log.info("####### : {}", i.getDeptName());
        }
        //then
        assertThat(codeList.size()).isEqualTo(deptInfo.size());
    }
}