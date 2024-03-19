package com.mcnc.assetmgmt.util.ldap;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.mcnc.assetmgmt.util.common.CodeAs;
import com.mcnc.assetmgmt.util.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
/**
 * title : MCNCLdap
 *
 * description : Ldap 서버 내에 있는 임직원 정보 조회를 통해 로그인을 진행하는 함수
 *
 * reference : 저도 받아온 코드에요..
 *
 *
 * author : 임현영
 * date : 2023.11.08
 **/
@Component
@Slf4j
public class MCNCLdap {
    private final String SIMPLE;
    private final String COM_SUN_JNDI_LDAP_CTX_FACTORY;
    private final String DOMAIN;
    private final String SEARCH_BASE;
    private final String LDAP_HOST;
    private final String MCNC;
    private final String LDAP_ADMIN_ID;
    private final String LDAP_ADMIN_PW;

    @Autowired
    public MCNCLdap(@Value("${ldap.simple}") String SIMPLE, @Value("${ldap.com.sum.jndi.ldap}") String COM_SUN_JNDI_LDAP_CTX_FACTORY,
                    @Value("${ldap.domain}") String DOMAIN, @Value("${ldap.search.base}") String SEARCH_BASE,
                    @Value("${ldap.host}") String LDAP_HOST, @Value("${ldap.mcnc}") String MCNC,
                    @Value("${ldap.admin.id}") String LDAP_ADMIN_ID, @Value("${ldap.admin.pw}") String LDAP_ADMIN_PW) {
        this.SIMPLE=SIMPLE;
        this.COM_SUN_JNDI_LDAP_CTX_FACTORY=COM_SUN_JNDI_LDAP_CTX_FACTORY;
        this.DOMAIN = DOMAIN;
        this.SEARCH_BASE = SEARCH_BASE;
        this.LDAP_HOST = LDAP_HOST;
        this.MCNC = MCNC;
        this.LDAP_ADMIN_ID = LDAP_ADMIN_ID;
        this.LDAP_ADMIN_PW = LDAP_ADMIN_PW;
    }

    //LDAP을통한 로그인 코드
    public Map ldapLogin(String userId, String userPw) {
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + userId
                    + "))";

        try {
            // Create the search controls
            SearchControls searchCtls = new SearchControls();

            // Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //createLdapContext
            LdapContext ctxGC = createLdapContext(userId, userPw, CodeAs.LOGIN);

            // Search objects in GC using filters
            NamingEnumeration answer = ctxGC.search(SEARCH_BASE, searchFilter,
                    searchCtls);

            Map userInfoMap = getUserInfoElements(answer,new HashMap());
            return userInfoMap;
        } catch (Exception e) {
            throw new CustomException(CodeAs.USER_LDAP_LOGIN_FAIL_CODE , HttpStatus.INTERNAL_SERVER_ERROR, CodeAs.USER_LDAP_LOGIN_FAIL_MESSAGE , e);
        }
    }

    //LDAP 유저 검증
    public boolean validateLdapUser(String userId) throws Exception {
        LdapContext ctxGC = null;

        try {
            // Search objects in GC using filters
            String[] returnedAtts = { "name", "sAMAccountName" };
            String searchFilter = "(&(objectClass=user)(sAMAccountName=" + userId + "))";	//검증하고자 하는 아이디

            // Create the search controls
            SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);
            // Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            ctxGC = createLdapContext(CodeAs.BLANK, CodeAs.BLANK,CodeAs.VALIDATION);
            ctxGC.addToEnvironment(Context.SECURITY_AUTHENTICATION, SIMPLE);
            ctxGC.addToEnvironment(Context.SECURITY_PRINCIPAL, MCNC + "\\" + LDAP_ADMIN_ID);	//관리자 아이디
            ctxGC.addToEnvironment(Context.SECURITY_CREDENTIALS, LDAP_ADMIN_PW);	//관리자 패스워드
            ctxGC.reconnect(null);

            NamingEnumeration answer = ctxGC.search(SEARCH_BASE, searchFilter, searchCtls);
            Map userMap = getUserInfoElements(answer, new HashMap());

            //조회되는 유저가 없을 경우
            if (userMap.isEmpty()) {
                log.info("##### 유저 LDAP 검증 결과: "+userId+"에 해당하는 유저 없음");
                return false;
            } else {
                log.info("##### 유저 LDAP 검증 결과: "+userId+"에 해당하는 유저 존재");
            }

            return true;
        } catch (Exception e) {
            throw new CustomException(CodeAs.LDAP_VALIDATION_USER_FAIL_CODE ,
                    HttpStatus.INTERNAL_SERVER_ERROR, CodeAs.LDAP_VALIDATION_USER_FAIL_MESSAGE , e);
        } finally {
            if (ctxGC != null) {
                ctxGC.close();
            }
        }
    }

    private LdapContext createLdapContext(String userId, String passWd, String type) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, COM_SUN_JNDI_LDAP_CTX_FACTORY);
        env.put(Context.PROVIDER_URL, LDAP_HOST);
        if(type.equals(CodeAs.LOGIN)){
            env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
            env.put(Context.SECURITY_PRINCIPAL, userId + DOMAIN);
            env.put(Context.SECURITY_CREDENTIALS, passWd);
        }

        return new InitialLdapContext(env, null);
    }

    private HashMap getUserInfoElements(NamingEnumeration answer, HashMap userInfoMap) throws NamingException {
        while (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();
            Attributes attrs = sr.getAttributes();
            if (attrs != null) {
                NamingEnumeration ne = attrs.getAll();
                while (ne.hasMore()) {
                    Attribute attr = (Attribute) ne.next();
                    userInfoMap.put(attr.getID(), attr.get());
                }
                ne.close();
            }
        }

        return userInfoMap;
    }
}
