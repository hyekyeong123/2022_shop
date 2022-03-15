package com.coding.shop.Service;

import com.coding.shop.dto.ItemFormDto;
import com.coding.shop.dto.ItemImgDto;
import com.coding.shop.dto.ItemSearchDto;
import com.coding.shop.entity.Item;
import com.coding.shop.entity.ItemImg;
import com.coding.shop.repository.ItemImgRepository;
import com.coding.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // START *************************** 아이템 생성 : C **********************************
    public Long saveItem(
            ItemFormDto itemFormDto,
            List<MultipartFile> itemImgFileList
    )throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            // 첫번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅
            if(i == 0) itemImg.setRepimgYn("Y");
            else itemImg.setRepimgYn("N");

            // 아이템이미지 DB에다가 각각 저장
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }


    // START *************************** 아이템 조회 : R **********************************

    // 아이템 상세 조회
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        // 해당 상품의 이미지를 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // ItemFormDto에 item 정보 넣기기
       ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;

    }

    // 상품 조회 조건과 페이지 정보를 파라미터로 받아서 조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }


    // START *************************** 아이템 업데이트 : U *********************************
    public Long updateItem(
            ItemFormDto itemFormDto,
            List<MultipartFile> itemImgFileList
    ) throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 상품 정보 업데이트
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }
}

