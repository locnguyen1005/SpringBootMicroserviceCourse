package com.example.paymentservice.PaymentController;

import com.example.commonservice.Model.*;
import com.example.paymentservice.PaymentConfig.PaymentConfig;
import com.example.paymentservice.PaymentDTO.Mail;
import com.example.paymentservice.PaymentDTO.PaymentDTO;
import com.example.paymentservice.PaymentRepository.PaymentRepository;
import com.example.paymentservice.PaymentService.PaymentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.paymentservice.Event.PaymentProducer;
import com.example.paymentservice.PaymentService.EmailServiceImpl;
import com.example.commonservice.utils.ConstantCommon;
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

import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:3006")
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
	public Mono<String> paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response)
			throws IOException {
		String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
		String registerServiceId = queryParams.get("registerServiceId");
		PaymentDTO paymentEntity = new PaymentDTO();
		log.info(queryParams.get("account"));
		log.info(queryParams.get("vnp_Amount"));
		log.info(queryParams.get("productID"));

		paymentEntity.setAccountid(Long.parseLong(queryParams.get("account")));
		paymentEntity.setPrice(Long.parseLong(queryParams.get("vnp_Amount")));
		paymentEntity.setProductid(Long.parseLong(queryParams.get("productID")));
		paymentEntity.setProductstreamid("");
		paymentEntity.setDay(LocalDateTime.now());
//		if (service.checkDuplicate(paymentEntity).block()) {
//			return Mono.error(new Exception("Tài Khoản đã đăng ký khóa học"));
//		}
		log.info(queryParams.get("account"));
		log.info(queryParams.get("vnp_Amount"));
		log.info(queryParams.get("productID"));
		Mono<Account> account = webBuilder.build().get()
				.uri("http://localhost:9000/Account/" + Long.parseLong(queryParams.get("account"))).retrieve()
				.bodyToMono(Account.class);
		log.info(account.block().toString());
		Mono<Product> product = webBuilder.build().get()
				.uri("http://localhost:9000/product/" + Long.parseLong(queryParams.get("productID"))).retrieve()
				.bodyToMono(Product.class);
		log.info(product.block().toString());
		//tạo lession_account
		Lession_Account lession_Account = new Lession_Account();
		lession_Account.setAccountId(Long.parseLong(queryParams.get("account")));
		lession_Account.setProductid(Long.parseLong(queryParams.get("productID")));
		//tạo người đã đăng ký khóa học
		AccountRegister accountRegisterCommon = new AccountRegister();
		accountRegisterCommon.setAccountId(Long.parseLong(queryParams.get("account")));
		accountRegisterCommon.setProductId(Long.parseLong(queryParams.get("productID")));
		// tạo email
		Mail mailsend = new Mail();
		mailsend.setRecipient(account.block().getEmail());
		mailsend.setMsgBody("<html><body style=\"text-align: center\"><h1>Bạn đã đặt hàng thành công</h1>"
							+"<p>Tên khóa học của bạn là: <strong>"+ product.block().getName()+"</strong></p>"
							+"<p>Giá tiền: <strong>"+ product.block().getPrice()+"</strong></p>"
							+ "<img src=\"" + product.block().getApiimage() + "\" alt=\"" + product.block().getApiimage() + "\">" +"</body></html>");
		mailsend.setSubject("Đăng ký khóa học thành công");
		// Tạo quiz cho người đăng ký
		Answer answer = new Answer();
		answer.setAccountid(Long.parseLong(queryParams.get("account")));
		answer.setProductid(Long.parseLong(queryParams.get("productID")));
		

		if (paymentEntity != null && !paymentEntity.equals("")) {
			if ("00".equals(vnp_ResponseCode)) {
				// tạo quizz khi đăng ký thành công
				Mono<PaymentDTO> payment = service.creatPayment(paymentEntity);
				Mono<Answer> resultQuiz = webBuilder.build().post().uri("http://localhost:9000/Answer/Create")
						.body(BodyInserters.fromValue(answer))
						.retrieve()
						.bodyToMono(Answer.class);

				Mono<AccountRegister> resultAccountRegister = webBuilder.build().post()
						.uri("http://localhost:9000/ProductAccount/Post")
						.body(BodyInserters.fromValue(accountRegisterCommon)).retrieve()
						.bodyToMono(AccountRegister.class);
				log.info(payment.block().toString());
				log.info(resultAccountRegister.block().toString());
				log.info(resultQuiz.block().toString());
				String status = paymentProducer.send(ConstantCommon.EMAIL, gson.toJson(mailsend)).block();
				String status1 = paymentProducer.send(ConstantCommon.LESSION_ACCOUNT, gson.toJson(lession_Account)).block();
				String status2 = paymentProducer.send(ConstantCommon.ACCOUNT, gson.toJson(product)).block();

				response.sendRedirect("http://localhost:3006/paymentsuccess");
			} else {
				// Giao dịch thất bại
				// Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
				response.sendRedirect("http://localhost:3006/payment-failed");

			}
		}
		return Mono.just("true");
	}
	@GetMapping("/payment-callback-productstream")
	public Mono<String> paymentCallback_productstream(@RequestParam Map<String, String> queryParams, HttpServletResponse response)
			throws IOException {
		String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
		String registerServiceId = queryParams.get("registerServiceId");
		PaymentDTO paymentEntity = new PaymentDTO();
		log.info(queryParams.get("account"));
		log.info(queryParams.get("vnp_Amount"));
		log.info(queryParams.get("productID"));

		paymentEntity.setAccountid(Long.parseLong(queryParams.get("account")));
		paymentEntity.setPrice(Long.parseLong(queryParams.get("vnp_Amount")));
		paymentEntity.setProductstreamid(queryParams.get("productID"));
		paymentEntity.setProductid(0l);
		paymentEntity.setDay(LocalDateTime.now());
//		if (service.checkDuplicate(paymentEntity).block()) {
//			return Mono.error(new Exception("Tài Khoản đã đăng ký khóa học"));
//		}
		log.info(queryParams.get("account"));
		log.info(queryParams.get("vnp_Amount"));
		log.info(queryParams.get("productID"));
		Mono<Account> account = webBuilder.build().get()
				.uri("http://localhost:9000/Account/" + Long.parseLong(queryParams.get("account"))).retrieve()
				.bodyToMono(Account.class);
		log.info(account.block().toString());
		Mono<ProductStream> product = webBuilder.build().get()
				.uri("http://localhost:9000/ProductStream/" + queryParams.get("productID")).retrieve()
				.bodyToMono(ProductStream.class);

		//tạo lession_account

		//tạo người đã đăng ký khóa học

		// tạo email
		Mail mailsend = new Mail();
		mailsend.setRecipient(account.block().getEmail());
		mailsend.setMsgBody("<html><body style=\"text-align: center\"><h1>Bạn đã đặt hàng thành công</h1>"
				+"<p>Tên khóa học của bạn là: <strong>"+ product.block().getName()+"</strong></p>"
				+"<p>Giá tiền: <strong>"+ product.block().getPrice()+"</strong></p>"
				+ "<img src=\"" + product.block().getImage() + "\" alt=\"" + product.block().getImage() + "\">" +"</body></html>");
		mailsend.setSubject("Đăng ký khóa học thành công");
		// Tạo quiz cho người đăng ký



		if (paymentEntity != null && !paymentEntity.equals("")) {
			if ("00".equals(vnp_ResponseCode)) {
				// tạo quizz khi đăng ký thành công
				Mono<PaymentDTO> payment = service.creatPayment(paymentEntity);

				log.info(payment.block().toString());

				String status = paymentProducer.send(ConstantCommon.EMAIL, gson.toJson(mailsend)).block();
				String status2 = paymentProducer.send(ConstantCommon.ACCOUNT, gson.toJson(product)).block();
				response.sendRedirect("http://localhost:3006/paymentsuccess");
			} else {
				// Giao dịch thất bại
				// Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
				response.sendRedirect("http://localhost:3006/payment-failed");

			}
		}
		return Mono.just("true");
	}




	@GetMapping("/pay/{productID}/{accountID}")
	public String getPay(@PathVariable Long productID, @PathVariable Long accountID)
			throws UnsupportedEncodingException {
		Mono<Product> resultProduct = webBuilder.build().get().uri("http://localhost:9000/product/" + productID)
				.retrieve().bodyToMono(Product.class);

		String vnp_Version = "2.1.0";
		String vnp_Command = String.valueOf(accountID);
		String orderType = "other";
		long amount = resultProduct.block().getPrice() * 100;
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
		vnp_Params.put("vnp_ReturnUrl",
				PaymentConfig.vnp_ReturnUrl + "?productID=" + productID + "&account=" + accountID);

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
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
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
	@GetMapping("/getPay_ProductStream/{productID}/{accountID}")
	public String getPay_ProductStream(@PathVariable String productID, @PathVariable Long accountID)
			throws UnsupportedEncodingException {
		Mono<ProductStream> resultProduct = webBuilder.build().get().uri("http://localhost:9000/ProductStream/" + productID)
				.retrieve().bodyToMono(ProductStream.class);

		String vnp_Version = "2.1.0";
		String vnp_Command = String.valueOf(accountID);
		String orderType = "other";
		long amount = resultProduct.block().getPrice() * 100;
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
		vnp_Params.put("vnp_ReturnUrl",
				PaymentConfig.vnp_ReturnUrl_productstream + "?productID=" + productID + "&account=" + accountID);

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
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
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

		String status = paymentProducer.send("PaymentSend", "Loc").block();

		return status;
	}

	@GetMapping("/checkregister/{productID}/{accountID}")
	public Boolean getCheck(@PathVariable Long productID, @PathVariable Long accountID) {
		PaymentDTO paymentEntity = new PaymentDTO();
		paymentEntity.setAccountid(accountID);
		paymentEntity.setProductid(productID);
		return service.checkDuplicate(paymentEntity).block();
	}

	@GetMapping("/findbyaccountid/{accountid}")
	public Flux<PaymentDTO> findbyaccountid(@PathVariable Long accountid){
		return service.findbyaccountid(accountid);
	}
	@GetMapping("/findbypaymenttid/{paymentid}")
	public Mono<PaymentDTO> findbyid(@PathVariable Long paymentid){
		return service.findbypaymentid(paymentid);
	}
	@GetMapping("/GetAll")
	public Flux<PaymentDTO> findAll(){
		return service.getAllProduct();
	}
	@GetMapping("/GetAllStatistical")
	public List<com.example.paymentservice.PaymentDTO.statistical> Statistical(){
		Flux<PaymentDTO> paymentDTOFlux = service.getAllProduct();
		Flux<ProductClient> resultProduct = webBuilder.build().get().uri("http://localhost:9000/product/getall" )
				.retrieve().bodyToFlux(ProductClient.class);
		List<com.example.paymentservice.PaymentDTO.statistical> statisticalLis = new ArrayList<>();
		for (ProductClient element : resultProduct.toIterable()){
			com.example.paymentservice.PaymentDTO.statistical statistical = new com.example.paymentservice.PaymentDTO.statistical();
			statistical.setProductid(element.getId());
			statistical.setAccountid(element.getAccountid());
			statistical.setPrice(0l);
			statistical.setNameproduct(element.getName());
			statisticalLis.add(statistical);
		}
		for (PaymentDTO element : paymentDTOFlux.toIterable()){
			for(com.example.paymentservice.PaymentDTO.statistical statistical : statisticalLis){
				if(element.getProductid() == statistical.getProductid()){
					statistical.setPrice(statistical.getPrice()+element.getPrice());
				}
			}
		}
		return statisticalLis;
	}
}
