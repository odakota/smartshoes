package com.odakota.tms.system.service.sns;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class SmsService {

    //private final AmazonSNS amazonSNS;

    public static final String ACCOUNT_SID = "AC3176795b9377b110ba81995f9216dd9b";
    public static final String AUTH_TOKEN = "acf67ce18c2d26a33a127f33c9009dc1";

    public void sendSMSMessage(String message, String phoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        if (phoneNumber.startsWith("0")){
            phoneNumber = phoneNumber.replaceFirst("0", "+84");
        }

        Message mss = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber("+14086764546"), message).create();

        System.out.println(mss.getSid());
//        Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
//        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
//                .withStringValue("0.5") //Sets the max price to 0.50 USD.
//                .withDataType("Number"));
//        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
//                .withStringValue("Promotional") //Sets the type to promotional.
//                .withDataType("String"));
//        PublishResult result =amazonSNS.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
//                                                                    .withMessageAttributes(smsAttributes));
//        System.out.println(result);
    }
}
