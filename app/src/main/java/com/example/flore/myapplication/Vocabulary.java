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

    public char[] masterConvert(double assid1[], double assid2[],double assid3[])
         {
             char[] myChar1 = new char[100];
             char[] myChar2 = new char[100];
             char[] myChar3 = new char[100];

             char[] myCharFirst = new char[100];
             char[] myCharSecond = new char[100];
             char[] myCharFinal = new char[100];

             char[] myLongestChar = new char[100];
             char[] mySecLongChar = new char[100];
             char[] myThirdLongChar = new char[100];

             myChar1 = vocabularyConvert(assid1);
             myChar2 = vocabularyConvert(assid2);
             myChar3 = vocabularyConvert(assid3);

        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        for(double i : assid1)
            sum1 +=i;
        for(double i : assid2)
            sum2 +=i;
        for(double i : assid3)
            sum3 +=i;
        if((sum1> sum2) &&(sum1> sum3))
        {
            myLongestChar = myChar1;
            if(sum2 > sum3)
            {
                mySecLongChar = myChar2;
                myThirdLongChar = myChar3;
            }
            else
            {
                mySecLongChar = myChar3;
                myThirdLongChar = myChar2;
            }
        }
        if((sum2 > sum1) &&(sum2> sum3))
        {
            myLongestChar = myChar2;
            if(sum1 > sum3)
            {
                mySecLongChar = myChar1;
                myThirdLongChar = myChar3;
            }
            else
            {
                mySecLongChar = myChar3;
                myThirdLongChar = myChar1;
            }
        }
        if((sum3 > sum1) &&(sum3 > sum2))
        {
            myLongestChar = myChar3;
            if(sum1 >sum2)
            {
                mySecLongChar = myChar1;
                myThirdLongChar = myChar2;
            }
            else
            {
                mySecLongChar = myChar2;
                myThirdLongChar = myChar1;
            }
        }

             char[] char31 = new char[100];
             char[] char32 = new char[100];
             char[] char13 = new char[100];
             char[] char12 = new char[100];
             char[] char21 = new char[100];
             char[] char23 = new char[100];


             char31 = commonElem(myThirdLongChar, myLongestChar);
             char32 = commonElem(myThirdLongChar, mySecLongChar);

             char13 = commonElem(myLongestChar, myThirdLongChar);
             char12 = commonElem(myLongestChar, mySecLongChar);

             char23 = commonElem(mySecLongChar, myThirdLongChar);
             char21 = commonElem(mySecLongChar, myLongestChar);

             int size1 =0;
             int size2 =0;
             int size3 =0;
             int i = 0;
             while(myChar1[i]!=0)
             {
                 i++;
                 size1++;
             }
             i=0;
             while(myChar2[i]!=0)
             {
                 i++;
                 size2++;
             }
             i=0;
             while(myChar3[i]!=0)
             {
                 i++;
                 size3++;
             }
             char[] mycharFinal1 = new char[100];
             char[] mycharFinal2 = new char[100];
             char[] mycharFinal3 = new char[100];

             if(size1 == size2)
             {
                 mycharFinal1 = commonElem(myChar1, myChar2);
             }
             if(size2 == size3)
             {
                 mycharFinal2 = commonElem(myChar2, myChar3);
             }
             if(size1 == size3)
             {
                 mycharFinal3 = commonElem(myChar1, myChar3);
             }

             /*
        myCharFirst = concat(myChar1, myChar2);
        myCharSecond = concat(myCharFirst, otherChar2);
        myCharFinal = toUniqueArray(myCharSecond);
        */

             return myCharFinal;
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
