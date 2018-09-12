package Model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//import static org.junit.jupiter.api.Assertions.*;

class Test {
	DBhelper dbHelper = new DBhelper();
	@org.junit.jupiter.api.Test
	
	void testDel() {
		sftpDelete sftp = new sftpDelete();
		try {
			sftp.sftpDel("files/testHospital", "111.png");
			System.out.println("dele");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	void test() {
		
		String tableName = "user_primary_info inner join user_sub_info on user_primary_info.ssn = user_sub_info.ssn";
		String[] columns = new String[] {"user_primary_info.*, user_sub_info.*"};
		dbHelper.database = "medic";
		ArrayList<HashMap<String, String>> list = dbHelper.getList(tableName, columns);
		System.out.println(list.get(0));
		
		
		String tableName = "user_primary_info";
		String[] columns = new String[] {"distinct branch"};
		dbHelper.database = "medic";
		ArrayList<HashMap<String, String>> list = dbHelper.getList(tableName, columns);
		for(HashMap<String, String> map:list) {
			System.out.println(map.get("branch"));
		}
		
		//System.out.println(list.get(0));
	}
	*/
	/*
	void inserUsertest() {
		HashMap<String, String> map = new HashMap<String, String>();
	
		//每个test case都要写这句话
		dbHelper.database = "medic";
		
		map.put("ssn", "8898");
		map.put("name", "hahahahaa");
		map.put("department", "Oral");
		
		//test
		String output = dbHelper.insertUserHelper(map);
		boolean aaa  = dbHelper.insertUser(map);
		System.out.println(output);
		System.out.println(aaa);
	
		assertEquals("aa", "aa");
	}
	*/
	
	/*
	void deleteUserTest() {
		HashMap<String, String> map = new HashMap<String, String>();

		dbHelper.database = "medic";
		
		map.put("ssn", "666");
	//	map.put("name", "hahahahaa");
	//	map.put("department", "Oral");
		
		boolean output = dbHelper.deleteUser(map);
		System.out.println(output);
		
		assertEquals("aa", "aa");
		
	}
	*/
	
	/*
	void updateUser() {
		HashMap<String, String> map = new HashMap<String, String>();

		dbHelper.database = "medic";
		
		map.put("ssn", "333");
		map.put("name", "hlolllo");
		map.put("department", "O_O");
		map.put("sex", "male");
		map.put("address", "kinsrow ave.");
		
		String output = dbHelper.updateUserHelper(map);
		System.out.println(output);
		boolean bbb = dbHelper.updateUser(map);
		System.out.println(bbb);
		
	}
	 */
	
	/*
	void insertUserListTest() {
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();

		dbHelper.database = "medic";
		
		map.put("ssn", "1010");
		map.put("name", "yyyyy");
		map.put("department", "@_@");
		map.put("sex", "male");
		map.put("address", "kinsrow ave.");
		
		map2.put("ssn", "1111");
		map2.put("name", "xxxxx");
		map2.put("department", "X_X");
		map2.put("sex", "female");
		map2.put("address", "marine dr.");
		
		ArrayList<HashMap <String, String>> maplist = new ArrayList<HashMap<String, String>>();
		maplist.add(map);
		maplist.add(map2);
		System.out.println(maplist);
		
		boolean output = dbHelper.insertUserList(maplist);
		System.out.println(output);
				
	}
	*/
	
	/*
	void publishTest() {
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		HashMap<String, String> item = new HashMap<String, String>();

		dbHelper.database = "medic";
		
		map.put("ssn", "1010");
		map.put("name", "yyyyy");
		map.put("department", "@_@");
		map.put("sex", "male");
		map.put("address", "kinsrow ave.");
		//map.put("totalScore", "100");
		
		map2.put("ssn", "1111");
		map2.put("name", "xxxxx");
		map2.put("department", "X_X");
		map2.put("sex", "female");
		map2.put("address", "marine dr.");
		//map2.put("totalScore", "200");
		
		item.put("id", "3");
		item.put("totalPoint", "10");
		//item.put("examName", "hello world");
		
		ArrayList<HashMap <String, String>> userMaplist = new ArrayList<HashMap<String, String>>();
		userMaplist.add(map);
		userMaplist.add(map2);
		
		boolean output = dbHelper.publish(userMaplist, item, "exam_list");
		System.out.println(output);
		
	}
	*/
	
	
	
	/*
	void deleteMeetingTest() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		dbHelper.database = "medic";
		
		map.put("id", "2");
		boolean output = dbHelper.deleteMeeting(map);
		System.out.println(output);
	}
	 */
	
	/*
	void deleteStudyTest() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		//每个test case都要写这句话
		dbHelper.database = "medic";
		
		map.put("id", "2");
		boolean output = dbHelper.deleteStudy(map);
		System.out.println(output);
	}
	 
	*/
	
	/*
	void emptyScoreTest() {
		dbHelper.database = "medic";
		boolean output = dbHelper.emptyScore();
		System.out.println(output);
	}
	*/
	/*
	void insertExamTest() {
		dbHelper.database = "medic";
		
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		
		map.put("exam_id", "1");
		map.put("question", "护理队伍素质的高低决定了 优劣");
		map.put("answer", "A");
		map.put("option1", "护理质量");
		map.put("option2", "道德修养");
		map.put("option3", "医学道德");
		map.put("option4", "护理技术");
		map.put("option5", "");
		map.put("type", "S");
		
		map2.put("exam_id", "1");
		map2.put("question", "防病治病、 是社会主义医疗卫生工作的核心任务和基本内容。");
		map2.put("answer", "ABC");
		map2.put("option1", "身心健康");
		map2.put("option2", "精神文明");
		map2.put("option3", "救死扶伤");
		map2.put("option4", "人道主义");
		map2.put("option5", "人道主义人道主义");
		map2.put("type", "M");
		
		ArrayList<HashMap <String, String>> maplist = new ArrayList<HashMap<String, String>>();
		maplist.add(map);
		maplist.add(map2);
		System.out.println(maplist);
		
		boolean output = dbHelper.inserExam(maplist);
		System.out.println(output);
	}
	*/
}
