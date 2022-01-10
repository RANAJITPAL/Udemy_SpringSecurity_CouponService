package com.ranajit.couponservice;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CouponserviceApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void testGetCouponWithoutAuth_Forbidden() throws Exception {
		mvc.perform(get("/couponapi/coupons/testCode")).andExpect(status().isForbidden());
	}

	@Test
//	@WithMockUser()
	@WithUserDetails("doug@bailey.com")
	void testGetCouponWithAuth_Success() throws Exception {
		mvc.perform(get("/couponapi/coupons/testCode")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateCoupon_WithoutCSRF() throws Exception{
		mvc.perform(post("/couponapi/coupons").content("{\n" +
				"    \"code\":\"testCode11\",\n" +
				"    \"discount\":60,\n" +
				"    \"expDate\":\"25/12/2021\"\n" +
				"}").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateCoupon_WithCSRF() throws Exception{
		mvc.perform(post("/couponapi/coupons").content("{\n" +
				"    \"code\":\"testCode12\",\n" +
				"    \"discount\":60,\n" +
				"    \"expDate\":\"25/12/2021\"\n" +
				"}").contentType(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"USER"})
	void testCORS() throws Exception {
		mvc.perform(options("/couponapi/coupons")
				.header("Access-Control-Request-Method","POST")
				.header("Origin","http://www.test.com"))
				.andExpect(status().isOk())
				.andExpect(header().exists("Access-Control-Allow-Origin"))
				.andExpect(header().string("Access-Control-Allow-Origin","*"))
				.andExpect(header().exists("Access-Control-Allow-Methods"))
				.andExpect(header().string("Access-Control-Allow-Methods","POST"));
	}


}
