package com.timmattison.sirn;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import com.sun.net.httpserver.HttpServer;

public class Main {
	private static final int BACKLOG = 10;

	public static void main(String[] args) {
		if (args.length < 2) {
			// Need at least two arguments
			System.out.println("You must specify at least two arguments.");
			System.out.println("The first argument is the port number, the second and subsequent arguments are the files you would like to serve.");
			System.out.println("");
			System.out.println("For example:");
			System.out.println("java -jar sirn.jar 8080 myfile1.txt myfile2.txt");
			return;
		}

		int port = -1;

		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Couldn't parse the specified port number [" + args[0] + "]");
			return;
		}

		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(port), BACKLOG);

			for (String path : Arrays.copyOfRange(args, 1, args.length)) {
				server.createContext("/" + path, new SirnHTTPHandler(path));
				File file = new File(path);
				if (!file.exists()) {
					System.out.println("Warning!  File " + path + " not found.  It will not be accessible until it is moved into place.");
				}
			}

			server.setExecutor(null); // creates a default executor
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
