package com.timmattison.sirn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SirnHTTPHandler implements HttpHandler {
	private static final String ERROR_MESSAGE_404 = "File not found";
	private String path = null;

	public SirnHTTPHandler(String path) {
		this.path = path;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		// We don't need the request body
		//InputStream inputStream = httpExchange.getRequestBody();
		//String requestBody = streamToString(inputStream);

		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);

		byte[] output = null;

		// Was the file found?
		if (file.length() == 0) {
			// No, just return a 404
			httpExchange.sendResponseHeaders(404, ERROR_MESSAGE_404.length());
			output = ERROR_MESSAGE_404.getBytes();
		} else {
			// Yes, return the entire file
			httpExchange.sendResponseHeaders(200, file.length());
			output = streamToByteArray(fileInputStream);
		}

		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(output);
		outputStream.close();
	}

	private String streamToString(InputStream inputStream) throws IOException {
		final char[] buffer = new char[0x10000];
		int read;

		StringBuilder stringBuilder = new StringBuilder();
		Reader reader = new InputStreamReader(inputStream, "UTF-8");

		do {
			read = reader.read(buffer, 0, buffer.length);
			if (read > 0) {
				stringBuilder.append(buffer, 0, read);
			}
		} while (read >= 0);

		return stringBuilder.toString();
	}

	private byte[] streamToByteArray(InputStream inputStream) throws IOException {
		final byte[] buffer = new byte[0x10000];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int read;

		do {
			read = inputStream.read(buffer, 0, buffer.length);

			if (read > 0) {
				baos.write(buffer, 0, read);
			}
		} while (read >= 0);

		return baos.toByteArray();
	}
}
