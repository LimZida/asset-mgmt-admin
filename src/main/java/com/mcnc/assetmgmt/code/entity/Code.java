package com.mcnc.assetmgmt.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * title : entity
 *
 * description : CODE 테이블 컬럼 매핑용 entity
 *
 * reference :  생성자 https://kadosholy.tistory.com/91
 *
 *              롬복  https://www.daleseo.com/lombok-popular-annotations/
 *              롬복을 쓸 경우 주의할 점 https://www.nowwatersblog.com/springboot/springstudy/lombok
 *                                   https://velog.io/@rosa/Lombok-%EC%A7%80%EC%96%91%ED%95%B4%EC%95%BC-%ED%95%A0-annotation
 *
 *              엔티티 https://choiseonjae.github.io/jpa/jpa-%EA%B8%B0%EB%B3%B8%ED%82%A4%EC%A0%84/
 *              엔티티 setter 쓰지않는 이유 https://velog.io/@langoustine/setter%EB%A5%BC-%EC%93%B0%EC%A7%80%EB%A7%90%EB%9D%BC%EA%B3%A0
 *                                       https://velog.io/@aidenshin/%EB%82%B4%EA%B0%80-%EC%83%9D%EA%B0%81%ED%95%98%EB%8A%94-JPA-%EC%97%94%ED%8B%B0%ED%8B%B0-%EC%9E%91%EC%84%B1-%EC%9B%90%EC%B9%99
 *
 *              Timestamp https://kyhslam.tistory.com/entry/1-Springboot-JPA-Oracle-%EC%97%B0%EB%8F%99-%EC%84%A4%EC%A0%95
 *                        https://velog.io/@koo8624/Spring-CreationTimestamp-UpdateTimestamp
 *
 *              빌더 : https://velog.io/@mooh2jj/%EC%98%AC%EB%B0%94%EB%A5%B8-%EC%97%94%ED%8B%B0%ED%8B%B0-Builder-%EC%82%AC%EC%9A%A9%EB%B2%95
 *              https://pamyferret.tistory.com/67
 *              올바른 빌더 사용법: https://velog.io/@mooh2jj/%EC%98%AC%EB%B0%94%EB%A5%B8-%EC%97%94%ED%8B%B0%ED%8B%B0-Builder-%EC%82%AC%EC%9A%A9%EB%B2%95
 *
 *              더티체킹 : https://jojoldu.tistory.com/415
 *
 *              null value was assigned to a property 에러 해결법 : https://rigun.tistory.com/66
 *
 *              Camel Case 변환: 필드명이 expireDate와 같이 Camel Case 형식이라면,
 *              JPA는 기본적으로 해당 필드를 데이터베이스 컬럼에 매핑할 때 언더스코어(_)를 사용하여 Snake Case 형식으로 변환합니다.
 *              예를 들어, expireDate 필드는 expire_date 컬럼과 매핑됩니다.
 *
 *              JPA @CreatedDate Column Update시 Null 되는 현상 해결방법 : https://wakestand.tistory.com/935
 *
 * author : 임현영
 * date : 2023.11.09
 **/
@Getter
//@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor(access = AccessLevel.PROTECTED) //protected 접근 제어자를 가진 기본 생성자를 자동으로 생성해주는 역할(다른 패키지의 기본 생성자 호출을 막음), Builder와 호환
@Entity(name = "CODE") //DB 테이블과 매핑
@ToString
public class Code{

    @Id
    private String code;
    @NotNull
    private String codeName;
    @NotNull
    private String codeType;
    private Integer codeDepth;
    private String upperCode;
    @NotNull
    private boolean activeYn;
    private String codeCtg;
    private String codeRemark;
    private Integer sortOrder;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;
    private String createId;
    @UpdateTimestamp
    private Timestamp updateDate;
    private String updateId;

    public void updateCodeName(String codeName) { this.codeName=codeName; }
    public void updateCodeRemark(String codeRemark) { this.codeRemark=codeRemark; }
    public void updateActiveYn(boolean activeYn) { this.activeYn =activeYn; }


    //빌더패턴 사용
    @Builder
    private Code(String code, String codeName, String codeType, Integer codeDepth,
                 String upperCode, Boolean activeYn, String codeCtg, String codeRemark,
                 Integer sortOrder, Timestamp createDate, String createId, Timestamp updateDate, String updateId){
        this.code = code;
        this.codeName = codeName;
        this.codeType = codeType;
        this.codeDepth = codeDepth;
        this.upperCode = upperCode;
        this.activeYn = activeYn;
        this.codeCtg = codeCtg;
        this.codeRemark = codeRemark;
        this.sortOrder = sortOrder;
        this.createDate = createDate;
        this.createId = createId;
        this.updateDate = updateDate;
        this.updateId = updateId;
    }
}
