package utils;

import java.sql.*;

public class DbHelper {
    private Connection con;

    public DbHelper() throws SQLException {
        con = DriverManager.getConnection("jdbc:h2:mem:oms;DB_CLOSE_DELAY=-1", "sa", "");
        con.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS orders(id INT, user_id INT, product_id INT, qty INT, status VARCHAR(20))");
    }

    // in real project the app does this insert, here we simulate it
    public void saveOrder(int id, int userId, int productId, int qty, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO orders VALUES(?,?,?,?,?)");
        ps.setInt(1, id); ps.setInt(2, userId); ps.setInt(3, productId);
        ps.setInt(4, qty); ps.setString(5, status);
        ps.executeUpdate();
    }

    public void updateStatus(int id, String status) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE orders SET status=? WHERE id=?");
        ps.setString(1, status); ps.setInt(2, id);
        ps.executeUpdate();
    }

    public ResultSet getOrder(int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE id=?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }
}