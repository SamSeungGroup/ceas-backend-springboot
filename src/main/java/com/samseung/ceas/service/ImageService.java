package com.samseung.ceas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.samseung.ceas.handler.ImageHandler;
import com.samseung.ceas.model.Image;
import com.samseung.ceas.repository.ImageRepository;

@Service
public class ImageService {

	private final ImageRepository imageRepository;

	private final ImageHandler imageHandler;

	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
		this.imageHandler = new ImageHandler();
	}

	public void addImage(Image image, List<MultipartFile> files) throws Exception {
		// 파일을 저장하고 그 Board 에 대한 list 를 가지고 있는다
		List<Image> list = imageHandler.parseFileInfo(image.getId(), files);

		// 파일에 대해 DB에 저장하고 가지고 있을 것

		for (Image imageEntitys : list) {
			imageRepository.save(imageEntitys);
		}

	}

	public List<Image> findImages() {
		return imageRepository.findAll();
	}

	public Optional<Image> findImage(long id) {
		return imageRepository.findById(id);
	}

}