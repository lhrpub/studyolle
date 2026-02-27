package com.studyolle;

import com.studyolle.mail.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyolleApplicationTests {

    @Mock
    private EmailService emailService;

    @Test
    void contextLoads() {
    }

}
