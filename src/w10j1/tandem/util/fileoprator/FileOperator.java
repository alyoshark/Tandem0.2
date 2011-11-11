/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package w10j1.tandem.util.fileoprator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * 
 * @author Chenhong
 */
public class FileOperator implements FileOperatorAPI {

    protected String path = "Tandem_data.txt";
    protected File fileName = new File(path);

    /* (non-Javadoc)
	 * @see w10j1.tandem.util.fileoprator.FileOperatorAPI#createFile()
	 */
    @Override
	public void createFile() {
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
	 * @see w10j1.tandem.util.fileoprator.FileOperatorAPI#readFile()
	 */
    @Override
	public String readFile() {
        String read;
        BufferedReader bufRead;
        FileReader fileReader;
        StringBuilder readStr = new StringBuilder();
        try {
            fileReader = new FileReader(fileName);
            bufRead = new BufferedReader(fileReader);
            try {
                while ((read = bufRead.readLine()) != null) {
                    readStr.append(read + "\r\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //System.out.println("File content is:\n" + "\r\n" + readStr);
        return readStr.toString();
    }

    /* (non-Javadoc)
	 * @see w10j1.tandem.util.fileoprator.FileOperatorAPI#appendToFile(java.lang.String)
	 */
    @Override
	public void appendToFile(String newStr) {
        try {
            BufferedWriter fileOut = new BufferedWriter(new FileWriter(
                    fileName, true));
            fileOut.write(newStr);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
	 * @see w10j1.tandem.util.fileoprator.FileOperatorAPI#writeFile(java.lang.String)
	 */
    @Override
	public void writeFile(String newStr) {
        try {
            BufferedWriter fileOut = new BufferedWriter(
                    new FileWriter(fileName));
            fileOut.write(newStr);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
	 * @see w10j1.tandem.util.fileoprator.FileOperatorAPI#replaceBy(java.lang.String, java.lang.String)
	 */
    @Override
	public void replaceBy(String oldStr, String newStr) {
        String temp = "";
        try {
            FileInputStream dataInputStream = new FileInputStream(fileName);
            InputStreamReader dataStreamReader = new InputStreamReader(
                    dataInputStream);
            BufferedReader dataBufferedReader = new BufferedReader(
                    dataStreamReader);
            StringBuffer dataStringBuffer = new StringBuffer();

            while ((temp = dataBufferedReader.readLine()) != null
                    && !temp.equals(oldStr)) {
                dataStringBuffer = dataStringBuffer.append(temp);
                dataStringBuffer = dataStringBuffer.append(System.getProperty("line.separator"));
            }

            dataStringBuffer = dataStringBuffer.append(newStr);

            while ((temp = dataBufferedReader.readLine()) != null) {
                dataStringBuffer = dataStringBuffer.append(System.getProperty("line.separator"));
                dataStringBuffer = dataStringBuffer.append(temp);
            }

            dataBufferedReader.close();
            FileOutputStream dataRewriterStream = new FileOutputStream(fileName);
            PrintWriter dataRewriter = new PrintWriter(dataRewriterStream);
            dataRewriter.write(dataStringBuffer.toString().toCharArray());
            dataRewriter.flush();
            dataRewriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}