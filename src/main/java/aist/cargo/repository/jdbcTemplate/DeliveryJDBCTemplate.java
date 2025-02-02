package aist.cargo.repository.jdbcTemplate;

import aist.cargo.dto.user.DeliveryResponse;
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
public class DeliveryJDBCTemplate {
    private final JdbcTemplate jdbcTemplate;

    DeliveryResponse getAllDeliveriesRs(ResultSet rs, int rowName) throws SQLException {
        return new DeliveryResponse(
                rs.getLong("deliveryId"),
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

    public List<DeliveryResponse> getAllDeliveries(String email){
        String sql = """
                SELECT
                    d.id AS deliveryId,
                    u.id AS userId,
                    u.user_image AS userImage,
                    concat(u.first_name, ' ', u.last_name) AS fullName,
                    d.transport_number AS transportNumber,
                    d.description AS description,
                    d.from_where AS fromWhere,
                    COALESCE(d.dispatch_date, CURRENT_DATE) AS dispatchDate,  
                    d.to_where AS toWhere,
                    COALESCE(d.arrival_date, CURRENT_DATE) AS arrivalDate, 
                    d.package_type AS packageType,
                    d.size AS size,
                    u.phone_number AS phoneNumber,
                    u.role AS role
                FROM
                    users u
                    LEFT JOIN deliveries d ON d.user_id = u.id
                WHERE
                    u.role = 'DELIVERY'
                ORDER BY
                    CASE
                        WHEN u.email = ? THEN 0
                        ELSE 1
                    END,
                    d.dispatch_date DESC;
                """;
         return jdbcTemplate.query(sql, this::getAllDeliveriesRs, email);
    }
}