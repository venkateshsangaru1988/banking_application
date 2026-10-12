package com.bank.application.notifications;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bank.application.constants.ApplicationConstants;
import com.bank.application.entity.notifications.SmsEntity;
import com.bank.application.repository.SmsRepository;

@Component
public class SmsNotifications {

	private final SmsRepository smsRepository;

	SmsNotifications(SmsRepository smsRepository) {
		this.smsRepository = smsRepository;
	}

	public String sendSms(SmsRequestDto requestObj) {
		String smsContent = "Subject :" + requestObj.getSubject() + " Body : " + requestObj.getBody()
				+ " and mobile no : " + requestObj.getMobileNo();
		System.out.println("----SMS----" + smsContent);
		// External API If Status is SUCCESS / FAIL
		requestObj.setSmsStatus(ApplicationConstants.SUCCESS);
		saveSms(requestObj);
		return smsContent;
	}

	public void saveSms(SmsRequestDto requestObj) {

		SmsEntity smsObj = new SmsEntity();
		BeanUtils.copyProperties(requestObj, smsObj);
		smsRepository.save(smsObj);

	}

}
