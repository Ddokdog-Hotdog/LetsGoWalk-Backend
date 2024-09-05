package com.ddokdoghotdog.gowalk.payment.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long>{

	Optional<Order> findByKakaoOrderId(String kakaoOrderId);
	List<Order> findByMemberId(Long memberId);
	
	@Query(value = "SELECT o.address FROM orders o WHERE o.memberid = :memberId "
			+ "ORDER BY o.orderdat DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<String> findLatestAddressByMember(@Param("memberId") Long memberId);
	
	// 한 달간 상품별 매출 (매출 내림차순 정렬)
	@Query(value = "SELECT\n"
	        + "    p.id AS productId,\n"
	        + "    p.name AS productName,\n"
	        + "    p.price AS price,\n"
	        + "    p.thumbnailimage AS thumbnailImage,\n"
	        + "    p.stock_quantity AS stockQuantity,\n"
	        + "    c.name AS categoryName,\n"
	        + "    v.name AS vendorName,\n"
	        + "    COALESCE(SUM(oi.quantity * p.price), 0) AS totalSales\n"
	        + "FROM\n"
	        + "    products p\n"
	        + "LEFT JOIN\n"
	        + "    order_items oi ON oi.productid = p.id\n"
	        + "    AND oi.createdat >= TRUNC(SYSDATE, 'MM')  -- 현재 달의 첫 날\n"
	        + "    AND oi.createdat < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)  -- 다음 달의 첫 날\n"
	        + "    AND oi.status = 1  -- status가 1인 항목만 포함\n"
	        + "LEFT JOIN\n"
	        + "    categories c ON p.categoryid = c.id\n"
	        + "LEFT JOIN\n"
	        + "    vendors v ON p.vendorid = v.id\n"
	        + "WHERE\n"
	        + "    p.visible = 1\n"
	        + "GROUP BY\n"
	        + "    p.id, p.name, p.price, p.thumbnailimage, p.stock_quantity, p.visible, c.name, v.name\n"
	        + "ORDER BY\n"
	        + "    totalSales DESC", nativeQuery = true)
	List<Map<String, Object>> findMonthStatisticsInfo();
	
	// 주문건별 멤버 정보 및 상품 정보
	@Query(value = "SELECT\n"
			+ "    o.memberid AS memberId,\n"
			+ "    m.nickname AS memberName,\n"
			+ "    m.email AS memberEmail,\n"
			+ "    p.id AS productId,\n"
			+ "    p.name AS productName,\n"
			+ "    p.price AS productPrice,\n"
			+ "    p.thumbnailimage AS productThumbnail,\n"
			+ "    c.name AS categoryName,\n"
			+ "    v.name AS vendorName,\n"
			+ "    oi.quantity,\n"
			+ "    oi.createdat AS orderDate,\n"
			+ "    oi.status, \n"
			+ "    p.price * oi.quantity AS totalPrice\n"
			+ "FROM\n"
			+ "    order_items oi\n"
			+ "JOIN\n"
			+ "    orders o ON oi.orderid = o.id\n"
			+ "JOIN\n"
			+ "    products p ON oi.productid = p.id\n"
			+ "LEFT JOIN\n"
			+ "    categories c ON p.categoryid = c.id\n"
			+ "LEFT JOIN\n"
			+ "    vendors v ON p.vendorid = v.id\n"
			+ "LEFT JOIN\n"
			+ "    members m ON o.memberid = m.id\n"
			+ "WHERE\n"
			+ "    p.visible = 1  -- 상품이 활성화된 것만 포함\n"
			+ "ORDER BY\n"
			+ "    oi.createdat DESC,  -- 주문 날짜 기준으로 내림차순 정렬\n"
			+ "    o.memberid,         -- 멤버 ID 기준으로 정렬\n"
			+ "    p.id  -- 상품 ID 기준으로 정렬\n"
			+ "OFFSET 8*(:page - 1) ROWS FETCH NEXT 8 ROWS ONLY", nativeQuery = true)
	List<Map<String, Object>> findAllOrderList(Integer page);
	
	

}
