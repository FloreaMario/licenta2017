package com.example.flore.myapplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by flore on 4/18/2017.
 */

public class Vocabulary {
    char stringTable[] = {'a','b','c','d','e','f','g','h','i','j'
            ,'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    int freqTable[] ={18000,18100,18200,18300,18400,18500,18600,18700,
            18800,18900,19200,19300,19400,19500,19600,19700,19800,19900,
            20000,20100,20200,20300,20400,20500,20600,20700};
    /*
   	a = 18000
    b = 18100       o = 19600
    c = 18200	    p = 19700
    d = 18300	    q = 19800
    e = 18400	    r = 19800
    f = 18500	    s = 20000
    g = 18600	    t = 20100
    h = 18700	    u = 20200
    i = 18800	    v = 20300
    j = 18900	    w = 20400
    k = 19200	    x = 20500
    l = 19300	    y = 20600
    m = 19400	    z = 20700
    n = 19500

     */
    public char[] concat(char[] a, char[] b) {
        int aLen = a.length;
        int bLen = b.length;
        char[] c= new char[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static boolean isUnique(char[] array, int num) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == num) {
                return false;
            }
        }
        return true;
    }

    public static char[] toUniqueArray(char[] array) {
        char[] temp = new char[array.length];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = ' '; // in case u have value of 0 in he array
        }
        int counter = 0;

        for (int i = 0; i < array.length; i++) {
            if (isUnique(temp, array[i]))
                temp[counter++] = array[i];
        }
        char[] uniqueArray = new char[counter];

        System.arraycopy(temp, 0, uniqueArray, 0, uniqueArray.length);

        return uniqueArray;
    }

    public char[] commonElem(char[] array1, char[] array2) {
        char[] arrayFinal = new char[100];
        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array2.length; j++) {
                if (array1[i] == array2[j]) {
                    arrayFinal[i] = array1[i];
                }
            }

        }
        return arrayFinal;
    }

    public char[] masterConvert(double assid1[], double assid2[])
    {
        char[] myChar1 = new char[100];
        char[] myChar2 = new char[100];

        char[] myCharFinal = new char[100];

        myChar1 = vocabularyConvert(assid1);
        myChar2 = vocabularyConvert(assid2);

        myCharFinal = commonElem(myChar1, myChar2);



        return myCharFinal;
    }
    public boolean checkSize(double[] assid1, double[] assid2)
    {
        int size1 =0;
        int size2 =0;
        int i = 0;
        boolean boResult = false;
        while(assid1[i]!=0)
        {
            i++;
            size1++;
        }
        i=0;
        while(assid2[i]!=0)
        {
            i++;
            size2++;
        }
        if(size1 == size2)
        {
            boResult = true;
        }
        else
        {
            boResult = false;
        }

        return boResult;
    }

    public char[] vocabularyConvert(double assid[])
    {
        double[] intPart = new double[100];
        char[] myChar = new char[100];
        for(int j = 0; j < assid.length; j++)
        {
            double fractPart = assid[j] % 100;
            if(fractPart == 0)
            {
                intPart[j] = assid[j];
            }
            if(fractPart > 0)
            {
                intPart[j] = (assid[j]-fractPart)+100;
            }/*
            if(fractPart < 50)
            {
                intPart[j] = (assid[j]-fractPart);
            }*/

            for(int i = 0; i < freqTable.length; i++)
            {
                if(intPart[j] == freqTable[i])
                {
                    myChar[j] = stringTable[i];
                }
            }
        }
        return myChar;
    }
}
