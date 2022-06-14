package com.pusiknas.jasper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pusiknas.jasper.print.JasperService;

import net.sf.jasperreports.engine.JasperPrint;

@SpringBootApplication
public class JasperApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasperApplication.class, args);
		JasperService js = new JasperService();
		js.writePdf();
	}

}
