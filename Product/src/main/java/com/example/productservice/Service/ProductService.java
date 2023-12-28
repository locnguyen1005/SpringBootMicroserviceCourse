package com.example.productservice.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.example.productservice.DTO.ProductClient;
import com.example.productservice.DTO.ProductDTO;
import com.example.productservice.Entity.ProductEntity;
import com.example.productservice.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.commonservice.Common.CommonException;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@EnableScheduling
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucketName;
	public Flux<ProductClient> getAllProduct(){
		return productRepository.findAll().map(ProductDTO -> modelMapper.map(ProductDTO, ProductClient.class))
				.flatMap(lessionClient -> getvideoapi(lessionClient))
				.switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<Boolean> checkDuplicate(ProductDTO productDTO){
		return productRepository.findByName(productDTO.getName())
				.map(product ->  Boolean.TRUE)
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<ProductDTO> createProduct(ProductDTO productDTO , MultipartFile file) throws IOException{
		try {
			
			if(checkDuplicate(productDTO).block().equals(Boolean.TRUE)) {
				return Mono.error(new CommonException(productDTO.getName(), "The course name already exists", HttpStatus.BAD_REQUEST));
			}
			else {
				ProductDTO newproduDto = modelMapper.map(productDTO, ProductDTO.class);
				File fileObj = convertMultiPartFileToFile(file);
		        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		        fileObj.delete();
		        newproduDto.setImage(fileName);
				return Mono.just(newproduDto)
						.map(productdto -> modelMapper.map(productdto, ProductEntity.class))
						.flatMap(product -> productRepository.save(product))
						.map(productentity -> modelMapper.map(productentity, ProductDTO.class))
						.doOnSubscribe(dto -> log.info("susscess"));
			}
		} catch (Exception e) {
			return Mono.error(new CommonException("", "", HttpStatus.BAD_REQUEST));
		}
	}
	public Mono<ProductClient> finđById(Long productDTOID){
		return productRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, ProductClient.class)).flatMap(lessionClient -> getvideoapi(lessionClient)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	
	public Mono<ProductDTO> editproduct(Long id , ProductDTO productDTO , MultipartFile file){
		ProductDTO productDTO2= productRepository.findById(id).map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST))).block();
		productDTO2.setDescription(productDTO.getDescription());
		productDTO2.setName(productDTO.getName());
		productDTO2.setPrice(productDTO.getPrice());
		if(file == null) {
			return Mono.just(productDTO2)
					.map(productdto -> modelMapper.map(productdto, ProductEntity.class))
					.flatMap(product -> productRepository.save(product))
					.map(productentity -> modelMapper.map(productentity, ProductDTO.class))
					.doOnSubscribe(dto -> log.info("susscess"));	
		}
		else {

			File fileObj = convertMultiPartFileToFile(file);
	        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
	        fileObj.delete();
	        productDTO2.setImage(fileName);
			return Mono.just(productDTO2)
					.map(productdto -> modelMapper.map(productdto, ProductEntity.class))
					.flatMap(product -> productRepository.save(product))
					.map(productentity -> modelMapper.map(productentity, ProductDTO.class))
					.doOnSubscribe(dto -> log.info("susscess"));
		}
	}
	private java.io.File convertMultiPartFileToFile(MultipartFile file) {
        java.io.File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
	public Mono<ProductClient> getvideoapi(ProductClient lessionClient) {
		lessionClient.setApiimage(generatePreSignedUrl(lessionClient.getImage(),HttpMethod.GET));
		return Mono.just(lessionClient);
	}
	public String generatePreSignedUrl(String filePath, HttpMethod http) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,6);
        return amazonS3.generatePresignedUrl(bucketName,filePath,cal.getTime(),http).toString();
    }

}
