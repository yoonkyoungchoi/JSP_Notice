package notice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NoticeDB {
	private Connection conn;
	private ResultSet rs;

	//�⺻ ������
	public NoticeDB() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/jsp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; // localhost:3306 ��Ʈ�� ��ǻ�ͼ�ġ�� mysql�ּ�
			String dbID = "root";
			String dbPassword = "mirim2";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//�ۼ����� �޼ҵ�
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
			return ""; //�����ͺ��̽� ����
		}
		
		//�Խñ� ��ȣ �ο� �޼ҵ�
		public int getNext() {
			//���� �Խñ��� ������������ ��ȸ�Ͽ� ���� ������ ���� ��ȣ�� ���Ѵ�
			String sql = "select bsID from notice order by bsID desc";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getInt(1) + 1;
				}
				return 1; //ù ��° �Խù��� ���
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //�����ͺ��̽� ����
		}
		
		//�۾��� �޼ҵ�
		public int write(String bsTitle, String userID, String bsContent) {
			String sql = "insert into notice values(?, ?, ?, ?, ?, ?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, getNext());
				pstmt.setString(2, bsTitle);
				pstmt.setString(3, userID);
				pstmt.setString(4, getDate());
				pstmt.setString(5, bsContent);
				pstmt.setInt(6, 1); //���� ��ȿ��ȣ
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //�����ͺ��̽� ����
		}
		
		//�Խñ� ����Ʈ �޼ҵ�
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
		
		//����¡ ó�� �޼ҵ�
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
		
		//�ϳ��� �Խñ��� ���� �޼ҵ�
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
		
		//�Խñ� ���� �޼ҵ�
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
			return -1; //�����ͺ��̽� ����
		}
		
		//�Խñ� ���� �޼ҵ�
		public int delete(int bsID) {
			//���� �����͸� �����ϴ� ���� �ƴ϶� �Խñ� ��ȿ���ڸ� '0'���� �����Ѵ�
			String sql = "update notice set bsAvailable = 0 where bsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bsID);
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //�����ͺ��̽� ���� 
		}
		
	}