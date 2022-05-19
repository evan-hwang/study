package datetime;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateDemoTest {

    @Test
    public void createNow() {
        LocalDate now = LocalDate.now();
        System.out.println("now = " + now);
    }
}
