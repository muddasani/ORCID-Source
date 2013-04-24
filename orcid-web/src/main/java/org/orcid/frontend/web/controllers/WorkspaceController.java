/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2013 ORCID, Inc.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.orcid.core.manager.ExternalIdentifierManager;
import org.orcid.core.manager.ThirdPartyImportManager;
import org.orcid.frontend.web.forms.CurrentWork;
import org.orcid.jaxb.model.clientgroup.OrcidClient;
import org.orcid.jaxb.model.message.ExternalIdentifier;
import org.orcid.jaxb.model.message.OrcidProfile;
import org.orcid.pojo.ExternalIdentifiers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * @author Will Simpson
 */
@Controller("workspaceController")
@RequestMapping(value = { "/my-orcid", "/workspace" })
public class WorkspaceController extends BaseWorkspaceController {

    @Resource
    private ThirdPartyImportManager thirdPartyImportManager;

    @Resource
    private ExternalIdentifierManager externalIdentifierManager;
    
    @ModelAttribute("thirdPartiesForImport")
    public List<OrcidClient> retrieveThirdPartiesForImport() {
        return thirdPartyImportManager.findOrcidClientsWithPredefinedOauthScopeForImport();
    }

    @RequestMapping
    public ModelAndView viewWorkspace(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int pageNo,
            @RequestParam(value = "maxResults", defaultValue = "200") int maxResults) {
        ModelAndView mav = new ModelAndView("workspace");
        mav.addObject("showPrivacy", true);

        OrcidProfile profile = orcidProfileManager.retrieveOrcidProfile(getCurrentUserOrcid());
        getCurrentUser().setEffectiveProfile(profile);
        List<CurrentWork> currentWorks = getCurrentWorksFromProfile(profile);
        if (currentWorks != null && !currentWorks.isEmpty()) {
            mav.addObject("currentWorks", currentWorks);
        }
        mav.addObject("profile", profile);
        return mav;
    }

    @RequestMapping(value = { "/public", "/preview" })
    public ModelAndView preview(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int pageNo,
            @RequestParam(value = "maxResults", defaultValue = "200") int maxResults) {
        ModelAndView mav = new ModelAndView("public_profile");
        mav.addObject("isPreview", true);

        OrcidProfile profile = orcidProfileManager.retrievePublicOrcidProfile(getCurrentUserOrcid());
        request.getSession().removeAttribute(PUBLIC_WORKS_RESULTS_ATTRIBUTE);
        List<CurrentWork> currentWorks = getCurrentWorksFromProfile(profile);
        if (currentWorks != null && !currentWorks.isEmpty()) {
            mav.addObject("currentWorks", currentWorks);
        }
        mav.addObject("profile", profile);
        return mav;
    }

    /**
     * Retrieve all external identifiers as a json string
     * */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/externalIdentifiers.json", method = RequestMethod.GET)
    public @ResponseBody
    org.orcid.pojo.ExternalIdentifiers getExternalIdentifiersJson(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
        OrcidProfile currentProfile = getCurrentUser().getEffectiveProfile();
        ExternalIdentifiers externalIdentifiers = new org.orcid.pojo.ExternalIdentifiers();
        externalIdentifiers.setExternalIdentifiers((List<org.orcid.pojo.ExternalIdentifier>) (Object) currentProfile.getOrcidBio().getExternalIdentifiers()
                .getExternalIdentifier());
        return externalIdentifiers;
    }

    /**
     * Updates the list of external identifiers assigned to a user
     * */
    @RequestMapping(value = "/externalIdentifiers.json", method = RequestMethod.DELETE)
    public @ResponseBody
    org.orcid.pojo.ExternalIdentifiers removeExternalIdentifierJson(HttpServletRequest request, @RequestBody org.orcid.pojo.ExternalIdentifier externalIdentifier) {
        List<String> allErrors = new ArrayList<String>();

        externalIdentifier.setErrors(allErrors);
        
        // If the orcid is blank, add an error
        if (externalIdentifier.getOrcid() == null || StringUtils.isBlank(externalIdentifier.getOrcid().getValue())) {
            allErrors.add(getMessage("ExternalIdentifier.orcid"));
        }

        // If the external identifier is blank, add an error
        if (externalIdentifier.getExternalIdReference() == null || StringUtils.isBlank(externalIdentifier.getExternalIdReference().getContent())) {
            allErrors.add(getMessage("ExternalIdentifier.externalIdReference"));                
        }

        
        if (allErrors.isEmpty()) {
            OrcidProfile currentProfile = getCurrentUser().getEffectiveProfile();
            List<ExternalIdentifier> externalIdentifiers = currentProfile.getOrcidBio().getExternalIdentifiers().getExternalIdentifier();
            Iterator<ExternalIdentifier> externalIdentifierIterator = externalIdentifiers.iterator();
            
            while(externalIdentifierIterator.hasNext()){
                ExternalIdentifier identifier = externalIdentifierIterator.next();
                if(identifier.getOrcid().getValue().equals(externalIdentifier.getOrcid().getValue()) && identifier.getExternalIdReference().getContent().equals(externalIdentifier.getExternalIdReference().getContent())){
                    externalIdentifierIterator.remove();
                    break;
                }
            }
            
            externalIdentifierManager.removeExternalIdentifier(externalIdentifier.getOrcid().getValue(), externalIdentifier.getExternalIdReference().getContent());
        }

        return null;
    }
}
