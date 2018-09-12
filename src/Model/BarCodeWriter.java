package Model;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class BarCodeWriter {
	
	public BarCodeWriter() {
		
	}
	
	public void generateCode(Path path, String barCode) throws WriterException, IOException {
        int width = 300;   
        int height = 300;   
        String format = "gif";   
        Hashtable<EncodeHintType, String> hints= new Hashtable<EncodeHintType, String>();   
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   
        BitMatrix bitMatrix = new MultiFormatWriter().encode(barCode, BarcodeFormat.QR_CODE, width, height,hints);   
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);   
	}
	
	

}