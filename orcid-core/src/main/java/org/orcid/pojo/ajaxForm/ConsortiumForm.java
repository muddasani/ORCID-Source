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
package org.orcid.pojo.ajaxForm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.orcid.core.salesforce.model.Contact;
import org.orcid.core.salesforce.model.Member;
import org.orcid.core.salesforce.model.MemberDetails;

public class ConsortiumForm implements ErrorsInterface, Serializable {
    private static final long serialVersionUID = 1L;

    private String accountId;
    private List<String> errors = new ArrayList<String>();
    private Text name;
    private Text website;
    private List<Contact> contactsList;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public Text getWebsite() {
        return website;
    }

    public void setWebsite(Text website) {
        this.website = website;
    }

    public List<Contact> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    public static ConsortiumForm fromMemberDetails(MemberDetails memberDetails) {
        ConsortiumForm form = new ConsortiumForm();
        Member member = memberDetails.getMember();
        form.setAccountId(member.getId());
        form.setName(Text.valueOf(member.getPublicDisplayName()));
        form.setWebsite(Text.valueOf(member.getWebsiteUrl().toString()));
        return form;
    }

    public MemberDetails toMemberDetails() {
        MemberDetails memberDetails = new MemberDetails();
        Member member = new Member();
        memberDetails.setMember(member);
        member.setId(getAccountId());
        member.setPublicDisplayName(getName().getValue());
        try {
            member.setWebsiteUrl(new URL(getWebsite().getValue()));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error parsing website", e);
        }
        return memberDetails;
    }

}
