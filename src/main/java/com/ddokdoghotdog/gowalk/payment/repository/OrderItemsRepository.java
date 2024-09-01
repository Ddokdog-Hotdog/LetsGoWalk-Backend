package com.ddokdoghotdog.gowalk.payment.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.OrderItem;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Long>{

	// 멤버별 주문 상품 조회
	@Query(value = "SELECT\n"
			+ "    o.memberid AS memberId,\n"
			+ "    oi.id AS orderItemId,\n"
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
			+ "    p.price * oi.quantity AS totalPrice,\n"
			+ "    CASE\n"
			+ "        WHEN oi.status = 1 THEN 'N' -- 환불이 안되었으므로 NO \n"
			+ "        WHEN oi.status = 0 THEN 'Y' -- 환불이 되었으므로 YES \n"
			+ "        ELSE 'Unknown'  -- 예외적인 경우를 처리하기 위해 추가\n"
			+ "    END AS isRefunded\n"
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
			+ "    o.memberid = :memberId  -- 특정 멤버 ID로 필터링\n"
			+ "ORDER BY\n"
			+ "    oi.createdat DESC,  -- 주문 날짜 기준으로 내림차순 정렬\n"
			+ "    p.id  -- 상품 ID 기준으로 정렬\n"
			+ "OFFSET 8*(:page - 1) ROWS FETCH NEXT 8 ROWS ONLY", nativeQuery = true)
	List<Map<String, Object>> findOrderItemsListByMemberId(@Param("page") Integer page, @Param("memberId") Long memberId);
	
}
