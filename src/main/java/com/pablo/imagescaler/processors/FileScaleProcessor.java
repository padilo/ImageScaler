package com.pablo.imagescaler.processors;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConverter;

public class FileScaleProcessor implements Processor {
	public FileScaleProcessor() {
		super();
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Object obj = exchange.getIn().getBody();
		@SuppressWarnings("unchecked")
		GenericFile<File> inFile = (GenericFile<File>)obj;

		InputStream is = null;
		ByteArrayOutputStream baos = null;

		try{
			is = GenericFileConverter.genericFileToInputStream(inFile, exchange);
			baos = new ByteArrayOutputStream();
			scale(is, 1024, baos, extractFormat(inFile.getFile().getName()));
		}finally{
			if(is != null) is.close();
			if(baos != null) baos.close();
		}

		exchange.getIn().setBody(baos);
	}


	private String extractFormat(String name) throws Exception {
		int index = name.lastIndexOf('.');
		
		if(index == -1){
			throw new Exception("file extension not found");
		}

		return name.substring(index+1);
	}


	private OutputStream scale(InputStream iStreamImage, int maxHeightWidth, OutputStream oStreamImage, String format) throws Exception {
		InputStream imageStream = new BufferedInputStream(iStreamImage);
		BufferedImage imageBuffer = ImageIO.read(imageStream);
		int width = imageBuffer.getWidth();
		int height = imageBuffer.getHeight();
		double ratio = width / (double)height;
		int targetWidth, targetHeight;

		if(width > height){
			targetWidth = maxHeightWidth;
			targetHeight = (int)(height * ratio);
		}else{
			targetHeight = maxHeightWidth;
			targetWidth = (int)(height / ratio);
		}
		
		Thumbnails.of(imageBuffer)
		        .size(targetWidth,targetHeight)
		        .outputFormat(format)
		        .outputQuality(0.85f)
		        .toOutputStream(oStreamImage);

		return oStreamImage;       
	}

}
