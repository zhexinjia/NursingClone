package Model;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class sftpDelete {
	public void sftpDel(String path, String fileName) throws Exception {
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
			sftp.rm(fileName);
			System.out.println("after remove");
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
}
