/**
 * @Author: Ajay Pratap Kerketta
 * @Description: PatientListCtl is a Servlet controller responsible for handling
 * operations related to listing, searching, deleting, and paginating Patient entities.
 * It extends BaseCtl to leverage common controller functionalities such as bean 
 * population and validation.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.PatientModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

/**
 * Servlet implementation class PatientListCtl. Handles displaying, searching,
 * deleting, and paginating Patient records.
 */
@WebServlet(name = "PatientListCtl", urlPatterns = { "/ctl/PatientListCtl" })
public class PatientListCtl extends BaseCtl {

	Logger log = Logger.getLogger(PatientListCtl.class);

	/**
	 * Loads pre-populated data for Patient list page (e.g., unique deceases) into
	 * the request scope.
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
	 * Populates a PatientBean from request parameters for searching/filtering.
	 * 
	 * @param request HttpServletRequest containing search/filter form data
	 * @return populated PatientBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("PatientListCtl populateBean Method Started");
		PatientBean bean = new PatientBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDateOfVisit(DataUtility.getDate(request.getParameter("dateOfVisit")));
		bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
		bean.setDecease(DataUtility.getString(request.getParameter("decease")));
		log.info("PatientListCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles HTTP GET request. Displays the list of patients with pagination.
	 * 
	 * @param req  HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("PatientListCtl doGet Method Started");
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		PatientBean bean = (PatientBean) populateBean(req);
		PatientModel model = new PatientModel();

		try {
			List<PatientBean> list = model.search(bean, pageNo, pageSize);
			List<PatientBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found", req);
			}

			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.setBean(bean, req);
			req.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), req, resp);

		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
		}
		log.info("PatientListCtl doGet Method Ended");
	}

	/**
	 * Handles HTTP POST request. Supports operations like search, pagination,
	 * delete, reset, and back.
	 * 
	 * @param req  HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("PatientListCtl doPost Method Started");
		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(PropertyReader.getValue("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		PatientBean bean = (PatientBean) populateBean(req);
		PatientModel model = new PatientModel();

		String op = DataUtility.getString(req.getParameter("operation"));
		String[] ids = req.getParameterValues("ids");

		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.PATIENT_CTL, req, resp);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					PatientBean deleteBean = new PatientBean();
					for (String id : ids) {
						deleteBean.setId(DataUtility.getInt(id));
						model.delete(deleteBean.getId());
						ServletUtility.setSuccessMessage("Patient deleted successfully", req);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least 1 id.", req);
				}

			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.PATIENT_LIST_CTL, req, resp);
				return;
			}

			List list = model.search(bean, pageNo, pageSize);
			List next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", req);
			}

			ServletUtility.setBean(bean, req);
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			req.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
		log.info("PatientListCtl doPost Method Ended");
		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns the view page for Patient list.
	 * 
	 * @return JSP page path as String
	 */
	@Override
	protected String getView() {
		return ORSView.PATIENT_LIST_VIEW;
	}

}