package in.co.rays.proj4.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import in.co.rays.proj4.bean.FacultyBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.FacultyModel;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.model.UserModel;
import in.co.rays.proj4.util.JDBCDataSource;

public class TestRoleModel {
	public static RoleModel model = new RoleModel();

	public static void main(String[] args) throws ApplicationException, DuplicateRecordException, Exception {

//		testAdd();
//	RoleBean bean =  findByPk(1l);
//	System.out.println(bean);
//		testFindByPk();
		FacultyModel model = new FacultyModel();
//		FacultyBean bean =  model.findByEmail("akkki@gmail.com");
		// System.out.println(model.findByPk(1));

	}

	public static void testAdd() throws Exception, ApplicationException, DuplicateRecordException {
		try {

			RoleBean bean = new RoleBean();
			bean.setName("student");
			bean.setDescription("student");
			bean.setCreatedBy("admin");
			bean.setModifiedBy("admin");
			bean.setCreatedDateTime(new Timestamp(new Date().getTime()));
			bean.setModifiedDateTime(new Timestamp(new Date().getTime()));
			long pk = model.add(bean);
			RoleBean addedbean = model.findByPk(pk);
			if (addedbean == null) {
				System.out.println("Test add fail");
			}
		} catch (ApplicationException | DuplicateRecordException e) {
			e.printStackTrace();
		}
	}

	public static void testfindByPk() {
		try {
			RoleBean bean = model.findByPk(1L);
			if (bean == null) {
				System.out.println("Test Find By PK fail");
			}
			System.out.println(bean.getId());
			System.out.println(bean.getName());
			System.out.println(bean.getDescription());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static RoleBean findByPk(Long pk) throws ApplicationException {

		RoleBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ptsmt = conn.prepareStatement("select * from st_role where id  = ?");
			ptsmt.setLong(1, pk);
			ResultSet rs = ptsmt.executeQuery();
			while (rs.next()) {
				bean = new RoleBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDateTime(rs.getTimestamp(6));
				bean.setModifiedDateTime(rs.getTimestamp(7));
			}
			rs.close();
			ptsmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in find by pk");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;

	}

	public static void testFindByPk() throws ApplicationException {

		RoleBean bean = new RoleBean();

		RoleModel model = new RoleModel();

		RoleBean pk = model.findByPk(1l);

		System.out.println(pk.getId());
		System.out.println(pk.getName());

	}

}
