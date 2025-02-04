package aist.cargo.service;


import java.util.Map;

public interface CloudinaryService {
    Map<String, Object> uploadFile(String filePath) throws Exception;

    Map<String, Object> getFileDetails(String publicId) throws Exception;

    Map<String, Object> deleteFile(String publicId) throws Exception;
}
