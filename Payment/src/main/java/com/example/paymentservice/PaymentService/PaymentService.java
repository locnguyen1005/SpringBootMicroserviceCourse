package com.example.paymentservice.PaymentService;

import com.example.commonservice.Model.Account;
import com.example.commonservice.Model.Product;
import com.example.paymentservice.PaymentDTO.PaymentDTO;
import com.example.paymentservice.PaymentEntity.PaymentEntity;
import com.example.paymentservice.PaymentRepository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.commonservice.Common.CommonException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentService {

	private PaymentRepository paymentRepository;
	private ModelMapper modelMapper;
	@Autowired
	private WebClient.Builder webBuilder;
	@Autowired
	public PaymentService(PaymentRepository paymentRepository, ModelMapper modelMapper) {
		this.paymentRepository = paymentRepository;
		this.modelMapper = modelMapper;
	}

	public Flux<PaymentDTO> getAllProduct(){
		return paymentRepository.findAll().map(PaymentDTO -> modelMapper.map(PaymentDTO, PaymentDTO.class)).switchIfEmpty(Mono.error(new CommonException("Payment 000", "PaymentDTO is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<Boolean> checkDuplicate(PaymentDTO lessionDTO){
		return paymentRepository.findByAccountid(lessionDTO.getAccountid() , lessionDTO.getProductid())
				.flatMap(lessionEntiy -> Mono.just(true))
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<PaymentDTO> creatPayment(PaymentDTO paymentDTO){
		
		
		if((checkDuplicate(paymentDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.error(new CommonException("Không thành công", "Bạn đã mua khóa học này", HttpStatus.BAD_REQUEST));
		}
		else {
			
			return Mono.just(paymentDTO)
					.map(newpayment -> modelMapper.map(newpayment, PaymentEntity.class))
					.flatMap(newPaymentEntity -> paymentRepository.save(newPaymentEntity))
					.map(paymentEntity -> modelMapper.map(paymentEntity, PaymentDTO.class));
		}
	}
	public Flux<PaymentDTO> findbyaccountid(Long accountid){
		return paymentRepository.findByAccountid(accountid).map(PaymentDTO -> modelMapper.map(PaymentDTO, PaymentDTO.class));
	}
	public Mono<PaymentDTO> findbypaymentid(Long accountid){
		return paymentRepository.findById(accountid).map(PaymentDTO -> modelMapper.map(PaymentDTO, PaymentDTO.class));
	}
	public Flux<Product> sortedProduct(){

		Flux<Product> resultProduct = webBuilder.build().get().uri("http://localhost:9000/product/getall" )
				.retrieve().bodyToFlux(Product.class);
		Flux<PaymentEntity> paymentEntityFlux = paymentRepository.findAll();
		Map<Long, Long> personMap = resultProduct.toStream()
				.collect(Collectors.toMap(Product::getId, product -> 0L));

		return null;
	}
}
