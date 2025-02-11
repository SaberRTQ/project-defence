package com.example.oop.controller;

import com.example.oop.dbconnection.DbConnection;
import com.example.oop.entities.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class MyController {
    @Autowired
    private ObjectMapper obMapper;

    @GetMapping("/index")
    public String listener1(){
        return "Book " + this.toString();
    }

    @GetMapping("/index/book")
    public String book2(){
        Book s1= new Book();
        s1.setTitle("Title 1");
        s1.setAuthor("Author");
        String jsonData=null;
        try{
            jsonData=obMapper.writeValueAsString(s1);
        }catch (JsonProcessingException e){
            System.out.println("Some error with book");
        }
        return jsonData;
    }

    @PostMapping("/index/secific")
    public String book3(@RequestParam String title){
        Book s1= new Book();
        s1.setTitle("Title 1");
        s1.setAuthor("Author");
        String jsonData=null;
        try{
            jsonData=obMapper.writeValueAsString(s1);
        }catch (JsonProcessingException e){
            System.out.println("Some error with book" + e.getMessage());
        }

        DbConnection con = new DbConnection();
        try {
            con.getConnectionToDb();
        } catch (Exception e) {
            System.out.println("Some error with book in db con");
            throw new RuntimeException(e);
        }
        return jsonData;
    }

    @GetMapping("/index/allBooks")
    public String listener5(){
        DbConnection myConnection=new DbConnection();
        Connection con=null;
        ArrayList<Book> books=new ArrayList<Book>();
        try  {
            con = myConnection.connect();
            books= myConnection.getAllBooksRS(con);
        }catch(Exception e){
            System.out.println(" EXCEPTION 1");
        }


        String jsonData=null;
        try{
            jsonData=obMapper.writeValueAsString(books);
        }catch (JsonProcessingException e){
            System.out.println("Some error with book");
        }


        return jsonData;
    }



    @PostMapping("/index/findBook")
    public String Book4(@RequestParam String title){
        DbConnection myConnection=new DbConnection();
        Connection con=null;
        Book s1=null;
        try  {
            con = myConnection.connect();
        }catch(Exception e){
            System.out.println(" EXCEPTION 1");
        }
        try  {
            s1= myConnection.findBookByName(con,title);
        }catch(Exception e){
            System.out.println(" EXCEPTION 2" + e.getMessage());
        }


        String jsonData=null;
        try{
            jsonData=obMapper.writeValueAsString(s1);
        }catch (JsonProcessingException e){
            System.out.println("Some error with book" + e.getMessage());
        }


        return jsonData;
    }



    @PostMapping("/index/createBook")
    public String createBook(@RequestParam String title,
                             @RequestParam String author,
                             @RequestParam int id){
        DbConnection myConnection=new DbConnection();
        Connection con=null;
        Book s1=new Book(id,author,title);
        String jsonData=null;
        try  {
            con = myConnection.connect();
        }catch(Exception e){
            System.out.println(" EXCEPTION 1");
        }

        try{
            jsonData=obMapper.writeValueAsString(myConnection.createBook(con,s1));
        }catch (JsonProcessingException e){
            System.out.println("Some error with book");
        }catch (SQLException e2){
            System.out.println("Some error with sql book");
        }


        return jsonData;
    }

    @PostMapping("/index/deleteBook")
    public String deleteBook(@RequestParam String title){
        DbConnection myConnection=new DbConnection();
        Connection con=null;
        Book s1=null;
        String jsonData=null;
        try  {
            con = myConnection.connect();
            s1=myConnection.findBookByName(con,title);
            System.out.println(s1);
            con = myConnection.connect();
            myConnection.deleteBook(con,s1);
        }catch(Exception e){
            System.out.println(" EXCEPTION 1");
        }
        try{
            jsonData=obMapper.writeValueAsString(s1);
        }catch (JsonProcessingException e){
            System.out.println("Some error with delete operation ");
        }


        return jsonData;
    }

    @PostMapping("/index/updateBook")
    public String updateStudent(@RequestParam String newAuthor,
                                @RequestParam String newTitle){
        DbConnection myConnection=new DbConnection();
        Connection con=null;
        Book s1=null;
        String jsonData=null;
        try  {
            con = myConnection.connect();
            s1=myConnection.findBookByName(con,newTitle);
            String oldTitle=s1.getTitle();
            System.out.println(s1);
            s1.setTitle(newTitle);
            s1.setAuthor(newAuthor);
            System.out.println(s1);
            con = myConnection.connect();
            myConnection.updateBook(con,s1,oldTitle);
        }catch(Exception e){
            System.out.println(" EXCEPTION 1");
        }


        try{
            jsonData=obMapper.writeValueAsString(s1);
        }catch (JsonProcessingException e){
            System.out.println("Some error with title");
        }


        return jsonData;
    }

}