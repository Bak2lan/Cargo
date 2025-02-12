package aist.cargo.repository.jdbcTemplate;

import aist.cargo.dto.user.CargoResponse;
import aist.cargo.enums.PackageType;
import aist.cargo.enums.Role;
import aist.cargo.enums.Size;
import aist.cargo.enums.Status;
import aist.cargo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeliveryJDBCTemplate {
    private final JdbcTemplate jdbcTemplate;

    CargoResponse getAllCargoRs(ResultSet rs, int rowNum) throws SQLException {
        return CargoResponse.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("userId"))
                .userImage(rs.getString("userImage"))
                .fullName(rs.getString("fullName"))
                .transportNumber(rs.getString("transportNumber"))
                .description(rs.getString("description"))
                .fromWhere(rs.getString("fromWhere"))
                .dispatchDate(rs.getDate("dispatchDate") != null ? rs.getDate("dispatchDate").toLocalDate() : null)
                .toWhere(rs.getString("toWhere"))
                .arrivalDate(rs.getDate("arrivalDate") != null ? rs.getDate("arrivalDate").toLocalDate() : null)
                .packageType(PackageType.valueOf(rs.getString("packageType")))
                .size(Size.valueOf(rs.getString("size")))
                .status(Status.valueOf(rs.getString("status")))
                .phoneNumber(rs.getString("phoneNumber"))
                .roleType(Role.valueOf(rs.getString("roleType")))
                .build();
    }

    public List<CargoResponse> getAllCargo(String fromWhere, String toWhere, LocalDate dispatchDate, LocalDate arrivalDate,
                                           PackageType packageType, Size size, Role roleType, String email) {

        StringBuilder sql = new StringBuilder("""
                    SELECT
                        COALESCE(d.id, s.id) AS id,
                        u.id AS userId,
                        u.user_image AS userImage,
                        CONCAT(u.first_name, ' ', u.last_name) AS fullName,
                        d.transport_number AS transportNumber, -- Только для Delivery
                        COALESCE(d.description, s.description) AS description,
                        COALESCE(d.from_where, s.from_where) AS fromWhere,
                        COALESCE(d.dispatch_date, s.dispatch_date) AS dispatchDate,
                        COALESCE(d.to_where, s.to_where) AS toWhere,
                        COALESCE(d.arrival_date, s.arrival_date) AS arrivalDate,
                        COALESCE(d.package_type, s.package_type) AS packageType,
                        COALESCE(d.size, s.size) AS size,
                        u.phone_number AS phoneNumber,
                        u.role AS roleType
                    FROM users u
                    LEFT JOIN sendings s ON s.user_id = u.id AND ? = 'SENDER'
                    LEFT JOIN deliveries d ON d.user_id = u.id AND ? = 'DELIVERY'
                    WHERE
                        u.role = ? -- Фильтр по роли
                        AND u.email != ? -- Исключаем текущего пользователя
                """);
        List<Object> params = new ArrayList<>();
        params.add(roleType.name());
        params.add(roleType.name());
        params.add(roleType.name());
        params.add(email);

        if (fromWhere != null && !fromWhere.isEmpty()) {
            sql.append(" AND (s.from_where ILIKE ? OR d.from_where ILIKE ?)");
            params.add("%" + fromWhere + "%");
            params.add("%" + fromWhere + "%");
        }
        if (toWhere != null && !toWhere.isEmpty()) {
            sql.append(" AND (s.to_where ILIKE ? OR d.to_where ILIKE ?)");
            params.add("%" + toWhere + "%");
            params.add("%" + toWhere + "%");
        }
        if (dispatchDate != null) {
            sql.append(" AND (s.dispatch_date = ? OR d.dispatch_date = ?)");
            params.add(dispatchDate);
            params.add(dispatchDate);
        }
        if (arrivalDate != null) {
            sql.append(" AND (s.arrival_date = ? OR d.arrival_date = ?)");
            params.add(arrivalDate);
            params.add(arrivalDate);
        }
        if (packageType != null) {
            sql.append(" AND (s.package_type = ? OR d.package_type = ?)");
            params.add(packageType.name());
            params.add(packageType.name());
        }
        if (size != null) {
            sql.append(" AND (s.size = ? OR d.size = ?)");
            params.add(size.name());
            params.add(size.name());
        }
        sql.append(" ORDER BY COALESCE(d.dispatch_date, s.dispatch_date) DESC");
        List<CargoResponse> result = jdbcTemplate.query(sql.toString(), this::getAllCargoRs, params.toArray());
        if (result.isEmpty()) {
            throw new NotFoundException("Данные не найдены для указанных параметров.");
        }
        return result;
    }

    public CargoResponse getDeliveryById(Long deliveryById) {
        String sql = """
                SELECT
                    d.id AS id,
                    u.id AS userId,
                    u.user_image AS userImage,
                    CONCAT(u.first_name, ' ', u.last_name) AS fullName,
                    d.transport_number AS transportNumber,
                    d.description AS description,
                    d.from_where AS fromWhere,
                    d.dispatch_date AS dispatchDate,
                    d.to_where AS toWhere,
                    d.arrival_date AS arrivalDate,
                    d.package_type AS packageType,
                    d.size AS size,
                    u.phone_number AS phoneNumber,
                    u.role AS roleType
                FROM
                    users u
                LEFT JOIN deliveries d ON d.user_id = u.id
                WHERE
                    d.status = 'ARCHIVED'
                    AND u.email != ?
                ORDER BY d.arrival_date DESC
                """;
        try {
            return jdbcTemplate.queryForObject(sql, this::getAllCargoRs, deliveryById);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Доставка с ID " + deliveryById + " не найдена!");
        }
    }

    public List<CargoResponse> getAllArchivedDeliveries(String email) {
        String sql = """
                    SELECT
                    d.id AS id,
                    u.id AS userId,
                    u.user_image AS userImage,
                    CONCAT(u.first_name, ' ', u.last_name) AS fullName,
                    d.transport_number AS transportNumber,
                    d.description AS description,
                    d.from_where AS fromWhere,
                    d.dispatch_date AS dispatchDate,
                    d.to_where AS toWhere,
                    d.arrival_date AS arrivalDate,
                    d.package_type AS packageType,
                    d.size AS size,
                    u.phone_number AS phoneNumber,
                    u.role AS roleType
                FROM
                    users u
                LEFT JOIN deliveries d ON d.user_id = u.id
                WHERE
                    d.status = 'ARCHIVED'
                    AND u.email != ?
                ORDER BY d.arrival_date DESC
                """;

        List<CargoResponse> result = jdbcTemplate.query(sql, this::getAllCargoRs, email);
        if (result.isEmpty()) {
            throw new NotFoundException("Архивные доставки не найдены.");
        }
        return result;
    }

    public String getDeliveryStatus(Long id) {
        String sql = "SELECT status FROM deliveries WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public void updateDeliveryStatus(Long id, String status) {
        String sql = "UPDATE deliveries SET status = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, status, id);
        if (rowsAffected == 0) {
            throw new NotFoundException("Доставка с ID " + id + " не найдена!");
        }
    }
}