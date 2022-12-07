package com.app;

import com.model.User;
import com.model.Users;
import com.model.dao.UserDAO;
import com.utils.In;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;



/* 
 * Run Maven Standalone Java app (on localhost or other server)
 * (Assuming JAXBapp is the main class)
 * (Assuming com.app is the main class package)
 *
 * In CLI Execute: mvn exec:java -Dexec.mainClass="com.app.JAXBapp"
 */
public class JAXBapp {
    public static void main(String[] args) throws JAXBException, IOException {
        JAXBapp app = new JAXBapp();
        app.menu();
    }
    
    private String fileName = "C:\\Users\\236349\\Documents\\GitHub\\Labs\\lab\\WEB-INF\\users.xml";
    private Users users;
    private UserDAO userDAO;
    
    public JAXBapp() throws JAXBException, FileNotFoundException{  
        System.out.println("Loading JAXB app ...");
        userDAO = new UserDAO();
        userDAO.setFileName(fileName);
        users = userDAO.getUsers();
    }
    
    private int readID(){
        System.out.print("ID: ");
        return In.nextInt();
    }
    
    private String readString(String prompt){
        System.out.print(prompt);
        return In.nextLine();
    }
    
    private void find(){
        System.out.println("FIND\t>> ");
        int ID = readID();
        users = userDAO.getUsers();
        
        // from Users.java in com.model imported up in the class here
        User user = users.user(ID);
        
        if (user != null) {
            System.out.println(" User "+ user.getName() +" record: ");
            System.out.println(user);
        } else {
            System.out.println(" User "+ ID +" record does not exist");
        }
    }
    
    
    private void add() throws JAXBException, IOException{
        System.out.println("ADD\t>> ");
        String name = readString("Name: ");
        String email = readString("Email: ");
        String password = readString("Password: ");
        String dob = readString("Date of Birth: ");

        User user = new User(name, email, password, dob);
        User xmlUser = users.user(email);
        users = userDAO.getUsers();
        
        if (xmlUser != null) {
            System.out.println(" User "+ name +" record already exists");
        } else {
            System.out.println(" User "+ name +" record added to XML");
            users.add(user);
            userDAO.save(users, fileName);
        }
    }
    
    private void update() throws JAXBException, IOException{
        System.out.println("UPDATE\t>> ");
        int ID = readID();
        String password = readString("Password: ");
        
        users = userDAO.getUsers(); 
        
        // from Users.java in com.model imported up in the class here
        User user = users.user(ID);
        
        
        if (user != null) {
            System.out.println(" Updating " + user.getName() + "'s password ...");
            user.setPassword(password);
            userDAO.update(users, user);
        } else {
            System.out.println(" User "+ user.getName() +" record does not exist");
        }
    }
    
    private void delete() throws JAXBException, IOException{
        System.out.println("DELETE\t>> ");
        int ID = readID();
       
        users = userDAO.getUsers();
        User user = users.user(ID);   
        
        if (user != null) {
            System.out.println("Deleting " + user.getName() + "'s record ...");
            userDAO.delete(users, user);
        } else {
            System.out.println(" User "+ ID +" record does not exist");
        }
    }
    
    private void view(){
        System.out.println("VIEW\t>> ");
        System.out.println("ID\tNAME\t\tEMAIL\t\t\tD.O.B.");
        users = userDAO.getUsers();
        users.show();
    }
    
    private char readChoice(){
        System.out.print("Choice (a/f/u/d/v/x): ");
        return In.nextChar();
    }
    
    private void menu() throws JAXBException, IOException{
        char c;
        
        while((c = readChoice()) != 'x'){
            switch(c){
                case 'a': add(); break;
                case 'f': find(); break;
                case 'u': update(); break;
                case 'd': delete(); break;
                case 'v': view(); break;
                default: System.out.println("Unknown Command");
            }
        }
    }
}
