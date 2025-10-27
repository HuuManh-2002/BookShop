package com.example.bookshop.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;

@Service
public class FileStorageService {


    public String saveFile(MultipartFile file, String directoryPath) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 1. Tạo tên file DUY NHẤT
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            // Lấy phần mở rộng từ sau dấu chấm cuối cùng
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Công thức tạo tên file: timestamp_uuid.ext
        String uniqueFileName = System.currentTimeMillis() + "_"
                + UUID.randomUUID().toString()
                + fileExtension;

        // 2. Định nghĩa đường dẫn vật lý và đảm bảo thư mục tồn tại
        // Sử dụng Paths.get() để tạo đường dẫn từ chuỗi directoryPath
        Path uploadPath = Paths.get(directoryPath);

        if (!Files.exists(uploadPath)) {
            // Tạo thư mục nếu nó chưa tồn tại
            Files.createDirectories(uploadPath);
        }

        // 3. Tạo đường dẫn file đầy đủ và Lưu file
        Path filePath = uploadPath.resolve(uniqueFileName);

        // Files.copy để lưu file, ghi đè nếu tồn tại (mặc dù tên đã là duy nhất)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    public boolean deleteFile(String fileName, String subDirectory) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return true; // Coi như thành công nếu không có tên file để xóa
        }

        // 1. Tạo đường dẫn (Path) tới file cần xóa
        // Kết hợp thư mục gốc, thư mục con, và tên file
        Path deletePath = Paths.get(subDirectory);
        Path filePath = deletePath.resolve(fileName);

        // 2. Kiểm tra file tồn tại trước khi xóa
        if (Files.exists(filePath)) {
            // 3. Thực hiện xóa file vật lý
            try {
                // Files.delete(path) sẽ ném IOException nếu không xóa được
                // Ví dụ: file đang được sử dụng bởi process khác
                Files.delete(filePath);
                return true;
            } catch (IOException e) {
                // Bạn có thể log lỗi ở đây
                throw new AppException(ErrorCode.DETETE_FILE_FAILED); // Ném lại lỗi để controller/service khác bắt và xử lý
            }
        }

        // File không tồn tại
        return false;
    }
}
