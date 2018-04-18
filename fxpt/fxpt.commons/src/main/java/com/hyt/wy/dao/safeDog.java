package com.hyt.wy.dao;

/**
*
* <p>Title: 加密狗读写类</p>
*
* <p>Description: </p>
*
* <p>Copyright: Copyright (c) 2009</p>
*
* <p>Company: </p>
*
* @author not attributable
* @version 1.0
*/
public class safeDog{

 private native int add(int x,int y);
 private native String readDog(String pwd);
 private native int writeDog(String data);


 /**
  *
  * @return int 0=无限制;<0 =检查失败;>0 =限制数目
  */
 public static int checkDog(){
     int baseSize=25;
     int scanSize=10;
     int pingjuanSize=10;
     safeDog dog = new safeDog();
     String data=dog.readDog("sea");
     int ret = -1;
     System.out.println("["+data+"]");
     if(data==null || "".equalsIgnoreCase(data)){
         System.out.println("safe dog check fail!pls check!");
         return -2;
     }
     if(data!=null){
         try{
             String baseData = data.substring(0, baseSize);
             //System.out.println("baseData:"+baseData+"  pingjuanStr:"+pingjuanStr);

             if ("SeaSkyLight:CEPINGLIMITED".equalsIgnoreCase(baseData)) {
                 System.out.println("safe dog check pass!continue...");
                 ret = 0;
             } else {
                 System.out.println("safe dog check fail!pls check!");
                 ret = -2;
             }
         }catch(Exception e){

         }
          try{
              if(data.length()<baseSize + scanSize +pingjuanSize){

              }else{
                  String pingjuanStr = data.substring(baseSize + scanSize,
                                                      baseSize + scanSize +
                                                      pingjuanSize);
                  String studentLimitStr = pingjuanStr.substring(0, 4);
                  int limitBase = Integer.parseInt(studentLimitStr);
                  if (limitBase == 0) {
                      ret = 0;
                  } else {
                      //System.out.println("pingjuanStr:"+pingjuanStr);
                      studentLimitStr = pingjuanStr.substring(4, 5);
                      int x = Integer.parseInt(studentLimitStr);
                      if (x == 0) {
                          ret = limitBase;
                      }
                      if (x == 1) {
                          ret = limitBase * 10;
                      }
                      if (x == 2) {
                          ret = limitBase * 100;
                      }
                      if (x == 3) {
                          ret = limitBase * 1000;
                      }
                      if (x == 4) {
                          ret = limitBase * 10000;
                      }
                      if (x == 5) {
                          ret = limitBase * 100000;
                      }
                      if (x == 6) {
                          ret = limitBase * 1000000;
                      }
                      if (x == 7) {
                          ret = limitBase * 10000000;
                      }
                  }
              }
         }catch(Exception e){

         }
     }
     System.out.println("stuLimit:"+ret);
     return ret;
 }

 public static void main(String[] args) {
     safeDog hh = new safeDog();
     System.out.println("java result");
     String data=hh.readDog("sea");

     System.out.println("package result = "+data);
     checkDog();


   /*

     int data=readDog("sea");
     System.out.println("package result = "+data);
     return;


     safeDog hh = new safeDog();
   int r = hh.add(30,20);
   System.out.println("result = "+r);
   */
 }

 static {
   System.loadLibrary("libDog");
 }
}
