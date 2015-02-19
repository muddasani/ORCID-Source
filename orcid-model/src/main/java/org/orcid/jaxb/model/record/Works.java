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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.18 at 10:32:14 AM GMT 
//

package org.orcid.jaxb.model.record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "work" })
@XmlRootElement(name = "works")
public class Works implements Serializable, ActivitiesContainer {

    private static final long serialVersionUID = 1L;
    @XmlElement(name = "work")
    protected List<Work> works;

    /**
     * Gets the value of the orcidWork property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the orcidWork property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getOrcidWork().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Work }
     * 
     * 
     */
    public List<Work> getOrcidWork() {
        if (works == null) {
            works = new ArrayList<Work>();
        }
        return this.works;
    }

    public Map<String, Work> retrieveActivitiesAsMap() {
        Map<String, Work> workMap = new HashMap<String, Work>();
        if (works != null) {
            for (Work work : works) {
                if (StringUtils.isNotBlank(work.putCode)) {
                    workMap.put(work.putCode, work);
                }
            }
        }
        return workMap;
    }
    
    @Override
    public List<Work> retrieveActivities() {
        return getOrcidWork();
    }


    public void setOrcidWork(List<Work> orcidWork) {
        this.works = orcidWork;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Works that = (Works) o;

        if (works != null ? !works.equals(that.works) : that.works != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = works != null ? works.hashCode() : 0;
        result = 31 * result;
        return result;
    }

}
