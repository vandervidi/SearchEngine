import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class MysqlConnector {

	Connection connection  = null;
	Statement statement = null;
	
	
	public MysqlConnector() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost/searchengine", "jaja", "gaga");
			initTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void initTable(){
		//Create index table statement
        String createIndexTable = "CREATE TABLE indexFile ("
                                +"        word VARCHAR(30) NOT NULL, "
                                +"        docNumber INT(4) NOT NULL, "
                                +"        freq INT(4) NOT NULL "
                                +"        )";

        try {
                statement = connection.createStatement();
                //executing statements the create the index table
                statement.executeUpdate(createIndexTable);
                statement.close();
        } catch (SQLException e) {
                // TODO Auto-generated catch block
                try {
					statement.close();
					System.out.println("Probably the table was created.");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        }
	}
	
	public void insert(String word, int docNum, int freq) throws SQLException{
		
		PreparedStatement prepstate =connection.prepareStatement
                ("INSERT INTO `indexFile` (`word`, `docNumber`, `freq`) "
                                + "VALUES (?, ?, ?)");
                prepstate.setString(1, word);
                prepstate.setInt(2, docNum);
                prepstate.setInt(3, freq);
       
                prepstate.execute();
                prepstate.close();
	}
	
	

	public void sortByWord() throws SQLException{
	
		statement = connection.createStatement();
		statement.execute(
				"ALTER TABLE `indexFile` ORDER BY word ASC;"
				);
        statement.close();   
	}
	
	
	public void removeDuplicate() throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate(
				"CREATE temporary TABLE tsum AS"  
				+"		SELECT word, docNumber, SUM(freq) as freq "
				+"		FROM indexfile group by word, docNumber;"
				);

		statement.executeUpdate("TRUNCATE TABLE indexFile;");
		
		statement.executeUpdate(
				"INSERT INTO indexFile (`word`,`docNumber`,`freq`)"
				+"		SELECT word,docNumber,freq"
				+"		FROM tsum;"
				);
		
		statement.executeUpdate(
				"DROP TEMPORARY TABLE IF EXISTS tsum;"
				);
		

        statement.close();  
        System.out.println("close-statement");
	}
}
