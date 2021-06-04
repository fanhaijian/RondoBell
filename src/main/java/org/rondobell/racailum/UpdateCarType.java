package org.rondobell.racailum;

import org.apache.ibatis.session.SqlSession;
import org.rondobell.racailum.base.dao.MzMapper;
import org.rondobell.racailum.base.dao.SqlSessionFactoryHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;

public class UpdateCarType {
    public static void main(String[] args) {
        String filePath = "/usr/local/task/car_type_valid.txt";
        //String filePath = "C:\\Users\\fanhj\\Desktop\\car_type\\car_type_valid.txt";

        if (args != null && args.length > 0)
            filePath = args[0];
        //System.out.println(filePath);

        File file = null;
        FileReader fr = null;
        BufferedReader br = null;
        long start = System.currentTimeMillis();
        try {
            file = new File(filePath);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String tmp = null;
            int all = 0;
            int num = 0;
            SqlSession session = null;
            while ((tmp = br.readLine()) != null) {
                all++;

                String[] info = tmp.split("\\t");
                if (info.length == 2) {
                    String deviceId = info[0];
                    String carType = info[1];
                    if(isNumeric(carType)){
                        num++;
                        continue;
                    }
                    session = SqlSessionFactoryHolder.getSession();
                    MzMapper mapper = session.getMapper(MzMapper.class);
                    int count = mapper.updateCarType(carType, deviceId);
                    session.close();
                    Thread.sleep(40);
                    System.out.println(all+"\tcar_type=" + carType + "\tdevice_id=" + deviceId);
                }else {
                    System.out.println(all+"\terr split " + tmp);
                }
            }
            System.out.println("finish " + (System.currentTimeMillis() - start));
            System.out.println(all);
            System.out.println(num);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }
}
