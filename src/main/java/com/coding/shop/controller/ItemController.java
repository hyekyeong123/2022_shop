package com.coding.shop.controller;

import com.coding.shop.Service.ItemService;
import com.coding.shop.dto.ItemFormDto;
import com.coding.shop.dto.ItemSearchDto;
import com.coding.shop.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

//   START ************************* 상품 등록 관련 ************************

    // 상품 등록 페이지
    @GetMapping(value="/admin/item/new")
    public String itemRegistrationView(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "/item/itemForm";
    }

    // 상품 등록 액션
    @PostMapping(value="/admin/item/new")
    public String itemRegistrationAction(
        @Valid ItemFormDto itemFormDto,
        BindingResult bindingResult,
        Model model,
        @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList
    ){

        // 필수 값이 없다면
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        // 상품이 없는 경우
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
// END ************************* 상품 등록 관련 ************************

// START ************************* 상품 조회 관련 : R ************************

    // 상품 목록 보여주기
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemList(
        ItemSearchDto itemSearchDto,
        @PathVariable("page") Optional<Integer> page,
        Model model
    ){

        // 페이징을 위해 PageRequest.of(조회할 페이지 번호, 한번에 가지고 올 데이터 수)
        Pageable pageable = PageRequest.of(
                page.isPresent() ? page.get() : 0,
                3);

        // 조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item>객체 생성
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);

        // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 다시 전달
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }
// END ************************* 상품 조회 관련 : R ************************


// START ************************* 상품 수정 관련 ************************
    @GetMapping(value="/admin/item/{itemId}")
    public String itemModifyView(
            @PathVariable Long itemId,
            Model model
    ){
        try{
            // 아이디로 조회
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }

        catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "/item/itemForm";
        }

        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemModifyAction(
            @Valid ItemFormDto itemFormDto,
            BindingResult bindingResult,
            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
            Model model
    ){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
// END ************************* 상품 수정 관련 ************************


}

