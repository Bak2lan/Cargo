package aist.cargo.repository.jdbcTemplate;

import aist.cargo.dto.user.SendingResponse;
import aist.cargo.enums.PackageType;
import aist.cargo.enums.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                rs.getString("transportNumber"),
                rs.getString("description"),
                rs.getString("fromWhere"),
                rs.getDate("dispatchDate").toLocalDate(),
                rs.getString("toWhere"),
                rs.getDate("arrivalDate").toLocalDate(),
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
                    s.transport_number AS transportNumber,
                    s.description AS description,
                    s.from_where AS fromWhere,
                    s.dispatch_date AS dispatchDate,
                    s.to_where AS toWhere,
                    s.arrival_date AS arrivalDate,
                    s.package_type AS packageType,
                    s.size AS size,
                    u.phone_number,
                    u.role AS role
                FROM
                    users u
                        LEFT JOIN sending s ON d.user_id = u.id
                WHERE
                    u.role = 'SENDER'
                ORDER BY
                    CASE
                        WHEN u.email = ? THEN 0
                        ELSE 1
                        END,
                    d.dispatch_date DESC;
                """;
        return jdbcTemplate.query(sql, this::getAllSendingRs, email);
    }
}