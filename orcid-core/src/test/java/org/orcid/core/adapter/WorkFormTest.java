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
package org.orcid.core.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.jaxb.model.common.ContributorEmail;
import org.orcid.jaxb.model.common.ContributorOrcid;
import org.orcid.jaxb.model.common.Country;
import org.orcid.jaxb.model.common.CreatedDate;
import org.orcid.jaxb.model.common.CreditName;
import org.orcid.jaxb.model.common.Day;
import org.orcid.jaxb.model.common.Iso3166Country;
import org.orcid.jaxb.model.common.LastModifiedDate;
import org.orcid.jaxb.model.common.Month;
import org.orcid.jaxb.model.common.PublicationDate;
import org.orcid.jaxb.model.common.Subtitle;
import org.orcid.jaxb.model.common.Title;
import org.orcid.jaxb.model.common.Url;
import org.orcid.jaxb.model.common.Year;
import org.orcid.jaxb.model.message.FuzzyDate;
import org.orcid.jaxb.model.record.CitationType;
import org.orcid.jaxb.model.record.Work;
import org.orcid.jaxb.model.record.WorkExternalIdentifierId;
import org.orcid.jaxb.model.record.WorkExternalIdentifierType;
import org.orcid.jaxb.model.record.WorkExternalIdentifiers;
import org.orcid.jaxb.model.record.WorkTitle;
import org.orcid.jaxb.model.record.WorkType;
import org.orcid.pojo.ajaxForm.Citation;
import org.orcid.pojo.ajaxForm.Contributor;
import org.orcid.pojo.ajaxForm.Date;
import org.orcid.pojo.ajaxForm.PojoUtil;
import org.orcid.pojo.ajaxForm.Text;
import org.orcid.pojo.ajaxForm.TranslatedTitle;
import org.orcid.pojo.ajaxForm.Visibility;
import org.orcid.pojo.ajaxForm.WorkExternalIdentifier;
import org.orcid.pojo.ajaxForm.WorkForm;
import org.orcid.test.OrcidJUnit4ClassRunner;

/**
 * 
 * @author Angel Montenegro
 * 
 */
@RunWith(OrcidJUnit4ClassRunner.class)
public class WorkFormTest {

    private DatatypeFactory datatypeFactory = null;

    @Before
    public void before() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            // We're in serious trouble and can't carry on
            throw new IllegalStateException("Cannot create new DatatypeFactory");
        }
    }

    @Test
    public void equalsTest() {
        WorkForm form1 = getWorkForm();
        WorkForm form2 = getWorkForm();
        assertEquals(form1, form2);
        form1.setPutCode(Text.valueOf(String.valueOf(System.currentTimeMillis())));
        assertFalse(form1.equals(form2));
    }

    @Test
    public void toWorkTest() {
        WorkForm form = getWorkForm();
        Work work = form.toWork();
        assertEquals(getWork(), work);
    }

    @Test
    public void toWorkFormTest() {
        Work work = getWork();
        WorkForm form = WorkForm.valueOf(work);
        assertEquals(getWorkForm(), form);
    }

    private Work getWork() {
        Work work = new Work();
        work.setCountry(new Country(Iso3166Country.US));
        work.setJournalTitle(new Title("Journal title"));
        work.setLanguageCode("en");
        work.setPutCode("1");
        work.setShortDescription("Short description");
        work.setSource(new org.orcid.jaxb.model.common.Source("0000-0000-0000-0000"));
        work.setUrl(new Url("http://myurl.com"));
        work.setVisibility(org.orcid.jaxb.model.common.Visibility.PUBLIC);
        org.orcid.jaxb.model.record.Citation citation = new org.orcid.jaxb.model.record.Citation();
        citation.setCitation("Citation");
        citation.setWorkCitationType(CitationType.FORMATTED_UNSPECIFIED);
        work.setWorkCitation(citation);
        WorkTitle title = new WorkTitle();
        title.setTitle(new Title("Title"));
        title.setTranslatedTitle(new org.orcid.jaxb.model.common.TranslatedTitle("Translated Title", "es"));
        title.setSubtitle(new Subtitle("Subtitle"));
        work.setWorkTitle(title);
        work.setWorkType(WorkType.ARTISTIC_PERFORMANCE);
        Date date = new Date();
        date.setDay("1");
        date.setMonth("1");
        date.setYear("2015");
        GregorianCalendar calendar = date.toCalendar();
        work.setCreatedDate(new CreatedDate(datatypeFactory.newXMLGregorianCalendar(calendar)));
        date = new Date();
        date.setDay("2");
        date.setMonth("2");
        date.setYear("2015");
        calendar = date.toCalendar();
        work.setLastModifiedDate(new LastModifiedDate(datatypeFactory.newXMLGregorianCalendar(calendar)));
        work.setPublicationDate(new PublicationDate(new Year(2015), new Month(3), new Day(3)));
        org.orcid.jaxb.model.record.WorkContributors contributors = new org.orcid.jaxb.model.record.WorkContributors();
        org.orcid.jaxb.model.common.Contributor contributor = new org.orcid.jaxb.model.common.Contributor();
        org.orcid.jaxb.model.common.ContributorAttributes attributes = new org.orcid.jaxb.model.common.ContributorAttributes();
        attributes.setContributorRole(org.orcid.jaxb.model.common.ContributorRole.CO_INVENTOR);
        attributes.setContributorSequence(org.orcid.jaxb.model.record.SequenceType.FIRST);
        contributor.setContributorAttributes(attributes);
        contributor.setContributorEmail(new ContributorEmail("Contributor email"));
        contributor.setContributorOrcid(new ContributorOrcid("Contributor orcid"));
        contributor.setCreditName(new CreditName("Contributor credit name"));
        contributors.getContributor().add(contributor);
        work.setWorkContributors(contributors);
        WorkExternalIdentifiers externalIdentifiers = new WorkExternalIdentifiers();
        org.orcid.jaxb.model.record.WorkExternalIdentifier extId = new org.orcid.jaxb.model.record.WorkExternalIdentifier();
        extId.setWorkExternalIdentifierId(new WorkExternalIdentifierId("External Identifier ID"));
        extId.setWorkExternalIdentifierType(WorkExternalIdentifierType.ASIN);
        externalIdentifiers.getExternalIdentifier().add(extId);
        work.setWorkExternalIdentifiers(externalIdentifiers);
        return work;
    }

    private WorkForm getWorkForm() {
        WorkForm form = new WorkForm();
        form.setCitation(new Citation("Citation", "formatted-unspecified"));
        form.setCitationForDisplay("Citation for display");
        List<Contributor> çontributors = new ArrayList<Contributor>();
        Contributor contributor = new Contributor();
        contributor.setContributorRole(Text.valueOf("co_inventor"));
        contributor.setContributorSequence(Text.valueOf("first"));
        contributor.setCreditName(Text.valueOf("Contributor credit name"));
        contributor.setCreditNameVisibility(new Visibility());
        contributor.setEmail(Text.valueOf("Contributor email"));
        contributor.setOrcid(Text.valueOf("Contributor orcid"));
        contributor.setUri(Text.valueOf("Contributor uri"));
        çontributors.add(contributor);
        form.setContributors(çontributors);
        form.setCountryCode(Text.valueOf("US"));
        form.setCountryName(Text.valueOf("United States"));
        Date createdDate = new Date();
        createdDate.setDay("1");
        createdDate.setMonth("1");
        createdDate.setYear("2015");
        form.setCreatedDate(createdDate);
        form.setDateSortString(PojoUtil.createDateSortString(null, new FuzzyDate(2015, 1, 1)));
        form.setJournalTitle(Text.valueOf("Journal title"));
        form.setLanguageCode(Text.valueOf("en"));
        form.setLanguageName(Text.valueOf("English"));
        Date lastModifiedDate = new Date();
        lastModifiedDate.setDay("2");
        lastModifiedDate.setMonth("2");
        lastModifiedDate.setYear("2015");
        form.setLastModified(lastModifiedDate);
        Date publicationDate = new Date();
        publicationDate.setDay("3");
        publicationDate.setMonth("3");
        publicationDate.setYear("2015");
        form.setPublicationDate(publicationDate);
        form.setPutCode(Text.valueOf("1"));
        form.setShortDescription(Text.valueOf("Short description"));
        form.setSource("0000-0000-0000-0000");
        form.setSourceName("Source Name");
        form.setSubtitle(Text.valueOf("Subtitle"));
        form.setTitle(Text.valueOf("Title"));
        form.setTranslatedTitle(new TranslatedTitle("Translated Title", "es"));
        form.setUrl(Text.valueOf("http://myurl.com"));
        form.setVisibility(org.orcid.jaxb.model.message.Visibility.PUBLIC);
        form.setWorkCategory(Text.valueOf("Category"));
        List<WorkExternalIdentifier> extIds = new ArrayList<WorkExternalIdentifier>();
        WorkExternalIdentifier extId = new WorkExternalIdentifier();
        extId.setWorkExternalIdentifierId(Text.valueOf("External Identifier ID"));
        extId.setWorkExternalIdentifierType(Text.valueOf("asin"));
        extIds.add(extId);
        form.setWorkExternalIdentifiers(extIds);
        form.setWorkType(Text.valueOf("artistic-performance"));
        return form;
    }

    private void cleanTransientFields(WorkForm form) {
        form.setCitationForDisplay(null);
        form.setCountryName(null);
        form.setDateSortString(null);
        form.setLanguageName(null);
        form.setSourceName(null);
        form.setWorkCategory(null);
    }
}
