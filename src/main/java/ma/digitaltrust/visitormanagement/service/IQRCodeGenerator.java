package ma.digitaltrust.visitormanagement.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface IQRCodeGenerator {

    void getQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException;

    byte[] getQRCodeByte(String text, int width, int height) throws WriterException, IOException;
}
