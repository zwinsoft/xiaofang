package com.example.xiaofang.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;

public class FileServiceUtil {
	private Context context;

	public FileServiceUtil(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 文件保存
	 * 
	 * @param fileName
	 *            文件名称
	 * @param fileContent
	 *            文件内容
	 *            
	 *            Context.MODE_PRIVATE
	 *            	Private方式创建的文件只能被本应用访问，每次都会创建一个新的文件，如果文件存在就覆盖原来的文件
	 *            Context.MODE_APPEND
	 *               APPEND 方式创建的文件，文件也只能被本应用访问，如果文件存在就在文件的末尾添加内容，如果不存在就创建一个新的文件
	 *            Context.MODE_WORLD_READABLE
	 *                创建的文件其他应用程序可以读取该文件  ，但不能写入
	 *            Context.MODE_WORLD_WRITEABLE
	 *            	创建的文件可以被其他应用程序写入数据，但不能读取数据
	 *            
	 *            Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE
	 *            	将会允许其他应用程序读取和写入数据
	 */
	public boolean savePrivate(String fileName, String fileContent) {
		return save(fileName,fileContent,Context.MODE_PRIVATE);
	}
	//追加内容
	public boolean saveAppend(String fileName, String fileContent) {
		return save(fileName,fileContent,Context.MODE_APPEND);
	}
	//可写入
	public boolean saveWriteable(String fileName, String fileContent) {
		return save(fileName,fileContent,Context.MODE_WORLD_WRITEABLE);
	}
	//可读取
	public boolean saveReadable(String fileName, String fileContent) {
		return save(fileName,fileContent,Context.MODE_WORLD_READABLE);
	}
	public boolean deleteFileFromSDCard(String fileName)
	{
		boolean flag = false;
		try { 
			File myfile = new File(
					Environment.getExternalStorageDirectory()+File.separator+"xiaofang"+File.separator+fileName);
			if(myfile.exists())
			{
				myfile.delete();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	//保存文件到sdcard
	public boolean saveToSdcard(String fileName, String fileContent) {
		boolean flag = false;
		try { 
			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory()+File.separator+"xiaofang"+File.separator+fileName,true);
			flag = write(fileContent, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 保存文件
	 * @param fileName 指定文件名
	 * @param fileContent 文件内容
	 * @param mode 文件模式
	 * @return
	 */
	public boolean save(String fileName, String fileContent,int mode) {
		boolean flag = false;
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					mode);
			flag = write(fileContent, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	private boolean write(String fileContent, FileOutputStream fos)
			throws IOException {
		boolean flag;
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write(fileContent);
		bw.flush();
		bw.close();
		flag = true;
		return flag;
	}
	/**
	 * 读取文本内容
	 * @param fileName 文件名称
	 * @return 
	 */
	public String read(String fileName){
		String content = "";
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len  = 0;
			byte buf[] = new byte[1024];
			while((len = fis.read(buf))!=-1){
				bos.write(buf, 0, len);
			}
			fis.close();
			content = bos.toString();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	//读取文本文件中的内容
    public static String ReadTxtFile(String fileName)
    {
        String path = Environment.getExternalStorageDirectory()+File.separator+"xiaofang"+File.separator+fileName;
        String content = ""; //文件内容字符串
            //打开文件
            File file = new File(path);
            //如果path是传递过来的参数，可以做一个非目录的判断
            if (file.isDirectory())
            {
                return content;
            }
            else
            {
                try {
                    InputStream instream = new FileInputStream(file); 
                    if (instream != null) 
                    {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line;
                        //分行读取
                        while (( line = buffreader.readLine()) != null) {
                            content += line ;
                        }                
                        instream.close();
                    }
                }
                catch (java.io.FileNotFoundException e) 
                {
                    
                } 
                catch (IOException e) 
                {
                     
                }
            }
            return content;
    }
}
