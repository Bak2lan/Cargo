package aist.cargo.repository.jdbcTemplate;

import aist.cargo.dto.user.SendingResponse;
import aist.cargo.enums.PackageType;
import aist.cargo.enums.Size;
import aist.cargo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SendingJDBCTemplate {
    private final JdbcTemplate jdbcTemplate;

    public SendingResponse getAllSendingRs(ResultSet rs, int rowName) throws SQLException {
        return new SendingResponse(
                rs.getLong("sendingId"),
                rs.getLong("userId"),
                rs.getString("userImage"),
                rs.getString("fullName"),
                rs.getString("description"),
                rs.getString("fromWhere"),
                rs.getDate("dispatchDate") != null ? rs.getDate("dispatchDate").toLocalDate() : null,
                rs.getString("toWhere"),
                rs.getDate("arrivalDate") != null ? rs.getDate("arrivalDate").toLocalDate() : null,
                rs.getString("packageType") != null ? PackageType.valueOf(rs.getString("packageType")) : null,
                rs.getString("size") != null ? Size.valueOf(rs.getString("size")) : null,
                rs.getString("phoneNumber")
        );
    }

    public List<SendingResponse> getAllSending(String email){
        String sql = """
                SELECT
                    s.id AS sendingId,
                    u.id AS userId,
                    u.user_image AS userImage,
                    concat(u.first_name ,' ' ,u.last_name) AS fullName,
                    s.description AS description,
                    s.from_where AS fromWhere,
                    s.dispatch_date AS dispatchDate,
                    s.to_where AS toWhere,
                    s.arrival_date AS arrivalDate,
                    s.package_type AS packageType,
                    s.size AS size,
                    u.phone_number AS phoneNumber,
                    u.role AS role
                FROM
                    users u
                        INNER JOIN sendings s ON s.user_id = u.id
                WHERE
                    d.from_where ILIKE '%' and d.to_where ILIKE '%' and d.dispatch_date =?3 and d.arrival_date =?4 and d.package_type =?5 and d.size =?6 and u.role = 'SENDER'
                ORDER BY
                    CASE
                        WHEN u.email = ? THEN 0
                        ELSE 1
                        END,
                    s.dispatch_date DESC;
                """;
        return jdbcTemplate.query(sql, this::getAllSendingRs, email);
    }

    public SendingResponse getSendingById(Long sendingId){
        String sql = """
                SELECT
                    s.id AS sendingId,
                    u.id AS userId,
                    u.user_image AS userImage,
                    concat(u.first_name ,' ' ,u.last_name) AS fullName,
                    s.description AS description,
                    s.from_where AS fromWhere,
                    s.dispatch_date AS dispatchDate,
                    s.to_where AS toWhere,
                    s.arrival_date AS arrivalDate,
                    s.package_type AS packageType,
                    s.size AS size,
                    u.phone_number AS phoneNumber,
                    u.role AS role
                FROM
                    users u
                        LEFT JOIN sendings s ON s.user_id = u.id
                WHERE
                    u.role = 'SENDER'
                    AND s.id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, this::getAllSendingRs, sendingId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Отправка с ID " + sendingId + " не найдена!");
        }
    }
}