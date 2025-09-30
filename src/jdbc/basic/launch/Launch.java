package jdbc.basic.launch;

import java.util.LinkedHashMap;

public class Launch {

	public static void main(String[] args) {
		MenuManager manager = new MenuManager();
		//manager.menuHandler();
		LinkedHashMap<String,Object> rowdata = new LinkedHashMap<>();
		rowdata.put("name", "rupesh");
		rowdata.put("email", "a@b");
		rowdata.put("gmail", "a@b");
		rowdata.put("cmail", "a@b");
		rowdata.put("tmail", "a@b");
		rowdata.put("omail", "a@b");
		int a = ConnectionFactory.insertDataUsingPS("employee", "product", rowdata);
		System.out.println(a);
		
			
		
	}

}
