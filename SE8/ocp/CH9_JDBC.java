package ocp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CH9_JDBC {

	public static void main(String[] args) {

		String url = "jdbc:derby:zoo";
		try (Connection connection = DriverManager.getConnection(url);
				Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)){
			
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
					java.util.Date utilDate = sqlDate.toLocalDate();
					System.out.println(id + " "+name+" "+utilDate);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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
