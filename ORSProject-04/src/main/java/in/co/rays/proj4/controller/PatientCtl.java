/**
 * @Author: Ajay Pratap Kerketta
 * @Description: PatientCtl is a Servlet controller responsible for handling
 * operations related to Patient management such as adding, updating, and validating
 * patient data. It extends BaseCtl to inherit common controller functionalities
 * like validation and bean population.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.PatientModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * Servlet implementation class PatientCtl.
 * Handles CRUD operations for Patient entities including form validation,
 * bean population, and forwarding/redirecting to views.
 */
@WebServlet(name = "PatientCtl", urlPatterns = { "/ctl/PatientCtl" })
public class PatientCtl extends BaseCtl {
	
	Logger log = Logger.getLogger(PatientCtl.class);

    /**
     * Loads pre-populated data required for the Patient form (e.g., list of deceases)
     * into the request scope.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
        HashMap<String, String> deceaseMap = new HashMap<>();
        deceaseMap.put("Diabetes", "Diabetes");
        deceaseMap.put("Hypertension", "Hypertension");
        deceaseMap.put("Asthma", "Asthma");
        deceaseMap.put("Tuberculosis", "Tuberculosis");
        deceaseMap.put("Malaria", "Malaria");
        deceaseMap.put("Alzheimer's", "Alzheimer's");
        deceaseMap.put("Parkinson's", "Parkinson's");
        deceaseMap.put("Hepatitis", "Hepatitis");
        deceaseMap.put("Cholera", "Cholera");
        deceaseMap.put("Ebola", "Ebola");

        request.setAttribute("deceaseMap", deceaseMap);
    }

    /**
     * Validates Patient form data.
     * 
     * @param request HttpServletRequest containing form data
     * @return true if data is valid, false otherwise
     */
    @Override
    protected boolean validate(HttpServletRequest request) {
    	
    	log.info("PatientCtl validate Method Started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("name"))) {
            request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
            pass = false;
        } else if (!DataValidator.isName(request.getParameter("name"))) {
            request.setAttribute("name", "Invalid Name");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.require", "Date of Visit"));
            pass = false;
        } else if (!DataValidator.isDate(request.getParameter("dateOfVisit"))) {
            request.setAttribute("dateOfVisit", PropertyReader.getValue("error.date", "Date of Visit"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("mobile"))) {
            request.setAttribute("mobile", PropertyReader.getValue("error.require", "MobileNo"));
            pass = false;
        } else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Mobile No must have 10 digits");
            pass = false;
        } else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
            request.setAttribute("mobile", "Invalid Mobile No");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("decease"))) {
            request.setAttribute("decease", PropertyReader.getValue("error.require", "Decease"));
            pass = false;
        }
        
        log.info("PatientCtl validate Method Ended");
        return pass;
    }

    /**
     * Populates a PatientBean from request parameters.
     * 
     * @param request HttpServletRequest containing form data
     * @return populated PatientBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
    	log.info("PatientCtl populateBean Method Started");
        PatientBean bean = new PatientBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDateOfVisit(DataUtility.getDate(request.getParameter("dateOfVisit")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setDecease(DataUtility.getString(request.getParameter("decease")));

        populateDTO(bean, request);
        
        log.info("PatientCtl populateBean Method Ended");
        return bean;
    }

    /**
	 * Handles HTTP GET request. Fetches Patient data for editing if id is provided.
	 * 
	 * @param req  HttpServletRequest
	 * @param resp HttpServletResponse
	 *  ServletException
	 *  IOException
	 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	log.info("PatientCtl doGet Method Started");
    	
        long id = DataUtility.getLong(req.getParameter("id"));
        PatientModel model = new PatientModel();

        if (id > 0) {
            try {
                PatientBean bean = model.findByPk(id);
                ServletUtility.setBean(bean, req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        }
        log.info("PatientCtl doGet Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Handles HTTP POST request. Processes save, update, reset, and cancel operations
     * for Patient entity.
     * 
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     *  ServletException
     *  IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	log.info("PatientCtl doPost Method Started");
    	
        String op = DataUtility.getString(req.getParameter("operation"));
        long id = DataUtility.getLong(req.getParameter("id"));
        PatientModel model = new PatientModel();

        if (OP_SAVE.equalsIgnoreCase(op)) {
            PatientBean bean = (PatientBean) populateBean(req);
            try {
                model.add(bean);
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient added successfully", req);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient already exists", req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_CTL, req, resp);
            return;
        } else if (OP_UPDATE.equalsIgnoreCase(op)) {
            PatientBean bean = (PatientBean) populateBean(req);
            try {
                if (id > 0) {
                    model.update(bean);
                }
                ServletUtility.setBean(bean, req);
                ServletUtility.setSuccessMessage("Patient updated successfully", req);
            } catch (DuplicateRecordException e) {
                ServletUtility.setBean(bean, req);
                ServletUtility.setErrorMessage("Patient already exists", req);
            } catch (ApplicationException e) {
                e.printStackTrace();
                ServletUtility.handleException(e, req, resp);
                return;
            }
        } else if (OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, req, resp);
            return;
        }
        log.info("PatientCtl doPost Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the view page for Patient form.
     * 
     * @return JSP page path as String
     */
    @Override
    protected String getView() {
        return ORSView.PATIENT_VIEW;
    }
}