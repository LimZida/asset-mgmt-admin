package com.mcnc.assetmgmt.token.security;

import com.mcnc.assetmgmt.token.entity.Token;
import com.mcnc.assetmgmt.token.repository.TokenRepository;
import com.mcnc.assetmgmt.util.common.CodeAs;
import com.mcnc.assetmgmt.util.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
/**
 * title : JwtProvider
 *
 * description : jwt 토큰 발급 및 검증 함수
 *
 * reference : 환경변수 가져오는 법: https://kim-jong-hyun.tistory.com/17
 *
 *             Test 환경에서 Value 못쓰는 이유: https://chat.openai.com/share/7515cac0-7d3f-4523-8c7f-42beef6ca768
 *             JwtService 클래스의 secretKey 필드를 초기화하는 부분에서 문제가 발생하는 것 같습니다. @Value 어노테이션을 사용하여 encryptKey 프로퍼티 값을 주입하려고 하지만, 테스트 환경에서는 @Value 어노테이션을 사용할 수 없습니다.
 *             테스트 클래스에서 @DataJpaTest 어노테이션을 사용하고 있는데, 이 어노테이션은 JPA 관련 컴포넌트만을 테스트하기 위한 것입니다. 따라서 JwtService 클래스의 @Value 어노테이션을 사용하는 부분은 테스트 환경에서 작동하지 않습니다.
 *             해결 방법으로는 다음과 같이 JwtService 클래스의 생성자를 통해 secretKey 값을 주입받도록 변경하는 것입니다:
 *
 *             생성자 주입 vs 멤버변수 주입 and 암호화 권장방식: https://chat.openai.com/share/c1534be0-9898-4e86-aea4-f457e79259a8
 *             ~ which is not secure enough for the HS256 algorithm key size 오류: https://limm-jk.tistory.com/50
 *
 * author : 임현영
 * date : 2023.11.28
 **/
@Component
@Slf4j
public class JwtProvider {
    private final Key key;
    private final String AUTHORITIES_KEY;
    private final long accessTokenValidTime; //30분
    private final long refreshTokenValidTime; //7일
    private final TokenRepository tokenRepository;
    @Autowired
    public JwtProvider(@Value("${jwt.key}") String secretKey,
                       @Value("${jwt.authorities.key}") String AUTHORITIES_KEY,
                       @Value("${jwt.access.token.time}") long accessTokenValidTime,
                       @Value("${jwt.refresh.token.time}") long refreshTokenValidTime,
                       TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        this.AUTHORITIES_KEY=AUTHORITIES_KEY;
        this.accessTokenValidTime=accessTokenValidTime;
        this.refreshTokenValidTime=refreshTokenValidTime;
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String generateToken(Authentication authentication, Long accessTokenValidTime) {
        // 인증된 사용자의 권한 목록 조회
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenValidTime); // 만료 시간

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now) // 발행 시간
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512) // (비밀키, 해싱 알고리즘)
                .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenValidTime);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshTokenValidTime);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        log.info("###### authorities : {}" , authorities);

        User principal = new User(claims.getSubject(), CodeAs.BLANK, authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return CodeAs.APPROVAL_JWT_MESSAGE;
        } catch (ExpiredJwtException e) { // 기한 만료 시
            return CodeAs.EXPIRED_JWT_ERROR_MESSAGE;
        } catch (Exception e) { // 기타 오류 시
            return CodeAs.NON_VALIDATION_JWT_MESSAGE;
        }
    }

    //RefreshToken 조회
    public String getRefreshToken(String userId) {
        try {
            Token refreshToken = tokenRepository.findTokenByUserId(userId);
            if (refreshToken == null){
                throw new CustomException(CodeAs.SELECT_REFRESH_QUERY_JWT_ERROR_CODE,
                        HttpStatus.INTERNAL_SERVER_ERROR,CodeAs.SELECT_REFRESH_QUERY_JWT_ERROR_MESSAGE, null);
            }

            //일반 refresh토큰 인증 시
            if(StringUtils.hasText(refreshToken.getRefreshToken())){
                return refreshToken.getRefreshToken().substring(7);
            }
            //로그아웃시에는 refresh 토큰이 공백
            else{
                return refreshToken.getRefreshToken();
            }

        }catch (Exception e){
            throw new CustomException(CodeAs.SELECT_REFRESH_JWT_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR,CodeAs.SELECT_REFRESH_JWT_ERROR_MESSAGE, e);
        }
    }

    // JWT TOKEN 정보 확인
    public  Claims getInformation(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims;
    }

}
