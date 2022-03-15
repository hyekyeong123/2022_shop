package com.coding.shop.Service;

import com.coding.shop.entity.ItemImg;
import com.coding.shop.repository.ItemImgRepository;
import com.coding.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final FileService fileService;
    private final ItemImgRepository itemImgRepository;//  ****************************************************************

    // START ***************** 상품 이미지 저장 ************************
    public void saveItemImg(
            ItemImg itemImg, MultipartFile itemImgFile
    ) throws Exception{

        String oriImgName = itemImgFile.getOriginalFilename(); // 업로드 했던 상품 이미지 파일의 원래 이름
        String imgName = ""; // 실제 서버에 저장된 상품 이미지 파일의 이름
        String imgUrl = ""; // 업로드 결과 서버에 저장된 상품 이미지 파일을 불러오는 경로

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){

            imgName = fileService.uploadFile(
                    itemImgLocation, oriImgName, itemImgFile.getBytes()
            );
            imgUrl = "/images/item/"+imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    // START ***************** 상품 이미지 수정 ************************
    public void updateItemImg(
            Long itemImgId,
            MultipartFile itemImgFile
    ) throws Exception{

        if(!itemImgFile.isEmpty()){

            // 기존에 저장했던 상품 이미지 객체 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                .orElseThrow(EntityNotFoundException::new);

            // 가존에 등록된 이미지가 있을 경우 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFIle(itemImgLocation+"/"+savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(
                    itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName;


            // 상품 이미지 정보 저장
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            // 현재 영속 상태이므로 데이터를 변경하는 것 만으로 update 쿼리가 발생
        }
    }
}
