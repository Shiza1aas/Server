    package com.example.shiza.server;

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


    public class MainActivity extends AppCompatActivity
    {
        TextView ip_address ;
        TextView client_message ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ip_address = (TextView)findViewById(R.id.ip_address);
            client_message = (TextView)findViewById(R.id.get_client_message);


            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

           ip_address.setText(ip);

//        Making a server socket here



        }

        public void startServer(View view)
        {
            GetFromClient getFromClient = new GetFromClient();
            getFromClient.execute();
        }

    }

    class GetFromClient extends AsyncTask<Void, Void,String>
    {
        String inputFromClient = null;

        @Override
        protected String doInBackground(Void... params) {

                        try {
                ServerSocket serverSocket  = new ServerSocket(8080);


                Socket socket = serverSocket.accept();

                DataInputStream input = new DataInputStream(socket.getInputStream());
                inputFromClient = input.readUTF();
                socket.close();

            } catch (IOException e) {
                Log.d("SERVER","I am in catch.");
                e.printStackTrace();

            }


            return inputFromClient;
        }

        protected void onPostExecute(String dataFromClient) {
           if ( dataFromClient != null )
           {
               Log.d("SERVER","The client message is" + dataFromClient);
           }
            else
           {
               Log.d("SERVER","Sorry for interruption");
           }
        }
    }

