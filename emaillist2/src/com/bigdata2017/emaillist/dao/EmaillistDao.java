package com.bigdata2017.emaillist.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bigdata2017.emaillist.vo.EmaillistVo;

public class EmaillistDao {

	private Connection getConnection() 
			throws SQLException {

		Connection conn = null;

		try {
			//1. JDBC 드라이버 로딩(JDBC 클래스 로딩)
			Class.forName( "oracle.jdbc.driver.OracleDriver" );

			//2. Connection 가져오기
			/* "jdbc:oracle:thin:" 이건 정해져 있는 포맷 */
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection( url, "webdb", "webdb" );
		} catch (ClassNotFoundException e) {
			System.out.println( "드라이버 로딩 실패:" + e );
		}

		return conn; 
	}
	
	public List<EmaillistVo> getList(){
		List<EmaillistVo> list = new ArrayList<EmaillistVo>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			//3. Statement 객체 생성
			stmt = conn.createStatement();

			//4. SQL문 실행 - 쿼리문은 에러가 많을 수 있으니, 따로 쿼리문만 테스트해 본 후 추가할 것, JDBC를 사용할 때는 ';'이 있으면 jdbc가 쿼리문이 더 있는 것으로 생각함
			String sql = 
					" select no, first_name, last_name, email" + 
					"   from emaillist";
			rs = stmt.executeQuery(sql);

			//5. 결과 가져오기 - 입력한 순서대로 컬럼이 만들어지니 컬럼의 번호로 가져옴
			while( rs.next() ) {
				long no = rs.getLong( 1 );	// 보통 employee_id같은 no는 큰 값이니까 넉넉하게 long으로 사용
				String firstName = rs.getString( 2 );
				String lastName = rs.getString( 3 );
				String email = rs.getString(4);

				EmaillistVo vo = new EmaillistVo();
				vo.setNo(no);
				vo.setFirstName(firstName);
				vo.setLastName(lastName);
				vo.setEmail(email);

				list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println( "연결실패 : " + e );
		} finally {
			//3. 자원 정리
			try {
				if( rs != null ) {
					rs.close();
				}
				if( stmt != null ) {
					stmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}
	

		
	public int delete() {
		int count = 0;

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();

			//3. Statement 객체 생성
			stmt = conn.createStatement();

			//4. SQL문 실행 - 쿼리문은 에러가 많을 수 있으니, 따로 쿼리문만 테스트해 본 후 추가할 것, JDBC를 사용할 때는 ';'이 있으면 jdbc가 쿼리문이 더 있는 것으로 생각함
			String sql = "delete from emaillist";
			count = stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println( "연결실패 : " + e );
		} finally {
			//3. 자원 정리
			try {
				if( stmt != null ) {
					stmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}
		
	public int insert( EmaillistVo vo ) {
		int count = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			//3. Statement 준비
			String sql = 
					" insert" + 
					"   into emaillist" + 
					" values( seq_emaillist.nextval, ?, ?, ?)";
			/* prepareStatement는 Statement의 상속을 받아 구현되었으며, jdbc에 sql문을 미리 준비시켜놓고, 값들은 '?'로 놔두었다가 나중에 java코드에서 바인딩하면서 값을 세팅한다. */
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩 - ?에다가 값을 세팅하게됨
			pstmt.setString( 1, vo.getFirstName() );
			pstmt.setString( 2, vo.getLastName() );
			pstmt.setString( 3, vo.getEmail() );

			//5. SQL문 실행 - 주의!!!!sql을 넣어주면 안됨!!
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println( "연결실패 : " + e );
		} finally {
			//3. 자원 정리
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if( conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}
}
