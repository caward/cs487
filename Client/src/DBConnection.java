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
			String url = "jdbc:mysql://sql2.freesqldatabase.com:3306/sql239408";
			Driver jdbc = DriverManager.getDriver(url);
			DriverManager.registerDriver(jdbc);
			sqlConnection = DriverManager.getConnection(url, "sql239408", "tC7!vQ8!");
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
	
	public void close()
	{
		try
		{
			sqlConnection.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void updateCoordinates(int id, int x, int y, int gameId)
	{
		try
		{
			PreparedStatement pStmt = sqlConnection.prepareStatement("UPDATE Player SET xCoor = ?, yCoor = ?, Resume = ?, Gameid = ? WHERE ID = ?");
			pStmt.setInt(1, x);
			pStmt.setInt(2, y);
			pStmt.setInt(3, 1);
			pStmt.setInt(4, gameId);
			pStmt.setInt(5, id);
			pStmt.executeUpdate();	
		} catch (SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	public void loss(int loss, int id)
	{
		try
		{
			PreparedStatement pStmt = sqlConnection.prepareStatement("UPDATE Player SET Loss = ? WHERE ID = ?");
			pStmt.setInt(1, loss);
			pStmt.setInt(2, id);
			pStmt.executeUpdate();	
		} catch (SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	public void win(int win, int id)
	{
		try
		{
			PreparedStatement pStmt = sqlConnection.prepareStatement("UPDATE Player SET Win = ? WHERE ID = ?");
			pStmt.setInt(1, win);
			pStmt.setInt(2, id);
			pStmt.executeUpdate();	
		} catch (SQLException e)
		{
			e.printStackTrace();
		}	
	}

	public Player login()
	{
		Player p = null;
		String name=null;
		int id=-1;
		String username;
		int level= -1;
		int win = -1;
		int loss = -1;
		int resume = -1;
		
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
					PreparedStatement stmt = sqlConnection.prepareStatement("SELECT Name, ID, Level, Loss, Win, Resume FROM Player WHERE Name = ?");
					stmt.setString(1, username);
					ResultSet rset = stmt.executeQuery();
					if(rset.next())
					{
						id = rset.getInt("ID");
						name = rset.getString("Name");
						level = rset.getInt("Level");
						loss = rset.getInt("Loss");
						win = rset.getInt("Win");
						resume = rset.getInt("Resume");		
					}
					if (name==null)
					{
						JOptionPane.showMessageDialog(null,
								"Username not found");
					}else{
						p=new Player(name,id);
						p.setLevel(level);
						p.setLoss(loss);
						p.setWin(win);
						p.setResume(resume);
						return p;
					}
				} catch (SQLException e)
				{
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
						PreparedStatement pStmt = sqlConnection.prepareStatement("INSERT INTO Player (Name, ID, Level, Loss, Win, Resume) VALUES (?, ?, ?, ?, ?, ?)");
						pStmt.setString(1, username);
						pStmt.setInt(2, id);
						pStmt.setInt(3, 1);
						pStmt.setInt(4, 0);
						pStmt.setInt(5, 0);
						pStmt.setInt(6, 0);
						pStmt.executeUpdate();
						p=new Player(username,id);
						return p;
					}else{
						JOptionPane.showMessageDialog(null,
								"Username already exists");
					}
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}