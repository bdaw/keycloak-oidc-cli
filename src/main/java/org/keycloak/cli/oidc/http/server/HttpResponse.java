package org.keycloak.cli.oidc.http.server;

import org.keycloak.cli.oidc.http.MimeType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpResponse {

    private String statusLine;
    private byte[] body;
    private MimeType contentType;

    private HttpResponse(String statusLine, byte[] body, MimeType contentType) {
        this.statusLine = statusLine;
        this.body = body;
        this.contentType = contentType;
    }

    public static HttpResponse ok(byte[] body, MimeType contentType) {
        return new HttpResponse("200 OK", body, contentType);
    }

    public static HttpResponse badRequest() {
        return new HttpResponse("400 Bad Request", null, null);
    }

    public void send(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os);

        pw.print("HTTP/1.1 " + statusLine + "\r\n");
        if (contentType != null) {
            pw.print("Content-Type: " + contentType.toString() + "\r\n");
        }
        if (body != null) {
            pw.print("Content-Length: " + body.length + "\r\n");
        } else {
            pw.print("Content-Length: 0\r\n");
        }

        pw.print("\r\n");
        pw.flush();

        if (body != null) {
            os.write(body);
        }
    }

}
