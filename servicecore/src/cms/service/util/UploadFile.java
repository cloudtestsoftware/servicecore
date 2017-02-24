package cms.service.util;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Dictionary;
import java.util.Hashtable;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UploadFile {
  static Log logger = LogFactory.getLog(UploadFile.class);
  private String savePath, filepath, filename, contentType;
  private Dictionary fields;

  public String getFilename() {
    return filename;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  public String getContentType() {
    return contentType;
  }

  public String getFieldValue(String fieldName) {
    if (fields == null || fieldName == null)
      return null;
    return (String) fields.get(fieldName);
  }

  private void setFilename(String s) {
    if (s==null)
      return;

    int pos = s.indexOf("filename=\"");
    if (pos != -1) {
      filepath = s.substring(pos+10, s.length()-1);
      // Windows browsers include the full path on the client
      // But Linux/Unix and Mac browsers only send the filename
      // test if this is from a Windows browser
      pos = filepath.lastIndexOf("\\");
      if (pos != -1)
        filename = filepath.substring(pos + 1);
      else
        filename = filepath;
    }
  }
private void setContentType(String s) {
    if (s==null)
      return;

    int pos = s.indexOf(": ");
    if (pos != -1)
      contentType = s.substring(pos+2, s.length());
  }

  public void doUpload(HttpServletRequest request) throws IOException {
    ServletInputStream in = request.getInputStream();

    byte[] line = new byte[128];
    int i = in.readLine(line, 0, 128);
    if (i < 3)
      return;
    int boundaryLength = i - 2;

    String boundary = new String(line, 0, boundaryLength); //-2 discards the newline character
    fields = new Hashtable();

    while (i != -1) {
      String newLine = new String(line, 0, i);
      if (newLine.startsWith("Content-Disposition: form-data; name=\"")) {
        if (newLine.indexOf("filename=\"") != -1) {
          setFilename(new String(line, 0, i-2));
          if (filename==null)
            return;
          //this is the file content
          i = in.readLine(line, 0, 128);
          setContentType(new String(line, 0, i-2));
          i = in.readLine(line, 0, 128);
          // blank line
          i = in.readLine(line, 0, 128);
          newLine = new String(line, 0, i);
          PrintWriter pw = new PrintWriter(new BufferedWriter(new
            FileWriter((savePath==null? "" : savePath) + filename)));
          while (i != -1 && !newLine.startsWith(boundary)) {
            // the problem is the last line of the file content
            // contains the new line character.
            // So, we need to check if the current line is
            // the last line.
            i = in.readLine(line, 0, 128);
            if ((i==boundaryLength+2 || i==boundaryLength+4) // + 4 is eof
              && (new String(line, 0, i).startsWith(boundary)))
              pw.print(newLine.substring(0, newLine.length()-2));
            else
              pw.print(newLine);
            newLine = new String(line, 0, i);

          }
          pw.close();

        }
        else {
          //this is a field
          // get the field name
          int pos = newLine.indexOf("name=\"");
          String fieldName = newLine.substring(pos+6, newLine.length()-3);
          //System.out.println("fieldName:" + fieldName);
          // blank line
          i = in.readLine(line, 0, 128);
          i = in.readLine(line, 0, 128);
          newLine = new String(line, 0, i);
          StringBuffer fieldValue = new StringBuffer(128);
          while (i != -1 && !newLine.startsWith(boundary)) {
            // The last line of the field
            // contains the new line character.
            // So, we need to check if the current line is
            // the last line.
            i = in.readLine(line, 0, 128);
            if ((i==boundaryLength+2 || i==boundaryLength+4) // + 4 is eof
              && (new String(line, 0, i).startsWith(boundary)))
              fieldValue.append(newLine.substring(0, newLine.length()-2));
            else
              fieldValue.append(newLine);
            newLine = new String(line, 0, i);
          }
          //System.out.println("fieldValue:" + fieldValue.toString());
          fields.put(fieldName, fieldValue.toString());
        }
      }
      i = in.readLine(line, 0, 128);

    } // end while
  }



}
