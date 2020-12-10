package notice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NoticeDB {
	private Connection conn;
	private ResultSet rs;

	//기본 생성자
	public NoticeDB() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/jsp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
			String dbID = "root";
			String dbPassword = "mirim2";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//작성일자 메소드
		public String getDate() {
			String sql = "select now()";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString(1);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return ""; //데이터베이스 오류
		}
		
		//게시글 번호 부여 메소드
		public int getNext() {
			//현재 게시글을 내림차순으로 조회하여 가장 마지막 글의 번호를 구한다
			String sql = "select bsID from notice order by bsID desc";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getInt(1) + 1;
				}
				return 1; //첫 번째 게시물인 경우
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
		
		//글쓰기 메소드
		public int write(String bsTitle, String userID, String bsContent) {
			String sql = "insert into notice values(?, ?, ?, ?, ?, ?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getNext());
				pstmt.setString(2, bsTitle);
				pstmt.setString(3, userID);
				pstmt.setString(4, getDate());
				pstmt.setString(5, bsContent);
				pstmt.setInt(6, 1); //글의 유효번호
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
		
		//게시글 리스트 메소드
		public ArrayList<Notice> getList(int pageNumber){
			String sql = "select * from notice where bsID < ? and bsAvailable = 1 order by bsID desc limit 10";
			ArrayList<Notice> list = new ArrayList<Notice>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					Notice notice = new Notice();
					notice.setBsID(rs.getInt(1));
					notice.setBsTitle(rs.getString(2));
					notice.setUserID(rs.getString(3));
					notice.setBsDate(rs.getString(4));
					notice.setBsContent(rs.getString(5));
					notice.setBsAvailable(rs.getInt(6));
					list.add(notice);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
		//페이징 처리 메소드
		public boolean nextPage(int pageNumber) {
			String sql = "select * from notice where bsID < ? and bsAvailable = 1";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return true;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		//하나의 게시글을 보는 메소드
		public Notice getNotice(int bsID) {
			String sql = "select * from notice where bsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bsID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					Notice notice = new Notice();
					notice.setBsID(rs.getInt(1));
					notice.setBsTitle(rs.getString(2));
					notice.setUserID(rs.getString(3));
					notice.setBsDate(rs.getString(4));
					notice.setBsContent(rs.getString(5));
					notice.setBsAvailable(rs.getInt(6));
					return notice;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//게시글 수정 메소드
		public int update(int bsID, String bsTitle, String bsContent) {
			String sql = "update notice set bsTitle = ?, bsContent = ? where bsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bsTitle);
				pstmt.setString(2, bsContent);
				pstmt.setInt(3, bsID);
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
		
		//게시글 삭제 메소드
		public int delete(int bsID) {
			//실제 데이터를 삭제하는 것이 아니라 게시글 유효숫자를 '0'으로 수정한다
			String sql = "update notice set bsAvailable = 0 where bsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bsID);
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류 
		}
		
	}