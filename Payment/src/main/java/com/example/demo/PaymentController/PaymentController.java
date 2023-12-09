package com.example.demo.PaymentController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Event.PaymentProducer;
import com.example.demo.Model.Account;
import com.example.demo.Model.AccountRegister;
import com.example.demo.Model.Answer;
import com.example.demo.Model.Product;
import com.example.demo.PaymentConfig.PaymentConfig;
import com.example.demo.PaymentDTO.Mail;
import com.example.demo.PaymentDTO.PaymentDTO;
import com.example.demo.PaymentEntity.PaymentEntity;
import com.example.demo.PaymentRepository.PaymentRepository;
import com.example.demo.PaymentService.EmailServiceImpl;
import com.example.demo.PaymentService.PaymentService;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class PaymentController {
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	Gson gson;
	@Autowired
	private PaymentService service;
	@Autowired
	private EmailServiceImpl emailService;
	@Autowired
	private WebClient.Builder webBuilder;
	@Autowired
	private PaymentProducer paymentProducer;
	@GetMapping("/payment-callback")
    public Mono<String> paymentCallback(@RequestParam Map<String, String> queryParams,HttpServletResponse response ) throws IOException {
		if(paymentRepository.findByAccountid(Long.parseLong(queryParams.get("account")), Long.parseLong(queryParams.get("productID")))!=null) {
			return Mono.error(new Exception("Tài Khoản đã đăng ký khóa học"));
		}
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");

        
        String registerServiceId = queryParams.get("registerServiceId");
        PaymentDTO paymentEntity = new PaymentDTO();
        log.info(queryParams.get("account"));
        log.info(queryParams.get("vnp_Amount"));
        log.info(queryParams.get("productID"));
        

        paymentEntity.setAccountid(Long.parseLong(queryParams.get("account")));
        paymentEntity.setPrice(Long.parseLong(queryParams.get("vnp_Amount")));
        paymentEntity.setProductid(Long.parseLong(queryParams.get("productID")));
        paymentEntity.setDay(LocalDateTime.now());
        
        log.info(queryParams.get("account"));
        log.info(queryParams.get("vnp_Amount"));
        log.info(queryParams.get("productID"));
        Mono<Account> account = webBuilder.build().get()
                .uri("http://localhost:9000/Account/"+Long.parseLong(queryParams.get("account")))
                .retrieve()
                .bodyToMono(Account.class);
        log.info(account.block().toString());
        Mono<Product> product = webBuilder.build().get()
                .uri("http://localhost:9000/product/"+Long.parseLong(queryParams.get("productID")))
                .retrieve()
                .bodyToMono(Product.class);
        log.info(product.block().toString());
        //tạo người đã đăng ký khóa học
        
        AccountRegister accountRegisterCommon = new AccountRegister();
        accountRegisterCommon.setAccountId(Long.parseLong(queryParams.get("account")));
        accountRegisterCommon.setProductId(Long.parseLong(queryParams.get("productID")));
        
        //tạo email
        Mail mailsend = new Mail();
        mailsend.setRecipient(account.block().getEmail());
        mailsend.setMsgBody("Bạn đã đăng ký khóa học "+product.block().getName());
        mailsend.setSubject("Đăng ký khóa học thành công");
        
        //Tạo quiz cho người đăng ký
        Answer answer = new Answer();
        answer.setAccountid(Long.parseLong(queryParams.get("account")));
        answer.setProductid(Long.parseLong(queryParams.get("productID")));
        
        if(paymentEntity!= null && !paymentEntity.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
            	//tạo quizz khi đăng ký thành công
            	Mono<Answer> resultQuiz = webBuilder.build().post()
                        .uri("http://localhost:8889/Answer/Create")
                        .body(BodyInserters.fromValue(answer))
                        .retrieve()
                        .bodyToMono(Answer.class);
            	log.info(resultQuiz.block().toString());
            	Mono<AccountRegister> resultAccountRegister = webBuilder.build().post()
                        .uri("http://localhost:9000/ProductAccount/Post")
                        .body(BodyInserters.fromValue(accountRegisterCommon))
                        .retrieve()
                        .bodyToMono(AccountRegister.class);
            	
            	log.info(resultAccountRegister.block().toString());
            	Mono<PaymentDTO> payment= service.creatPayment(paymentEntity);

            	String status = paymentProducer.send("PaymentSend",gson.toJson(mailsend)).block();
//            	//tạo accountregister khi dky thành công
//            	
//            	log.info(resultQuiz.block().toString());
            	
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect("http://localhost:4200/payment-failed");
                
            }
        }
        return Mono.just("true");
    }
	@GetMapping
	public String getpaymen() {
		return "loc";
	}
    @GetMapping("/pay/{productID}/{accountID}")
	public String getPay(@PathVariable Long productID, @PathVariable Long accountID) throws UnsupportedEncodingException{
    	Mono<Product> resultProduct = webBuilder.build().get()
                .uri("http://localhost:9000/product/"+productID)
                .retrieve()
                .bodyToMono(Product.class);

		String vnp_Version = "2.1.0";
        String vnp_Command = String.valueOf(accountID);
        String orderType = "other";
        long amount = resultProduct.block().getPrice()*100;
        String bankCode = "NCB";
        
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl+"?productID="+productID +"&account=" +accountID );
        
  
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
		
		return paymentUrl;
	}
    @GetMapping("/payment")
    public String demo(@RequestBody Mail details) {
   
    	String status = paymentProducer.send("PaymentSend","Loc").block();
    	
    	return status;
    } 
    
}
