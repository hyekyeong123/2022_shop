package com.coding.shop.repository;

import com.coding.shop.constant.ItemSellStatus;
import com.coding.shop.dto.ItemSearchDto;
import com.coding.shop.entity.Item;
import com.coding.shop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

// p. 268 - 이해 하나도 안되는 부분.....
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 상품 판매 조건이 전체(null)인 경우 null을 리턴, 아니라면 해당 조건(판매중 or 품절)의 상품만 조회
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){

        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);

    }

    // searchDateType의 값에 따라 dateTime의 값을 이전 시간의 값으로 세팅한 후 해당 시간 이후로 등록된 상품만 조회
    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        }
        else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }
        else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }
        else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }
        else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    // 검색어를 포함하고 있는 상품만 조회
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    // queryFactory를 이용해서 쿼리 생성
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

//    @Override
//    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        QItem item = QItem.item;
//        QItemImg itemImg = QItemImg.itemImg;
//
//        QueryResults<MainItemDto> results = queryFactory
//                .select(
//                        new QMainItemDto(
//                                item.id,
//                                item.itemNm,
//                                item.itemDetail,
//                                itemImg.imgUrl,
//                                item.price)
//                )
//                .from(itemImg)
//                .join(itemImg.item, item)
//                .where(itemImg.repimgYn.eq("Y"))
//                .where(itemNmLike(itemSearchDto.getSearchQuery()))
//                .orderBy(item.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        List<MainItemDto> content = results.getResults();
//        long total = results.getTotal();
//        return new PageImpl<>(content, pageable, total);
//    }

}