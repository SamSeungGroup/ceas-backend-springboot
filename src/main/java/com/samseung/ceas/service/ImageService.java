package com.samseung.ceas.service;

import java.util.List;
import java.util.Optional;

import com.samseung.ceas.dto.ProductDTO;
import com.samseung.ceas.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.samseung.ceas.handler.ImageHandler;
import com.samseung.ceas.model.Image;
import com.samseung.ceas.repository.ImageRepository;

@Service
public class ImageService {
	@Autowired
	private final ImageRepository imageRepository;
	@Autowired
	private final ImageHandler imageHandler;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
		this.imageHandler = new ImageHandler();
	}

	public void addUserImage(Image image, List<MultipartFile> files, UserDTO userDTO) throws Exception {
		// 파일을 저장하고 그 Board 에 대한 list 를 가지고 있는다
		List<Image> list = imageHandler.parseFileUserInfo(image.getId(), files, userDTO);
		for (Image images : list) {
			imageRepository.save(images);
		}
	}

	public void addProductImage(Image image, List<MultipartFile> files, ProductDTO productDTO) throws Exception {
		// 파일을 저장하고 그 Board 에 대한 list 를 가지고 있는다
		List<Image> list = imageHandler.parseFileProductInfo(image.getId(), files, productDTO);
		for (Image images : list) {
			imageRepository.save(images);
		}
	}

	public Image findByUserId(String userId) {
		return imageRepository.findByUser_Id(userId);
	}

	public Image findByProductId(Long productId) {
		return imageRepository.findByProduct_Id(productId);
	}
}