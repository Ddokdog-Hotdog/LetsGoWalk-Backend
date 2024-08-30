package com.ddokdoghotdog.gowalk.product.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Product;

import jakarta.persistence.LockModeType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	// 일반 상품 목록 조회
	@Query(value = "SELECT\r\n"
			+ "    p.id AS productId,\r\n"
			+ "    p.name AS name,\r\n"
			+ "    p.price AS price,\r\n"
			+ "    p.thumbnailimage AS thumbnailImage,\r\n"
			+ "    p.stock_quantity AS stockQuantity,\r\n"
			+ "    CASE WHEN pl.memberid IS NOT NULL THEN 'Y' ELSE 'N' END AS isLike,\r\n"
			+ "    c.name AS category,\r\n"
			+ "    v.name AS vendor\r\n"
			+ "FROM\r\n"
			+ "    products p\r\n"
			+ "LEFT JOIN\r\n"
			+ "    product_member_likes pl ON p.id = pl.productid AND pl.memberid = :memberId\r\n"
			+ "LEFT JOIN\r\n"
			+ "    categories c ON p.categoryid = c.id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    vendors v ON p.vendorid = v.id\r\n"
			+ "ORDER BY\r\n"
			+ "    p.created_at DESC\r\n"
			+ "OFFSET 8*(:page - 1) ROWS FETCH NEXT 8 ROWS ONLY", nativeQuery = true)
	List<Map<String, Object>> getItemList(@Param("page") Integer page, @Param("memberId") Long memberId);
	
	
	// 찜 상품 목록 조회
	@Query(value = "SELECT\r\n"
			+ "    p.id AS productId,\r\n"
			+ "    p.name AS name,\r\n"
			+ "    p.price AS price,\r\n"
			+ "    p.thumbnailimage AS thumbnailImage,\r\n"
			+ "    p.stock_quantity AS stockQuantity,\r\n"
			+ "    CASE WHEN pl.memberid IS NOT NULL THEN 'Y' ELSE 'N' END AS isLike,\r\n"
			+ "    c.name AS category,\r\n"
			+ "    v.name AS vendor\r\n"
			+ "FROM\r\n"
			+ "    products p\r\n"
			+ "JOIN\r\n"
			+ "    product_member_likes pl ON p.id = pl.productid AND pl.memberid = :memberId\r\n"
			+ "LEFT JOIN\r\n"
			+ "    categories c ON p.categoryid = c.id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    vendors v ON p.vendorid = v.id\r\n"
			+ "ORDER BY\r\n"
			+ "    p.created_at DESC\r\n"
			+ "OFFSET 8*(:page - 1) ROWS FETCH NEXT 8 ROWS ONLY", nativeQuery = true)
	List<Map<String, Object>> getLikeItemList(@Param("page") Integer page, @Param("memberId") Long memberId);
	
	
	// 상품 상세 페이지 조회
	@Query(value = "SELECT \r\n"
			+ "    p.id AS productId,\r\n"
			+ "    c.name AS category,\r\n"
			+ "    p.name AS name,\r\n"
			+ "    p.thumbnailimage AS thumbnailImage,\r\n"
			+ "    p.detailimage AS detailImage,\r\n"
			+ "    p.price AS price,\r\n"
			+ "    v.name AS vendor,\r\n"
			+ "    CASE WHEN pl.memberid IS NOT NULL THEN 'Y' ELSE 'N' END AS isLike,\r\n"
			+ "    (SELECT COUNT(*) FROM product_member_likes WHERE productid = p.id) AS likeTotal,\r\n"
			+ "    (SELECT COUNT(*) FROM reviews WHERE productid = p.id) AS reviewTotal,\r\n"
			+ "    r.id AS reviewId,\r\n"
			+ "    r.title AS reviewTitle,\r\n"
			+ "    r.contents AS reviewContent,\r\n"
			+ "    r.created_at AS reviewCreatedAt,\r\n"
			+ "    r.updated_at AS reviewUpdatedAt\r\n"
			+ "FROM \r\n"
			+ "    products p\r\n"
			+ "LEFT JOIN \r\n"
			+ "    categories c ON p.categoryid = c.id\r\n"
			+ "LEFT JOIN \r\n"
			+ "    vendors v ON p.vendorid = v.id\r\n"
			+ "LEFT JOIN \r\n"
			+ "    product_member_likes pl ON p.id = pl.productid AND pl.memberid = :memberId\r\n"
			+ "LEFT JOIN \r\n"
			+ "    reviews r ON r.id = (\r\n"
			+ "        SELECT id \r\n"
			+ "        FROM (SELECT id \r\n"
			+ "              FROM reviews \r\n"
			+ "              WHERE productid = p.id \r\n"
			+ "              ORDER BY created_at DESC) \r\n"
			+ "        WHERE ROWNUM = 1\r\n"
			+ "    )\r\n"
			+ "WHERE \r\n"
			+ "    p.id = :productId", nativeQuery = true)
	Map<String, Object> getItemDetail(@Param("productId") Long productId, @Param("memberId") Long memberId);
	
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value="SELECT p FROM Product p WHERE p.id = :id")
	Optional<Product> findByIdForUpdate(@Param("id") Long id);
	
	@Modifying
	@Query("UPDATE Product p SET p.stockQuantity = :stockQuantity WHERE p.id = :id")
	int updateStock(@Param("stockQuantity") Long stockQuantity, @Param("id") Long id);
	
	
}
