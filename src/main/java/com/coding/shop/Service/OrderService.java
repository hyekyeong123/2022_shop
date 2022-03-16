package com.coding.shop.Service;

import com.coding.shop.dto.OrderDto;
import com.coding.shop.dto.OrderHistDto;
import com.coding.shop.dto.OrderItemDto;
import com.coding.shop.entity.*;
import com.coding.shop.repository.ItemImgRepository;
import com.coding.shop.repository.ItemRepository;
import com.coding.shop.repository.MemberRepository;
import com.coding.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    // 주문하기
    public Long order(
        OrderDto orderDto,
        String email
    ){
        // 근데 각각 다른 아이템을 여러개 주문했을때에는??? -> 각각 하나보다..이메일은 계속 가져오고

        // 주문한 아이템 객체와 멤버 객체 생성
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityExistsException::new);
        Member member = memberRepository.findByEmail(email);

        // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // 호원 정보와 주문할 상품 리스트를 이용하여 주문 엔티티를 생성 -> 저장
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    // ******************************* 주문 목록 조회하기 *********************************
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 유저의 아이디와 페이징 조건을 이용해서 주문 목록 조회
        List<Order> orders = orderRepository.findOrdersByMember(email, pageable);
        Long totalCount = orderRepository.countOrdersByMember(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {

            OrderHistDto orderHistDto = new OrderHistDto(order);

            // 현재 orders의 사이즈만큼 쿼리문이 실행
           List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    // 현재 로그인한 사용자와 주문 데이터를 생성한 데이터가 같은지 확인
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        // 현재 로그인한 사람
        Member currentMember = memberRepository.findByEmail(email);

        // 주문한 사람
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(currentMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    // 주문 취소
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

}
