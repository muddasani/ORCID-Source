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
package org.orcid.core.salesforce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Will Simpson
 *
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String role = ContactRoleType.OTHER_CONTACT.value();
    private String orcid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isMainContact() {
        return ContactRoleType.MAIN_CONTACT.value().equals(role);
    }

    public void setMainContact(boolean isMainContact) {
        if (isMainContact) {
            setRole(ContactRoleType.MAIN_CONTACT.value());
        }
    }

    public boolean isTechnicalContact() {
        return ContactRoleType.TECHNICAL_CONTACT.value().equals(role);
    }

    public void setTechnicalContact(boolean isTechnicalContact) {
        if (isTechnicalContact) {
            setRole(ContactRoleType.TECHNICAL_CONTACT.value());
        }
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    @Override
    public String toString() {
        return "Contact [id=" + id + ", accountId=" + accountId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", role=" + role
                + ", orcid=" + orcid + "]";
    }

}
