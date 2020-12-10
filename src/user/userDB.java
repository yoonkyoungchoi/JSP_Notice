package user;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class userDB {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public userDB() { // 생성자 실행될때마다 자동으로 db연결이 이루어 질 수 있도록함
		try {
			String dbURL = "jdbc:mysql://localhost:3306/jsp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
			String dbID = "root";
			String dbPW = "mirim2";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
		} catch (Exception e) {
			e.printStackTrace(); // 오류가 무엇인지 출력
		}
	}
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM user WHERE userID = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(userPassword)) {
					return 1;
				} else
					return 0;
			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO user VALUES (?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return -1; // DB 오류
	}
}
