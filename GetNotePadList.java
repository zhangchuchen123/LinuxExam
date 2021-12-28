import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@WebServlet(urlPatterns = "/GetNotePadList")
public class GetNotePadList extends HttpServlet {
   final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   final static  String DB_URL = "jdbc:mysql://180.76.60.24/linux_final";
   final static  String USER = "root";
   final static  String PASS = "aA@123";
   final static String SQL_QURERY_ALL_NOTEPAD= "SELECT * FROM notepad;";
   Connection conn = null;

   public void init() {
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void destroy() {
      try {
         conn.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = response.getWriter();

      List<NotePad> noList = getAllNotePad();
 	for (int i = 0; i < noList.size(); i++) {
		System.out.println(noList.get(i).id+"\t"+noList.get(i).notepadContent+"\t"+noList.get(i).notepadTime);
	}
      Gson gson = new Gson();
      String json = gson.toJson(noList, new TypeToken<List<NotePad>>() {
      }.getType());
      out.println(json);
      out.flush();
      out.close();
   }

   private List<NotePad> getAllNotePad() {
      List<NotePad> noList = new ArrayList<NotePad>();
      Statement stmt = null;
      try {
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(SQL_QURERY_ALL_NOTEPAD);
         while (rs.next()) {
            NotePad no = new NotePad();
            no.id = rs.getInt("id");
            no.notepadContent = rs.getString("notepadContent");
            no.notepadTime = rs.getString("notepadTime");
            noList.add(no);
         }
         rs.close();
         stmt.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }

      return noList;
   }
}

class NotePad {
    int id;
    String notepadContent;
    String notepadTime;

    @Override
    public String toString() {
        return "NotePad [id=" + id + ", notepadContent=" + notepadContent + ", notepadTime=" + notepadTime + "]";
    }
}
