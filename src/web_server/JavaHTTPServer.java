package web_server;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;


public class JavaHTTPServer implements Runnable{ 
	
	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	
	// port to listen connection
	static final int PORT = 8080;
	
	// verbose mode
	static final boolean verbose = true;
	
	// Client Connection via Socket Class
	private Socket connect;
	
	public JavaHTTPServer(Socket c) {
		connect = c;
	}
	
	
	public static boolean isNumeric(String strNum) {
	    try {
	        int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}

	
	public static void main(String[] args) throws IOException {
		try {
			//Server started
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			
			// we listen until user halts server execution
			while (true) {
				JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());
				
				if (verbose) {
					System.out.println("Connecton opened. (" + new Date() + ")");
				}
				
				// create dedicated thread to manage the client connection
				Thread thread = new Thread(myServer);
				thread.start();
			}
			
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
		
		
	
	}

	@Override
	public void run() {
		// we manage our particular client connection
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		String fileRequested = null;
		
		try {
			// we read characters from the client via input stream on the socket
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			
			// we get character output stream to client 
			out = new PrintWriter(connect.getOutputStream());
			
			// get binary output stream to client (for requested data)
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			
			// get first line of the request from the client
			String input = in.readLine();
			
			// we parse the request string tokenizer
	         StringTokenizer parse = new StringTokenizer(input);
			
		
			
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
			// we get file requested
			fileRequested = parse.nextToken().toLowerCase();

			if (method.equals("GET") ) {
				// GET method
		
					String str=fileRequested;
					String strNew = str.replace("/", "");
					
			
		 
					int border=Integer.parseInt(strNew);
					int sonuc=border;
			//limitations for web server
		       if(border>100 && border<20000 && isNumeric(strNew)==true) {
						
			     if(border>=1000 && border <=9999) {
			    	 
			    	 border= border - 80;
			     }
			     else if(border>9999 && border <20000) {
			       	 border= border - 83;
			     }
			     
			     else {
			  	 border= border - 81;
			     }
					String input1="";
					for(int  i=0;   i<border;  i++) {
						 input1="a"+input1; 
						
				      	}
				
	                File file = new File("ex1.html");
					//Html file creation
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write("<html><head><title>I am "+sonuc+" bytes long </title></head><body><p>"+input1+"</p></body></html>");
					bw.close();
					
					
					out.append("HTTP/1.0 200 Bad Request\r\n\r\n");
					
					// send HTTP Headers
					out.println("HTTP/1.1 200 OK");
					out.println("Server: Succesful ");
					out.println("Date: " + new Date());
					out.println(); // blank line between headers and content, very important !
					out.flush(); // flush character output stream buffer
					dataOut.flush(); // flush character output stream buffer
				 
					
			     
			
					}
				      
				      
				      else {
				       out.append("HTTP/1.0 400 Bad Request\r\n\r\n");
						// we send HTTP Headers with data to client
						out.println("HTTP/1.1 400 Bad request");
						out.println("Date: " + new Date());
						out.println(); // blank line between headers and content, very important !
						out.flush(); // flush character output stream buffer
						dataOut.flush();
				      }
					
			
					
				}
			
				
			
			
		} catch (FileNotFoundException fnfe) {
			//try {
				//fileNotFound(out, dataOut, fileRequested);
			//} //catch (IOException ioe) {
				//System.err.println("Error with file not found exception : " + ioe.getMessage());
		//	}
			
		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			} 
			
			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}
		
		
	}

	

	
}