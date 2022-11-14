package gr.uaegean.palaemondbproxy;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesTests {


    @Test
    public void testParsingDate(){
        String inputDate = "2022-11-14T12:54:01.564Z";
        SimpleDateFormat sdfu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date udate = null;
        try {
            udate = sdfu.parse(inputDate);
            System.out.println(udate.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
