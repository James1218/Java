package ocp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.attribute.standard.MediaSize.JIS;

public class CH9_JDBC {

	public static void main(String[] args) {

		String url = "jdbc:derby:zoo";
		try (Connection connection = DriverManager.getConnection(url);
				Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);){

			ResultSet resultSet = statement.executeQuery("select name from animal");
			while (resultSet.next()){
				String s = resultSet.getString(1);
				System.out.println(s);
			}
			boolean isResultSet = statement.execute("select name from animal");
			System.out.println(isResultSet);
			if (isResultSet){
				resultSet = statement.getResultSet();
				System.out.println(resultSet.next());
				System.out.println(resultSet.getString(1));
				System.out.println(resultSet.next());
				System.out.println(resultSet.getString(1));
			}else {
				int rows = statement.getUpdateCount();
				System.out.println(rows);
			}

			PreparedStatement preparedStatement = connection.prepareStatement("select * from animal where name = ?");
			preparedStatement.setString(1, "Elsa");
			isResultSet = preparedStatement.execute();
			if (isResultSet){
				resultSet = preparedStatement.getResultSet();
				while (resultSet.next()){
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					java.sql.Date sqlDate = resultSet.getDate("date_born");
					java.time.LocalDate timeDate = sqlDate.toLocalDate();
					System.out.println(id + " "+name+" "+timeDate);
				}
			}

			ResultSet rs =  statement.executeQuery("select id, name from species");
			while(rs.next()) {
				Object idField = rs.getObject("id");
				Object nameField = rs.getObject("name");
				if (idField instanceof Integer) {
					int id = (Integer) idField;
					System.out.println(id);
				}
				if (nameField instanceof String) {
					String name = (String) nameField;
					System.out.println(name);
				}
			}

			preparedStatement = connection.prepareStatement("select * from animal order by id ",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			isResultSet = preparedStatement.execute(); 
			if(isResultSet){
				resultSet = preparedStatement.getResultSet();
				boolean isFirst = resultSet.first();
				System.out.println("First() : " + isFirst);
				boolean isLast = resultSet.last();
				System.out.println("Last() : "+isLast);
				resultSet.beforeFirst();
				resultSet.next();
				System.out.println(resultSet.getInt("id"));
				resultSet.afterLast();
				resultSet.previous();
				System.out.println(resultSet.getInt("id"));

				System.out.println(resultSet.absolute(2));        // true
				System.out.println(resultSet.getString("id"));    // 2
				System.out.println(resultSet.absolute(0));        // false, move to before first
				System.out.println(resultSet.absolute(5));        // true
				System.out.println(resultSet.getString("id"));    // 5
				System.out.println(resultSet.absolute(-2));       // true
				System.out.println(resultSet.getString("id"));    // 4


				resultSet.absolute(0);
				System.out.println(resultSet.next());          // true
				System.out.println(resultSet.getString("id")); // 1
				System.out.println(resultSet.relative(2));     // true
				System.out.println(resultSet.getString("id")); // 3
				System.out.println(resultSet.relative(-1));    // true
				System.out.println(resultSet.getString("id")); // 2
				System.out.println(resultSet.relative(4));     // false
				
				int result = statement.executeUpdate("update animal set name = name");
				System.out.println(result);
				
				resultSet = statement.executeQuery("select * from animal");
				resultSet.next();
				resultSet.next();
				System.out.println("default forward only : "+resultSet.absolute(1));
				
			}




		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getSQLState());
			System.out.println(e.getErrorCode());
		}
		try {
			//Class.forName() loads a class.
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/ocp-book",
					"username",
					"password");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
