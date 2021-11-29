import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class jdbcTest {

	static String memberID = "";
	static int memberKindInt = 0;
	static String memberKind = "";
	static String Admin = "";
	static Scanner scanner = new Scanner(System.in);
	static java.sql.Statement st = null;
	static ResultSet rs1 = null;
	static String url = "jdbc:mysql://localhost:3306/team_7?serverTimezone=Asia/Seoul&useSSL=false";
	static String pw = "6177spdlqj";

	// ----------------------------------
	static SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
	static Date time = new Date();
	static String time1 = format1.format(time);

	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("*******       도서 관리 프로그램               *******");
		System.out.print("1. 로그인   2. 회원가입    ㅡㅡㅡㅡ>  ");
		int x = scanner.nextInt();
		try {

			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			int rs = 0;

			if (x == 2) {
				String input = "insert in"
						+ "to team_7.member(memberName, memberId, memberPasswd, memberEmail, memberPhone, memberKind, borrowNum) ";
				input += "values(";
				System.out.print("* 이름을 입력하세요  : ");
				String name = scanner.next();
				input += ("'" + name + "'" + ", ");
				while (true) {
					System.out.print("* ID를 입력하세요  : ");
					String id = scanner.next();
					input += ("'" + id + "'" + ", ");
					ResultSet identify = null;
					identify = st.executeQuery("select memberId from team_7.member where memberId = '" + id + "'");
					String existId = "";
					while (identify.next()) {
						existId = identify.getString(1);
					}
					if (existId.equals(id)) {
						System.out.println("! 이미 존재하는 Id 입니다. 다시 입력하세요 !");
					} else {
						break;
					}
				}
				System.out.print("* 비밀번호를 입력하세요  : ");
				String passwd = scanner.next();
				input += ("'" + passwd + "'" + ", ");
				System.out.print("* 이메일을 입력하세요  : ");
				String email = scanner.next();
				input += ("'" + email + "'" + ", ");
				System.out.print("* 전화번호를 입력하세요  : ");
				String phoneNum = scanner.next();
				input += ("'" + phoneNum + "'" + ", ");
				System.out.print("* 신분을 입력하세요  (학부생 : 1, 대학원생 : 2, 교직원 : 3 ) ㅡㅡ> ");
				int kind = scanner.nextInt();
				String kindReal = "";
				switch (kind) {
				case 1:
					kindReal = "10";
					break;
				case 2:
					kindReal = "30";
					break;
				case 3:
					kindReal = "60";
					break;
				}
				input += (kindReal + ", ");
				input += ("0" + ")");
				rs = st.executeUpdate(input);
			}
			if (x == 1) {
				while (true) {
					String input = "select * from team_7.member where memberId = ";
					System.out.print("* ID를 입력하세요 (관리자 로그인 - ! 입력) : ");
					String id = scanner.next();
					if (id.equals("!")) {
						Admin = "admin";
						break;
					}
					input += ("'" + id + "'" + " and memberPasswd = '");
					System.out.print("* 비밀번호를 입력하세요  : ");
					String passwd = scanner.next();
					input += (passwd + "'");
					rs1 = st.executeQuery(input);
					String ID = "";
					String PW = "";
					while (rs1.next()) {
						ID = rs1.getString("memberId");
						PW = rs1.getString("memberPasswd");
						memberKind = rs1.getString("memberKind");
						memberKindInt = Integer.valueOf(memberKind);

					}
					if (ID.equals(id) && PW.equals(passwd)) {
						System.out.println("로그인 성공");
						memberID = ID;
//                  Thread.sleep(3000);
						clear();
						break;
					} else {
						System.out.println("잘못된 id와 비밀번호 입니다. 다시 입력하세요 !");
					}
				}
				execute();
			}

		} catch (SQLException sqex) {

			System.out.println("SQLException: " + sqex.getMessage());

			System.out.println("SQLState: " + sqex.getSQLState());

		}

	}

	private static void clear() throws IOException, InterruptedException {
		if (System.getProperty("os.name").startsWith("Windows")) {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			System.out.println("");
		} else
			Runtime.getRuntime().exec("clear");
	}

	// 로그인 후 메인 실행 화면
	private static void execute() throws SQLException, InterruptedException, IOException {
		while (true) {
			if (Admin.equals("admin")) {
				System.out.print("1. 대출 순위 확인  2. 반납승인   3. 도서 관리   4. 회원 관리    ㅡㅡ>  ");
				int doing = scanner.nextInt();
				if (doing == 1) {
					adminBorrowRank();
				} else if (doing == 2) {
					adminBorrowIdentify();
				} else if (doing == 3) {
					adminBook();
				} else if (doing == 4) {
					adminMember();
				} else {
					System.out.print("잘못된 숫자를 입력하였습니다. 다시 입력하세요!");
				}
			} else {
				System.out.print("1. 대출  2. 반납  3. 대출확인  4. 회원관리  5. 예약확인 ㅡㅡ>  ");
				int doing = scanner.nextInt();
				if (doing == 1) {
					memberBorrow();
				} else if (doing == 2) {
					memberReturnBook();
				} else if (doing == 3) {
					memberIdentify();
				} else if (doing == 4) {
					handleMember();
				} else if (doing == 5) {
					handleReservation();
				} else {
					System.out.print("잘못된 숫자를 입력하였습니다. 다시 입력하세요!");
				}
			}
		}

	}
	
	// 관리자 - 회원 정보 관리
	private static void adminMember() throws SQLException, InterruptedException, IOException {
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			String input = "select * from team_7.member";
			rs1 = st.executeQuery(input);
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			System.out.println("이름" + "\t" + "아이디" + "\t" + "비밀번호" + "\t" + "이메일" + "\t" + "전화번호");
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			while (rs1.next()) {
				System.out.print(rs1.getString("memberName") + "\t");
				System.out.print(rs1.getString("memberId") + "\t");
				System.out.print(rs1.getString("memberPasswd") + "\t");
				System.out.print(rs1.getString("memberEmail") + "\t");
				System.out.print(rs1.getString("memberPhone"));
				System.out.println();
			}
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			System.out.print("1. 회원 정보 수정  2. 회원탈퇴   ㅡㅡ>  ");
			int select = scanner.nextInt();
			if (select == 1) {
				System.out.print("수정하고자 하는 회원의 아이디를 입력하세요. ㅡㅡ>  ");
				String id = scanner.next();
				memberID = id;
				modifyMember();
			} else if (select == 2) {
				System.out.print("탈퇴시킬 회원의 아이디를 입력하세요. ㅡㅡ>  ");
				String id = scanner.next();
				String input2 = "delete from team_7.member where memberId = '" + id + "'";
				st.executeUpdate(input2);
				System.out.println("탈퇴를 완료하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 회원 - 도서검색과 대출 메뉴
	private static void memberBorrow() throws SQLException, InterruptedException, IOException {
		System.out.print("1. 도서검색  2. 대출   ㅡㅡ>  ");
		int select = scanner.nextInt();
		if (select == 1) {
			bookSearch();
		} else if (select == 2) {
			bookBorrow();
		}
	}

	// 회원 - 도서검색 함수
	private static void bookSearch() throws SQLException, InterruptedException, IOException {
		System.out.println("검색 방법을 선택해주세요.");
		System.out.print(" 1. ISBN   2. 도서제목  ㅡㅡㅡ>  ");
		int num = scanner.nextInt();
		String input = "";

		if (num == 1) {
			System.out.print("ISBN을 입력해주세요    ㅡㅡ>  ");
			String isbn = scanner.next();
			input = "select * from team_7.book where ISBN = '" + isbn + "'";
		} else if (num == 2) {
			System.out.print("도서제목을 입력해주세요   ㅡㅡ>  ");
			String name = scanner.next();
			input = "select * from team_7.book where bookName like '%" + name + "%'";
		} else {
			System.out.println("잘못된 숫자 입력입니다. 다시 입력해주세요!");
			System.out.println();
			execute();
		}
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			rs1 = st.executeQuery(input);
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			System.out.println("ISBN" + "\t" + "책 이름" + "\t" + "작가" + "\t" + "출판사" + "\t" + "수량");
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			while (rs1.next()) {
				System.out.print(rs1.getString("ISBN") + "\t");
				System.out.print(rs1.getString("bookName") + "\t");
				System.out.print(rs1.getString("bookAuthor") + "\t");
				System.out.print(rs1.getString("publish") + "\t");
				System.out.print(rs1.getString("quantity"));
				System.out.println();
			}
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			System.out.println("검색결과입니다.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 회원 - 반납신청
	private static void memberReturnBook() throws SQLException {
		rs1 = st.executeQuery("select * from team_7.memberborrowbook where memberId = '" + memberID + "'");
		System.out.println("---------------------------------------");
		int i = 1;
		while (rs1.next()) {
			String ISBN = rs1.getString(1);
			String title = rs1.getString(2);
			String author = rs1.getString(3);
			System.out.println(i + "\t" + ISBN + "\t " + title + "\t" + author + "\t");
			i++;
		}
		System.out.println("---------------------------------------");
		System.out.print("* 반납하고자 하는 도서의 번호를 입력하세요    ㅡ> ");
		String apply = scanner.next();
		int rs;
		rs1 = st.executeQuery("select borrowBookISBN from team_7.memberborrowbook where memberId = '" + memberID
				+ "' limit " + apply);
		String ISBN = "";
		while (rs1.next()) {
			ISBN = rs1.getString(1);
		}
		String input = "insert into team_7.return(returnISBN, returnApply, returnIdentify, returnmemberId) values('";
		input += (ISBN + "', " + "'1', '0', '" + memberID + "')");
		rs = st.executeUpdate(input);
		System.out.println("* 반납신청 되었습니다. ");
	}

	// 회원 - 도서대출내역
	private static void memberIdentify() throws SQLException {
		System.out.println("**** 회원님의 도서 대출 내역입니다. ****");
		rs1 = st.executeQuery("select * from team_7.memberborrowbook where memberId = '" + memberID + "'");
		System.out.println("ISBN " + "\t" + "제목\t저자");
		System.out.println("---------------------------------------");
		while (rs1.next()) {
			String ISBN = rs1.getString(1);
			String title = rs1.getString(2);
			String author = rs1.getString(3);
			System.out.println(ISBN + "\t " + title + "\t" + author + "\t");
		}
		System.out.println("---------------------------------------");
	}

	// 관리자 - 회원 대출목록 랭킹10위
	private static void adminBorrowRank() throws SQLException {
		System.out.println("**** 가장 대출을 많이 한 10명의 회원 목록입니다 ****");
		st.executeUpdate("delete from team_7.borrowrank");
		rs1 = st.executeQuery("select borrowMemberId, count(*) from team_7.borrowidentify group by borrowMemberId");
		while (rs1.next()) {
			String borrowMemberId = rs1.getString(1);
			String count = rs1.getString(2);
			Connection con1 = null;
			con1 = DriverManager.getConnection(url, "root", pw);
			java.sql.Statement st1 = con1.createStatement();
			int rs = st1.executeUpdate("insert into team_7.borrowrank(memberId, borrowNumber) values('" + borrowMemberId
					+ "', " + count + ")");
		}
		rs1 = st.executeQuery("select memberId from team_7.borrowrank order by borrowNumber desc");
		int rank = 1;
		while (rs1.next() && rank <= 10) {
			String name = rs1.getString(1);
			System.out.println(rank + "\t" + name);
			rank++;
		}
	}

	// 관리자 - 반납대기목록
	private static void adminBorrowIdentify() throws SQLException {
		rs1 = st.executeQuery("select * from team_7.return where returnApply = 1 and returnIdentify = 0");
		System.out.println("** 반납 대기 목록입니다 **");
		System.out.println("---------------------------------------");
		int i = 1;
		while (rs1.next()) {
			String ISBN = rs1.getString(1);
			String returnApply = rs1.getString(2);
			String returnIdentify = rs1.getString(3);
			String returnmemberId = rs1.getString(4);
			System.out.println(i + "\t" + ISBN + "\t " + "\t" + returnmemberId + "\t");
			i++;
		}
		System.out.println("---------------------------------------");
		System.out.println("* 반납 승인을 하고자 하는 도서의 번호를 입력하세요  ");
		System.out.print("* 같은 책이라면 한 번에 처리됩니다  ㅡ> ");
		String apply = scanner.next();
		rs1 = st.executeQuery(
				"select * from (select * from team_7.return where returnApply = 1 and returnIdentify = 0) as temp1 limit "
						+ apply);
		String ISBN = "";
		String returnIdentify = "";
		String returnmemberId = "";
		int temp1 = 0;
		while (rs1.next()) {
			ISBN = rs1.getString(1);
			returnmemberId = rs1.getString(4);
			temp1++;
		}
		int rs;
		if (temp1 != 0) {

			rs1 = st.executeQuery(
					"select * from team_7.return where returnIdentify = 0 and returnApply = 1 and returnmemberId = '"
							+ returnmemberId + "' and returnISBN = '" + ISBN + "'");
			String isbn = "";
			String returnApply = "";
			String identify = "";
			String returnId = "";
			int t2 = 0;
			while (rs1.next()) {
				t2++;
				isbn = rs1.getString(1);
				returnApply = rs1.getString(2);
				identify = rs1.getString(3);
				returnId = rs1.getString(4);
			}
			rs = st.executeUpdate("update team_7.member set borrowNum = borrowNum -" + t2 + " where memberId = '"
					+ returnmemberId + "'");
			rs = st.executeUpdate(
					"update team_7.book set quantity = quantity + " + t2 + " where ISBN = '" + ISBN + "'");
			rs = st.executeUpdate(
					"update team_7.return set returnIdentify = 1 where returnIdentify = 0 and returnApply = 1 and returnmemberId = '"
							+ returnmemberId + "' and returnISBN = '" + ISBN + "'");
			rs = st.executeUpdate("delete from team_7.memberborrowbook where memberId = '" + returnmemberId
					+ "' and borrowBookISBN = '" + ISBN + "'");
			System.out.println("* 반납승인 되었습니다. ");
		} else {
			System.out.println("* 반납 요청된 도서가 없습니다 !");
		}
	}

	// 회원 - 대출 가능 도서 목록, 대출
	private static void bookBorrow() throws SQLException {
		rs1 = st.executeQuery("select * from team_7.book");
		System.out.println(" **** 현재 대출 가능 도서 목록입니다  ****");
		System.out.println("------------------------------------------------------");
		System.out.println("|\t\tISBN\t제목\t저자\t출판사\t권수\t|");
		String space = "";
		int num = 1;
		while (rs1.next()) {
			space = "";
			String ISBN = rs1.getString(1) + "\t";
			String title = rs1.getString(2) + "\t";
			String author = rs1.getString(3) + "\t";
			String publish = rs1.getString(4) + "\t";
//         String date = rs1.getString(5) + "\t";
			String number = rs1.getString(6) + "\t";
//         if (date == null || date.equals("null")) {
//            date = "\t\t\t";
//         } else {
//            date += "\t";
//         }
			System.out.println("|\t" + num + "\t" + ISBN + title + author + publish + number + "|");
			num++;
		}
		System.out.println("--------------------------------------------------");
		System.out.print(" ** 대출할 도서의 번호를 입력하세요  ㅡㅡ>  ");
		String bookNum = scanner.next();
		rs1 = st.executeQuery("select * from team_7.book limit " + bookNum);
		String ISBN = "";
		String title = "";
		String author = "";
		String publish = "";
		String date = "";
		String number = "";
		while (rs1.next()) {
			ISBN = rs1.getString(1);
			title = rs1.getString(2);
			author = rs1.getString(3);
			publish = rs1.getString(4);
			date = rs1.getString(5);
			number = rs1.getString(6);
		}
		int quantity = Integer.valueOf(number);
		if (quantity < 1) {
			System.out.print(" * 수량이 부족하여 대출할 수 없습니다. 예약하시겠습니까 ? (Y/N) ㅡ> ");
			String response = scanner.next();
			if (response.equals("Y")) {
				String input = "insert into team_7.reservation(ISBN, memberKind, reserveDate, memberId) values(";
				input += ("'" + ISBN + "', " + memberKind + ", '" + time1 + "', '" + memberID + "')");
				int rs = st.executeUpdate(input);
			}
		} else {

			System.out.println(" * 대출이 완료되었습니다. ");
			String input = "update team_7.member set borrowNum = borrowNum + 1 where memberId = '" + memberID + "'";
			int rs = st.executeUpdate(input);
			input = "update team_7.book set quantity = quantity - 1 where ISBN = ' " + ISBN + "'";
			rs = st.executeUpdate(input);
			input = "insert into team_7.borrowidentify(borrowMemberId, borrowDate) values('";
			input += (memberID + "', '" + time1 + "')");
			rs = st.executeUpdate(input);
			input = "insert into team_7.memberborrowbook(borrowBookISBN, borrowBookName, borrowBookAuthor, memberId) values('";
			input += (ISBN + "', '" + title + "', '" + author + "', '" + memberID + "')");
			rs = st.executeUpdate(input);
		}
	}

	// 관리자 - 도서  등록, 도서 수정, 도서 삭제
	private static void adminBook() throws InterruptedException, IOException {
		try {

			Connection con = null;

			// connection 얻어오기
			con = DriverManager.getConnection(url, "root", pw);
			java.sql.Statement st = null;

			ResultSet rs1 = null;
			int rs = 0;

			// query문을 작성하여 적용하기 위한 용도
			st = con.createStatement();

			System.out.print("1.도서 정보 등록   2.도서 정보 수정   3. 도서 정보 삭제     ㅡㅡㅡㅡ>  ");
//          System.out.println();
			int care = scanner.nextInt();
			if (care == 1) {
				// 도서 정보 등록
				String input = "insert in" + "to team_7.book(ISBN, bookName, bookAuthor, publish, quantity)";
				input += "values(";
				System.out.print("* 등록하려는 도서의 ISBN을 입력하세요 : ");
				String isbn = scanner.next();
				input += ("'" + isbn + "'" + ", ");
				ResultSet identify = null;
				identify = st.executeQuery("select ISBN from team_7.book where ISBN = '" + isbn + "'");

				System.out.print("* 등록하려는 도서의 제목을 입력하세요 : ");
				String bookName = scanner.next();
				input += ("'" + bookName + "'" + ", ");

				System.out.print("* 등록하려는 도서의 저자를 입력하세요 : ");
				String bookAuthor = scanner.next();
				input += ("'" + bookAuthor + "'" + ", ");

				System.out.print("* 등록하려는 도서의 출판사를 입력하세요 : ");
				String publish = scanner.next();
				input += ("'" + publish + "'" + ", ");

				System.out.print("* 등록하려는 도서의 수량을 입력하세요 : ");
				String quantity = scanner.next();
				input += ("'" + quantity + "')");
				rs = st.executeUpdate(input);
				System.out.println("** 도서 등록이 완료되었습니다! **");
				System.out.println();
				execute();

			} else if (care == 2) {
				// 도서 정보 수정
				rs1 = st.executeQuery("select ISBN from team_7.book");
				System.out.println("도서 정보 수정을 하기 위한 검색 방법을 선택해주세요.");
				System.out.print(" 1. ISBN   2. 도서제목  ㅡㅡㅡ>  ");
				int num = scanner.nextInt();

				if (num == 1) {
					searchForISBN();
				} else if (num == 2) {
					searchForBookName();
				} else {
					System.out.println("잘못된 숫자 입력입니다. 다시 입력해주세요!");
					System.out.println();
					execute();
				}

			} else {
				// 도서 정보 삭제
				System.out.println("도서 정보 삭제를 위한 검색 방법을 선택해주세요.");
				System.out.print("1. ISBN으로 검색   2. 도서제목으로 검색  ㅡㅡㅡ> ");
				int num = scanner.nextInt();
				System.out.println();

				if (num == 1) {
					deleteForISBN();
				} else if (num == 2) {
					deleteForBookName();
				} else {
					System.out.println("잘못된 숫자 입력입니다. 다시 입력해주세요!");
					System.out.println();
					execute();
				}
			}

		} catch (SQLException sqex) {

			System.out.println("SQLException: " + sqex.getMessage());

			System.out.println("SQLState: " + sqex.getSQLState());

		}
	}

	// 관리자 - 도서 정보 수정 시 필요한 검색함수(ISBN 이용)
	private static void searchForISBN() throws SQLException, InterruptedException, IOException {

		String in = "";
		System.out.print("수정할 ISBN을 입력하세요. ㅡㅡㅡ> ");
		String writeISBN = scanner.next();
		String input = "select * from team_7.book where ISBN = '" + writeISBN + "'";
		ResultSet rs = st.executeQuery(input);

		while (rs.next()) {

			if (rs.getString(1).equals(writeISBN)) {
				System.out.println("수정하고자 하는 항목을 고르세요.");
				System.out.print("1. ISBN  2. 제목   3. 출판사   4. 수량  ----->  ");
				int modifyNum = scanner.nextInt();

				if (modifyNum == 1) {
					System.out.print("수정할 ISBN을 입력하세요. ㅡㅡ> ");
					String isbn = scanner.next();
					in = "update team_7.book set ISBN = '" + isbn + "'" + "where ISBN = '" + writeISBN + "'";
				} else if (modifyNum == 2) {
					System.out.print("수정할 제목을 입력하세요. ㅡㅡ> ");
					String bookName = scanner.next();
					in = "update team_7.book set bookName = '" + bookName + "'" + "where ISBN = '" + writeISBN + "'";
				} else if (modifyNum == 3) {
					System.out.print("수정할 출판사를 입력하세요. ㅡㅡ> ");
					String publish = scanner.next();
					in = "update team_7.book set publish = '" + publish + "'" + "where ISBN = '" + writeISBN + "'";
				} else if (modifyNum == 4) {
					System.out.print("수정할 수량을 입력하세요. ㅡㅡ> ");
					String quantity = scanner.next();
					in = "update team_7.book set quantity = '" + quantity + "'" + "where ISBN = '" + writeISBN + "'";
				} else {
					System.out.print("다시 입력해주세요!");
					System.out.println();
					searchForISBN();
				}
			}

		}
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			st.execute(in);
			System.out.println("수정이 완료되었습니다.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 관리자 - 도서 정보 수정 및 삭제 시 필요한 검색함수(도서제목 이용)
	private static void searchForBookName() throws SQLException, InterruptedException, IOException {
		String bn = "bookName";
		String in = "";
		System.out.print("수정할 도서 제목을 입력하세요. ㅡㅡㅡ> ");
		String writeBookName = scanner.next();
		String input = "select * from team_7.book where bookName = '" + writeBookName + "'";
		ResultSet rs = st.executeQuery(input);
//      System.out.println(rs);

		while (rs.next()) {
//         System.out.println(rs.getString(1));

			if (rs.getString(bn).equals(writeBookName)) {
				System.out.println("수정하고자 하는 항목을 고르세요.");
				System.out.print("1. ISBN  2. 제목   3. 출판사   4. 수량  ----->  ");
				int modifyNum = scanner.nextInt();

				if (modifyNum == 1) {
					System.out.print("수정할 ISBN을 입력하세요. ㅡㅡ> ");
					String isbn = scanner.next();
					in = "update team_7.book set ISBN = '" + isbn + "'" + "where bookName = '" + writeBookName + "'";
				} else if (modifyNum == 2) {
					System.out.print("수정할 제목을 입력하세요. ㅡㅡ> ");
					String bookName = scanner.next();
					in = "update team_7.book set bookName = '" + bookName + "'" + "where bookName = '" + writeBookName
							+ "'";
				} else if (modifyNum == 3) {
					System.out.print("수정할 출판사를 입력하세요. ㅡㅡ> ");
					String publish = scanner.next();
					in = "update team_7.book set publish = '" + publish + "'" + "where bookName = '" + writeBookName
							+ "'";
				} else if (modifyNum == 4) {
					System.out.print("수정할 수량을 입력하세요. ㅡㅡ> ");
					String quantity = scanner.next();
					in = "update team_7.book set quantity = '" + quantity + "'" + "where bookName = '" + writeBookName
							+ "'";
				} else {
					System.out.print("다시 입력해주세요!");
					System.out.println();
					searchForBookName();
				}
			}

		}
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			st.execute(in);
			System.out.println("수정이 완료되었습니다.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 관리자 - 도서 정보 삭제 시 필요한 검색함수(ISBN 이용)
	private static void deleteForISBN() throws SQLException, InterruptedException, IOException {

		Connection con = null;
		con = DriverManager.getConnection(url, "root", pw);
		PreparedStatement st1 = null;
//      String in = "";
		System.out.print("삭제할 ISBN을 입력하세요. ㅡㅡㅡ> ");
		String writeISBN = scanner.next();

		System.out.print("선택하신 도서를 삭제하시겠습니까? (y/n) ㅡㅡㅡ>  ");
		String sel = scanner.next();
		String input = "";

		if (sel.equals("y")) {
			input = "delete from team_7.book where ISBN = '" + writeISBN + "'";

		} else if (sel.equals("n")) {
			execute();
		} else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요!");
			deleteForISBN();
		}
		System.out.println("도서 삭제가 완료되었습니다.");
		st1 = con.prepareStatement(input);
		st1.execute();
	}

	// 관리자 - 도서 정보 삭제 시 필요한 검색함수(도서제목 이용)
	private static void deleteForBookName() throws SQLException, InterruptedException, IOException {

		Connection con = null;
		con = DriverManager.getConnection(url, "root", pw);
		PreparedStatement st1 = null;
//      String in = "";
		System.out.print("삭제할 도서 제목을 입력하세요. ㅡㅡㅡ> ");
		String writeBookName = scanner.next();

		System.out.print("선택하신 도서를 삭제하시겠습니까? (y/n) ㅡㅡㅡ>  ");
		String sel = scanner.next();
		String input = "";

		if (sel.equals("y")) {
			input = "delete from team_7.book where bookName = '" + writeBookName + "'";

		} else if (sel.equals("n")) {
			execute();
		} else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요!");
			deleteForISBN();
		}
		System.out.println("도서 삭제가 완료되었습니다.");
		st1 = con.prepareStatement(input);
		st1.execute();
	}

	// 회원 - 회원 정보 관리
	private static void handleMember() throws InterruptedException, IOException {
		System.out.println("** 회원관리  **");
		System.out.print("1. 회원정보수정   2. 회원탈퇴    3. 회원정보보기    ㅡㅡ>  ");
		int select = scanner.nextInt();
		if (select == 1) {
			modifyMember();
		} else if (select == 2) {
			deleteMember();
		} else if (select == 3) {
			checkMember();
		} else {
			System.out.print("잘못된 숫자를 입력하였습니다.");
			System.out.println();
			handleMember();
		}
	}

	// 회원 - 회원 정보 수정
	private static void modifyMember() throws InterruptedException, IOException {
		System.out.println("수정하고자 하는 항목을 고르세요.");
		System.out.print("1. 이름   2. 비밀번호   3. 이메일  4. 전화번호   ㅡㅡ>  ");
		int select = scanner.nextInt();
		String input = "";
		if (select == 1) {
			System.out.print("수정할 내용을 입력하세요. ㅡㅡ>   ");
			String name = scanner.next();
			input = "update team_7.member set memberName = '" + name + "'" + " where memberId = '" + memberID + "'";
		} else if (select == 2) {
			System.out.print("수정할 내용을 입력하세요. ㅡㅡ>   ");
			String pw = scanner.next();
			input = "update team_7.member set memberPasswd = '" + pw + "'" + " where memberId = '" + memberID + "'";
		} else if (select == 3) {
			System.out.print("수정할 내용을 입력하세요. ㅡㅡ>   ");
			String email = scanner.next();
			input = "update team_7.member set memberEmail = '" + email + "'" + " where memberId = '" + memberID + "'";
		} else if (select == 4) {
			System.out.print("수정할 내용을 입력하세요. ㅡㅡ>   ");
			String phone = scanner.next();
			input = "update team_7.member set memberPhone = '" + phone + "'" + " where memberId = '" + memberID + "'";
		} else {
			System.out.print("잘못된 숫자를 입력하였습니다.");
			System.out.println();
			modifyMember();
		}
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			st.execute(input);
			System.out.println("변경되었습니다.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 회원 - 회원 정보 삭제
	private static void deleteMember() throws InterruptedException, IOException {
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			PreparedStatement st1 = null;
			System.out.print("정말 탈퇴하시겠습니까 ? (y/n)  ㅡㅡ>  ");
			String choice = scanner.next();
			String input = "";
			if (choice.equals("y")) {
				input = "delete from team_7.member where memberId = '" + memberID + "'";

			} else if (choice.equals("n")) {
				execute();
			} else {
				System.out.println("잘못 입력하셨습니다.");
				deleteMember();
			}
			System.out.print("탈퇴가 완료되었습니다.");
			st1 = con.prepareStatement(input);
			st1.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 회원 - 회원 정보 확인
	private static void checkMember() throws InterruptedException, IOException {
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			String input = "select * from team_7.member where memberId = '" + memberID + "'";
			rs1 = st.executeQuery(input);
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			System.out.println("이름" + "\t" + "아이디" + "\t" + "비밀번호" + "\t" + "이메일" + "\t" + "전화번호");
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			while (rs1.next()) {
				System.out.print(rs1.getString("memberName") + "\t");
				System.out.print(rs1.getString("memberId") + "\t");
				System.out.print(rs1.getString("memberPasswd") + "\t");
				System.out.print(rs1.getString("memberEmail") + "\t");
				System.out.print(rs1.getString("memberPhone"));
				System.out.println();
			}
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 회원 - 예약 확인
	private static void handleReservation() throws InterruptedException, IOException {
		try {
			int num = 1;
			PreparedStatement pst1 = null;
			PreparedStatement pst2 = null;
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			String input = "select * from team_7.reservation where memberId = '" + memberID + "'";
			rs1 = st.executeQuery(input);
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			System.out.println("\tISBN" + "\t" + "대출 가능 날짜" + "\t" + "현재 예약대기자 수");
			System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
			while (rs1.next()) {
				int date = 0;
				String input2 = "select count(ISBN) from team_7.reservation where ISBN = " + rs1.getString("ISBN");
				pst1 = con.prepareStatement(input2);
				ResultSet rs2 = pst1.executeQuery();
				String input3 = "select memberKind from team_7.reservation where memberId != '" + memberID
						+ "' and ISBN = " + rs1.getInt("ISBN");
				pst2 = con.prepareStatement(input3);
				ResultSet rs3 = pst2.executeQuery();
				while (rs3.next()) {
					date = date + rs3.getInt("memberKind");
				}
				String checkDate = dateCalculator(rs1.getInt("reserveDate"), date);
//               System.out.println(checkDate);
				while (rs2.next()) {
					System.out.print(num + "\t");
					System.out.print(rs1.getString("ISBN") + "\t");
					System.out.print(checkDate + "\t");
					System.out.print(rs2.getInt(1) - 1);
					System.out.println();
					num++;
				}
			}
			System.out.print("예약된 도서를 취소하시겠습니까 ? (y/n)  ㅡㅡ>  ");
			String choice = scanner.next();
			if (choice.equals("y")) {
				cancelReservation();
			} else if (choice.equals("n")) {
				execute();
			} else {
				System.out.println("잘못 입력하셨습니다.");
				handleReservation();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 회원 - 예약 취소
	private static void cancelReservation() throws InterruptedException, IOException {
		System.out.print("예약 취소하고자 하는 책 번호를 입력하세요.  ㅡㅡ>  ");
		int select = scanner.nextInt();
		String input = "select ISBN from team_7.reservation where memberId = '" + memberID + "' limit " + (select - 1)
				+ "," + select;
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			rs1 = st.executeQuery(input);
//            System.out.println(input);
			rs1.next();
			input = "delete from team_7.reservation where ISBN = '" + rs1.getString("ISBN") + "' and memberId = '"
					+ memberID + "'";
//            System.out.println(input);
			st = con.createStatement();
			st.executeUpdate(input);
			System.out.println("취소되었습니다.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 오늘날짜와 반납일을 매개변수로 한 날짜 계산 함수
	private static String dateCalculator(int current, int new1) throws InterruptedException, IOException {
		int date = current;
		String temp = Integer.toString(date);
		int year = Integer.parseInt(temp.substring(0, 4));
//      System.out.println(year);
		int month = Integer.parseInt(temp.substring(4, 6));
//      System.out.println(month);
		int day = Integer.parseInt(temp.substring(6, 8)) + new1;
//      System.out.println(day);
		while (day > 28) {
			if (month == 2) {
				if (day > 28) {
					day = day - 28;
					month = month + 1;
					if (month > 12) {
						month = month - 12;
						year = year + 1;
					}
				} else {
					break;
				}
			} else if (month == 4 || month == 6 || month == 9 || month == 11) {
				if (day > 30) {
					day = day - 30;
					month = month + 1;
					if (month > 12) {
						month = month - 12;
						year = year + 1;
					}
				} else {
					break;
				}
			} else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
					|| month == 12) {
				if (day > 31) {
					day = day - 31;
					month = month + 1;
					if (month > 12) {
						month = month - 12;
						year = year + 1;
					}
				} else {
					break;
				}
			}
		}
		String yyyy = Integer.toString(year);
		String mm = Integer.toString(month);
		String dd = Integer.toString(day);
		if (month < 10) {
			mm = "0" + mm;
		}
		if (day < 10) {
			dd = "0" + dd;
		}
		String result = yyyy + mm + dd;
		return result;
	}
}