//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.26 at 11:29:18 AM GMT 
//


package xmlns.org.eurocris.cerif_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cfResPublVersInfo__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfResPublVersInfo__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfResPublId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfVersInfo" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfResPublVersInfo__Type", propOrder = {
    "cfResPublId",
    "cfVersInfo"
})
public class CfResPublVersInfoType {

    @XmlElement(required = true)
    protected String cfResPublId;
    @XmlElement(required = true)
    protected CfMLangStringType cfVersInfo;

    /**
     * Gets the value of the cfResPublId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfResPublId() {
        return cfResPublId;
    }

    /**
     * Sets the value of the cfResPublId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfResPublId(String value) {
        this.cfResPublId = value;
    }

    /**
     * Gets the value of the cfVersInfo property.
     * 
     * @return
     *     possible object is
     *     {@link CfMLangStringType }
     *     
     */
    public CfMLangStringType getCfVersInfo() {
        return cfVersInfo;
    }

    /**
     * Sets the value of the cfVersInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfMLangStringType }
     *     
     */
    public void setCfVersInfo(CfMLangStringType value) {
        this.cfVersInfo = value;
    }

}