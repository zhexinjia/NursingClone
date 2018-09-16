package Model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Controller.LoginController;
import javafx.collections.ObservableList;

/*
 * below is the insert statement check if exists before insert
 * 
 * INSERT INTO user_primary_info (ssn)
 * SELECT * FROM (SELECT '2') AS tmp
 * WHERE NOT EXISTS (
 *    SELECT ssn FROM training_history WHERE ssn = '2'
 * ) LIMIT 1;
 * 
 * 
 * 
 */


public class DBhelper {
	String registerURL = "http://zhexinj.cn/API/register.php";
	String urlGet = "http://zhexinj.cn/API/getdb.php";
	//String testGet = "http://zhexinj.cn/API/getdb2.php";
	String urlSend = "http://zhexinj.cn/API/sendPost.php";
	JSONParser parser = new JSONParser();
	
	String database;
	String branch;
	public DBhelper() {
		database = LoginController.database;
		branch = LoginController.branch;
	}
	
	/*
	 * below is the helper of needed function
	 */
	
	//convert JSON to HashMap
	private HashMap<String, String> jsonToMap(String json){
		return jsonToList(json).get(0);
	}
	
	//convert JSON array to array-list of HashMap
	private ArrayList<HashMap<String, String>> jsonToList(String json){
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		JSONArray objectArray = new JSONArray();
		try {
			objectArray = (JSONArray) parser.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<?> arrayIterator = objectArray.iterator();
		while (arrayIterator.hasNext()) {
			Object object = arrayIterator.next();
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>)object;
			output.add(map);
		}
		return output;
	}	
	
	//FIXME: security issue in future
	/*
	 * @param: sql statement
	 * return: boolean value check send success
	 */
	public Boolean sendPost(String url, String param) {
		String result = "";
        BufferedReader in = null;
        String final_param = "database="+database+"&"+param;
        byte[] postData = final_param.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接            
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoOutput( true );
            connection.setInstanceFollowRedirects( false );
            connection.setRequestMethod( "POST" );
            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            connection.setUseCaches( false );
            connection.connect();
            
            try( DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            	   wr.write( postData );
            	}
            
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
            String line;
            
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        		//FIXME: add POPUP
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        
        //使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (result.equals("success")) {
        		success();
        		return true;
        }else {
        		fail();
        }
        return false;
	}
	
	public String sendHospitalPost(String url, String param) {
		String result = "";
        BufferedReader in = null;
        String final_param = "database=medic"+"&"+param;
        byte[] postData = final_param.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接            
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoOutput( true );
            connection.setInstanceFollowRedirects( false );
            connection.setRequestMethod( "POST" );
            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            connection.setUseCaches( false );
            connection.connect();
            
            try( DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            	   wr.write( postData );
            	}
            
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
            String line;
            
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        		//FIXME: add POPUP
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        
        //使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        /*
        if (result.equals("success")) {
        		return true;
        }
        return false;
        */
        return result;
	}
	/*
	 * @param: sql statement
	 * @return: JSON formated string from executing SQL statement
	 */
	public String sendGet(String url, String param) {
		String result = "";
        BufferedReader in = null;
        String final_param = "database="+database+"&"+param;
        byte[] postData = final_param.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接            
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoOutput( true );
            connection.setInstanceFollowRedirects( false );
            connection.setRequestMethod( "POST" );
            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            connection.setUseCaches( false );
            connection.connect();
            
            try( DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            	   wr.write( postData );
            	}
            
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
            String line;
            
            while ((line = in.readLine()) != null) {
                result += line;
            }
            
            //result = in.readLine();
        } catch (Exception e) {
        		//FIXME: add POPUP
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        
        //使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

	public String sendHospitalGet(String url, String param) {
		String result = "";
        BufferedReader in = null;
        String final_param = "database=medic"+"&"+param;
        byte[] postData = final_param.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接            
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoOutput( true );
            connection.setInstanceFollowRedirects( false );
            connection.setRequestMethod( "POST" );
            connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            connection.setRequestProperty( "charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            connection.setUseCaches( false );
            connection.connect();
            
            try( DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            	   wr.write( postData );
            	}
            
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
            String line;
            
            while ((line = in.readLine()) != null) {
                result += line;
            }
            
            //result = in.readLine();
        } catch (Exception e) {
        		//FIXME: add POPUP
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        
        //使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	
	
	
	
	
	
	
	/*
	 * Below are all the method used in controller to insert, update, delete
	 * 
	 * 
	 */
	
	public String getSearchStatement(String[] searchColumn, String[] values) {
		String statement = "";
		for(int i = 0; i < searchColumn.length; i++) {
			if(i == searchColumn.length-1) {
				statement += searchColumn[i] + " = '" + values[i] + "';"; 
			}else {
				statement += searchColumn[i] + " = '" + values[i] + "' AND ";
			}
		}
		return statement;
	}
	
	public String getStatement(String tableName, String[] columns) {
		String statement = "";
		for (int i = 0; i<columns.length; i++) {
			if (i == columns.length-1) {
				statement += columns[i] + " from " + tableName + " ";
			}else {
				statement += columns[i] + ", ";
			}
		}
		return statement;
	}

	//get entire user table
	public ArrayList<HashMap<String, String>> getEntireList(String tableName){
		String sql = "sql=select * from " + tableName +";";
		String output = sendGet(urlGet, sql);
		return jsonToList(output);
	}

	//return same as above @param: column name and value
	public ArrayList<HashMap<String, String>> getEntireList(String[] searchColumn, String[] values, String tableName){
		String sql = "sql=select * from " + tableName + " where ";
		sql += getSearchStatement(searchColumn, values);
		String output = sendGet(urlGet, sql);
		return jsonToList(output);
	}

	
	//same function as above
	public ArrayList<HashMap<String, String>> getList(String tableName, String[] columns){
		String sql = "sql=select ";
		sql+=getStatement(tableName, columns);
		String output = sendGet(urlGet, sql);
		System.out.println(sql);
		return jsonToList(output);
	}
	
	public ArrayList<HashMap<String, String>> getBranchList(String tableName, String[] columns){
		String sql = "sql=select ";
		sql+=getStatement(tableName, columns);
		if(!branch.equals("0")) {
			sql+="Where branch = '" + branch + "'";
		}
		String output = sendGet(urlGet, sql);
		System.out.println(sql);
		return jsonToList(output);
	}
	
	//Keep this method only
	public ArrayList<HashMap<String, String>> getList(String[] searchColumn, String[] values, String tableName, String[] columns){
		String sql = "sql=select ";
		sql+=getStatement(tableName, columns);
		sql+=" where " + getSearchStatement(searchColumn, values);
		String output = sendGet(urlGet, sql);
		System.out.println(sql);
		return jsonToList(output);
	}
	
	public ArrayList<ArrayList<HashMap<String, String>>> getMultipleList(String sqlStatement){
		return null;
	}
	

	
	public ArrayList<HashMap<String, String>> getList(String sqlStatement, String tableName, String[] columns){
		String sql = "sql=select ";
		sql+=getStatement(tableName, columns);
		sql += sqlStatement;
		String output = sendGet(urlGet, sql);
		System.out.println(sql);
		return jsonToList(output);
	}
	
	//get selected user Info
	//TODO no test
	/*
	public HashMap<String, String> getEntireUserInfo(String hospitalID, String tableName, String ssn){
		String sql = "sql=select * from " + tableName + " where hospital_id = " + hospitalID + " AND ssn = " + ssn + ";";
		String output = sendGet(urlGet, sql);
		return jsonToMap(output);
	}
	*/
	
	
	/**********************************************************************
	/ * INSERT Methods   
	/**********************************************************************/
	

	public String insertUserHelper(HashMap<String, String> map) {
		//insert ignore into exam_history (ssn, exam_id) VALUES
		String sqlPrim = "insert into user_primary_info" + " (";
		String sqlSub= "insert into user_sub_info" + " (";
		String sqlScore = "insert into user_score" + " (ssn) VALUES (";
		Set<String> keys = map.keySet();
		ArrayList<String> keyset = new ArrayList<String>(keys);
		
		
		for(int i = 0; i < keyset.size();i++) {
			if (keyset.get(i) == "name" || keyset.get(i) == "department" || 
					keyset.get(i) == "position" || keyset.get(i) == "title" ||
					keyset.get(i) == "level" || keyset.get(i) == "password" || keyset.get(i) == "branch") {
				sqlPrim+= keyset.get(i) + ", ";
			}else {
				sqlSub+= keyset.get(i) + ", ";
			}		
		}
		sqlPrim += "ssn) VALUES (";
	
		sqlSub = sqlSub.substring(0, sqlSub.length()-2) + ") VALUES (";
		
		sqlScore += "'" + map.get("ssn") + "');";
		
		for(int i = 0; i < keyset.size();i++) {
			if (keyset.get(i) == "name" || keyset.get(i) == "department" ||
					keyset.get(i) == "position" || keyset.get(i) == "title" ||
					keyset.get(i) == "level" || keyset.get(i) == "password" || keyset.get(i) == "branch") {
				sqlPrim += "'" + map.get(keyset.get(i)) + "', ";
			}else {
				sqlSub+= "'" + map.get(keyset.get(i)) + "', ";
			}		
		}
		sqlPrim += "'" + map.get("ssn") + "'";
		sqlPrim += ");";
		sqlSub = sqlSub.substring(0, sqlSub.length()-2) + ");";
		
		String res = sqlPrim + " " +sqlSub + " " + sqlScore;
		
		return res;
	}

	/*
	 * Used in UserNewController, given one HashMap insert into three tables, nurse primary, sub, nurse score
	 */
	public boolean insertUser(HashMap<String, String> map) {
		String sql = "sql=" + insertUserHelper(map);
		System.out.println(sql);
		if(sendPost(urlSend, sql)) {
			return true;
		}
		return false;
	}	
	
	/*
	 * hard insert function, insert list of user info into 3 tables
	 */
	public boolean insertUserList(ArrayList<HashMap<String, String>> maplist) {
		//boolean res;
		String sql = "sql=";
		for (HashMap<String, String> user : maplist){
			sql += insertUserHelper(user);
			
		}
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			return true;
		}
		return false;
	}
	
	
	public String mapInsert(HashMap<String, String> map, String tableName) {
		String sql = "insert into " + tableName + " (";
		Set<String> keys = map.keySet();
		ArrayList<String> keyset = new ArrayList<String>(keys);
		for(int i = 0; i < keyset.size();i++) {
			if(i == keyset.size()-1) {
				sql+= keyset.get(i) + ") VALUES (";
			}else {
				sql+= keyset.get(i) + ", ";
			}
		}
		for(int i = 0; i < keyset.size();i++) {
			if(i == keyset.size()-1) {
				sql+= "'" + map.get(keyset.get(i)) + "');";
			}else {
				sql+= "'" + map.get(keyset.get(i)) + "', ";
			}
		}
		return sql;
	}
	
	//simple insert, insert map's value into table
	public boolean insert(HashMap<String, String> map, String tableName) {
		String sql = "sql=" + mapInsert(map, tableName);
		System.out.println(sql);
		if(sendPost(urlSend, sql)) {
			System.out.println(sql);
			return true;
		}
		System.out.println("Fail to insert");
		return false;
	}
	
	


	public String insertHistoryHelper(HashMap<String, String> map, String id, String type) {
		
		String sql = "";
		if (type == "offlineTest") {
			map.put("offlineexam_id", id);
			sql += "insert into offlineexam_history" + " (";
		}else if (type == "training") {
			map.put("training_id", id);
			sql += "insert into training_history" + " (";
		}else {
			System.out.println("ERROR");
		}
		
		Set<String> keys = map.keySet();
		ArrayList<String> keyset = new ArrayList<String>(keys);
		for(int i = 0; i < keyset.size();i++) {
			if(i == keyset.size()-1) {
				sql+= keyset.get(i) + ") VALUES (";
			}else {
				sql+= keyset.get(i) + ", ";
			}
		}
		for(int i = 0; i < keyset.size();i++) {
			if(i == keyset.size()-1) {
				sql+= "'" + map.get(keyset.get(i)) + "');";
			}else {
				sql+= "'" + map.get(keyset.get(i)) + "', ";
			}
		}
	
		return sql;
	}
	
	public boolean insertOfflineTest(ArrayList<HashMap<String, String>> maplist, String exam_id, String totalSocre) {
	//	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//LocalDate localDate = LocalDate.now();
		String sql = "sql=";
		
		for (HashMap<String, String> user : maplist){
			System.out.println(user);
			String scoreTemp = user.get("score");
			int score = 0;
			try {
				score = Integer.parseInt(scoreTemp);
			}catch(Exception e) {
				System.out.println("ERROR: "+e);
			}

		//	int totalScore = Integer.parseInt(totalPoint);
			String ssn = user.get("ssn");
			sql += insertHistoryHelper(user, exam_id, "offlineTest");
			
			sql += "update user_score set currentScore = (currentScore %2B " 
			+ score + "), totalScore = (totalScore %2B " + totalSocre + ") where ssn = '" + ssn + "';";
			
			//sql += "update user_score set comment = concat(ifnull(comment,''), '" 
			//+ ":" + name + " 实操考核得 " + score + " 分,') where ssn = '" + ssn + "';";
		}
		
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			System.out.println("Success");
			return true;
		}
		System.out.println("Fail");
		return false;
	}

	
	public boolean insertTrainning(ArrayList<HashMap<String, String>> maplist, String training_id, String name, String totalPoint) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		
		String sql = "sql=";
		for (HashMap<String, String> user : maplist){
			int score = 0;
			try{
				score = Integer.parseInt(user.get("point"));
			}catch(Exception e){
				System.out.println(e);
			}
			int totalScore = Integer.parseInt(totalPoint);
			String ssn = user.get("ssn");
			sql += insertHistoryHelper(user, training_id, "training");
			
			sql += "update user_score set currentScore = (currentScore %2B " 
			+ score + "), totalScore = (totalScore %2B " + totalScore + ") where ssn = '" + ssn + "';";
			sql += "update user_score set comment = concat(ifnull(comment,''), '" 
			+ dtf.format(localDate) + ":" + name + " 培训考核得 " + score + " 分,') where ssn = '" + ssn + "';";
		
		}
		System.out.println("SQL: "+sql);
		if (sendPost(urlSend, sql)) {
			System.out.println("Success");
			return true;
		}
		System.out.println("Fail");
		return false;
	}
	
	
	//simply insert entire HashMap List into table

	public boolean insertList(ArrayList<HashMap<String, String>> list, String tableName) {
		String sql = "sql=";
		for(HashMap<String, String> map:list) {
			sql+=mapInsert(map, tableName);
		}
		System.out.println(sql);
		if(sendPost(urlSend, sql)) {
			return true;
		}
		return false;
	}
	
	public boolean inserExam(ArrayList<HashMap <String, String>> map) {
		//Default each question worth 10-point
		
		String examId = null;
		String sql = "sql=";
		for (HashMap<String, String> exam:map) {
			sql += mapInsert(exam, "exam_qa");
			examId = exam.get("exam_id");
		}
		
		sql += "select count(*) into @num from exam_qa where exam_id ='" + examId 
				+ "'; select single_point into @point from exam_list where id ='" + examId + 
				"'; update exam_list set totalPoint = (@num*@point) where id = '" + examId + "';";
		
		if (sendPost(urlSend, sql)) {
			//System.out.println(sql);
			return true;
		}
		//System.out.println(sql);
		return false;
	
	}
	
	
	public boolean insertOffline(HashMap<String, String> map, ObservableList<HashMap<String, String>> req_list) {
		
		String sql = "sql=" + mapInsert(map, "offlineexam_list");
		sql += "SELECT @exam_id := max(id) from medic.offlineexam_list;";
		
		for(HashMap<String, String> req:req_list) {
			sql+="insert into offline_req_bank (offlineexam_id, point, req) "
					+ "VALUES (@exam_id, '" + req.get("point") + "', '" + req.get("req") + "');";
		}
		
		System.out.println(sql);
		
		if (sendPost(urlSend, sql)) {
			return true;
		}
		
		return false;
		
	}
		
	
	
	
	/****************************************************************************/
	
	
	
	/**********************************************************************
	/ * UPDATE Methods   
	/**********************************************************************/
	
	public String updateUserHelper(HashMap<String, String> map) {
		
		String sqlPrim = "UPDATE user_primary_info set " ;
		String sqlSub= "UPDATE user_sub_info set ";
		
		Set<String> keys = map.keySet();
		ArrayList<String> keyset = new ArrayList<String>(keys);	
		
		for(int i = 0; i < keyset.size();i++) {
			if (keyset.get(i) == "name" || keyset.get(i) == "department" || 
					keyset.get(i) == "position" || keyset.get(i) == "title" ||
					keyset.get(i) == "level" || keyset.get(i) == "password" || keyset.get(i) == "branch" || keyset.get(i) == "is_manager") {
				
				if(map.get(keyset.get(i))!=null) {
					sqlPrim+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
				}
				//sqlPrim+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
			}else {
				if(map.get(keyset.get(i))!=null) {
					sqlSub+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
				}
				//sqlSub+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
			}		
		}
		
		sqlPrim = sqlPrim.substring(0, sqlPrim.length()-2);
		sqlPrim += " WHERE ssn=" + "'" + map.get("ssn") + "';";
		
		sqlSub = sqlSub.substring(0, sqlSub.length()-2);
		sqlSub += " WHERE ssn=" + "'" + map.get("ssn") + "';";
		
		String res = sqlPrim + sqlSub;
		
		return res;
		
	}
	
	//used in UserModifyController, one HashMap as @Param, update two tables: primary, sub
	public boolean updateUser(HashMap<String, String> map) {
		String sql = "sql=" + updateUserHelper(map);
		System.out.println(sql);
		if(sendPost(urlSend, sql)) {
			return true;
		}
		return false;
	}
	
	public boolean update(HashMap<String, String> map, String tableName) {
		String sql = "sql=update " + tableName + " set ";
		ArrayList<String> keyset = new ArrayList<String>(map.keySet());
		for(int i = 0; i < keyset.size(); i++) {
			if(i == keyset.size()-1) {
				sql+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "'";
			}else {
				sql+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
			}
		}
		sql += " where id = " + map.get("id") + ";";
		if(sendPost(urlSend, sql)) {
			return true;
		}
		//System.out.println(sql);
		return false;
	}
	
	public boolean updateScore(HashMap<String, String> map, String tableName) {

		String sql = "sql=update " + tableName + " set ";
		ArrayList<String> keyset = new ArrayList<String>(map.keySet());
		for(int i = 0; i < keyset.size(); i++) {
			if(i == keyset.size()-1) {
				sql+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "'";
			}else {
				sql+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
			}
		}
		sql += " where ssn = " + map.get("ssn") + ";";
		
		if(sendPost(urlSend, sql)) {
			
			//success();
			return true;
		}
		System.out.println(sql);
		//fail();
		return false;
	}
	
	public boolean newUpdateScore(HashMap<String, String> map, String tableName, String newScore) {
		//String newScore = map.get("newScore");
		String ssn  = map.get("ssn");
		String sql = "sql=update " + tableName + " set ";
		ArrayList<String> keyset = new ArrayList<String>(map.keySet());
		for(int i = 0; i < keyset.size(); i++) {
			if(i == keyset.size()-1) {
				sql+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "'";
			}else {
				sql+= keyset.get(i) + " = '" + map.get(keyset.get(i)) + "', ";
			}
		}
		sql += " where id = " + map.get("id") + ";";
		sql += " update user_score set currentScore = '" + newScore + 
				"' where ssn = '" + ssn + "';";
		
		
		if(sendPost(urlSend, sql)) {
			System.out.println("update Score sql: " + sql);
			//success();
			return true;
		}
		System.out.println(sql);
		//fail();
		return false;
	}
	
	
	/****************************************************************************/

	
	/**********************************************************************
	/ * PUBLISH Methods   
	/**********************************************************************/

	//publishing Exam/Study/Training/Meeting
	public boolean publish(ArrayList<HashMap<String, String>> userList, HashMap<String, String> item, String table) {

		//id -> exam_id, study_id, training_id, meeting_id
		String id = item.get("id");
		int totalPoint = 0;
		String sql = "sql=";
		
		if (table == "exam_list") {
			totalPoint = Integer.parseInt(item.get("totalPoint"));
		}else if (table == "study_list") {
			totalPoint = Integer.parseInt(item.get("point"));
		}else if (table == "training_list") {
			totalPoint = Integer.parseInt(item.get("totalPoint"));
		}else if  (table == "meeting_list") {
			totalPoint = Integer.parseInt(item.get("totalPoint"));
		}else {
			System.out.println("ERROR");
		}
		
		for (HashMap<String, String> user:userList) {
			String ssn = user.get("ssn");
			if (table == "exam_list") {
				sql += "insert ignore into exam_history (ssn, exam_id) VALUES ('" + ssn + "', '" + id + "');";
				sql += "update exam_list set publish_status = '已发布' where id= '" + id + "';";
				sql += "update user_score set totalScore = (totalScore %2B" + totalPoint + ") where ssn = '" + ssn + "';";
								
			}
			else if (table == "study_list") {
				sql += "insert ignore into study_history (ssn, study_id) VALUES ('" + ssn + "', '" + id + "');";
				sql += "update study_list set publish_status = '已发布' where id= '" + id + "';";
				sql += "update user_score set totalScore = (totalScore %2B " + totalPoint + ") where ssn = '" + ssn + "';";
				
			}
			else if (table == "training_list") {
				sql += "insert ignore into training_history (ssn, study_id) VALUES ('" + ssn + "', '" + id + "');";
				sql += "update training_list set publish_status = '已发布' where id= '" + id + "';";
				sql += "update user_score set totalScore = (totalScore %2B " + totalPoint + ") where ssn = '" + ssn + "';";
		
			}
			else if (table == "meeting_list") {
				sql += "insert ignore into meeting_history (ssn, meeting_id) VALUES ('" + ssn + "', '" + id + "');";
				//sql += "update meeting_list set publish_status = '已发布' where id= '" + id + "';";
				sql += "update user_score set totalScore = (totalScore %2B " + totalPoint + ") where ssn = '" + ssn + "';";
		
			}
			
		}
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			return true;
		}
		return false;
	}
	
	public boolean publishOfflineExam(ArrayList<HashMap<String, String>> userList, HashMap<String, String> item) {
		String id = item.get("id");
		Integer totalPoint = Integer.parseInt(item.get("totalPoint"));
		
		String sql = "sql=";
		sql += "update offlineexam_list set publishStatus = '已发布' where id= '" + id + "';";
		for (HashMap<String, String> user:userList) {
			String ssn = user.get("ssn");
			sql += "insert ignore into offlineexam_history (ssn, offlineexam_id, finish_status) VALUES ('" + ssn + "', '" + id + "', '否');";
		//	sql += "update user_score set totalScore = (totalScore %2B " + totalPoint + ") where ssn = '" + ssn + "';";
		}
		
		System.out.println(sql);
		
		if (sendPost(urlSend, sql)) {
			System.out.println("Publish Offline Exam Success");
			return true;
		}
		System.out.println("Fail to publish Offline Exam");
		return false;
	}

	public boolean publishTraining(ArrayList<HashMap<String, String>> maplist, String tableName) {
		String sql = "sql=";
		for(HashMap<String, String> map:maplist) {
			sql+=preventDupSQL(map, tableName);
		}
		System.out.println(sql);
		if(sendPost(urlSend, sql)) {
			//success();
			
			return true;
		}
		fail();
		return false;
		
	}
	/****************************************************************************/
	
	
	
	/**********************************************************************
	/ * DELETE Methods   
	/**********************************************************************/
	
	//simple delete version, delete row by using hashmap.id
	public boolean delete(HashMap<String, String> map, String tableName) {
		System.out.printf("map: ", map);
		String id = map.get("id");
		String sql = "sql=delete from " + tableName + " where id = " + id + ";";
		if(sendPost(urlSend, sql)) {
			
			//success();
			return true;
		}
		//fail();
		return false;
	}
	
	
	//used in UserListController, delete user info from the 3-user tables and all other history tables
	public boolean deleteUser(HashMap<String, String> selectedUser) {
		String ssn = selectedUser.get("ssn");
		//Delete user from the 3-user tables
		String primSql = "delete from user_primary_info where ssn = " + ssn + ";";
		String subSql = "delete from user_sub_info where ssn = " + ssn + ";";
		String scoreSql = "delete from user_score where ssn = " + ssn + ";";
		
		//Delete user from all history tables
		String examSql = "delete from exam_history where ssn=" + "'" + ssn + "';";
		String meetingSql = "delete from meeting_history where ssn=" + "'" + ssn + "';";
		String reportSql = "delete from report_list where ssn=" + "'" + ssn + "';";
		String studySql = "delete from study_history where ssn=" + "'" + ssn + "';";
		String trainingSql = "delete from training_history where ssn=" + "'" + ssn + "';";
		
		String sql = "sql=" + primSql + subSql + scoreSql + examSql + meetingSql + reportSql + studySql + trainingSql ;
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;
	}
	
	
	//delete meeting and all meeting history related to this meeting
	public boolean deleteMeeting(HashMap<String, String> selectedMeeting) {
		String id = selectedMeeting.get("id");
		String historySql = "DELETE FROM meeting_history WHERE meeting_id=" + "'" + id + "';";
		String listSql = "DELETE FROM meeting_list WHERE id=" + "'" + id + "';";
		String sql = "sql=" + historySql + listSql;
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;	
	}
	
	//delete study and all study history related to this study
	public boolean deleteStudy(HashMap<String, String> selectedStudy) {
		String id = selectedStudy.get("id");
		String historySql = "DELETE FROM study_history WHERE study_id=" + "'" + id + "';";
		String listSql = "DELETE FROM study_list WHERE id=" + "'" + id + "';";
		String sql = "sql=" + historySql + listSql;
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;	
	}
	
	//delete training and all training history related to this training
	public boolean deleteTrainning(HashMap<String, String> selectedTraining) {
		String id = selectedTraining.get("id");
		String historySql = "DELETE FROM training_history WHERE training_id=" + "'" + id + "';";
		String listSql = "DELETE FROM training_list WHERE id=" + "'" + id + "';";
		String sql = "sql=" + historySql + listSql;
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;
	}
	
	public boolean deleteExam(HashMap<String, String> map) {
		String id  = map.get("id");
		
		String exam = "delete from exam_list where id = '" + id + "';";
		String history  = "delete from exam_history where exam_id = '" + id + "';";
		String sql = "sql=" + exam + history;
		
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;
		
	}
	
	public boolean deleteOfflineExam(HashMap<String, String> map) {
		String id = map.get("id");
		String exam = "delete from offlineexam_list where id=" + "'" + id + "';";
		String section = "delete from offline_section where offlineexam_id=" + "'" + id + "';";
		String req_bank = "delete from offline_req_bank where offlineexam_id=" + "'" + id + "';";
		String history = "delete from offlineexam_history where offlineexam_id=" + "'" + id + "';";
		String sql = "sql=" + exam + section + req_bank + history;
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			System.out.println("Delete Offline Exam Success");
			//success();
			return true;
		}
		//fail();
		System.out.println("Fail to Delete Offline Exam");
		return false;
	}
	
	public boolean deleteOfflineSection(HashMap<String, String> map) {
		String id = map.get("id");
		String exam = "delete from offline_section where id=" + "'" + id + "';";
		String history = "delete from offlineexam_history where section_id=" + "'" + id + "';";
		String sql = "sql=" + exam + history;
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			System.out.println("Delete Offline Section Success");
			//success();
			return true;
		}
		//fail();
		System.out.println("Fail to Delete Section Exam");
		return false;
	}
	
	
	
	

	//delete all information about this test, given exam id
	public boolean deleteBank(HashMap<String, String> map) {
		String id = map.get("id");
		//String historySql = "delete from exam_history where bank_id=" + "'" + id + "';";
		//String listSql = "delete from exam_list where id=" + "'" + id + "';";
		String mulSql = "delete from exam_qa_multiple where bank_id=" + "'" + id + "';";
		String singleSql = "delete from exam_qa_single where bank_id=" + "'" + id + "';";
		String tfSql = "delete from exam_qa_tf where bank_id=" + "'" + id + "';";
		String quesBank = "delete from question_bank where id = " + "'" + id + "';";
		 
		String sql = "sql=" + mulSql + singleSql + tfSql + quesBank;
		System.out.println(sql);
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;
	}
	
	/*****************************************************************************/
	
	
	/*********************************************************************** 
	/ * Methods on 'Manager' 
	/************************************************************************/

	public boolean setManager(String ssn) {
		// TODO Auto-generated method stub
		String sql = "sql=UPDATE user_primary_info set is_manager = '1' Where ssn= '" + ssn + "';" ;
		boolean output = sendPost(urlSend, sql);
		return output;
	}
	
	public boolean updateManager(String newManager, String oldManager) {
		String sql = "sql=";
		sql += "update user_primary_info set is_manager = '1' where ssn ='" 
				+ newManager + "'; update user_primary_info set is_manager = '0' "
				+ "where ssn ='" + oldManager + "';";

		if(sendPost(urlSend, sql)) {
			System.out.println(sql);
			return true;
		}
		return false;
	}
	
	/***********************************************************************************/
	
	public boolean emptyScore() {
		String totalScoreSql = "UPDATE user_score SET totalScore = 0;";
		String currentScoreSql = "UPDATE user_score SET currentScore = 0;";
		String commentSql = "UPDATE user_score SET comment = NULL;";
		String sql = "sql=" + totalScoreSql + currentScoreSql + commentSql;
		if (sendPost(urlSend, sql)) {
			//success();
			return true;
		}
		//fail();
		return false;
	}


	private String preventDupSQL(HashMap<String, String> map, String tableName) {
		String sql = "INSERT INTO " + tableName + " ";
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		Set<String> keyset = map.keySet();
		for (String key:keyset) {
			keys.add(key);
			values.add(map.get(key));	
		}
		String keyString = "(";
		String valueString = "(SELECT ";
		for(int i = 0; i< keys.size(); i++) {
			if(i == keys.size()-1) {
				keyString += keys.get(i) + ") ";
				valueString += "'" + values.get(i) + "') ";
			}else {
				keyString += keys.get(i) + ", ";
				valueString += "'" + values.get(i) + "', ";
			}
		}
		sql += keyString + "SELECT * FROM " + valueString + "AS tmp WHERE NOT EXISTS (SELECT ssn FROM " + tableName
				+ " WHERE ssn =" + map.get("ssn") + " AND training_id = " + map.get("training_id") + ");";
		return sql;
	}
	


	public boolean register(String userName, String passWord, String hospitalName, String code) {
		String param = "userName=" + userName + "&password=" + passWord + "&hospitalName="+hospitalName+"&code="+code;
		//String result = sendHospitalPost("http://localhost/API/test.php", param);
		String result = sendHospitalPost(registerURL, param);
		if(result.equals("注册成功")) {
			//TODO: double check
			success();
			return true;
		}else {
			PopupWindow pop = new PopupWindow();
			pop.alertWindow("注册失败", result);
			System.out.println(result);
			return false;
		}
	}
	
	private void success() {
		PopupWindow pop = new PopupWindow();
		pop.confirmButton.setOnAction(e->{
			pop.stage.close();
		});
		pop.confirmWindow("操作成功", "点击确定返回");
	}
	
	private void fail() {
		/*
		PopupWindow pop = new PopupWindow();
		pop.errorWindow();
		*/
	}
	
	public boolean checkbankID(String bank_id) {
		
		String sql = "sql= SELECT * FROM question_bank" + " where id = '" + bank_id + "';";

		String ans = sendGet(urlGet, sql);
		ArrayList<HashMap<String, String>> list = jsonToList(ans);
		
		System.out.println(sql);
		System.out.println("list: "+list);
		
		if (list.isEmpty()) {

			return false;
		}
		return true;
		
	}
	
	public boolean resetPW(HashMap<String, String> map) {
		String sql = "sql=UPDATE user_primary_info set password = null WHERE ssn = " + map.get("ssn");
		if(sendPost(urlSend, sql)) {
			return true;
		}
		return false;
	}

	/*
	private boolean popSelection() {
		PopupWindow pop = new PopupWindow();
		pop.alertWindow("操作失败", "没有选中目标。");
		return false;
	}
	*/
	

	//given ssn, return list of test score histoys, list of study score history, list of training, list of meeting
	/*
	public ArrayList<ArrayList<HashMap<String, String>>> getRecordLists(String ssn){
		String sql = "sql=";
		sql+="select exam_list.examName, exam_history.score, exam_history.totalScore from "
				+ "exam_history inner join exam_list on exam_history.exam_id = exam_list.id where "
				+ "exam_history.ssn = " + ssn + ";";
		sql+="select study_list.name, study_history.finish_status from "
				+ "study_history inner join study_list on study_history.study_id = study_list.id where "
				+ "study_history.ssn = " + ssn + ";";
		sql+="select training_list.name, training_history.point, training_list.point from "
				+ "training_list inner join training_history on training_list.id = training_history.training_id where "
				+ "training_history.ssn = " + ssn + ";";
		sql+="select meeting_list.name, meeting_history.checkin, meeting_history.checkout from "
				+ "meeting_history inner join meeting_list on meeting_list.id = meeting_history.meeting_id where "
				+ "meeting_history.ssn = " + ssn + ";";
		String output = sendGet(testGet, sql);
		System.out.println(sql);
		System.out.println(output);
		return toList(output);
	}
	
	private ArrayList<ArrayList<HashMap<String, String>>> toList(String json){
		ArrayList<ArrayList<HashMap<String, String>>> ret = new ArrayList<ArrayList<HashMap<String, String>>>();
		JSONArray objectArray = new JSONArray();
		try {
			objectArray = (JSONArray) parser.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<?> arrayIterator = objectArray.iterator();
		while (arrayIterator.hasNext()) {
			JSONArray object = (JSONArray) arrayIterator.next();
			ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
			Iterator<?> secondIterator = object.iterator();
			while(secondIterator.hasNext()) {
				Object obj = secondIterator.next();
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>)obj;
				output.add(map);
			}
			ret.add(output);
		}
		return ret;
	}	
	*/
	

	

	
}