package com.cookandroid.client;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class fileText {
    final static  String filename = "/login.txt";
    String getFileName ="/data/data/com.cookandroid.client/files/login";

    public void TextWriter(String string){
       File dir = new File(getFileName);
       if(!dir.exists()){
           dir.mkdir();
       }
       File file = new File(dir, filename);
       try {
           FileWriter writer = new FileWriter(dir  + filename,true);
           BufferedWriter bufferedWriter = new BufferedWriter(writer);
           bufferedWriter.append(string+"-");
           bufferedWriter.newLine();
           bufferedWriter.close();
       } catch (IOException ioException) {
           ioException.printStackTrace();
           Log.d("qq", "에러");
       }

    }

    public StringBuffer TextRead() throws IOException {
        File read = new File("/data/data/com.cookandroid.client/files/login/login.txt");
        FileReader fileReader = new FileReader(read);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String s="";
        StringBuffer strings = new StringBuffer();
        while((s=bufferedReader.readLine())!=null){
            strings.append(s);
        }
        return strings;
    }

    public void fileDelete(){
        File file = new File("/data/data/com.cookandroid.client/files/login/login.txt");
        if(file.exists()){
            file.delete();
        }
    }
}
