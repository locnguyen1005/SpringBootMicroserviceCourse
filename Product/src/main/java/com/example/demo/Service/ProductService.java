package com.example.demo.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.Common.CommonException;
import com.example.demo.DTO.ProductClient;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import com.example.demo.Model.Account;
import com.example.demo.Repository.ProductRepository;



import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
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
	public Mono<ProductDTO> finđById(Long productDTOID){
		return productRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	
	public Mono<ProductDTO> editproduct(Long id , ProductDTO productDTO , MultipartFile file){
		ProductDTO productDTO2= productRepository.findById(id).map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST))).block();
		productDTO2.setDescription(productDTO.getDescription());
		productDTO2.setName(productDTO.getName());
		productDTO2.setPrice(productDTO.getPrice());
		if(file.isEmpty()) {
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
