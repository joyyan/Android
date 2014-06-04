
package bupt.ygj.datacollector.util;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *  
 * @param path
 * @return
 * @author guowla
 */
public class ReadUtil {

	
	public static String  writeXml(String path) throws Exception{
		String reqstr = "";
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			reqstr = baos.toString("");
			is.close();
			baos.close();
		} catch (Exception e) {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e1) {
				throw new Exception();
			}
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return reqstr;
	}
	public static byte[]  writeToXml(String path) throws Exception{
		
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			byte[] reqstr = baos.toByteArray();
			is.close();
			baos.close();
			return reqstr;
		} catch (Exception e) {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e1) {
				throw new Exception();
			}
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	

}
