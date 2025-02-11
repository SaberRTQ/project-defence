package com.example.oop.dbconnection;

import com.example.oop.entities.Book;

import java.sql.*;
import java.util.ArrayList;

public class DbConnection {
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String username = "postgres";
    private String password = "0000";


    public void getConnectionToDb() throws Exception{
        Connection con = DriverManager.getConnection(url, username, password);
        System.out.println("Connection Established successfully");
        String query = "select * from public.books";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        rs.next();
        String title  = rs.getString("title");
        System.out.println(title);
        st.close();
        con.close();
        System.out.println("Connection Closed....");
    }


    public Connection connect() throws Exception{

        Connection con = DriverManager.getConnection(url, username, password);
        System.out.println("Connection Established successfully");
        return con;
    }

    public int closeConn(Connection con) throws SQLException {
        if(con!=null) {
            con.close();
            System.out.println("Connection Closed....");
            return 0;
        }
        System.out.println("Connection is null....");
        return 1;
    }
    public ArrayList<Book> getAllBooksRS(Connection con) throws SQLException {

        String query = "select * from public.books";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        ArrayList<Book> books=new ArrayList<Book>();
        while (rs.next()){
            Book book=new Book();
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setId(rs.getInt("id"));
            books.add(book);
        }
        st.close();
        closeConn(con);
        return books;
    }

    public Book findBookByName(Connection con, String book) throws SQLException {

        String query ="SELECT * FROM public.books WHERE  title= ? "; // query to be run
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1,book);

        ResultSet rs = st.executeQuery(); // Execute query

        ArrayList<Book> books=new ArrayList<Book>();
        Book student=new Book();
        while (rs.next()){

            student.setTitle(rs.getString("title"));
            student.setAuthor(rs.getString("author"));
            student.setId(rs.getInt("id"));
            books.add(student);
        }

        st.close();

        closeConn(con);
        return student;
    }


    public Book createBook(Connection con, Book s) throws SQLException {

        String query ="INSERT INTO public.books (id,title,author) VALUES (?,?,?)";
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1,s.getId());
        st.setString(2,s.getTitle());
        st.setString(3, s.getAuthor());

        int success = st.executeUpdate();
        st.close();
        closeConn(con);
        if(success>0){
            System.out.println("Book is added ");
            return s;
        }

        return null;
    }

    public Book updateBook(Connection con, Book s, String oldName) throws SQLException {

        String query ="UPDATE public.books SET id=?,title=?, author=? WHERE title=?"; // query to be run
        PreparedStatement st = con.prepareStatement(query);
        st.setInt(1,s.getId());
        st.setString(2,s.getTitle());
        st.setString(3,s.getAuthor());

        int success = st.executeUpdate();
        st.close();
        closeConn(con);
        if(success>0){
            System.out.println("Book is updated ");
            return s;
        }

        return null;
    }

    public Book deleteBook(Connection con, Book s) throws SQLException {

        String query ="DELETE FROM  public.books WHERE title=?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1,s.getTitle());


        int success = st.executeUpdate();
        st.close();
        closeConn(con);
        if(success>0){
            System.out.println("Book is deleted ");
            return s;
        }

        return null;
    }


}