package org.dieschnittstelle.mobile.android.skeleton.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverters {

    public static String makeStringFromListOfStrings(List<String> stringList){

        return stringList.stream().collect(Collectors.joining(";"));
    }

    public static  List<String> makeListOfStringsFromString(String string){
        if (string == null) {
            return new ArrayList<>();
        }
        return  new ArrayList<>();
        //return Arrays.stream(string.split(";")).collect(Collectors.joining(Collectors.toList()));
    }
}
