/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.frontend.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.orcid.core.oauth.OrcidOAuth2Authentication;
import org.orcid.core.oauth.service.OrcidAuthorizationEndpoint;
import org.orcid.jaxb.model.message.CreationMethod;
import org.orcid.jaxb.model.message.OrcidProfile;
import org.orcid.pojo.ajaxForm.Checkbox;
import org.orcid.pojo.ajaxForm.OauthRegistrationForm;
import org.orcid.pojo.ajaxForm.RequestInfoForm;
import org.orcid.pojo.ajaxForm.Text;
import org.orcid.test.DBUnitTest;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.collect.Lists;

@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-frontend-web-servlet.xml", "classpath:orcid-core-context.xml" })
public class OauthRegistrationControllerTest extends DBUnitTest {
    private static final List<String> DATA_FILES = Arrays.asList("/data/EmptyEntityData.xml", "/data/SecurityQuestionEntityData.xml",
            "/data/SourceClientDetailsEntityData.xml", "/data/ProfileEntityData.xml", "/data/ClientDetailsEntityData.xml", "/data/RecordNameEntityData.xml", "/data/BiographyEntityData.xml");
    
    @Resource
    OauthRegistrationController oauthRegistrationController;
    
    @Mock
    RegistrationController registrationController;

    @Mock
    OrcidAuthorizationEndpoint authorizationEndpoint;
    
    @Mock
    AuthenticationManager authenticationManager;
    
    @Mock
    private HttpServletRequest servletRequest;
    
    @Mock
    private HttpServletResponse servletResponse;
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        initDBUnitData(DATA_FILES);
    }
    
    @AfterClass
    public static void afterClass() throws Exception {
        removeDBUnitData(Lists.reverse(DATA_FILES));
    }
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        oauthRegistrationController.setRegistrationController(registrationController);
        oauthRegistrationController.setAuthorizationEndpoint(authorizationEndpoint);
        oauthRegistrationController.setAuthenticationManager(authenticationManager);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testStripHtmlFromNames() throws UnsupportedEncodingException {
        HttpSession session = mock(HttpSession.class);
        RequestInfoForm rf = new RequestInfoForm();                
        RedirectView mv = new RedirectView();
        when(servletRequest.getSession()).thenReturn(session);
        when(servletRequest.getSession().getAttribute("requestInfoForm")).thenReturn(rf);
        when(authorizationEndpoint.approveOrDeny(Matchers.anyMap(), Matchers.anyMap(), Matchers.any(SessionStatus.class), Matchers.any(Principal.class))).thenReturn(mv);
        when(authenticationManager.authenticate(Matchers.any(Authentication.class))).thenAnswer(new Answer<Authentication>(){
            @Override
            public Authentication answer(InvocationOnMock invocation) throws Throwable {
                OrcidOAuth2Authentication mockedAuthentication = mock(OrcidOAuth2Authentication.class);
                return mockedAuthentication;
            }
        });
        
        Text email = Text.valueOf(System.currentTimeMillis() + "@test.orcid.org");
        
        OauthRegistrationForm reg = new OauthRegistrationForm();
        org.orcid.pojo.ajaxForm.Visibility fv = new org.orcid.pojo.ajaxForm.Visibility();
        fv.setVisibility(org.orcid.jaxb.model.common_v2.Visibility.PUBLIC);
        reg.setActivitiesVisibilityDefault(fv);        
        reg.setEmail(email);
        reg.setEmailConfirm(email);
        reg.setFamilyNames(Text.valueOf("<button onclick=\"alert('hello')\">Family Name</button>"));
        reg.setGivenNames(Text.valueOf("<button onclick=\"alert('hello')\">Given Names</button>"));
        reg.setPassword(Text.valueOf("1234abcd"));
        reg.setPasswordConfirm(Text.valueOf("1234abcd"));
        reg.setValNumClient(2L);
        reg.setValNumServer(4L);
        reg.setApproved(true);
        Checkbox c = new Checkbox();
        c.setValue(true);
        reg.setTermsOfUse(c);
        reg.setCreationType(Text.valueOf(CreationMethod.DIRECT.value()));
        reg.setPersistentTokenEnabled(true);
        oauthRegistrationController.registerAndAuthorize(servletRequest, servletResponse, reg);
        
        ArgumentCaptor<HttpServletRequest> argument1 = ArgumentCaptor.forClass(HttpServletRequest.class);
        ArgumentCaptor<OrcidProfile> argument2 = ArgumentCaptor.forClass(OrcidProfile.class);
        ArgumentCaptor<Boolean> argument3 = ArgumentCaptor.forClass(Boolean.class);
        
        
        verify(registrationController).createMinimalRegistration(argument1.capture(), argument2.capture(), argument3.capture());
        assertNotNull(argument2.getValue());
        OrcidProfile profile = argument2.getValue();
        assertNotNull(profile.getOrcidBio());
        assertNotNull(profile.getOrcidBio().getPersonalDetails());
        assertNotNull(profile.getOrcidBio().getPersonalDetails().getGivenNames());
        assertNotNull(profile.getOrcidBio().getPersonalDetails().getFamilyName());
        
        assertEquals("Given Names", profile.getOrcidBio().getPersonalDetails().getGivenNames().getContent());
        assertEquals("Family Name", profile.getOrcidBio().getPersonalDetails().getFamilyName().getContent());        
    }
}
