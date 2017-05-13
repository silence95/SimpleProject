package com.httpserver.sun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SunHTTPServer {

	static class MyHandler implements HttpHandler {

		public void handle(HttpExchange httpExchange) throws IOException {
			InputStream is = httpExchange.getRequestBody();
			printRequest(is);
			String response = "this is the response,ok!";
			httpExchange.sendResponseHeaders(200, response.length());
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		private BufferedReader printRequest(InputStream is) throws IOException {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
			return bufferedReader;
		}
	}

	public static void main(String[] args) {
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(8000), 100);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.createContext("/applications/myapp", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("server start");
	}
}