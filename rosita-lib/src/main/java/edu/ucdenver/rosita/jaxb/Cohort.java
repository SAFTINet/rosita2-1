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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for cohort complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cohort">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cohort_source_identifier" type="{}string_max50"/>
 *         &lt;element name="cohort_source_value" type="{}string_max50"/>
 *         &lt;element name="cohort_start_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="cohort_end_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="subject_source_identifier" type="{}string_max50"/>
 *         &lt;element name="stop_reason" type="{}string_max20"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cohort", propOrder = {
    "cohortSourceIdentifier",
    "cohortSourceValue",
    "cohortStartDate",
    "cohortEndDate",
    "subjectSourceIdentifier",
    "stopReason"
})
public class Cohort {

    @XmlElement(name = "cohort_source_identifier", required = true)
    protected String cohortSourceIdentifier;
    @XmlElement(name = "cohort_source_value", required = true)
    protected String cohortSourceValue;
    @XmlElement(name = "cohort_start_date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cohortStartDate;
    @XmlElement(name = "cohort_end_date", required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cohortEndDate;
    @XmlElement(name = "subject_source_identifier", required = true)
    protected String subjectSourceIdentifier;
    @XmlElement(name = "stop_reason", required = true, nillable = true)
    protected String stopReason;

    /**
     * Gets the value of the cohortSourceIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCohortSourceIdentifier() {
        return cohortSourceIdentifier;
    }

    /**
     * Sets the value of the cohortSourceIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCohortSourceIdentifier(String value) {
        this.cohortSourceIdentifier = value;
    }

    /**
     * Gets the value of the cohortSourceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCohortSourceValue() {
        return cohortSourceValue;
    }

    /**
     * Sets the value of the cohortSourceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCohortSourceValue(String value) {
        this.cohortSourceValue = value;
    }

    /**
     * Gets the value of the cohortStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCohortStartDate() {
        return cohortStartDate;
    }

    /**
     * Sets the value of the cohortStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCohortStartDate(XMLGregorianCalendar value) {
        this.cohortStartDate = value;
    }

    /**
     * Gets the value of the cohortEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCohortEndDate() {
        return cohortEndDate;
    }

    /**
     * Sets the value of the cohortEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCohortEndDate(XMLGregorianCalendar value) {
        this.cohortEndDate = value;
    }

    /**
     * Gets the value of the subjectSourceIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectSourceIdentifier() {
        return subjectSourceIdentifier;
    }

    /**
     * Sets the value of the subjectSourceIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectSourceIdentifier(String value) {
        this.subjectSourceIdentifier = value;
    }

    /**
     * Gets the value of the stopReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStopReason() {
        return stopReason;
    }

    /**
     * Sets the value of the stopReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStopReason(String value) {
        this.stopReason = value;
    }

}
