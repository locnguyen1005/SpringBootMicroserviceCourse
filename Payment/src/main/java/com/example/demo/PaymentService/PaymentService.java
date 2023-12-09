package com.example.demo.PaymentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.example.demo.Common.CommonException;
import com.example.demo.PaymentDTO.PaymentDTO;
import com.example.demo.PaymentEntity.PaymentEntity;
import com.example.demo.PaymentRepository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {
	private PaymentRepository paymentRepository;
	private ModelMapper modelMapper;
	
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
}
