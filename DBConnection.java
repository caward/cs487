import java.sql.*;
import javax.swing.JOptionPane;



public class DBConnection
{
	public Connection sqlConnection;
	
	public DBConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://sql4.freemysqlhosting.net:3306/sql435023";
			Driver jdbc = DriverManager.getDriver(url);
			DriverManager.registerDriver(jdbc);
			sqlConnection = DriverManager.getConnection(url, "sql435023", "hB9!zV9*");
		}

		catch(SQLException sqle)
		{
			System.out.println("Couldn't make the connection");
		}

		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Class wasn't found...\n");
			cnfe.printStackTrace(System.out);
		}
	}

	public Player login()
	{
		Player p = null;
		String name=null;
		int id=-1;
		String username;
		
		while(true)
		{
			Object[] options = {"Register",
			"Login"};
			int n = JOptionPane.showOptionDialog(null, "Welcome","Gamerz",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
			if (n==1)
			{
				username = JOptionPane.showInputDialog("Enter UserName");
				try
				{
					PreparedStatement stmt = sqlConnection.prepareStatement("SELECT Name, ID FROM Player WHERE Name = ?");
					stmt.setString(1, username);
					ResultSet rset = stmt.executeQuery();
					if(rset.next())
					{
						id = rset.getInt("ID");
						name = rset.getString("Name");
					}
					if (name==null)
					{
						JOptionPane.showMessageDialog(null,
								"Username not found");
					}else{
						p=new Player(name,id);
						return p;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else
			{
				username = JOptionPane.showInputDialog("Enter UserName");
				Statement st;
				try
				{
					PreparedStatement stmt = sqlConnection.prepareStatement("SELECT Name, ID FROM Player WHERE Name = ?");
					stmt.setString(1, username);
					ResultSet rset = stmt.executeQuery();
					if(rset.next())
					{
						name = rset.getString("Name");
					}
					if (name==null)
					{
						st = sqlConnection.createStatement();
						ResultSet rset1 = st.executeQuery("SELECT COUNT(*) FROM Player");
						if(rset1.next())
							id = rset1.getInt(1)+1;
						PreparedStatement pStmt = sqlConnection.prepareStatement("INSERT INTO Player (Name, ID) VALUES (?, ?)");
						pStmt.setString(1, username);
						pStmt.setInt(2, id);
						pStmt.executeUpdate();
						p=new Player(username,id);
						return p;
					}else{
						JOptionPane.showMessageDialog(null,
								"Username already exists");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}