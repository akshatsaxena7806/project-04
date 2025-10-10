/**
 * @Author: akshat saxena
 * @Description: PatientBean class represents a patient entity in the system.
 * It stores details such as patient name, date of visit, mobile number, and disease.
 * This class extends BaseBean to inherit common attributes like id, createdBy,
 * modifiedBy, and timestamps.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.bean;

import java.util.Date;

/**
 * PatientBean is a JavaBean that encapsulates data for a patient.
 */
public class PatientBean extends BaseBean {

    /** The name of the patient */
    private String name;

    /** The date when the patient visited */
    private Date dateOfVisit;

    /** The mobile number of the patient */
    private String mobile;

    /** The disease or condition of the patient */
    private String decease;

    /**
     * Gets the name of the patient.
     * 
     * @return patient name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the patient.
     * 
     * @param name the patient's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the date of visit of the patient.
     * 
     * @return date of visit
     */
    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    /**
     * Sets the date of visit of the patient.
     * 
     * @param dateOfVisit the date of visit to set
     */
    public void setDateOfVisit(Date dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    /**
     * Gets the mobile number of the patient.
     * 
     * @return mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile number of the patient.
     * 
     * @param mobile the mobile number to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the disease or condition of the patient.
     * 
     * @return disease or condition
     */
    public String getDecease() {
        return decease;
    }

    /**
     * Sets the disease or condition of the patient.
     * 
     * @param decease the disease to set
     */
    public void setDecease(String decease) {
        this.decease = decease;
    }

    /**
     * Returns the key for the dropdown list (used in UI selections).
     * 
     * @return disease as key
     */
    @Override
    public String getKey() {
        return decease;
    }

    /**
     * Returns the value for the dropdown list (used in UI selections).
     * 
     * @return disease as value
     */
    @Override
    public String getValue() {
        return decease;
    }

    /**
     * Returns a string representation of the PatientBean.
     * 
     * @return string representation of the patient
     */
    @Override
    public String toString() {
        return "PatientBean [name=" + name + ", dateOfVisit=" + dateOfVisit + ", mobile=" + mobile + ", decease="
                + decease + "]";
    }

}