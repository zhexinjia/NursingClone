package Model;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStream;
import java.io.OutputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;  
  
public class SFTPtool extends Task<Void>{  
	private File file;
	private String path;
	private String fileName;
	private long filesize, byteTransfered;
	private SimpleStringProperty percentage;
	
	public SFTPtool(File file, String path, String fileName) {
		this.file = file;
		this.path = path;
		this.fileName = fileName;
		this.byteTransfered = 0;
		percentage = new SimpleStringProperty();
	}
		
	//执行SFTP上传
	public void sshSftp(File file, String path, String fileName) throws Exception{
		System.out.println("in sftp connection");
		String ip = "101.200.39.52";
		String user = "uploader";
		String psw = "uploader123321";
		int port = 22;
		
		
		Session session = null;
		Channel channel = null;

		
		JSch jsch = new JSch();
		
		
		if(port <=0){
			//连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		}else{
			//采用指定的端口连接服务器
			session = jsch.getSession(user, ip ,port);
		}

		//如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}
		
		//设置登陆主机的密码
		session.setPassword(psw);//设置密码   
		//设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		//设置登陆超时时间   
		session.connect(30000);
			
		try {
			//创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(10000);
			ChannelSftp sftp = (ChannelSftp) channel;
			
			
			
			//进入服务器指定的文件夹
			sftp.cd(path);
			
			//列出服务器指定的文件列表
			/*
			Vector v = sftp.ls("*.xlsx");
			for(int i=0;i<v.size();i++){
				System.out.println(v.get(i));
			}
			*/
			
			//以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
			this.filesize = file.length();
			
			OutputStream outstream = sftp.put(fileName, ChannelSftp.OVERWRITE);
			InputStream instream = new FileInputStream(file);
			
			byte b[] = new byte[1024];
			int n;
		    while ((n = instream.read(b)) != -1) {
		    		this.byteTransfered += n;
		    		updateProgress(this.byteTransfered, this.filesize);
		    		int percentage = (int) (this.byteTransfered * 100 / this.filesize);
		    		this.percentage.setValue(percentage + "%");
		    		outstream.write(b, 0, n);
		    }
		    
		    outstream.flush();
		    outstream.close();
		    instream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}

	@Override
	protected Void call() throws Exception {
		this.sshSftp(file, path, fileName);
		return null;
	}
	
	public SimpleStringProperty getPercentage() {
		return percentage;
	}
	public void setPercentage(SimpleStringProperty percentage) {
		this.percentage = percentage;
	}

}  
