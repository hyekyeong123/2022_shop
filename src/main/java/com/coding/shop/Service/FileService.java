package com.coding.shop.Service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(
            String uploadPath, String originalFileName, byte[] fileData
    ) throws Exception{

        // Universally Unique Identifier 서로 다른 개체들을 구분하기 위해 이름을 부여할 때 사용(ex) 파일명)
        UUID uuid = UUID.randomUUID();
        // 73723458-cb24-4a01-8b07-731ff791e56c

        String extension = originalFileName.substring(
                originalFileName.lastIndexOf(".")
        );

        // 새로 저장할 파일의 이름
        String savedFileName = uuid.toString()+extension;
        String fileUploadFullUrl = uploadPath + "/"+savedFileName; // 경로 +"/"+ 이롬
        // C:/shop/item/73723458-cb24-4a01-8b07-731ff791e56c.jpg

        // 바이트 단위의 파일 출력 시스템

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        // java.io.FileNotFoundException: C:\shop\item\7ee0eb8c-1256-42d3-87ad-abc0b0e83d3e.jpg (지정된 경로를 찾을 수 없습니다)


        return savedFileName;
    }


    public void deleteFIle(String filePath) throws Exception{

        // 파일이 저장된 경로를 이용하여 파일 객체 생성
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일을 존재하지 않습니다.");
        }
    }
}
