package com.luna;

import java.sql.*;

public class OceanBaseDemo {
    // 连接信息 - 请根据你的实际情况修改
    // 注意：如果你在本机运行Docker，host就是127.0.0.1
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:2881/test";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // 1. 注册驱动 (对于MySQL Connector/J 8.x，这步可以省略)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立连接
            conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("连接OceanBase数据库成功！");
            System.out.println("AutoCommit状态：" + conn.getAutoCommit());

            // 3. 创建Statement对象并执行SQL
            stmt = conn.createStatement();

            // 创建表
            String createTableSQL = "CREATE TABLE IF NOT EXISTS demo_table (" +
                    "id INT PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(createTableSQL);
            System.out.println("表创建/确认完成。");

            // 插入数据
            String insertSQL = "INSERT INTO demo_table (id, name) VALUES (1, 'OceanBase测试')";
            int rows = stmt.executeUpdate(insertSQL);
            System.out.println("插入了 " + rows + " 行数据。");

            // 查询数据
            String querySQL = "SELECT * FROM demo_table";
            ResultSet rs = stmt.executeQuery(querySQL);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Timestamp time = rs.getTimestamp("create_time");
                System.out.printf("查询结果: id=%d, name=%s, time=%s%n", id, name, time);
            }
            rs.close();

            // （可选）清理：删除表
            // stmt.executeUpdate("DROP TABLE demo_table");

        } catch (ClassNotFoundException e) {
            System.err.println("未找到JDBC驱动类！请确认驱动已引入。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库操作出现异常！");
            System.err.println("错误信息：" + e.getMessage());
            System.err.println("SQL状态码：" + e.getSQLState());
            System.err.println("厂商错误码：" + e.getErrorCode());
            e.printStackTrace();
        } finally {
            // 4. 关闭资源
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                System.out.println("数据库连接已关闭。");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}