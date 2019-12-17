
package project;

import java.io.*;	 
import java.net.*;
import java.nio.charset.*;

import javax.swing.JDialog;

import com.google.gson.*;


public class RssCroller  {
	public static String topVal; //도/시
	public static String mdlVal; //시/군/구
	public static String leafVal; //읍/면/동
	public static String gx, gy; //동네좌표
	
	
	public static boolean reset(String topval, String mdlval, String leafval) {
		topVal = topval;
		mdlVal = mdlval;
		leafVal = leafval;
		
		try {
			String topCode = returnTopCode(readJsonFromUrl("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt"));
			String mdlCode = returnMdlCode(readJsonFromUrl("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl." + topCode + ".json.txt"));
			String leafCode = returnLeafCode(readJsonFromUrl("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf." + mdlCode + ".json.txt"));
		
			if(topCode == null || mdlCode == null || leafCode == null) {
				System.out.println("존재하지 않는 지역입니다.");
				return false;
			}
			XmlSoup.xmlParse();
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
		
	}
	public static String readXmlFromUrl(String putUrl) throws MalformedURLException, IOException {
		InputStream url = new URL(putUrl).openStream();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(url, Charset.forName("UTF-8")));
		String xmlText = readAll(rd);

		return xmlText;
	}
	
	public static String returnLeafCode(JsonArray jsonArr) {
		String code = null;
		String value= null;
		
		for(int i = 0; i < jsonArr.size(); i++) {
			JsonObject json = (JsonObject) jsonArr.get(i);
			code = json.get("code").getAsString();
			value = json.get("value").getAsString();
			
			if(leafVal.equals(value)) {
				gx = json.get("x").getAsString();
				gy = json.get("y").getAsString();
				return code;
			}
		}
		
		return null;
	}
	
	public static String returnMdlCode(JsonArray jsonArr) {
		String code = null;
		String value= null;
		
		for(int i = 0; i < jsonArr.size(); i++) {
			JsonObject json = (JsonObject) jsonArr.get(i);
			code = json.get("code").getAsString();
			value = json.get("value").getAsString();
			
			if(mdlVal.equals(value)) {
				return code;
			}
		}
		
		return null;
	}
	
	public static String returnTopCode(JsonArray jsonArr) {
		
		String code = null;
		String value= null;
		
		for(int i = 0; i < jsonArr.size(); i++) {
			JsonObject json = (JsonObject) jsonArr.get(i);
			code = json.get("code").getAsString();
			value = json.get("value").getAsString();
			
			if(topVal.equals(value)) {
				return code;
			}
		}
		
		return null;
	}
	
	public static JsonArray readJsonFromUrl(String putUrl) throws IOException{
		/*
			도/시  : "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt"
			시/군/구 : "http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.[도/시 코드].json.txt"
			읍/면/동 : "http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf.[시/군/구 코드].json.txt"
		*/
		
		InputStream url = new URL(putUrl).openStream();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(url, Charset.forName("UTF-8")));
		String jsonText = readAll(rd);

		JsonParser jsonParse = new JsonParser(); 
		JsonArray jsonArr =  (JsonArray) jsonParse.parse(jsonText);			
		
		return jsonArr;	
	}
	
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		
		while((cp = rd.read()) != -1) {
			sb.append((char)cp);
		}
	
		return sb.toString();
	}
	
}
