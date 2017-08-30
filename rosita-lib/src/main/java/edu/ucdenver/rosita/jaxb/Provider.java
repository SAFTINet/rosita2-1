/*
*   Copyright 2012-2013 The Regents of the University of Colorado
*
*   Licensed under the Apache License, Version 2.0 (the "License")
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.21 at 10:33:43 PM EDT 
//


package edu.ucdenver.rosita.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for provider complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="provider">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="provider_source_value" type="{}string_max50"/>
 *         &lt;element name="x_data_source_type" type="{}string_max20"/>
 *         &lt;element name="npi" type="{}string_max50"/>
 *         &lt;element name="dea" type="{}string_max50"/>
 *         &lt;element name="specialty_source_value" type="{}string_max50"/>
 *         &lt;element name="x_provider_first" type="{}string_max75"/>
 *         &lt;element name="x_provider_middle" type="{}string_max75"/>
 *         &lt;element name="x_provider_last" type="{}string_max75"/>
 *         &lt;element name="care_site_source_value" type="{}string_max50"/>
 *         &lt;element name="x_organization_source_value" type="{}string_max50"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "provider", propOrder = {
    "providerSourceValue",
    "xDataSourceType",
    "npi",
    "dea",
    "specialtySourceValue",
    "xProviderFirst",
    "xProviderMiddle",
    "xProviderLast",
    "careSiteSourceValue",
    "xOrganizationSourceValue"
})
public class Provider {

    @XmlElement(name = "provider_source_value", required = true)
    protected String providerSourceValue;
    @XmlElement(name = "x_data_source_type", required = true)
    protected String xDataSourceType;
    @XmlElement(required = true, nillable = true)
    protected String npi;
    @XmlElement(required = true, nillable = true)
    protected String dea;
    @XmlElement(name = "specialty_source_value", required = true, nillable = true)
    protected String specialtySourceValue;
    @XmlElement(name = "x_provider_first", required = true, nillable = true)
    protected String xProviderFirst;
    @XmlElement(name = "x_provider_middle", required = true, nillable = true)
    protected String xProviderMiddle;
    @XmlElement(name = "x_provider_last", required = true, nillable = true)
    protected String xProviderLast;
    @XmlElement(name = "care_site_source_value", required = true, nillable = true)
    protected String careSiteSourceValue;
    @XmlElement(name = "x_organization_source_value", required = true)
    protected String xOrganizationSourceValue;

    /**
     * Gets the value of the providerSourceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProviderSourceValue() {
        return providerSourceValue;
    }

    /**
     * Sets the value of the providerSourceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProviderSourceValue(String value) {
        this.providerSourceValue = value;
    }

    /**
     * Gets the value of the xDataSourceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXDataSourceType() {
        return xDataSourceType;
    }

    /**
     * Sets the value of the xDataSourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXDataSourceType(String value) {
        this.xDataSourceType = value;
    }

    /**
     * Gets the value of the npi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNpi() {
        return npi;
    }

    /**
     * Sets the value of the npi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNpi(String value) {
        this.npi = value;
    }

    /**
     * Gets the value of the dea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDea() {
        return dea;
    }

    /**
     * Sets the value of the dea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDea(String value) {
        this.dea = value;
    }

    /**
     * Gets the value of the specialtySourceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialtySourceValue() {
        return specialtySourceValue;
    }

    /**
     * Sets the value of the specialtySourceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialtySourceValue(String value) {
        this.specialtySourceValue = value;
    }

    /**
     * Gets the value of the xProviderFirst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXProviderFirst() {
        return xProviderFirst;
    }

    /**
     * Sets the value of the xProviderFirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXProviderFirst(String value) {
        this.xProviderFirst = value;
    }

    /**
     * Gets the value of the xProviderMiddle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXProviderMiddle() {
        return xProviderMiddle;
    }

    /**
     * Sets the value of the xProviderMiddle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXProviderMiddle(String value) {
        this.xProviderMiddle = value;
    }

    /**
     * Gets the value of the xProviderLast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXProviderLast() {
        return xProviderLast;
    }

    /**
     * Sets the value of the xProviderLast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXProviderLast(String value) {
        this.xProviderLast = value;
    }

    /**
     * Gets the value of the careSiteSourceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCareSiteSourceValue() {
        return careSiteSourceValue;
    }

    /**
     * Sets the value of the careSiteSourceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCareSiteSourceValue(String value) {
        this.careSiteSourceValue = value;
    }

    /**
     * Gets the value of the xOrganizationSourceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXOrganizationSourceValue() {
        return xOrganizationSourceValue;
    }

    /**
     * Sets the value of the xOrganizationSourceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXOrganizationSourceValue(String value) {
        this.xOrganizationSourceValue = value;
    }

}