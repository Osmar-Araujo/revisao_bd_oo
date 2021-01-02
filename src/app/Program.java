package app;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import db.DB;
import entity.Order;
import entity.OrderStatus;
import entity.Produto;

public class Program {

	public static void main(String[] args) throws SQLException {
		
		Connection conn = DB.getConnection();
	
		Statement st = conn.createStatement();
			
		ResultSet rs = st.executeQuery("SELECT * FROM tb_order "
				+ "INNER JOIN tb_order_product ON tb_order.id = tb_order_product.order_id "
				+ "INNER JOIN tb_product ON tb_product.id = tb_order_product.product_id");
		
		
		
		Map<Long, Order> map = new HashMap<>();
		Map<Long, Produto> prods = new HashMap<>();
		
		while (rs.next()) {
			
			Long order_Id = rs.getLong("order_id");
			if (map.get(order_Id) == null) {
				Order order = instantiateOrder(rs);
				map.put(order_Id, order);
			}
			
			Long productId = rs.getLong("product_id");
			if (prods.get(productId) == null) {
			    Produto p = instantiateProduct(rs);
			    prods.put(productId, p);
			
			}
			
			map.get(order_Id).getProducts().add((prods.get(productId)));
		}
		
		for(Long orderId : map.keySet()) {
			System.out.println(map.get(orderId));
			
			for (Produto p : map.get(orderId).getProducts()) {
				System.out.println(p);
			}
			
			System.out.println();
		}
		 
	}
	
	private static Order instantiateOrder(ResultSet rs) throws SQLException {
		
		Order order = new Order();
		order.setId(rs.getLong("order_id"));
		order.setLatitude(rs.getDouble("latitude"));
		order.setLongitude(rs.getDouble("longitude"));
		order.setLongitude(rs.getDouble("longitude"));
		order.setMoment(rs.getTimestamp("moment").toInstant());
		order.setStatus(OrderStatus.values()[rs.getInt("status")]);
				return order;
	}

	private static Produto instantiateProduct(ResultSet rs) throws SQLException{
		Produto p = new Produto();
		p.setId(rs.getLong("product_id"));
		p.setDescription(rs.getString("description"));
		p.setName(rs.getString("name"));
		p.setImageUri(rs.getString("image_uri"));
		p.setPrice(rs.getDouble("price"));
		return p;
	}
}
