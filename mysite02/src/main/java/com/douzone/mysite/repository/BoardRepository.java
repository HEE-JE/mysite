package com.douzone.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.douzone.mysite.vo.BoardVo;

public class BoardRepository {
	public boolean insert(BoardVo vo) {
		boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			if (vo.getgNo() == null) { // 새글
				String sql = "insert into board values(null, ?, ?, 0, now(), IFNULL((select max(g_no) from board a) + 1, 1), 1, 1, ?)";
				pstmt = connection.prepareStatement(sql);

				// 4. Parameter Mapping(binding)
				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContents());
				pstmt.setLong(3, vo.getUserNo());

			} else { // 답글
				String sql = "insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)";
				pstmt = connection.prepareStatement(sql);

				// 4. Parameter Mapping(binding)
				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContents());
				pstmt.setLong(3, vo.getgNo());
				pstmt.setLong(4, vo.getoNo() + 1);
				pstmt.setLong(5, vo.getDepth() + 1);
				pstmt.setLong(6, vo.getUserNo());

				BoardVo updateVo = new BoardVo();
				updateVo.setgNo(vo.getgNo());
				updateVo.setoNo(vo.getoNo() + 1);
				updateoNo(updateVo);
			}
			// 5. SQL 실행
			int count = pstmt.executeUpdate();
			result = count == 1;

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean delete(BoardVo vo) {
		boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			// 3. SQL 준비
			String sql = "delete from board where no = ?";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping(binding)
			pstmt.setLong(1, vo.getNo());

			// 5. SQL 실행
			int count = pstmt.executeUpdate();
			result = count == 1;

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean update(BoardVo vo) {
		boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			String sql = "update board set title=?, contents=? where no=?";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping(binding)
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());

			// 5. SQL 실행
			int count = pstmt.executeUpdate();
			result = count == 1;

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean updateoNo(BoardVo vo) {
		boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			String sql = "update board set o_no = o_no + 1 where g_no=? and o_no >= ?";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping(binding)
			pstmt.setLong(1, vo.getgNo());
			pstmt.setLong(2, vo.getoNo());

			// 5. SQL 실행
			int count = pstmt.executeUpdate();
			result = count == 1;

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean updateHit(BoardVo vo) {
		boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			String sql = "update board set hit = hit + 1 where no = ?";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping(binding)
			pstmt.setLong(1, vo.getNo());

			// 5. SQL 실행
			int count = pstmt.executeUpdate();
			result = count == 1;

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<BoardVo> findAll(int page) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			// 3. SQL 준비
			String sql = "select a.no, a.title, b.name, a.hit, date_format(a.reg_date, '%Y-%m-%d %r'), a.g_no, a.o_no, a.depth, a.user_no"
					+ "	from board a, user b" + "    where a.user_no = b.no"
					+ "	order by g_no desc, o_no asc limit ?, 5";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping
			pstmt.setInt(1, (page - 1) * 5);

			// 5. SQL 실행
			rs = pstmt.executeQuery();

			// 6. 결과처리
			while (rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setHit(rs.getInt(4));
				vo.setRegDate(rs.getString(5));
				vo.setgNo(rs.getLong(6));
				vo.setoNo(rs.getLong(7));
				vo.setDepth(rs.getInt(8));
				vo.setUserNo(rs.getLong(9));

				result.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public BoardVo findContents(BoardVo vo) {
		BoardVo result = new BoardVo();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			// 3. SQL 준비
			String sql = "select no, title, contents, g_no, o_no, depth, user_no from board where no=?";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping
			pstmt.setLong(1, vo.getNo());

			// 5. SQL 실행
			rs = pstmt.executeQuery();

			// 6. 결과처리
			if (rs.next()) {
				result.setNo(rs.getLong(1));
				result.setTitle(rs.getString(2));
				result.setContents(rs.getString(3));
				result.setgNo(rs.getLong(4));
				result.setoNo(rs.getLong(5));
				result.setDepth(rs.getInt(6));
				result.setUserNo(rs.getLong(7));
			}

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<BoardVo> findByKwd(int page, String kwd) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			// 3. SQL 준비
			String sql = "select a.no, a.title, b.name, a.hit, date_format(a.reg_date, '%Y-%m-%d %r'), a.g_no, a.o_no, a.depth, a.user_no"
					+ "	from board a, user b" + "    where a.user_no = b.no"
					+ "    and (b.name like concat('%', ?, '%')" + "	or a.title like concat('%', ?, '%'))"
					+ "	order by g_no desc, o_no asc limit ?, 5";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping
			pstmt.setString(1, kwd);
			pstmt.setString(2, kwd);
			pstmt.setInt(3, (page - 1) * 5);

			// 5. SQL 실행
			rs = pstmt.executeQuery();

			// 6. 결과처리
			while (rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setHit(rs.getInt(4));
				vo.setRegDate(rs.getString(5));
				vo.setgNo(rs.getLong(6));
				vo.setoNo(rs.getLong(7));
				vo.setDepth(rs.getInt(8));
				vo.setUserNo(rs.getLong(9));

				result.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int count() {
		int result = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			// 3. SQL 준비
			String sql = "select count(*) from board";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping

			// 5. SQL 실행
			rs = pstmt.executeQuery();

			// 6. 결과처리
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int countByKwd(String kwd) {
		int result = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			// 3. SQL 준비
			String sql = "select count(*)" + "	from board a, user b" + "    where a.user_no = b.no"
					+ "    and (b.name like concat('%', ?, '%')" + "	or a.title like concat('%', ?, '%'))";
			pstmt = connection.prepareStatement(sql);

			// 4. Parameter Mapping
			pstmt.setString(1, kwd);
			pstmt.setString(2, kwd);

			// 5. SQL 실행
			rs = pstmt.executeQuery();

			// 6. 결과처리
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			// 1. JDBC Driver(class) 로딩(JDBC Class 로딩: class loader)
			Class.forName("org.mariadb.jdbc.Driver");

			// 2. 연결하기
			String url = "jdbc:mysql://192.168.10.52:3306/webdb?charset=utf8";
			connection = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이브 로딩 실패:" + e);
		}
		return connection;
	}
}