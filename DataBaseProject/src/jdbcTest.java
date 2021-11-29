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
		System.out.println("*******       ���� ���� ���α׷�               *******");
		System.out.print("1. �α���   2. ȸ������    �ѤѤѤ�>  ");
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
				System.out.print("* �̸��� �Է��ϼ���  : ");
				String name = scanner.next();
				input += ("'" + name + "'" + ", ");
				while (true) {
					System.out.print("* ID�� �Է��ϼ���  : ");
					String id = scanner.next();
					input += ("'" + id + "'" + ", ");
					ResultSet identify = null;
					identify = st.executeQuery("select memberId from team_7.member where memberId = '" + id + "'");
					String existId = "";
					while (identify.next()) {
						existId = identify.getString(1);
					}
					if (existId.equals(id)) {
						System.out.println("! �̹� �����ϴ� Id �Դϴ�. �ٽ� �Է��ϼ��� !");
					} else {
						break;
					}
				}
				System.out.print("* ��й�ȣ�� �Է��ϼ���  : ");
				String passwd = scanner.next();
				input += ("'" + passwd + "'" + ", ");
				System.out.print("* �̸����� �Է��ϼ���  : ");
				String email = scanner.next();
				input += ("'" + email + "'" + ", ");
				System.out.print("* ��ȭ��ȣ�� �Է��ϼ���  : ");
				String phoneNum = scanner.next();
				input += ("'" + phoneNum + "'" + ", ");
				System.out.print("* �ź��� �Է��ϼ���  (�кλ� : 1, ���п��� : 2, ������ : 3 ) �Ѥ�> ");
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
					System.out.print("* ID�� �Է��ϼ��� (������ �α��� - ! �Է�) : ");
					String id = scanner.next();
					if (id.equals("!")) {
						Admin = "admin";
						break;
					}
					input += ("'" + id + "'" + " and memberPasswd = '");
					System.out.print("* ��й�ȣ�� �Է��ϼ���  : ");
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
						System.out.println("�α��� ����");
						memberID = ID;
//                  Thread.sleep(3000);
						clear();
						break;
					} else {
						System.out.println("�߸��� id�� ��й�ȣ �Դϴ�. �ٽ� �Է��ϼ��� !");
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

	// �α��� �� ���� ���� ȭ��
	private static void execute() throws SQLException, InterruptedException, IOException {
		while (true) {
			if (Admin.equals("admin")) {
				System.out.print("1. ���� ���� Ȯ��  2. �ݳ�����   3. ���� ����   4. ȸ�� ����    �Ѥ�>  ");
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
					System.out.print("�߸��� ���ڸ� �Է��Ͽ����ϴ�. �ٽ� �Է��ϼ���!");
				}
			} else {
				System.out.print("1. ����  2. �ݳ�  3. ����Ȯ��  4. ȸ������  5. ����Ȯ�� �Ѥ�>  ");
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
					System.out.print("�߸��� ���ڸ� �Է��Ͽ����ϴ�. �ٽ� �Է��ϼ���!");
				}
			}
		}

	}
	
	// ������ - ȸ�� ���� ����
	private static void adminMember() throws SQLException, InterruptedException, IOException {
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			String input = "select * from team_7.member";
			rs1 = st.executeQuery(input);
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			System.out.println("�̸�" + "\t" + "���̵�" + "\t" + "��й�ȣ" + "\t" + "�̸���" + "\t" + "��ȭ��ȣ");
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			while (rs1.next()) {
				System.out.print(rs1.getString("memberName") + "\t");
				System.out.print(rs1.getString("memberId") + "\t");
				System.out.print(rs1.getString("memberPasswd") + "\t");
				System.out.print(rs1.getString("memberEmail") + "\t");
				System.out.print(rs1.getString("memberPhone"));
				System.out.println();
			}
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			System.out.print("1. ȸ�� ���� ����  2. ȸ��Ż��   �Ѥ�>  ");
			int select = scanner.nextInt();
			if (select == 1) {
				System.out.print("�����ϰ��� �ϴ� ȸ���� ���̵� �Է��ϼ���. �Ѥ�>  ");
				String id = scanner.next();
				memberID = id;
				modifyMember();
			} else if (select == 2) {
				System.out.print("Ż���ų ȸ���� ���̵� �Է��ϼ���. �Ѥ�>  ");
				String id = scanner.next();
				String input2 = "delete from team_7.member where memberId = '" + id + "'";
				st.executeUpdate(input2);
				System.out.println("Ż�� �Ϸ��Ͽ����ϴ�.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ȸ�� - �����˻��� ���� �޴�
	private static void memberBorrow() throws SQLException, InterruptedException, IOException {
		System.out.print("1. �����˻�  2. ����   �Ѥ�>  ");
		int select = scanner.nextInt();
		if (select == 1) {
			bookSearch();
		} else if (select == 2) {
			bookBorrow();
		}
	}

	// ȸ�� - �����˻� �Լ�
	private static void bookSearch() throws SQLException, InterruptedException, IOException {
		System.out.println("�˻� ����� �������ּ���.");
		System.out.print(" 1. ISBN   2. ��������  �ѤѤ�>  ");
		int num = scanner.nextInt();
		String input = "";

		if (num == 1) {
			System.out.print("ISBN�� �Է����ּ���    �Ѥ�>  ");
			String isbn = scanner.next();
			input = "select * from team_7.book where ISBN = '" + isbn + "'";
		} else if (num == 2) {
			System.out.print("���������� �Է����ּ���   �Ѥ�>  ");
			String name = scanner.next();
			input = "select * from team_7.book where bookName like '%" + name + "%'";
		} else {
			System.out.println("�߸��� ���� �Է��Դϴ�. �ٽ� �Է����ּ���!");
			System.out.println();
			execute();
		}
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			rs1 = st.executeQuery(input);
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			System.out.println("ISBN" + "\t" + "å �̸�" + "\t" + "�۰�" + "\t" + "���ǻ�" + "\t" + "����");
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			while (rs1.next()) {
				System.out.print(rs1.getString("ISBN") + "\t");
				System.out.print(rs1.getString("bookName") + "\t");
				System.out.print(rs1.getString("bookAuthor") + "\t");
				System.out.print(rs1.getString("publish") + "\t");
				System.out.print(rs1.getString("quantity"));
				System.out.println();
			}
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			System.out.println("�˻�����Դϴ�.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ȸ�� - �ݳ���û
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
		System.out.print("* �ݳ��ϰ��� �ϴ� ������ ��ȣ�� �Է��ϼ���    ��> ");
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
		System.out.println("* �ݳ���û �Ǿ����ϴ�. ");
	}

	// ȸ�� - �������⳻��
	private static void memberIdentify() throws SQLException {
		System.out.println("**** ȸ������ ���� ���� �����Դϴ�. ****");
		rs1 = st.executeQuery("select * from team_7.memberborrowbook where memberId = '" + memberID + "'");
		System.out.println("ISBN " + "\t" + "����\t����");
		System.out.println("---------------------------------------");
		while (rs1.next()) {
			String ISBN = rs1.getString(1);
			String title = rs1.getString(2);
			String author = rs1.getString(3);
			System.out.println(ISBN + "\t " + title + "\t" + author + "\t");
		}
		System.out.println("---------------------------------------");
	}

	// ������ - ȸ�� ������ ��ŷ10��
	private static void adminBorrowRank() throws SQLException {
		System.out.println("**** ���� ������ ���� �� 10���� ȸ�� ����Դϴ� ****");
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

	// ������ - �ݳ������
	private static void adminBorrowIdentify() throws SQLException {
		rs1 = st.executeQuery("select * from team_7.return where returnApply = 1 and returnIdentify = 0");
		System.out.println("** �ݳ� ��� ����Դϴ� **");
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
		System.out.println("* �ݳ� ������ �ϰ��� �ϴ� ������ ��ȣ�� �Է��ϼ���  ");
		System.out.print("* ���� å�̶�� �� ���� ó���˴ϴ�  ��> ");
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
			System.out.println("* �ݳ����� �Ǿ����ϴ�. ");
		} else {
			System.out.println("* �ݳ� ��û�� ������ �����ϴ� !");
		}
	}

	// ȸ�� - ���� ���� ���� ���, ����
	private static void bookBorrow() throws SQLException {
		rs1 = st.executeQuery("select * from team_7.book");
		System.out.println(" **** ���� ���� ���� ���� ����Դϴ�  ****");
		System.out.println("------------------------------------------------------");
		System.out.println("|\t\tISBN\t����\t����\t���ǻ�\t�Ǽ�\t|");
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
		System.out.print(" ** ������ ������ ��ȣ�� �Է��ϼ���  �Ѥ�>  ");
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
			System.out.print(" * ������ �����Ͽ� ������ �� �����ϴ�. �����Ͻðڽ��ϱ� ? (Y/N) ��> ");
			String response = scanner.next();
			if (response.equals("Y")) {
				String input = "insert into team_7.reservation(ISBN, memberKind, reserveDate, memberId) values(";
				input += ("'" + ISBN + "', " + memberKind + ", '" + time1 + "', '" + memberID + "')");
				int rs = st.executeUpdate(input);
			}
		} else {

			System.out.println(" * ������ �Ϸ�Ǿ����ϴ�. ");
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

	// ������ - ����  ���, ���� ����, ���� ����
	private static void adminBook() throws InterruptedException, IOException {
		try {

			Connection con = null;

			// connection ������
			con = DriverManager.getConnection(url, "root", pw);
			java.sql.Statement st = null;

			ResultSet rs1 = null;
			int rs = 0;

			// query���� �ۼ��Ͽ� �����ϱ� ���� �뵵
			st = con.createStatement();

			System.out.print("1.���� ���� ���   2.���� ���� ����   3. ���� ���� ����     �ѤѤѤ�>  ");
//          System.out.println();
			int care = scanner.nextInt();
			if (care == 1) {
				// ���� ���� ���
				String input = "insert in" + "to team_7.book(ISBN, bookName, bookAuthor, publish, quantity)";
				input += "values(";
				System.out.print("* ����Ϸ��� ������ ISBN�� �Է��ϼ��� : ");
				String isbn = scanner.next();
				input += ("'" + isbn + "'" + ", ");
				ResultSet identify = null;
				identify = st.executeQuery("select ISBN from team_7.book where ISBN = '" + isbn + "'");

				System.out.print("* ����Ϸ��� ������ ������ �Է��ϼ��� : ");
				String bookName = scanner.next();
				input += ("'" + bookName + "'" + ", ");

				System.out.print("* ����Ϸ��� ������ ���ڸ� �Է��ϼ��� : ");
				String bookAuthor = scanner.next();
				input += ("'" + bookAuthor + "'" + ", ");

				System.out.print("* ����Ϸ��� ������ ���ǻ縦 �Է��ϼ��� : ");
				String publish = scanner.next();
				input += ("'" + publish + "'" + ", ");

				System.out.print("* ����Ϸ��� ������ ������ �Է��ϼ��� : ");
				String quantity = scanner.next();
				input += ("'" + quantity + "')");
				rs = st.executeUpdate(input);
				System.out.println("** ���� ����� �Ϸ�Ǿ����ϴ�! **");
				System.out.println();
				execute();

			} else if (care == 2) {
				// ���� ���� ����
				rs1 = st.executeQuery("select ISBN from team_7.book");
				System.out.println("���� ���� ������ �ϱ� ���� �˻� ����� �������ּ���.");
				System.out.print(" 1. ISBN   2. ��������  �ѤѤ�>  ");
				int num = scanner.nextInt();

				if (num == 1) {
					searchForISBN();
				} else if (num == 2) {
					searchForBookName();
				} else {
					System.out.println("�߸��� ���� �Է��Դϴ�. �ٽ� �Է����ּ���!");
					System.out.println();
					execute();
				}

			} else {
				// ���� ���� ����
				System.out.println("���� ���� ������ ���� �˻� ����� �������ּ���.");
				System.out.print("1. ISBN���� �˻�   2. ������������ �˻�  �ѤѤ�> ");
				int num = scanner.nextInt();
				System.out.println();

				if (num == 1) {
					deleteForISBN();
				} else if (num == 2) {
					deleteForBookName();
				} else {
					System.out.println("�߸��� ���� �Է��Դϴ�. �ٽ� �Է����ּ���!");
					System.out.println();
					execute();
				}
			}

		} catch (SQLException sqex) {

			System.out.println("SQLException: " + sqex.getMessage());

			System.out.println("SQLState: " + sqex.getSQLState());

		}
	}

	// ������ - ���� ���� ���� �� �ʿ��� �˻��Լ�(ISBN �̿�)
	private static void searchForISBN() throws SQLException, InterruptedException, IOException {

		String in = "";
		System.out.print("������ ISBN�� �Է��ϼ���. �ѤѤ�> ");
		String writeISBN = scanner.next();
		String input = "select * from team_7.book where ISBN = '" + writeISBN + "'";
		ResultSet rs = st.executeQuery(input);

		while (rs.next()) {

			if (rs.getString(1).equals(writeISBN)) {
				System.out.println("�����ϰ��� �ϴ� �׸��� ������.");
				System.out.print("1. ISBN  2. ����   3. ���ǻ�   4. ����  ----->  ");
				int modifyNum = scanner.nextInt();

				if (modifyNum == 1) {
					System.out.print("������ ISBN�� �Է��ϼ���. �Ѥ�> ");
					String isbn = scanner.next();
					in = "update team_7.book set ISBN = '" + isbn + "'" + "where ISBN = '" + writeISBN + "'";
				} else if (modifyNum == 2) {
					System.out.print("������ ������ �Է��ϼ���. �Ѥ�> ");
					String bookName = scanner.next();
					in = "update team_7.book set bookName = '" + bookName + "'" + "where ISBN = '" + writeISBN + "'";
				} else if (modifyNum == 3) {
					System.out.print("������ ���ǻ縦 �Է��ϼ���. �Ѥ�> ");
					String publish = scanner.next();
					in = "update team_7.book set publish = '" + publish + "'" + "where ISBN = '" + writeISBN + "'";
				} else if (modifyNum == 4) {
					System.out.print("������ ������ �Է��ϼ���. �Ѥ�> ");
					String quantity = scanner.next();
					in = "update team_7.book set quantity = '" + quantity + "'" + "where ISBN = '" + writeISBN + "'";
				} else {
					System.out.print("�ٽ� �Է����ּ���!");
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
			System.out.println("������ �Ϸ�Ǿ����ϴ�.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ������ - ���� ���� ���� �� ���� �� �ʿ��� �˻��Լ�(�������� �̿�)
	private static void searchForBookName() throws SQLException, InterruptedException, IOException {
		String bn = "bookName";
		String in = "";
		System.out.print("������ ���� ������ �Է��ϼ���. �ѤѤ�> ");
		String writeBookName = scanner.next();
		String input = "select * from team_7.book where bookName = '" + writeBookName + "'";
		ResultSet rs = st.executeQuery(input);
//      System.out.println(rs);

		while (rs.next()) {
//         System.out.println(rs.getString(1));

			if (rs.getString(bn).equals(writeBookName)) {
				System.out.println("�����ϰ��� �ϴ� �׸��� ������.");
				System.out.print("1. ISBN  2. ����   3. ���ǻ�   4. ����  ----->  ");
				int modifyNum = scanner.nextInt();

				if (modifyNum == 1) {
					System.out.print("������ ISBN�� �Է��ϼ���. �Ѥ�> ");
					String isbn = scanner.next();
					in = "update team_7.book set ISBN = '" + isbn + "'" + "where bookName = '" + writeBookName + "'";
				} else if (modifyNum == 2) {
					System.out.print("������ ������ �Է��ϼ���. �Ѥ�> ");
					String bookName = scanner.next();
					in = "update team_7.book set bookName = '" + bookName + "'" + "where bookName = '" + writeBookName
							+ "'";
				} else if (modifyNum == 3) {
					System.out.print("������ ���ǻ縦 �Է��ϼ���. �Ѥ�> ");
					String publish = scanner.next();
					in = "update team_7.book set publish = '" + publish + "'" + "where bookName = '" + writeBookName
							+ "'";
				} else if (modifyNum == 4) {
					System.out.print("������ ������ �Է��ϼ���. �Ѥ�> ");
					String quantity = scanner.next();
					in = "update team_7.book set quantity = '" + quantity + "'" + "where bookName = '" + writeBookName
							+ "'";
				} else {
					System.out.print("�ٽ� �Է����ּ���!");
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
			System.out.println("������ �Ϸ�Ǿ����ϴ�.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ������ - ���� ���� ���� �� �ʿ��� �˻��Լ�(ISBN �̿�)
	private static void deleteForISBN() throws SQLException, InterruptedException, IOException {

		Connection con = null;
		con = DriverManager.getConnection(url, "root", pw);
		PreparedStatement st1 = null;
//      String in = "";
		System.out.print("������ ISBN�� �Է��ϼ���. �ѤѤ�> ");
		String writeISBN = scanner.next();

		System.out.print("�����Ͻ� ������ �����Ͻðڽ��ϱ�? (y/n) �ѤѤ�>  ");
		String sel = scanner.next();
		String input = "";

		if (sel.equals("y")) {
			input = "delete from team_7.book where ISBN = '" + writeISBN + "'";

		} else if (sel.equals("n")) {
			execute();
		} else {
			System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���!");
			deleteForISBN();
		}
		System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
		st1 = con.prepareStatement(input);
		st1.execute();
	}

	// ������ - ���� ���� ���� �� �ʿ��� �˻��Լ�(�������� �̿�)
	private static void deleteForBookName() throws SQLException, InterruptedException, IOException {

		Connection con = null;
		con = DriverManager.getConnection(url, "root", pw);
		PreparedStatement st1 = null;
//      String in = "";
		System.out.print("������ ���� ������ �Է��ϼ���. �ѤѤ�> ");
		String writeBookName = scanner.next();

		System.out.print("�����Ͻ� ������ �����Ͻðڽ��ϱ�? (y/n) �ѤѤ�>  ");
		String sel = scanner.next();
		String input = "";

		if (sel.equals("y")) {
			input = "delete from team_7.book where bookName = '" + writeBookName + "'";

		} else if (sel.equals("n")) {
			execute();
		} else {
			System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���!");
			deleteForISBN();
		}
		System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
		st1 = con.prepareStatement(input);
		st1.execute();
	}

	// ȸ�� - ȸ�� ���� ����
	private static void handleMember() throws InterruptedException, IOException {
		System.out.println("** ȸ������  **");
		System.out.print("1. ȸ����������   2. ȸ��Ż��    3. ȸ����������    �Ѥ�>  ");
		int select = scanner.nextInt();
		if (select == 1) {
			modifyMember();
		} else if (select == 2) {
			deleteMember();
		} else if (select == 3) {
			checkMember();
		} else {
			System.out.print("�߸��� ���ڸ� �Է��Ͽ����ϴ�.");
			System.out.println();
			handleMember();
		}
	}

	// ȸ�� - ȸ�� ���� ����
	private static void modifyMember() throws InterruptedException, IOException {
		System.out.println("�����ϰ��� �ϴ� �׸��� ������.");
		System.out.print("1. �̸�   2. ��й�ȣ   3. �̸���  4. ��ȭ��ȣ   �Ѥ�>  ");
		int select = scanner.nextInt();
		String input = "";
		if (select == 1) {
			System.out.print("������ ������ �Է��ϼ���. �Ѥ�>   ");
			String name = scanner.next();
			input = "update team_7.member set memberName = '" + name + "'" + " where memberId = '" + memberID + "'";
		} else if (select == 2) {
			System.out.print("������ ������ �Է��ϼ���. �Ѥ�>   ");
			String pw = scanner.next();
			input = "update team_7.member set memberPasswd = '" + pw + "'" + " where memberId = '" + memberID + "'";
		} else if (select == 3) {
			System.out.print("������ ������ �Է��ϼ���. �Ѥ�>   ");
			String email = scanner.next();
			input = "update team_7.member set memberEmail = '" + email + "'" + " where memberId = '" + memberID + "'";
		} else if (select == 4) {
			System.out.print("������ ������ �Է��ϼ���. �Ѥ�>   ");
			String phone = scanner.next();
			input = "update team_7.member set memberPhone = '" + phone + "'" + " where memberId = '" + memberID + "'";
		} else {
			System.out.print("�߸��� ���ڸ� �Է��Ͽ����ϴ�.");
			System.out.println();
			modifyMember();
		}
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			st.execute(input);
			System.out.println("����Ǿ����ϴ�.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ȸ�� - ȸ�� ���� ����
	private static void deleteMember() throws InterruptedException, IOException {
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			PreparedStatement st1 = null;
			System.out.print("���� Ż���Ͻðڽ��ϱ� ? (y/n)  �Ѥ�>  ");
			String choice = scanner.next();
			String input = "";
			if (choice.equals("y")) {
				input = "delete from team_7.member where memberId = '" + memberID + "'";

			} else if (choice.equals("n")) {
				execute();
			} else {
				System.out.println("�߸� �Է��ϼ̽��ϴ�.");
				deleteMember();
			}
			System.out.print("Ż�� �Ϸ�Ǿ����ϴ�.");
			st1 = con.prepareStatement(input);
			st1.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// ȸ�� - ȸ�� ���� Ȯ��
	private static void checkMember() throws InterruptedException, IOException {
		try {
			Connection con = null;
			con = DriverManager.getConnection(url, "root", pw);
			st = con.createStatement();
			String input = "select * from team_7.member where memberId = '" + memberID + "'";
			rs1 = st.executeQuery(input);
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			System.out.println("�̸�" + "\t" + "���̵�" + "\t" + "��й�ȣ" + "\t" + "�̸���" + "\t" + "��ȭ��ȣ");
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
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

	// ȸ�� - ���� Ȯ��
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
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
			System.out.println("\tISBN" + "\t" + "���� ���� ��¥" + "\t" + "���� �������� ��");
			System.out.println("�ѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤѤ�");
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
			System.out.print("����� ������ ����Ͻðڽ��ϱ� ? (y/n)  �Ѥ�>  ");
			String choice = scanner.next();
			if (choice.equals("y")) {
				cancelReservation();
			} else if (choice.equals("n")) {
				execute();
			} else {
				System.out.println("�߸� �Է��ϼ̽��ϴ�.");
				handleReservation();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ȸ�� - ���� ���
	private static void cancelReservation() throws InterruptedException, IOException {
		System.out.print("���� ����ϰ��� �ϴ� å ��ȣ�� �Է��ϼ���.  �Ѥ�>  ");
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
			System.out.println("��ҵǾ����ϴ�.");
			execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ���ó�¥�� �ݳ����� �Ű������� �� ��¥ ��� �Լ�
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