package com.pablo.imagescaler;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.pablo.imagescaler.processors.FileScaleProcessor;

public class Main {
	
	public static void main(String[] args) throws Exception {
		String folder;
		if(args.length == 1){
			folder = args[0];
		}else{
			folder = "/imagescaler";
		}

		execute(folder);
	}

	private static void execute(String baseFolder) throws Exception {
		String baseFolderProto = "file://" + baseFolder;
		final String fromFolder = baseFolderProto + "/input";
		final String toFolder = baseFolderProto + "/output";
		final String copyFolder = fromFolder + "/.copy";
		final String errorFolder = fromFolder + "/.error";

		CamelContext context = new DefaultCamelContext();
		
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				onException(Exception.class)
					.handled(true)
					.to(errorFolder);
				
				from(fromFolder + "?recursive=true&delete=true&delay=5000")
					.multicast().to(copyFolder,"direct:scale");
				
				from("direct:scale")
					.process(new FileScaleProcessor())
					.to(toFolder);
			}
		});
		
		context.start();
		while(true);
	}
}
