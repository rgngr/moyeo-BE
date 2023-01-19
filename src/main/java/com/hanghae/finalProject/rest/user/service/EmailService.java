package com.hanghae.finalProject.rest.user.service;

//import com.hanghae.finalProject.rest.user.dto.EmailCertificationResponse;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.config.util.RedisUtil;
import com.hanghae.finalProject.rest.user.model.Email;
import com.hanghae.finalProject.rest.user.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@PropertySource ("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.mail.username}")
    private String id;


//    private final PasswordEncoder passwordencode;//



    public void emailCreate(String email) throws MessagingException, UnsupportedEncodingException {
        //인증번호 생성
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        String ePw = key.toString();
        ///////////////////////////
        log.info("보내는 대상 : " + email);
        log.info("인증 번호 : " + ePw);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email); // to 보내는 대상
        message.setSubject("ㅇㅇㅇ 회원가입 인증 코드: "); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id, "모여")); //보내는 사람의 메일 주소, 보내는 사람 이름

        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        Email emails = new Email(email, ePw);
        emailRepository.save(emails);
    }

    public void emailConfirm(String email,String ePw) {
        //받아온 이메일이 있는지 확인(Email)
        //있다면 코드진행
        //없으면 RestApiException
        //확인후 받아온 이메일의 인증코드를 대조
        //맞다면 true를 발송(boolean)
        //틀리다면 fluse를 발송(boolean)
        Email selectEmail= emailRepository.findById(email).orElseThrow(
                () -> new RestApiException(Code.EMAIL_CONFIRM_BAD)
        );

        if(!selectEmail.getEPw().equals(ePw)){
            throw new RestApiException(Code.EMAIL_CONFIRM_CODE_BAD);
        }
    }
    
    private RedisUtil getSpringProxy() {
        return applicationContext.getBean(RedisUtil.class);
    }
}