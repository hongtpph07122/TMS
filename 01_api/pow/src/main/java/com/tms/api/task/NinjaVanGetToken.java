package com.tms.api.task;

import com.tms.api.entity.LogToken;
import com.tms.api.helper.Const;
import com.tms.api.service.LogTokenService;
import com.tms.ff.dto.request.NinjaVan.NinjaVanToken;
import com.tms.ff.dto.response.NinjaVan.NinjaVanTokenResponse;
import com.tms.ff.service.NinjaVan.impl.NinjaVanOrderServiceImpl;
import com.tms.ff.utils.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NinjaVanGetToken {
	private final Logger logger = LoggerFactory.getLogger(NinjaVanGetToken.class);

	public static String NinjaVan_TOKEN = null;

	@Value("${config.run-get-token-ninjaVan}")
	public Boolean isRunGetTokenNinjaVan;

	@Autowired
	LogTokenService logTokenSevice;

	@Scheduled(initialDelay = 5000, fixedDelay = 1000 * 60 * 60 * 12)
	public String getTokenNinjaVan(){
		logger.info("getTokenNinjaVan --------------- {}", new Date());
		if(!isRunGetTokenNinjaVan) {
			return null;
		}
		NinjaVanOrderServiceImpl service = new NinjaVanOrderServiceImpl(null);
		NinjaVanToken loginRequestDto = new NinjaVanToken();
		loginRequestDto.setClient_id(AppProperties.getPropertyValue("api.NinjaVan.client_id"));
		loginRequestDto.setClient_secret(AppProperties.getPropertyValue("api.NinjaVan.client_secret"));
		loginRequestDto.setGrant_type("client_credentials");
		NinjaVanTokenResponse loginResponse = service.getToken(loginRequestDto);
		if (loginResponse.getAccess_token() != null && loginResponse.getAccess_token().length() > 0) {

			LogToken logToken = new LogToken();

			logToken.setPartnerId(String.valueOf(Const.NinjaVan_PARTNER_ID));
			logToken.setToken(loginResponse.getAccess_token());
			logToken = logTokenSevice.createLogToken(logToken);

			NinjaVan_TOKEN = logToken.getToken();
//			Integer expire = Integer.valueOf(String.valueOf(loginResponse.getExpires_in()));
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.add(Calendar.SECOND, expire);
//			NinjaVan_EXPIRE_TOKEN = calendar.getTime();
			return loginResponse.getAccess_token();
		}
		return null;
	}

	public String getTokenToCreateOrder() {
		return NinjaVan_TOKEN;
	}
}
