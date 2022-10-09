/**
 * 
 */
package com.pusiknas.jasper.print;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * @author rudi_
 * Mar 18, 2022
 */
public class JasperService {
	
	@Autowired
	private DataSource datasource;
	
	public void writePdf(){

		try {
			String dbURL = "jdbc:oracle:thin:eka/eka@128.21.23.31:1521:ajsdb";
			Connection conn = DriverManager.getConnection(dbURL);
//			Connection conn = datasource.getConnection();

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("name", "a");
			params.put("start_date", "09/09/2022");
			params.put("end_date", "10/10/2022");
			
	    	InputStream reportCampaign = getClass()
					.getResourceAsStream("/static/sinarmas/report_campaign.jrxml");
			
			JasperDesign jasperDesign = JRXmlLoader.load(reportCampaign);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);	
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
//			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/Work/sinarmas/reporting/report_campaign.pdf");
			
			JRXlsxExporter exporter = new JRXlsxExporter(); // initialize exporter 
		    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/Work/sinarmas/reporting/report_campaign.xlsx"));
		    exporter.exportReport();
		    
		} catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	    }
	}
	
	private String savePdf(JRBeanCollectionDataSource dataSource, HashMap<String, Object> params,
		      InputStream employeeReportStream) {
		    String ret="";
		    try {
		      JasperReport jr = JasperCompileManager.compileReport(employeeReportStream);
		        JasperPrint jrPrint = JasperFillManager.fillReport(jr, params, dataSource);
//		        ret = pathFile+fileName+"-"+time+".pdf";
//		        JasperExportManager.exportReportToPdfFile(jrPrint, ret);
		        System.out.println("export to byte...");
		        byte[] pdf = JasperExportManager.exportReportToPdf(jrPrint);
		        return Base64.getEncoder().encodeToString(pdf);
		    } catch (Exception e) {
		      return "Failed" + e;
		    }
		  }
		  
		  private String saveExcel(JRBeanCollectionDataSource dataSource, HashMap<String, Object> params,
		      InputStream employeeReportStream) {
		    String ret="";
		    try {
		      JasperReport jr = JasperCompileManager.compileReport(employeeReportStream);
		      JasperPrint jrPrint = JasperFillManager.fillReport(jr, params, dataSource);
		      JRXlsxExporter exporter = new JRXlsxExporter(); // initialize exporter 
		      exporter.setExporterInput(new SimpleExporterInput(jrPrint)); // set compiled report as input
//		      ret = pathFile+fileName+"-"+time+".xlsx";
//		        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ret));
//		        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
//		        configuration.setOnePagePerSheet(true); // setup configuration
//		        configuration.setDetectCellType(true);
//		        exporter.setConfiguration(configuration); // set configuration
		      
		      	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/Work/sinarmas/reporting/report_campaign.xlsx"));
		        exporter.exportReport();
		        return ret;
		    } catch (Exception e) {
		      return "Failed";
		    }
		  }



	
//	public void writePdf(){
//
//		try {
//			
////			String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public";
////			Properties props = new Properties();
////			props.setProperty("user","postgres");
////			props.setProperty("password","admin");
//////			props.setProperty("ssl","true");
////			Connection conn = DriverManager.getConnection(url, props);
//			
//			String dbURL = "jdbc:sqlserver://10.101.100.134;databaseName=datamart_kriminalitas_stagging";
//			Properties properties = new Properties();
//			properties.put("user", "ishaq");
//			properties.put("password", "ishaq123");
//			Connection conn = DriverManager.getConnection(dbURL, properties);
//			
//			HashMap<String, Object> params = new HashMap<String, Object>();
//			
//	    	String pathCofidental = getClass().getResource("/static/confidental.png").getPath();
//	    	String pathHeader = getClass().getResource("/static/header_dokumen.png").getPath();
//	    	String pathWatermark = getClass().getResource("/static/watermark.png").getPath();
//	    	String pathImgFoto = getClass().getResource("/static/foto.png").getPath();
//
//	    	params.put("confidental", pathCofidental);
//	    	params.put("header", pathHeader);
//	    	params.put("watermark", pathWatermark);
//	    	params.put("foto", pathImgFoto);
//	    	params.put("id_person", null);
//	    
//	    	
//	    	
//	    	//terlapor
//	    	InputStream subreportTerlapor = getClass().getResourceAsStream("/static/lengkap/terlapor/subreport_terlapor.jrxml");
//	    	JasperDesign subreportTerlaporDesign = JRXmlLoader.load(subreportTerlapor);
//			JasperReport subreportTerlaporReport = JasperCompileManager.compileReport(subreportTerlaporDesign);	
//	    	params.put("subreportTerlapor", subreportTerlaporReport);
//	    	
//	    	//tersangka
//	    	InputStream subreportTersangka = getClass().getResourceAsStream("/static/lengkap/tersangka/subreport_tersangka.jrxml");
//	    	JasperDesign subreportTersangkaDesign = JRXmlLoader.load(subreportTersangka);
//			JasperReport subreportTersangkaReport = JasperCompileManager.compileReport(subreportTersangkaDesign);	
//	    	params.put("subreportTersangka", subreportTersangkaReport);
//	    	
//	    	//napi
//	    	InputStream subreportNarapidana = getClass().getResourceAsStream("/static/lengkap/napi/subreport_napi_aktif.jrxml");
//	    	JasperDesign subreportNarapidanaDesign = JRXmlLoader.load(subreportNarapidana);
//			JasperReport subreportNarapidanaReport = JasperCompileManager.compileReport(subreportNarapidanaDesign);	
//	    	params.put("subreportNapiAktif", subreportNarapidanaReport);
//	    	
//	    	InputStream subreportNapiAsimilasi = getClass().getResourceAsStream("/static/lengkap/napi/subreport_napi_asimilasi.jrxml");
//	    	JasperDesign subreportNapiAsimilasiDesign = JRXmlLoader.load(subreportNapiAsimilasi);
//			JasperReport subreportNapiAsimilasiReport = JasperCompileManager.compileReport(subreportNapiAsimilasiDesign);	
//	    	params.put("subreportNapiAsimilasi", subreportNapiAsimilasiReport);
//	    	
//	    	InputStream subreportMantanNapi = getClass().getResourceAsStream("/static/lengkap/napi/subreport_mantan_napi.jrxml");
//	    	JasperDesign subreportMantanNapiDesign = JRXmlLoader.load(subreportMantanNapi);
//			JasperReport subreportMantanNapiReport = JasperCompileManager.compileReport(subreportMantanNapiDesign);	
//	    	params.put("subreportMantanNapi", subreportMantanNapiReport);
//	    	
//	    	//lantas
//	    	InputStream subreportLantas = getClass().getResourceAsStream("/static/lengkap/lantas/subreport_lantas.jrxml");
//	    	JasperDesign subreportLantasDesign = JRXmlLoader.load(subreportLantas);
//			JasperReport subreportLantasReport = JasperCompileManager.compileReport(subreportLantasDesign);	
//	    	params.put("subreportLantas", subreportLantasReport);
//	    	
//	    	/* CAKRIM LENGKAP UMUM */
//	    	
////			InputStream employeeReportStream = getClass()
////					.getResourceAsStream("/static/lengkap/report_cakrim_lengkap_tanpa_riwayat.jrxml");
////			InputStream employeeReportStream = getClass()
////					.getResourceAsStream("/static/lengkap/report_cakrim_lengkap_dengan_riwayat.jrxml");
//			
//	
//			/* CAKRIM LENGKAP INVESTIGATIF */
//	    	
////			InputStream employeeReportStream = getClass()
////					.getResourceAsStream("/static/investigatif/report_cakrim_investigatif_tanpa_riwayat.jrxml");
//	    	InputStream employeeReportStream = getClass()
//					.getResourceAsStream("/static/investigatif/report_cakrim_investigatif_tanpa_riwayat.jrxml");
//			
//			JasperDesign jasperDesign = JRXmlLoader.load(employeeReportStream);
//			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);			
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
//			JasperExportManager.exportReportToPdfFile(jasperPrint, "D:/Work/MTJ/report/cakrim.pdf");
//			
//		} catch (Exception e) {
//	        // TODO: handle exception
//	        e.printStackTrace();
//	    }
//	}


	
	
}
