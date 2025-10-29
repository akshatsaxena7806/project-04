/**
 * @Author: akshat saxena
 * @Description: PatientModel handles CRUD operations and search functionality 
 * for Patient entities. It interacts with the database using JDBC.
 * 
 * @Creation Date: 07-Oct-2025
 * @Version: 1.0
 */

package in.co.rays.proj4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.proj4.bean.PatientBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DatabaseException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.util.JDBCDataSource;

/**
 * Model class for managing Patient entity. Provides methods for 
 * add, update, delete, find by PK, find by Name, search, and list operations.
 */
public class PatientModel {

    /**
     * Returns next primary key for Patient table.
     * 
     * @return next primary key
     * @throws DatabaseException
     */
    public static Integer nextPk() throws DatabaseException {
        Connection conn = null;
        int pk = 0;

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(id) FROM st_patient");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                pk = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new DatabaseException("Exception in getting PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return pk + 1;
    }

    /**
     * Adds a new Patient record to the database.
     * 
     * @param bean PatientBean object
     * @throws ApplicationException
     * @throws DuplicateRecordException if patient with same name exists
     */
    public void add(PatientBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;

        // Check for duplicate patient name
        PatientBean existing = findByName(bean.getName());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Patient Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            int pk = nextPk();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO st_patient VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setLong(1, pk);
            pstmt.setString(2, bean.getName());
            pstmt.setDate(3, new java.sql.Date(bean.getDateOfVisit().getTime()));
            pstmt.setString(4, bean.getMobile());
            pstmt.setString(5, bean.getDecease());
            pstmt.setString(6, bean.getCreatedBy());
            pstmt.setString(7, bean.getModifiedBy());
            pstmt.setTimestamp(8, bean.getCreatedDateTime());
            pstmt.setTimestamp(9, bean.getModifiedDateTime());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception: " + ex.getMessage());
            }
            throw new ApplicationException("Exception in adding Patient");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Updates existing Patient record in the database.
     * 
     * @param bean PatientBean object
     * @throws ApplicationException
     * @throws DuplicateRecordException if patient with same name exists
     */
    public void update(PatientBean bean) throws ApplicationException, DuplicateRecordException {
        Connection conn = null;

        PatientBean existing = findByName(bean.getName());
        if (existing != null && existing.getId() != bean.getId()) {
            throw new DuplicateRecordException("Patient Name already exists");
        }

        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE st_patient SET name=?, dateofvisit=?, mobile=?, decease=?, "
                            + "created_by=?, modified_by=?, created_datetime=?, modified_datetime=? WHERE id=?");
            pstmt.setString(1, bean.getName());
            pstmt.setDate(2, new java.sql.Date(bean.getDateOfVisit().getTime()));
            pstmt.setString(3, bean.getMobile());
            pstmt.setString(4, bean.getDecease());
            pstmt.setString(5, bean.getCreatedBy());
            pstmt.setString(6, bean.getModifiedBy());
            pstmt.setTimestamp(7, bean.getCreatedDateTime());
            pstmt.setTimestamp(8, bean.getModifiedDateTime());
            pstmt.setLong(9, bean.getId());

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update rollback exception: " + ex.getMessage());
            }
        //    throw new ApplicationException("Exception in updating Patient");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Deletes a Patient record by ID.
     * 
     * @param id Patient ID
     * @throws ApplicationException
     */
    public void delete(long id) throws ApplicationException {
        Connection conn = null;
        try {
            conn = JDBCDataSource.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM st_patient WHERE id=?");
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Delete rollback exception: " + ex.getMessage());
            }
            throw new ApplicationException("Exception in deleting Patient");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    /**
     * Finds a Patient by primary key.
     * 
     * @param id Patient ID
     * @return PatientBean
     * @throws ApplicationException
     */
    public PatientBean findByPk(long id) throws ApplicationException {
        PatientBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_patient WHERE id=?";

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Patient by PK");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Finds a Patient by name.
     * 
     * @param name Patient name
     * @return PatientBean
     * @throws ApplicationException
     */
    public PatientBean findByName(String name) throws ApplicationException {
        PatientBean bean = null;
        Connection conn = null;
        String sql = "SELECT * FROM st_patient WHERE name=?";

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = mapResultSetToBean(rs);
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in getting Patient by Name");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return bean;
    }

    /**
     * Returns a list of all Patient records.
     * 
     * @return List of PatientBean
     * @throws ApplicationException
     */
    public List<PatientBean> list() throws ApplicationException {
        return search(null, 0, 0);
    }

    /**
     * Searches Patient records with optional pagination.
     * 
     * @param bean     PatientBean for search criteria
     * @param pageNo   Page number
     * @param pageSize Number of records per page
     * @return List of PatientBean
     * @throws ApplicationException
     */
    public List<PatientBean> search(PatientBean bean, int pageNo, int pageSize) throws ApplicationException {
        Connection conn = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM st_patient WHERE 1=1 ");

        if (bean != null) {
            if (bean.getId() > 0) {
                sql.append(" AND id=").append(bean.getId());
            }
            if (bean.getName() != null && !bean.getName().isEmpty()) {
                sql.append(" AND name LIKE '").append(bean.getName()).append("%'");
            }
            if (bean.getDateOfVisit() != null) {
                sql.append(" AND date_of_visit LIKE '")
                        .append(new java.sql.Date(bean.getDateOfVisit().getTime())).append("%'");
            }
            if (bean.getDecease() != null && !bean.getDecease().isEmpty()) {
                sql.append(" AND decease LIKE '").append(bean.getDecease()).append("%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT ").append(pageNo).append(",").append(pageSize);
        }

        List<PatientBean> list = new ArrayList<>();

        try {
            conn = JDBCDataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToBean(rs));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in searching Patient");
        } finally {
            JDBCDataSource.closeConnection(conn);
        }

        return list;
    }

    /**
     * Maps ResultSet row to PatientBean.
     * 
     * @param rs ResultSet
     * @return PatientBean
     * @throws Exception
     */
    private PatientBean mapResultSetToBean(ResultSet rs) throws Exception {
        PatientBean bean = new PatientBean();
        bean.setId(rs.getLong(1));
        bean.setName(rs.getString(2));
        bean.setDateOfVisit(rs.getDate(3));
        bean.setMobile(rs.getString(4));
        bean.setDecease(rs.getString(5));
        bean.setCreatedBy(rs.getString(6));
        bean.setModifiedBy(rs.getString(7));
        bean.setCreatedDateTime(rs.getTimestamp(8));
        bean.setModifiedDateTime(rs.getTimestamp(9));
        return bean;
    }
}