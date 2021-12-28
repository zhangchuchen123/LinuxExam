import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/DeleteNote")
public class DeleteNote extends HttpServlet {
   final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   final static  String DB_URL = "jdbc:mysql://180.76.60.24/linux_final";
   final static  String USER = "root";
   final static  String PASS = "aA@123";
   final static String SQL_DELETE_Note = "DELETE FROM notepad WHERE id=?";

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      NotePad req = getRequestBody(request);
      getServletContext().log(req.toString());
      PrintWriter out = response.getWriter();

      out.println(deleteNote(req));
      out.flush();
      out.close();
   }

   private NotePad getRequestBody(HttpServletRequest request) throws IOException {
      NotePad no = new NotePad();
      StringBuffer bodyJ = new StringBuffer();
      String line = null;
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null)
         bodyJ.append(line);
      Gson gson = new Gson();
      no = gson.fromJson(bodyJ.toString(), new TypeToken<NotePad>() {
      }.getType());
      return no;
   }

   private int deleteNote(NotePad req) {
      Connection conn = null;
      PreparedStatement stmt = null;
      int retcode = -1;
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.prepareStatement(SQL_DELETE_Note);

         stmt.setInt(1, req.id);
         int row = stmt.executeUpdate();
         if (row > 0)
            retcode = 1;

         stmt.close();
         conn.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
      return retcode;
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
