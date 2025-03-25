package com.example.baozi_list.util;


import com.example.baozi_list.bean.Car;
import com.example.baozi_list.bean.Disinfection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WxScanUtil {
    public static List<Car> parseWxString(String string){
        Scanner sc = new Scanner(string);
        List<Car> cars = new ArrayList<>();

        String[] strings;
        while(sc.hasNextLine()){
            string = sc.nextLine();
            string = string.trim();
            if(string.isEmpty())
                break;
            strings = string.split("[\\s\\t]+");
            if(strings.length ==4){
                if(strings[3].length()<=2){
                    String t1=strings[0];
                    String t2=strings[1];
                    String t3=strings[2];
                    String t4=strings[3];

                    strings=new String[]{t1,t2,t3,"",t4};

                }
            }
            Car car = new Car(0,strings[1],strings[0],strings[2],strings[3],strings[4],TimeUtilKt.getDate(),false,false,false,false,false);
            cars.add(car);
        }
        cars.remove(0);
        Collections.reverse(cars);
        return cars;
    }


    public static List<Disinfection> parseDisinfectionString(String s){
        List<String> list = Arrays.stream(s.split("\n")).map(String::trim).collect(Collectors.toList());
        if(list.get(0).equals("消毒")){
            list.remove(0);
        }
        List<Disinfection> l=new ArrayList<>();
        for (String string : list) {
            l.add(new Disinfection(0,TimeUtilKt.getDate(),string,false,false,false));
        }
        return l;
    }

}
