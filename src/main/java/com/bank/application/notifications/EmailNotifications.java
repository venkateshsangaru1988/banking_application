package com.bank.application.notifications;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bank.application.entity.notifications.EmailEntity;
import com.bank.application.repository.EmailRepository;

@Component
public class EmailNotifications {

	private final EmailRepository emailRepository;

	EmailNotifications(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}

	public String sendEmail(EmailRequestDto requestObj) {
		String emailContent = "Subject :" + requestObj.getSubject() + " Body : " + requestObj.getBody()
				+ " and mobile no : " + requestObj.getEmail();
		System.out.println("----EMAIL----" + emailContent);

		saveEmail(requestObj);
		return emailContent;
	}

	public void saveEmail(EmailRequestDto requestObj) {

		EmailEntity emailObj = new EmailEntity();
		BeanUtils.copyProperties(requestObj, emailObj);
		emailRepository.save(emailObj);
	}
}
