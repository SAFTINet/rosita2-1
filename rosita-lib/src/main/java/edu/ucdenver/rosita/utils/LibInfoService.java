package edu.ucdenver.rosita.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import edu.ucdenver.rosita.ArgHandler;

public class LibInfoService {
	
	public static String getVersion() throws IOException, IllegalArgumentException {
//		String version = "Unknown";
//		
//		Class clazz = LibInfoService.class;
//		String className = clazz.getSimpleName() + ".class";
//		String classPath = clazz.getResource(className).toString();
//		if (!classPath.startsWith("jar")) {
//		  // Class not from JAR
//		  return "Unknown";
//		}
//		String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
//		JarURLConnection jurlConn = (JarURLConnection)new URL(manifestPath).openConnection();
//		Manifest manifest = jurlConn.getManifest();
//		Map<String, Attributes> attr = manifest.getEntries();
//		for (Entry<String, Attributes> e : attr.entrySet()) {
//			System.out.println("------");
//			System.out.println(e.getKey());
//			for (Entry<Object, Object> eo : e.getValue().entrySet()) {
//				System.out.println(eo.getKey() + ": " + eo.getValue());
//			}
//		}
		
		//NB. Actually implement this in the future
		return "2.9";
	}
	
	public static String getScriptVersion() throws FileNotFoundException, IllegalArgumentException, IOException {
		//Gets the manifest of the known rosita-lib JAR - this can be different from the one in the webapp lib!
		String version = "Unknown";
		
		JarInputStream jarStream = new JarInputStream(new FileInputStream(new File("/usr/share/rosita/rosita-lib.jar")));
		Manifest manifest = jarStream.getManifest();
		Attributes attr = manifest.getAttributes("Rosita-Lib-Version");
		version = attr.getValue("Rosita-Lib-Version");
		
		return version;
	}
	
	public static boolean testDatabaseConnection() throws SQLException, IOException {
		//Just test the database connection - throw exception if not possible to connect
		ArgHandler.initialize();
		
		String db = ArgHandler.getArg("database");
		String host = ArgHandler.getArg("host", "localhost");
		String port = ArgHandler.getArg("port", "5432");
		String user = ArgHandler.getArg("user");
		String password = ArgHandler.getArg("password");
	
		DriverManagerDataSource ds = new DriverManagerDataSource("jdbc:postgresql://" + host + ":" + port + "/" + db, user, password);
		
		String testStatement = "SELECT * FROM cz.cz_data_source;";
		
		Connection conn = ds.getConnection();
		PreparedStatement ps = conn.prepareStatement(testStatement);
		ps.execute();
		ps.close();
		conn.close();
		
		return true;
	}
	
	public static boolean testGridConnection() throws SQLException, IOException {
		//Just test the GRID database connection - throw exception if not possible to connect
		ArgHandler.initialize();
		
		String db = ArgHandler.getArg("targetdatabase");
		String host = ArgHandler.getArg("targethost", "localhost");
		String port = ArgHandler.getArg("targetport", "3306");
		String user = ArgHandler.getArg("targetuser");
		String password = ArgHandler.getArg("targetpassword");
			
		SimpleDriverDataSource targetDs = new SimpleDriverDataSource(new com.mysql.jdbc.Driver(), "jdbc:mysql://" + host + ":" + port + "/" + db + "?allowMultiQueries=true", user, password);
		
		String testStatement = "SELECT state FROM state;";
		
		Connection conn = targetDs.getConnection();
		PreparedStatement ps = conn.prepareStatement(testStatement);
		ps.execute();
		ps.close();
		conn.close();
		
		return true;
	}


}
