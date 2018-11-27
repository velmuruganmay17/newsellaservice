package camel.sellacast.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import camel.sellacast.util.SellaHelper;

@Path("/file")
public class SellaCastUploadService {

	public static final Logger logger = Logger.getLogger(SellaCastUploadService.class);
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
			try {
				String uploadedFileLocation = "d://temp//uploaded/" +new Date().getTime()+getRandomIntegerBetweenRange(1000,10000)+ fileDetail.getFileName();
				// save it
				writeToFile(uploadedInputStream, uploadedFileLocation);
				logger.debug("file uploaded succssafully fieName : "+uploadedFileLocation);
				return SellaHelper.getSuccessMessage(null);
			} catch (IOException e) {
				e.printStackTrace();
				return SellaHelper.getErrorMessage(e.getMessage());
			}
		}

		// save uploaded file to new location
		private void writeToFile(InputStream uploadedInputStream,String uploadedFileLocation) throws IOException {
			OutputStream out=null;
			try {
				out = new FileOutputStream(new File(uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} finally {
					try {
						if(out!=null) {
						out.flush();
						}
						if(out!=null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}

		}
		
		public int getRandomIntegerBetweenRange(double min, double max){
			int x = (int) ((int)(Math.random()*((max-min)+1))+min);
		    return x;
		}
		 
}
