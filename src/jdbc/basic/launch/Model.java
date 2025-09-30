package jdbc.basic.launch;

import java.sql.Date;

public class Model {
	private int sn;
	private String userName;
	private String fullName;
	private String password;
	private String address;
	private int salary;
	private String fileName;
	private String fileUrl;
	private Date date;
	private String message;

	public Model(String userName, String fullName, String password, String address, int salary) {

		this.userName = userName;
		this.fullName = fullName;
		this.password = password;
		this.address = address;
		this.salary = salary;
	}

	public Model( String userName, String fullName, String password, String address, int salary, String fileName,
			String fileUrl, Date date, String message) {
		this(userName, fullName, password, address, salary);
		
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.date = date;
		this.message = message;
	}

	public int getSn() {
		return sn;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Model [sn=" + sn + ", userName=" + userName + ", fullName=" + fullName + ", password=" + password
				+ ", address=" + address + ", salary=" + salary + ", fileName=" + fileName + ", fileUrl=" + fileUrl
				+ ", date=" + date + ", message=" + message + "]";
	}
	

}
