//
//package in.co.rays.proj4.test;
//
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import in.co.rays.proj4.bean.FacultyBean;
//import in.co.rays.proj4.exception.ApplicationException;
//import in.co.rays.proj4.exception.DuplicateRecordException;
//import in.co.rays.proj4.model.FacultyModel;
//
//public class TestFacultyModel {
//	public static void main(String[] args) throws ParseException, ApplicationException, DuplicateRecordException {
//		testAddFaculty();
//		// testUpdateFaculty();
//		// testDeleteFaculty();
//		// testFindByPk();
//		// testFindByEmail();
//		// testSearch();
////		testList();
//	}
//
////	
//	public static void testAddFaculty() throws ParseException, ApplicationException, DuplicateRecordException {
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		FacultyModel model = new FacultyModel();
//		FacultyBean bean = new FacultyBean();
//		bean.setFirstName("Aniket");
//		bean.setLastName("Rajput");
//		bean.setDob(sdf.parse("12-02-2000"));
//		bean.setGender("Male");
//		bean.setMobileNo("7898037387");
//		bean.setEmail("aniket@gmail.com");
//		bean.setCollegeId(1l);
//		bean.setCourseId(1l);
//		bean.setSubjectId(1l);
//		bean.setCreatedBy("Aniket");
//		bean.setModifiedBy("Aniket");
//		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
//		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
//		Long id = model.addFaculty(bean);
//		System.out.println("Faculty added with id " + id);
//	}
//
//	public static void testUpdateFaculty() throws ParseException, ApplicationException, DuplicateRecordException {
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		FacultyModel model = new FacultyModel();
//		FacultyBean bean = new FacultyBean();
//		bean.setId(1l);
//		bean.setFirstName("Aniket");
//		bean.setLastName("Rajput");
//		bean.setDob(sdf.parse("12-02-2000"));
//		bean.setGender("Male");
//		bean.setMobileNo("7898037387");
//		bean.setEmail("aniket@gmail.com");
//		bean.setCollegeId(1l);
//		bean.setCourseId(1l);
//		bean.setSubjectId(2l);
//		bean.setCreatedBy("Anurag");
//		bean.setModifiedBy("Aniket");
//		bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
//		bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
////		model.update(bean);
//	}
//
////	public static void testDeleteFaculty() throws ApplicationException  {
////		FacultyModel model = new FacultyModel();
////		model.delete(2l);
////	}
//
//	public static void testFindByPk() throws ApplicationException {
//		FacultyModel model = new FacultyModel();
//		FacultyBean bean = model.findByPk(1l);
//		System.out.println(bean.getId());
//		System.out.println(bean.getFirstName());
//		System.out.println(bean.getLastName());
//		System.out.println(bean.getDob());
//		System.out.println(bean.getGender());
//		System.out.println(bean.getMobileNo());
//		System.out.println(bean.getEmail());
//		System.out.println(bean.getCollegeId());
//		System.out.println(bean.getCollegeName());
//		System.out.println(bean.getCourseId());
//		System.out.println(bean.getCourseName());
//		System.out.println(bean.getSubjectId());
//		System.out.println(bean.getSubjectName());
//		System.out.println(bean.getCreatedBy());
//		System.out.println(bean.getModifiedBy());
//		System.out.println(bean.getCreatedDateTime());
//		System.out.println(bean.getModifiedDateTime());
//
//	}
//
//	public static void testFindByEmail() throws ApplicationException {
//		FacultyModel model = new FacultyModel();
//		FacultyBean bean = model.findByPk(1l);
//		System.out.println(bean.getId());
//		System.out.println(bean.getFirstName());
//		System.out.println(bean.getLastName());
//		System.out.println(bean.getDob());
//		System.out.println(bean.getGender());
//		System.out.println(bean.getMobileNo());
//		System.out.println(bean.getEmail());
//		System.out.println(bean.getCollegeId());
//		System.out.println(bean.getCollegeName());
//		System.out.println(bean.getCourseId());
//		System.out.println(bean.getCourseName());
//		System.out.println(bean.getSubjectId());
//		System.out.println(bean.getSubjectName());
//		System.out.println(bean.getCreatedBy());
//		System.out.println(bean.getModifiedBy());
//		System.out.println(bean.getCreatedDateTime());
//		System.out.println(bean.getModifiedDateTime());
//
//	}
//
//	public static void testSearch() throws ApplicationException {
//		FacultyModel model = new FacultyModel();
//		List<FacultyBean> list = model.search(null, 1, 5);
//		Iterator<FacultyBean> it = list.iterator();
//
//		while (it.hasNext()) {
//			FacultyBean bean = it.next();
//			System.out.println(bean.getId());
//			System.out.println(bean.getFirstName());
//			System.out.println(bean.getLastName());
//			System.out.println(bean.getDob());
//			System.out.println(bean.getGender());
//			System.out.println(bean.getMobileNo());
//			System.out.println(bean.getEmail());
//			System.out.println(bean.getCollegeId());
//			System.out.println(bean.getCollegeName());
//			System.out.println(bean.getCourseId());
//			System.out.println(bean.getCourseName());
//			System.out.println(bean.getSubjectId());
//			System.out.println(bean.getSubjectName());
//			System.out.println(bean.getCreatedBy());
//			System.out.println(bean.getModifiedBy());
//			System.out.println(bean.getCreatedDateTime());
//			System.out.println(bean.getModifiedDateTime());
//		}
//	}
//
//	public static void testList() throws ApplicationException {
//		FacultyModel model = new FacultyModel();
//		List<FacultyBean> list = model.search(null, 1, 5);
//		Iterator<FacultyBean> it = list.iterator();
//
//		while (it.hasNext()) {
//			FacultyBean bean = it.next();
//			System.out.println(bean.getId());
//			System.out.println(bean.getFirstName());
//			System.out.println(bean.getLastName());
//			System.out.println(bean.getDob());
//			System.out.println(bean.getGender());
//			System.out.println(bean.getMobileNo());
//			System.out.println(bean.getEmail());
//			System.out.println(bean.getCollegeId());
//			System.out.println(bean.getCollegeName());
//			System.out.println(bean.getCourseId());
//			System.out.println(bean.getCourseName());
//			System.out.println(bean.getSubjectId());
//			System.out.println(bean.getSubjectName());
//			System.out.println(bean.getCreatedBy());
//			System.out.println(bean.getModifiedBy());
//			System.out.println(bean.getCreatedDateTime());
//			System.out.println(bean.getModifiedDateTime());
//		}
//	}
//
//}
