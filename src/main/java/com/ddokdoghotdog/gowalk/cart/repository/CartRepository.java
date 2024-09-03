package com.ddokdoghotdog.gowalk.cart.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.CartItem;
import com.ddokdoghotdog.gowalk.entity.Product;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long>{

	@Query(value = "SELECT\r\n"
			+ "    p.id,\r\n"
			+ "    p.name,\r\n"
			+ "    p.price,\r\n"
			+ "    p.thumbnailimage,\r\n"
			+ "    ci.quantity,\r\n"
			+ "    c.name AS category,\r\n"
			+ "    v.name AS vendor\r\n"
			+ "FROM\r\n"
			+ "    cartitems ci\r\n"
			+ "JOIN\r\n"
			+ "    products p ON ci.productid = p.id\r\n"
			+ "JOIN\r\n"
			+ "    categories c ON p.categoryid = c.id\r\n"
			+ "JOIN\r\n"
			+ "    vendors v ON p.vendorid = v.id\r\n"
			+ "WHERE\r\n"
			+ "    ci.memberid = :memberId", nativeQuery = true)
	List<Map<String, Object>> getCartItemList(@Param("memberId") Long memberId);
	
	// 장바구니에 이미 담긴 상품인지 확인용
	@Query("SELECT c FROM CartItem c WHERE c.memberid = :memberId AND c.product = :product")
	Optional<CartItem> findByMemberIdAndProductId(@Param("product") Product product, @Param("memberId") Long memberId);


}
