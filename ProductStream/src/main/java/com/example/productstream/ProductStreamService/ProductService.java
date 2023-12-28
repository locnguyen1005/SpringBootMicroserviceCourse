package com.example.productstream.ProductStreamService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.commonservice.Common.CommonException;
import com.example.productstream.Entity.ProductStream;
import com.example.productstream.ProductStreamDTO.ProductStreamDTO;
import com.example.productstream.Repository.ProductStreamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductStreamRepository productStreamRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    public Flux<ProductStreamDTO> getAllProduct(){
        return productStreamRepository.findAll().map(ProductStreamDTO -> modelMapper.map(ProductStreamDTO, ProductStreamDTO.class))
                .flatMap(productdto -> getvideoapi(productdto))
                .switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
    }
    public Mono<Boolean> checkDuplicate(ProductStreamDTO productDTO){
        return productStreamRepository.findByName(productDTO.getName())
                .map(product ->  Boolean.TRUE)
                .switchIfEmpty(Mono.just(Boolean.FALSE));
    }
    public Mono<ProductStreamDTO> createProduct(ProductStreamDTO productDTO , MultipartFile file) throws IOException {
        try {

            if(checkDuplicate(productDTO).block().equals(Boolean.TRUE)) {
                return Mono.error(new CommonException(productDTO.getName(), "The course name already exists", HttpStatus.BAD_REQUEST));
            }
            else {
                ProductStreamDTO newproduDto = modelMapper.map(productDTO, ProductStreamDTO.class);
                File fileObj = convertMultiPartFileToFile(file);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
                fileObj.delete();
                newproduDto.setImage(fileName);
                return Mono.just(newproduDto)
                        .map(productdto -> modelMapper.map(productdto, ProductStream.class))
                        .flatMap(product -> productStreamRepository.save(product))
                        .map(productentity -> modelMapper.map(productentity, ProductStreamDTO.class))
                        .doOnSubscribe(dto -> log.info("susscess"));
            }
        } catch (Exception e) {
            return Mono.error(new CommonException("", "", HttpStatus.BAD_REQUEST));
        }
    }
    public Mono<ProductStreamDTO> finđById(String productDTOID){
        return productStreamRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, ProductStreamDTO.class)).flatMap(lessionClient -> getvideoapi(lessionClient)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
    }

    public Mono<ProductStreamDTO> editproduct(String id , ProductStreamDTO productDTO , MultipartFile file){
        ProductStreamDTO productDTO2= productStreamRepository.findById(id).map(ProductDTO -> modelMapper.map(ProductDTO, ProductStreamDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST))).block();
        productDTO2.setDescription(productDTO.getDescription());
        productDTO2.setName(productDTO.getName());
        productDTO2.setPrice(productDTO.getPrice());
        if(file == null) {
            return Mono.just(productDTO2)
                    .map(productdto -> modelMapper.map(productdto, ProductStream.class))
                    .flatMap(product -> productStreamRepository.save(product))
                    .map(productentity -> modelMapper.map(productentity, ProductStreamDTO.class))
                    .doOnSubscribe(dto -> log.info("susscess"));
        }
        else {

            File fileObj = convertMultiPartFileToFile(file);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            fileObj.delete();
            productDTO2.setImage(fileName);
            return Mono.just(productDTO2)
                    .map(productdto -> modelMapper.map(productdto, ProductStream.class))
                    .flatMap(product -> productStreamRepository.save(product))
                    .map(productentity -> modelMapper.map(productentity, ProductStreamDTO.class))
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
    public Mono<ProductStreamDTO> getvideoapi(ProductStreamDTO lessionClient) {
        lessionClient.setImage(generatePreSignedUrl(lessionClient.getImage(), HttpMethod.GET));
        return Mono.just(lessionClient);
    }
    public String generatePreSignedUrl(String filePath, HttpMethod http) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,6);
        return amazonS3.generatePresignedUrl(bucketName,filePath,cal.getTime(),http).toString();
    }
    public Flux<ProductStreamDTO> getAllProductTrue(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Flux<ProductStream> productStreamFlux = productStreamRepository.findAll();
        Flux<ProductStream> filteredFlux = productStreamFlux.filter(number -> LocalDate.now().isAfter(LocalDate.parse(number.getDateStart(), formatter)));
        return filteredFlux.map(ProductStreamDTO -> modelMapper.map(ProductStreamDTO, ProductStreamDTO.class))
                .flatMap(productdto -> getvideoapi(productdto))
                .switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));

    }
}
