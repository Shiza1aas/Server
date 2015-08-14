package com.example.shiza.server;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {
    TextView ip_address;
    TextView client_message;
    TextView server_status;
    TextView show_client_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip_address = (TextView) findViewById(R.id.ip_address);
        client_message = (TextView) findViewById(R.id.get_client_message);
        server_status = (TextView) findViewById(R.id.server_status);
        show_client_message = (TextView) findViewById(R.id.show_client_message);


        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        ip_address.setText(ip);

//        Making a server socket here


    }

    public void startServer(View view) {
        GetFromClient getFromClient = new GetFromClient(this,server_status,show_client_message);
        getFromClient.execute();
    }

}

class GetFromClient extends AsyncTask<Void, String, Void> {
    Context context;
    TextView server_status;
    TextView show_client_message;
    String TAG = "SERVER_MESSAGE";
    String inputFromClient = null;

    public GetFromClient(Context context,TextView server_status,TextView show_client_message) {
        this.context = context;
        this.server_status = server_status;
        this.show_client_message = show_client_message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Socket socket;
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Log.d(TAG, "Server Socket is starting....");
//            server_status.setText("The server is running");


            publishProgress("okay");
            socket = serverSocket.accept();

            DataInputStream input = new DataInputStream(socket.getInputStream());

//            Calling the second background task for handling input from server

//            Log.d(TAG, "Server Socket is started....");
            do
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                inputFromClient = input.readUTF();
                publishProgress(inputFromClient);
            }
            while ( inputFromClient != "bye" );

//            publishProgress(2);
            socket.close();

        } catch (IOException e) {
            Log.d(TAG, "I am in catch.");
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "I am in onProgress update.");
        if ( values[0].equals("okay") )
        {
            server_status.setText("Server has been started");
            server_status.setTextColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            show_client_message.setText(values[0]);
        }


    }

    protected void onPostExecute(Void inputFromClient)
    {
        Log.d(TAG, "I am in onPostExecute.");

        server_status.setText("Server is not running");
        server_status.setTextColor(context.getResources().getColor(R.color.red));
    }
}



