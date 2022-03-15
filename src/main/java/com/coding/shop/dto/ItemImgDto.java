package com.coding.shop.dto;

import com.coding.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;

import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    // 먼저 멤버변수로 ModelMapper 객체 추가
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {

        // ItemImg 객체의 자료형과 멤버변수의 이름이 같을때 ItemImgDto로 값을 복사해서 반환
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
