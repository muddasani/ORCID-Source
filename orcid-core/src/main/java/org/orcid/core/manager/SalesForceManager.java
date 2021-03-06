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
package org.orcid.core.manager;

import java.util.List;

import org.orcid.core.salesforce.model.Consortium;
import org.orcid.core.salesforce.model.Contact;
import org.orcid.core.salesforce.model.Member;
import org.orcid.core.salesforce.model.MemberDetails;

/**
 * 
 * @author Will Simpson
 *
 */
public interface SalesForceManager {

    List<Member> retrieveMembers();

    List<Member> retrieveConsortia();

    Consortium retrieveConsortium(String consortiumId);

    MemberDetails retrieveDetailsBySlug(String memberSlug);

    MemberDetails retrieveDetails(String memberId);

    List<Contact> retrieveContactsByAccountId(String accountId);

    void addOrcidsToContacts(List<Contact> contacts);

    void enableAccess(String accountId, List<Contact> contactsList);

    String retriveAccountIdByOrcid(String orcid);

    void updateMember(Member member);

    void createContact(Contact contact);

    /**
     * Updates the roles as specified by the boolean properties in the contact
     * object
     */
    void updateContact(Contact contact);

    void removeContact(Contact contact);

    /**
     * Clear caches
     * 
     */
    void evictAll();

}
