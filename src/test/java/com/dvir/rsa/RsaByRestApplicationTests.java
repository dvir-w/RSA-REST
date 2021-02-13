package com.dvir.rsa;

import com.dvir.rsa.component.RSAService;
import com.dvir.rsa.model.request.SignRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static com.dvir.rsa.component.RSAService.ID_INIT_VALUE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsaByRestApplicationTests {

	private final static Logger logger = LoggerFactory.getLogger(RsaByRestApplicationTests.class);

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final String TEST_DATA = "my data";
	public static final String URL_SIGN = "/keys/1000/sign";
	public static final String URL_CREATE_KEY = "/keys";


	@Autowired
	private MockMvc mvc;

	@Autowired
	private RSAService rsaService;

	@BeforeEach
	public void setUp() throws Exception {
		rsaService.init();
	}

	/**
	 * test generate key
	 *
	 * @throws Exception
	 */
	@Test
	public void postGenerateKey() throws Exception {
		logger.info("RsaByRestApplicationTests.postGenerateKey");

		mvc.perform(post(URL_CREATE_KEY).contentType(APPLICATION_JSON_UTF8)
				.content("{}"))
				.andExpect(status().isCreated())
				.andExpect(content().string(equalTo("{\"keyId\":" + ID_INIT_VALUE + "}")));

	}

	/**
	 * test sign key
	 * <br/> first create key and then sign data by the created key
	 * @throws Exception
	 */
	@Test
	public void postSignKey() throws Exception {
		logger.info("RsaByRestApplicationTests.postSignKey");
		String signRequestJson = getSignRequestJson(TEST_DATA);
		mvc.perform(post(URL_CREATE_KEY).contentType(APPLICATION_JSON_UTF8)
				.content("{}"));

		mvc.perform(post(URL_SIGN).contentType(APPLICATION_JSON_UTF8)
				.content(signRequestJson))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"signature\":")));

	}

	/**
	 * create json representation of SignRequest
	 *
	 * @param data data to sign
	 * @return json representation of SignRequest
	 * @throws JsonProcessingException
	 */
	private String getSignRequestJson(String data) throws JsonProcessingException {
		SignRequest signRequest = new SignRequest();
		signRequest.setData(data);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(signRequest);
	}

}
