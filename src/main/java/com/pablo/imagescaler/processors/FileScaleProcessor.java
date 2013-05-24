package com.pablo.imagescaler.processors;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConverter;
import org.imgscalr.Scalr;

public class FileScaleProcessor implements Processor {
	public FileScaleProcessor() {
		super();
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Object obj = exchange.getIn().getBody();
		@SuppressWarnings("unchecked")
		GenericFile<File> inFile = (GenericFile<File>) obj;

		InputStream is = null;
		ByteArrayOutputStream baos = null;

		try {
			is = GenericFileConverter
					.genericFileToInputStream(inFile, exchange);
			baos = new ByteArrayOutputStream();
			scale(is, 1024, baos, "jpeg");
			
		} finally {
			if (is != null)
				is.close();
			if (baos != null)
				baos.close();
		}

		exchange.getIn().setBody(baos);
	}

	private void scale(InputStream iStreamImage, int maxHeightWidth, OutputStream oStreamImage, String format) throws Exception {
		BufferedImage imageBuffer = ImageIO.read(iStreamImage);
		BufferedImage imageOutput = Scalr.resize(imageBuffer, maxHeightWidth);
		
		ImageIO.write(imageOutput, format, oStreamImage);
	}

}
