package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

public class RoleModel {

	public static void main(String[] args) throws Exception {

//		System.out.println(getNextPk());
		add(null);
	}

	public static Integer getNextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement ptsmt = conn.prepareStatement("select max(id) from st_role");

			ResultSet rs = ptsmt.executeQuery();

			while (rs.next()) {

				rs.getInt(1);

			}
			rs.close();
			ptsmt.close();

		} catch (Exception e) {
			throw new DatabaseException("data base not found ...... !!!!");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public static long add(RoleBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		int pk = 0;

		try {
			pk = getNextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ptsmt = conn.prepareStatement("insert into st_role values(?,?,?,?,?,?,?)");
			ptsmt.setInt(1, pk);
			ptsmt.setString(2, bean.getName());
			ptsmt.setString(3, bean.getDescription());
			ptsmt.setString(4, bean.getCreatedBy());
			ptsmt.setString(5, bean.getModifiedBy());
			ptsmt.setTimestamp(6, bean.getCreatedDateTime());
			ptsmt.setTimestamp(7, bean.getModifiedDateTime());
			ptsmt.executeQuery();
			conn.commit();
			ptsmt.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception : add rollback Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

	public void update(RoleBean bean) throws ApplicationException, DatabaseException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ptsmt = conn.prepareStatement(
					"update st_role set name = ?, description = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			ptsmt.setString(1, bean.getName());
			ptsmt.setString(2, bean.getDescription());
			ptsmt.setString(3, bean.getCreatedBy());
			ptsmt.setString(4, bean.getModifiedBy());
			ptsmt.setTimestamp(5, bean.getCreatedDateTime());
			ptsmt.setTimestamp(6, bean.getModifiedDateTime());
			ptsmt.setLong(7, bean.getId());
			ptsmt.executeUpdate();
			conn.close();
			ptsmt.close();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception : delete rollback Exception..!" + e2.getMessage());
			}
			throw new ApplicationException("Exception in updateing role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public void delete(RoleBean bean) throws ApplicationException {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ptsmt = conn.prepareStatement("delete from st_role where id = ? ");

			ptsmt.setLong(1, bean.getId());
			ptsmt.executeUpdate();
			conn.commit();
			ptsmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception :  delete rollback Exception " + e2.getMessage());
			}
			throw new ApplicationException("Exception in delete roll");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	public RoleBean findByPk(Long pk) throws ApplicationException {

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

	public List<RoleBean> list() throws ApplicationException {
		return search(null, 0, 0);
	}

	public RoleBean findByName(String name) throws ApplicationException {

		RoleBean bean = null;
		Connection conn = null;

		try {
			StringBuffer sql = new StringBuffer("select * from st_role where name = ?");
			conn = JDBCDataSource.getConnection();
			PreparedStatement ptsmt = conn.prepareStatement(sql.toString());
			ptsmt.setString(1, name);
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

			throw new ApplicationException("Exception : Exception in getting User by Role");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return bean;

	}

	public List<RoleBean> search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_role where 1=1");

		if (bean != null) {

			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append("and name like " + bean.getName() + "%'");
			}

			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" and Description like " + bean.getDescription() + "%'");

			}

		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		Connection conn = null;
		ArrayList<RoleBean> list = new ArrayList<RoleBean>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement ptsmt = conn.prepareStatement(sql.toString());
			ResultSet rs = ptsmt.executeQuery();
			while (rs.next()) {
				bean = new RoleBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDateTime(rs.getTimestamp(6));
				bean.setCreatedDateTime(rs.getTimestamp(7));
				list.add(bean);
			}
			rs.close();
			ptsmt.close();

		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search Role.......!!");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return list;

	}
}