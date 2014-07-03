package com.luckypants.books;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.luckypants.command.GetAuthorCommand;
import com.luckypants.model.Author;
import com.luckypants.model.Book;
import com.luckypants.mongo.ConnectionProvider;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;



public class readfromfile {
	public static void main(String[] args) {
		 
		readfromfile obj = new readfromfile();
		obj.readCsvFile();
	
}
	public ArrayList<Book> readCsvFile() {
		 
		String csvFile = "D:\\New folder\\samplefile1.csv";
		//InputStream input = getClass().getClassLoader().getResourceAsStream(csvFile);
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] title = null;
		String[] author= null;
		String[] authorid= null;
		String[] Isbn= null;
		String[] genres= null;
		ArrayList<Book> b = null;
	 
		
		try {
				b=new ArrayList<Book>();
				//br=new BufferedReader(new InputStreamReader(input));
				br = new BufferedReader(new FileReader(csvFile));
			     while((line = br.readLine()) != null) {
					String[] book_data = line.split(cvsSplitBy);
					String stitle='"'+"title"+ '"'+":";
					String sauthorid='"'+"_author_id"+ '"'+":";
					String sISBN='"'+"ISBN"+ '"'+":";
					String sgenres='"'+"genres"+ '"'+":";
					
					
				for(int i=0;i<book_data.length; i++){
					switch(i){
					case 0: title=book_data[i].split(stitle);
							//System.out.println(title[0]);
							break;
					/*case 1: author=book_data[i].split(sauthor);
							break;*/
					case 1: authorid=book_data[i].split(sauthorid);
							
							break;
					case 2: Isbn=book_data[i].split(sISBN);
							
							break;
					case 3: genres=book_data[i].split(sgenres);
							
							break;
					
					}
				}
				Book b1= new Book();
				//System.out.println(title[0]);
				b1.setTitle(title[1]);
				
				ConnectionProvider conn = new ConnectionProvider();
				DBCollection authorcollection = conn.getCollection("authors");
			
				DBCursor cursor = authorcollection.find();
				ArrayList<Author> authors = new ArrayList<Author>();
				GetAuthorCommand getauthor = new GetAuthorCommand();
				
					while (cursor.hasNext()) {
						Author auth = getauthor.execute("_id",
								cursor.next().get("_id").toString());				
						String a=authorid[1].substring(1, authorid[1].length()-1);
						//System.out.println("author id " + a);
						if(auth.getId()!=null){
						String author_id=auth.getId().toString();
						//System.out.println(" auth id "+author_id);
						//System.out.println();
						//
						 
						if(author_id.trim().equals(a.trim())){
							b1.setAuthor(auth);
							b1.set_author_id(authorid[1]);
							System.out.println("author " +auth);
						}
						}
					}
				
				b1.setISBN(Isbn[1]);
				String gen="";
				for(String s: genres){
					gen=gen+s;
				}
				b1.setGenres(gen);
				b.add(b1);
		}
		/*	for(Book a:b){
				System.out.println(a.getAuthor());
				
			}*/
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return b;
	}
			
}
	


