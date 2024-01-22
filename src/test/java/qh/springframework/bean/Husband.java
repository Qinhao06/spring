package qh.springframework.bean;

import qh.springframework.factory.annotation.core.Component;
import qh.springframework.factory.annotation.core.Qualifier;
import qh.springframework.factory.annotation.core.Value;

import java.time.LocalDate;
import java.util.Date;

@Component
public class Husband {

    @Value("你猜")
    private String wifiName;

    @Value("2018-01-01")
    private LocalDate marriageDate;

    @Value("100")
    private Long age;

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    @Override
    public String toString() {
        return "Husband{" +
                "wifiName='" + wifiName + '\'' +
                ", marriageDate=" + marriageDate +
                ", age=" + age +
                '}';
    }
}
