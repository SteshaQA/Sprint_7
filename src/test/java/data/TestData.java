package data;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class TestData {

    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    public static Faker user = new Faker();
    public static final String LOGIN = user.name().lastName() + System.currentTimeMillis();
    public static final String PASSWORD = user.regexify("[0-9]{4}");
    public static final String FIRSTNAME = user.name().firstName();

    public static final String LASTNAME = user.name().lastName();
    public static final String ADDRESS = user.address().fullAddress();
    public static final String METROSTATION = user.address().streetName();
    public static final String PHONE = user.phoneNumber().phoneNumber();
    public static final int RENTTIME = user.random().nextInt(1, 100);

    static SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
    public static final String DELIVERYDATE = dbFormatter.format(user.date().future(30, TimeUnit.DAYS));

    //public static final String DELIVERYDATE = user.date().future(1, TimeUnit.DAYS).toString();

    public static final String COMMENT = user.lorem().sentences(3).toString();
    public static final String[] COLOR = getColor();

    public static final String[] getColor(){
        int colorsCount = user.random().nextInt(1, 7);
        String[] color = new String[colorsCount];
        for (int i = 0; i < color.length; i++) {
            color[i] = user.color().name();
        }
        return color;
    }
}
