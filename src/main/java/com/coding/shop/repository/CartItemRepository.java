package com.coding.shop.repository;

import com.coding.shop.dto.CartDetailDto;
import com.coding.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회
    CartItem findByCartIdAndItemId(Long cardId, Long itemId);

    // 장바구니 페이지에 전달
    /*
    CartDetailDto의  생성자를 이용하여 DTO를 반환할땐 new키워드와 해당 DTO의 키워드와 패키지, 클래스명을 적어줘야함. 또한 순서를 DTO 클래스에 명시한 순서에 맞춰서 넣어줘야함
    */
    @Query("select new com.coding.shop.dto.CartDetailDto(" + "ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}
