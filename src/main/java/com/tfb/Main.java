package com.tfb;


import java.io.*;


public class Main {
    public static void main(String[] args){
        try{
            FileInputStream fis = new FileInputStream("src/main/resources/fileInput.txt");
            FileOutputStream fos = new FileOutputStream("src/main/resources/fileOutput.txt");
            int data;
            while((data = fis.read()) != -1){
                fos.write(data);
            }
            fis.close();
            fos.close();
        }catch (IOException e){
            throw new RuntimeException("Will find a better way to handle this");
        }
    }
}

