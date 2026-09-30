package vn.edu.rikkei.session17.ex01;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/rikkei_erp_db";
    private static final String USER = "postgres";
    // Thay bằng mật khẩu PostgreSQL trên máy của bạn trước khi chạy.
    private static final String PASSWORD = "THAY_BANG_MAT_KHAU_CUA_BAN";

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  KIỂM TRA KẾT NỐI POSTGRESQL - RIKKEI ERP DB");
        System.out.println("==================================================");

        // Giới hạn thời gian chờ đăng nhập (giây).
        DriverManager.setLoginTimeout(5);

        // Connection tự động được đóng khi thoát khỏi khối try.
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseName = metaData.getDatabaseProductName();
            String databaseVersion = metaData.getDatabaseProductVersion();
            String driverName = metaData.getDriverName();
            String driverVersion = metaData.getDriverVersion();

            System.out.println("[THÀNH CÔNG] Kết nối CSDL PostgreSQL thành công!");
            System.out.println("--------------------------------------------------");
            System.out.println("Tên hệ quản trị CSDL : " + databaseName);
            System.out.println("Phiên bản CSDL       : " + databaseVersion);
            System.out.println("Tên JDBC Driver      : " + driverName);
            System.out.println("Phiên bản Driver     : " + driverVersion);
            System.out.println("--------------------------------------------------");
        } catch (SQLException e) {
            String sqlState = e.getSQLState();

            System.out.println("[THẤT BẠI] Không hoàn tất kiểm tra kết nối CSDL.");
            if (sqlState == null) {
                System.out.println("SQLState            : Không được cung cấp");
            } else {
                System.out.println("SQLState            : " + sqlState);
            }
            System.out.println("Mã lỗi              : " + e.getErrorCode());
            System.out.println("Chi tiết            : " + e.getMessage());

            if ("28P01".equals(sqlState)) {
                System.out.println("Cách khắc phục: Kiểm tra USER và PASSWORD; xác thực mật khẩu thất bại.");
            } else if ("28000".equals(sqlState)) {
                System.out.println("Cách khắc phục: Kiểm tra tài khoản, quyền đăng nhập và cấu hình pg_hba.conf.");
            } else if ("3D000".equals(sqlState)) {
                System.out.println("Cách khắc phục: Tạo database rikkei_erp_db hoặc sửa tên database trong DB_URL.");
            } else if (sqlState != null && sqlState.startsWith("08")) {
                System.out.println("Cách khắc phục: Kiểm tra dịch vụ PostgreSQL, host, port, mạng và tường lửa.");
                System.out.println("Nếu có 'No suitable driver', kiểm tra JDBC Driver trong dependency/classpath và DB_URL.");
            } else {
                System.out.println("Cách khắc phục: Đối chiếu SQLState và thông báo trên với log PostgreSQL.");
            }
        } finally {
            System.out.println("==================================================");
            System.out.println("                KẾT THÚC KIỂM TRA");
            System.out.println("==================================================");
        }
    }
}
