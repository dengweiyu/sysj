package com.li.videoapplication.data.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/**
 * 功能：封装上传文件的信息
 */
public class FormFile {
	
	/*要上传的字节*/
    private byte[] data;
    
    /*要上传的文件的输入流*/
    private InputStream inputStream;
    
    /*要上传的文件*/
    private File file;
    
    /*文件名*/
    private String fileName;
    
    /*参数名*/
    private String parameterName;
    
    /*HTTP Content-Type(Mime-Type)*/
    private String contentType = "application/octet-stream";
    
    public FormFile(String fileName, byte[] data, String parameterName, String contentType) {
        this.data = data;
        this.fileName = fileName;
        this.parameterName = parameterName;
        if(contentType!=null) 
        	this.contentType = contentType;
    }
    
    public FormFile(String fileName, File file, String parameterName, String contentType) {
        this.fileName = fileName;
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(contentType!=null) 
        	this.contentType = contentType;
    }
    
    public File getFile() {
        return file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }

	public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}